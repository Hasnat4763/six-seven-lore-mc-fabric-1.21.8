package net.hasnat4763.ScreenHandler;

import net.hasnat4763.ModSounds;
import net.hasnat4763.ModStatusEffect.ModEffects;
import net.hasnat4763.SixSeven;
import net.hasnat4763.SixSevenCurse.SixSevenCurseDataKeeper;
import net.hasnat4763.networking.SixSevenNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class SixSevenStoryBookScreenHandler extends ScreenHandler {
    private final ItemStack bookStack;
    private static final int TOTAL_PAGES = 10;
    private final PropertyDelegate propertyDelegate;

    public static final int BTN_NEXT = 1;
    public static final int BTN_PREV = 2;

    public SixSevenStoryBookScreenHandler(int syncId, PlayerInventory playerInventory, Hand hand) {
        super(ModScreenHandler.SIX_SEVEN_STORY_BOOK_HANDLER, syncId);
        this.bookStack = playerInventory.player.getStackInHand(hand);
        this.propertyDelegate = new ArrayPropertyDelegate(1);
        this.addProperties(propertyDelegate);
    }

    private void nextPageServer() {
        int current = propertyDelegate.get(0);
        int lastPageIndex = TOTAL_PAGES - 1;
        if (current < lastPageIndex) {
            int newPage = current + 1;
            propertyDelegate.set(0, newPage);

            if (newPage == lastPageIndex) {
                NbtComponent customData = bookStack.get(DataComponentTypes.CUSTOM_DATA);
                NbtCompound nbt = customData != null ? customData.copyNbt() : new NbtCompound();
                if (!nbt.contains("six_seven_read_complete")) {
                    nbt.putBoolean("six_seven_read_complete", true);
                    bookStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
                    SixSeven.LOGGER.info("[Book] Marked book as read complete");
                    sendContentUpdates();
                }
            }
        }
    }

    private void previousPageServer() {
        int current = propertyDelegate.get(0);
        if (current > 0) {
            propertyDelegate.set(0, current - 1);
            sendContentUpdates();
        }
    }
    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (player.getWorld().isClient) return false;

        if (id == BTN_NEXT) {
            nextPageServer();
            return true;
        } else if (id == BTN_PREV) {
            previousPageServer();
            return true;
        }
        return false;
    }

    public int getCurrentPage() {
        return propertyDelegate.get(0);
    }

    public ItemStack getBookStack() {
        return this.bookStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.bookStack == player.getStackInHand(Hand.MAIN_HAND)
                || this.bookStack == player.getStackInHand(Hand.OFF_HAND);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        int currentPage = propertyDelegate.get(0);
        super.onClosed(player);

        SixSeven.LOGGER.info("[Book] onClosed - player: {}, currentPage: {}, isClient: {}",
                player.getName().getString(), currentPage, player.getWorld().isClient);

        if (!player.getWorld().isClient && player instanceof ServerPlayerEntity sp) {
            if (currentPage >= TOTAL_PAGES - 1) {
                SixSevenCurseDataKeeper data = SixSevenCurseDataKeeper.get(sp.getServer());
                SixSevenCurseDataKeeper.PlayerCurseInfo info = data.getCurseInfo(sp.getUuid());
                if (info == null || !info.isCursed) {
                    SixSeven.LOGGER.info("[Book] Applying curse to player {}", sp.getName().getString());

                    data.cursePlayer(sp.getUuid(), sp.getWorld().getTime());

                    sp.addStatusEffect(new StatusEffectInstance(
                            ModEffects.SIX_SEVEN_CURSE_EFFECT,
                            20 * 60 * 60 * 24,
                            0,
                            true,
                            true,
                            true
                    ));

                    sp.sendMessage(Text.literal("§4§oYou feel a dark presence settling within you..."), false);
                    SixSeven.LOGGER.info("[Book] Sent curse message to player");
                    SixSevenNetworking.SendCurseMusicPacket(sp);

                    sp.getWorld().playSound(
                            null,
                            sp.getBlockPos(),
                            ModSounds.SIX_SEVEN_BOOK_OPEN,
                            SoundCategory.AMBIENT,
                            1.0f,
                            1.0f
                    );
                }
            }
        }
    }
}
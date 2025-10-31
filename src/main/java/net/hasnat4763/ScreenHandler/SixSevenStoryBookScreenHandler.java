package net.hasnat4763.ScreenHandler;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;

public class SixSevenStoryBookScreenHandler extends ScreenHandler {
    private final ItemStack bookStack;
    private int currentPage = 0;
    private static final int TOTAL_PAGES = 20;

    public SixSevenStoryBookScreenHandler(int syncId, PlayerInventory playerInventory, Hand hand) {
        super(ModScreenHandler.SIX_SEVEN_STORY_BOOK_HANDLER, syncId);
        this.bookStack = playerInventory.player.getStackInHand(hand);
    }

    public void nextPage() {
        NbtComponent customData = bookStack.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound nbt = customData != null ? customData.copyNbt() : new NbtCompound();

        currentPage++;
        if (currentPage >= TOTAL_PAGES) {
            if (!nbt.contains("six_seven_read_complete")) {
                nbt.putBoolean("six_seven_read_complete", true);
            }
            bookStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        }
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public ItemStack getBookStack() {
        return this.bookStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.bookStack == player.getStackInHand(Hand.MAIN_HAND) ||
                this.bookStack == player.getStackInHand(Hand.OFF_HAND);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        return ItemStack.EMPTY;
    }
}
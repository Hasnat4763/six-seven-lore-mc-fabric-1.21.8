package net.hasnat4763.items;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.hasnat4763.ModSounds;
import net.hasnat4763.ScreenHandler.SixSevenStoryBookScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SixSevenStoryBook extends Item {
    public SixSevenStoryBook(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand finalHand) {

        if (!world.isClient && world instanceof ServerWorld serverWorld) {

            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverWorld.playSound(null, player.getBlockPos(), ModSounds.SIX_SEVEN_BOOK_OPEN,
                        player.getSoundCategory(), 2f, 0.7f);

                serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory<Hand>() {

                    @Override
                    public Hand getScreenOpeningData(ServerPlayerEntity player) {
                        return finalHand;
                    }

                    @Override
                    public Text getDisplayName() {
                        return Text.translatable("screen.six_seven.story_book");
                    }

                    @Override
                    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                        return new SixSevenStoryBookScreenHandler(syncId, playerInventory, finalHand);
                    }
                });
            }

            player.sendMessage(Text.literal("§8Those who nose..."), false);
            player.sendMessage(Text.literal("§8...will soon find out."), false);
        }

        return ActionResult.SUCCESS;
    }
}
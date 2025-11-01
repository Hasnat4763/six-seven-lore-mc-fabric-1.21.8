package net.hasnat4763.items;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.hasnat4763.ModSounds;
import net.hasnat4763.ScreenHandler.SixSevenStoryBookScreenHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SixSevenStoryBook extends Item {
    public SixSevenStoryBook(Settings settings) {
        super(settings);
    }

   //@Override
    //public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
       // if (!entity.getWorld().isClient()) {
          //  entity.playSound(SoundEvents.ENTITY_PILLAGER_AMBIENT, 2f, 0.7f);
        //}
        //return super.useOnEntity(stack, user, entity, hand);
    //}

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand finalHand) {
        ItemStack stack = player.getStackInHand(finalHand);

        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
            NbtCompound nbt = customData != null ? customData.copyNbt() : new NbtCompound();
            if (!nbt.contains("six_seven_story_map")) {
                ItemStack mapStack = FilledMapItem.createMap(serverWorld, player.getBlockX(), player.getBlockZ(),
                        (byte) 2, true, true);

                MapIdComponent mapIdComponent = mapStack.get(DataComponentTypes.MAP_ID);
                if (mapIdComponent != null) {
                    nbt.putInt("six_seven_story_map", mapIdComponent.id());
                    stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
                    player.getInventory().insertStack(mapStack);
                }
            }

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

            player.sendMessage(Text.literal("Those who nose.........."), false);


        }

        return ActionResult.SUCCESS;
    }
}
package net.hasnat4763.ScreenHandler;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;

public class SixSevenStoryBookScreenHandler extends ScreenHandler {
    private final ItemStack bookStack;
    private static final int TOTAL_PAGES = 10;
    private final PropertyDelegate propertyDelegate;

    public SixSevenStoryBookScreenHandler(int syncId, PlayerInventory playerInventory, Hand hand) {
        super(ModScreenHandler.SIX_SEVEN_STORY_BOOK_HANDLER, syncId);
        this.bookStack = playerInventory.player.getStackInHand(hand);
        this.propertyDelegate = new ArrayPropertyDelegate(1);
        this.addProperties(propertyDelegate);
    }

    public void nextPage() {
        NbtComponent customData = bookStack.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound nbt = customData != null ? customData.copyNbt() : new NbtCompound();
        int currentPage = propertyDelegate.get(0);
        if (currentPage < TOTAL_PAGES) {
            propertyDelegate.set(0, currentPage + 1);
        }
        if (currentPage >= TOTAL_PAGES) {
            if (!nbt.contains("six_seven_read_complete")) {
                nbt.putBoolean("six_seven_read_complete", true);
            }
            bookStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        }
    }

    public void previousPage() {
        int currentPage = propertyDelegate.get(0);
        if (currentPage > 0) {
            propertyDelegate.set(0, currentPage - 1);
        }
    }

    public int getCurrentPage() {
        return propertyDelegate.get(0);
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
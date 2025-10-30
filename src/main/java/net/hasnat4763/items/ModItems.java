package net.hasnat4763.items;

import net.hasnat4763.SixSeven;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

import static net.hasnat4763.SixSeven.MOD_ID;

public class ModItems {
    public static final Item SIX_SEVEN_STORY_BOOK = RegisterItem(
            "six_seven_story_book",
            SixSevenStoryBook::new,
            new Item.Settings().maxCount(1));
    public static Item RegisterItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }
    public static void RegisterModItems() {
        SixSeven.LOGGER.info("Registering SIX SEVEN Items" + MOD_ID);
    }
}

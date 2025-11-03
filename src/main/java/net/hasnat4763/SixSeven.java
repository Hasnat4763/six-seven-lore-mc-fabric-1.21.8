package net.hasnat4763;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.hasnat4763.SixSevenCurse.SixSevenCurseDataKeeper;
import net.hasnat4763.items.ModItems;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.hasnat4763.ModSounds.RegisterModSounds;
import static net.hasnat4763.ModStatusEffect.ModEffects.RegisterModEffects;
import static net.hasnat4763.SixSevenCurse.CurseEventHandlers.RegisterCurseEventHandlers;
import static net.hasnat4763.SixSevenCurse.CursePlayerJoinTickChecker.RegisterCursePlayerJoin;
import static net.hasnat4763.SixSevenCurse.SixSevenCurseTickHandler.RegisterServerCurseTick;
import static net.hasnat4763.items.ModItems.RegisterModItems;
import static net.hasnat4763.ScreenHandler.ModScreenHandler.RegisterModScreenHandler;
import static net.hasnat4763.networking.SixSevenNetworking.RegisterNetworking;

public class SixSeven implements ModInitializer {
    public static final String MOD_ID = "six_seven";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.equals(LootTables.DESERT_PYRAMID_CHEST)) {
                SixSeven.LOGGER.info("[LootTable] Modifying desert pyramid loot table");

                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.SIX_SEVEN_STORY_BOOK)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2)))
                        );

                tableBuilder.pool(poolBuilder);

                SixSeven.LOGGER.info("[LootTable] Added Six Seven Story Book to desert pyramid");
            }
        });
    }


    @Override
    public void onInitialize() {
        LOGGER.info("SixSeven mod initializing!");
        RegisterModItems();
        RegisterModScreenHandler();
        RegisterModSounds();
        RegisterServerCurseTick();
        RegisterModEffects();
        RegisterNetworking();
        RegisterCurseEventHandlers();
        modifyLootTables();
        RegisterCursePlayerJoin();
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            SixSevenCurseDataKeeper data = SixSevenCurseDataKeeper.get(server);
            data.save(server);
            SixSevenCurseDataKeeper.clearInstance(server);
        });
    }
}

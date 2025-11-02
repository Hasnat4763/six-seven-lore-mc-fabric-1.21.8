package net.hasnat4763.ModStatusEffect;

import net.fabricmc.api.ModInitializer;
import net.hasnat4763.SixSeven;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static net.hasnat4763.SixSeven.MOD_ID;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> SIX_SEVEN_CURSE_EFFECT =
            Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "six_seven_curse_effect"), new SixSevenCurseEffect());

    public static void RegisterModEffects() {
        SixSeven.LOGGER.info("Registering Mod Effects for " + MOD_ID);
    }
}

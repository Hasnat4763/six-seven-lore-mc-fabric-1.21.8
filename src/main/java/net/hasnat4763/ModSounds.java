package net.hasnat4763;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static net.hasnat4763.SixSeven.MOD_ID;

public class ModSounds {
    private ModSounds() {}
    public static SoundEvent RegisterSound(String id) {
        Identifier identifier = Identifier.of(MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static final SoundEvent SIX_SEVEN_BOOK_OPEN = RegisterSound("story_book_open");
    public static final SoundEvent RICKROLL = RegisterSound("rickroll");
    public static final SoundEvent THICK_OF_IT = RegisterSound("thick_of_it");

    public static void RegisterModSounds() {
        SixSeven.LOGGER.info("Registering SIX SEVEN Sounds for " + MOD_ID);
    }


}

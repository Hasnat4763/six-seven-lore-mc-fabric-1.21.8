package net.hasnat4763.SixSevenCurse;

import net.hasnat4763.SixSeven;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class BookCurseManager {

    private static final Random RANDOM = new Random();


    public static void triggerTimedEffect(ServerPlayerEntity player, int curseLevel) {
        if (player == null || player.isRemoved()) {
            SixSeven.LOGGER.warn("[SixSevenCurse] Player is null or removed, skipping effect");
            return;
        }

        ServerWorld world = player.getWorld();
        if (world == null || world.isClient) {
            SixSeven.LOGGER.warn("[SixSevenCurse] World is null or client-side, skipping effect");
            return;
        }

        SixSeven.LOGGER.info("[SixSevenCurse] Trigger effect L{} for {} in world {}",
                curseLevel, player.getName().getString(), world.getRegistryKey().getValue());

        switch (curseLevel) {
            case 1 -> triggerLevel1Effect(player, world);
            case 2 -> triggerLevel2Effect(player, world);
            case 3 -> triggerLevel3Effect(player, world);
            case 4 -> triggerLevel4Effect(player, world);
            case 5 -> triggerLevel5Effect(player, world);
            default -> triggerLevel0Effect(player, world);
        }

    }

    private static void playAtPlayer(ServerPlayerEntity p, SoundEvent sound, SoundCategory cat, float pitch) {
        try {
            p.getWorld().playSound(
                    null,
                    p.getX(),
                    p.getY(),
                    p.getZ(),
                    sound,
                    cat,
                    2.0f,
                    pitch
            );
            SixSeven.LOGGER.info("[SixSevenCurse] Played sound {} to {}", sound.id(), p.getName().getString());
        } catch (Exception e) {
            SixSeven.LOGGER.error("[SixSevenCurse] Failed to play sound", e);
        }
    }

    private static void playAtPos(ServerWorld world, BlockPos pos, SoundEvent sound, SoundCategory cat, float vol, float pitch) {
        world.playSound(null, pos, sound, cat, vol, pitch);
    }

    private static void addEffect(ServerPlayerEntity p, net.minecraft.registry.entry.RegistryEntry<net.minecraft.entity.effect.StatusEffect> effect, int durationTicks, int amplifier) {
        p.addStatusEffect(new StatusEffectInstance(effect, durationTicks, amplifier, false, false));
    }

    private static void chat(ServerPlayerEntity p, String msg) {
        try {
            p.sendMessage(Text.literal(msg), false); // false = chat, true = action bar
            SixSeven.LOGGER.info("[SixSevenCurse] Sent chat message to {}: {}", p.getName().getString(), msg);
        } catch (Exception e) {
            SixSeven.LOGGER.error("[SixSevenCurse] Failed to send message", e);
        }
    }

    private static void triggerLevel0Effect(ServerPlayerEntity player, ServerWorld world) {
        int choice = RANDOM.nextInt(3);
        SixSeven.LOGGER.info("[SixSevenCurse] Level 0 effect choice: {}", choice);
        switch (choice) {
            case 0 -> {
                SixSeven.LOGGER.info("[SixSevenCurse] Triggering chat message");
                chat(player, "§8§o...six...");
            }
            case 1 -> {
                SixSeven.LOGGER.info("[SixSevenCurse] Triggering sound");
                playAtPlayer(player, SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 3.8f);
            }
            case 2 -> {
                SixSeven.LOGGER.info("[SixSevenCurse] Spawning TNT L0");
                playAtPlayer(player, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.HOSTILE, 1.8f);
                spawnTnt(world, player.getPos().add(
                        RANDOM.nextInt(10) - 5,
                        0,
                        RANDOM.nextInt(10) - 5
                ), 120);
            }
        }
    }

    private static void triggerLevel1Effect(ServerPlayerEntity player, ServerWorld world) {
        switch (RANDOM.nextInt(4)) {
            case 0 -> {
                chat(player, "§4§o...seven...");
                playAtPlayer(player, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 0.7f);
            }
            case 1 -> playAtPlayer(player, SoundEvents.ENTITY_WARDEN_ROAR, SoundCategory.HOSTILE, 1.0f);
            case 2 -> {
                playAtPlayer(player, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.HOSTILE, 1.8f);
                SixSeven.LOGGER.info("[SixSevenCurse] Spawning TNT");
                spawnTnt(world, player.getPos().add(
                        RANDOM.nextInt(10) - 5,
                        0,
                        RANDOM.nextInt(10) - 5
                ), 40);
            }
            case 3 -> chat(player, "§k||||§r §4Those who know§r §k||||");
        }
    }

    private static void triggerLevel2Effect(ServerPlayerEntity player, ServerWorld world) {
        switch (RANDOM.nextInt(5)) {
            case 0 -> {
                Vec3d lookVec = player.getRotationVec(1.0f);
                Vec3d behind = player.getPos().subtract(lookVec.multiply(5));
                playAtPos(world, BlockPos.ofFloored(behind), SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 1.2f, 0.8f);
            }
            case 1 -> addEffect(player, StatusEffects.BLINDNESS, 100, 0); // 5s
            case 2 -> playAtPlayer(player, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0f);
            case 3 -> {
                addEffect(player, StatusEffects.SLOWNESS, 100, 0); // 5s
                chat(player, "§b§oYou feel a sudden chill...");
            }
            case 4 -> chat(player, "§4§o" + player.getName().getString() + "...");
        }
    }

    private static void triggerLevel3Effect(ServerPlayerEntity player, ServerWorld world) {
        switch (RANDOM.nextInt(4)) {
            case 0 -> {
                addEffect(player, StatusEffects.DARKNESS, 120, 0);
                playAtPlayer(player, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.HOSTILE, 0.8f);
            }
            case 1 -> {
                float basePitch = 0.7f;
                playAtPlayer(player, SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, basePitch);
                playAtPlayer(player, SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, basePitch + 0.12f);
                playAtPlayer(player, SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, basePitch + 0.24f);
            }
            case 2 -> {
                addEffect(player, StatusEffects.NAUSEA, 120, 0);
                playAtPlayer(player, SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0f);
            }
            case 3 -> {
                Vec3d lookVec = player.getRotationVec(1.0f);
                Vec3d behind = player.getPos().subtract(lookVec.multiply(8));
                for (int i = 0; i < 30; i++) {
                    world.spawnParticles(ParticleTypes.SMOKE, behind.x, behind.y + RANDOM.nextDouble() * 2, behind.z, 1, 0.2, 0.0, 0.2, 0.01);
                }
                playAtPos(world, BlockPos.ofFloored(behind), SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 1.0f, 0.6f);
            }
        }
    }

    private static void triggerLevel4Effect(ServerPlayerEntity player, ServerWorld world) {
        switch (RANDOM.nextInt(3)) {
            case 0 -> {
                addEffect(player, StatusEffects.DARKNESS, 200, 0);
                addEffect(player, StatusEffects.SLOWNESS, 200, 1);
                playAtPlayer(player, SoundEvents.ENTITY_WARDEN_AMBIENT, SoundCategory.HOSTILE, 0.6f);
                chat(player, "§4§lIT KNOWS WHERE YOU ARE");
            }
            case 1 -> {
                addEffect(player, StatusEffects.NAUSEA, 200, 1);
                playAtPlayer(player, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 2.0f);
                chat(player, "§k||||||||||||||||||||");
            }
            case 2 -> {
                BlockPos pos = player.getBlockPos();
                for (int i = 0; i < 50; i++) {
                    world.spawnParticles(ParticleTypes.ENCHANT,
                            pos.getX() + (RANDOM.nextDouble() - 0.5) * 10,
                            pos.getY() + 10,
                            pos.getZ() + (RANDOM.nextDouble() - 0.5) * 10,
                            1, 0, -0.5, 0, 0.1);
                }
                chat(player, "§4§l6 7 6 7 6 7 6 7 6 7");
            }
        }
    }

    private static void triggerLevel5Effect(ServerPlayerEntity player, ServerWorld world) {
        addEffect(player, StatusEffects.DARKNESS, 300, 0);
        addEffect(player, StatusEffects.NAUSEA, 300, 1);
        addEffect(player, StatusEffects.SLOWNESS, 300, 2);

        playAtPlayer(player, SoundEvents.ENTITY_WARDEN_ROAR, SoundCategory.HOSTILE, 0.5f);

        BlockPos pos = player.getBlockPos();
        for (int i = 0; i < 100; i++) {
            world.spawnParticles(
                    RANDOM.nextBoolean() ? ParticleTypes.SMOKE : ParticleTypes.LARGE_SMOKE,
                    pos.getX() + (RANDOM.nextDouble() - 0.5) * 15,
                    pos.getY() + RANDOM.nextDouble() * 5,
                    pos.getZ() + (RANDOM.nextDouble() - 0.5) * 15,
                    1, 0, 0, 0, 0.05);
        }

        chat(player, "§4§k||||§r §c§lTHE TOMB CALLS§r §4§k||||");
    }
    private static void spawnTnt(ServerWorld world, Vec3d pos, int fuse) {
        TntEntity tnt = new TntEntity(world, pos.x, pos.y, pos.z, null);
        tnt.setFuse(fuse);
        world.spawnEntity(tnt);
        SixSeven.LOGGER.info("[SixSevenCurse] Spawned TNT at {}", pos);
    }


}
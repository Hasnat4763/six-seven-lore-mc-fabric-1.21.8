package net.hasnat4763.SixSevenCurse;

import net.hasnat4763.SixSeven;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class BookCurseManager {

    private static final Random RANDOM = new Random();

    public static void triggerTimedEffect(ServerPlayerEntity player, int curseLevel) {
        // Use the player's current dimension world (ServerWorld)
        ServerWorld world = player.getWorld();

        SixSeven.LOGGER.info("[SixSevenCurse] Trigger effect L{} for {}", curseLevel, player.getName().getString());

        switch (curseLevel) {
            case 1 -> triggerLevel1Effect(player, world);
            case 2 -> triggerLevel2Effect(player, world);
            case 3 -> triggerLevel3Effect(player, world);
            case 4 -> triggerLevel4Effect(player, world);
            case 5 -> triggerLevel5Effect(player, world);
            default -> triggerLevel0Effect(player, world);
        }
    }

    private static void triggerLevel0Effect(ServerPlayerEntity player, ServerWorld world) {
        int effect = RANDOM.nextInt(3);
        switch (effect) {
            case 0 -> player.sendMessage(Text.literal("§8§o...six..."), true);
            case 1 -> world.playSound(
                    null, player.getBlockPos(),
                    SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundCategory.AMBIENT, 0.5f, 0.5f
            );
            case 2 -> world.spawnParticles(
                    ParticleTypes.SMOKE,
                    player.getX() + RANDOM.nextInt(10) - 5,
                    player.getY(),
                    player.getZ() + RANDOM.nextInt(10) - 5,
                    6, 0.1, 0.0, 0.1, 0.01
            );
        }
    }

    private static void triggerLevel1Effect(ServerPlayerEntity player, ServerWorld world) {
        int effect = RANDOM.nextInt(4);
        switch (effect) {
            case 0 -> {
                player.sendMessage(Text.literal("§4§o...seven..."), true);
                world.playSound(
                        null, player.getBlockPos(),
                        SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundCategory.AMBIENT, 0.5f, 0.5f
                );
            }
            case 1 -> world.playSound(
                    null, player.getBlockPos(),
                    SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.AMBIENT, 1.0f, 0.5f
            );
            case 2 -> {
                Vec3d lookVec = player.getRotationVec(1.0f);
                Vec3d behind = player.getPos().subtract(lookVec.multiply(3));
                for (int i = 0; i < 12; i++) {
                    world.spawnParticles(
                            ParticleTypes.SMOKE,
                            behind.x, behind.y + 1, behind.z,
                            1, 0.2, 0.5, 0.2, 0.01
                    );
                }
            }
            case 3 -> player.sendMessage(Text.literal("§k||||§r §4Those who know§r §k||||"), true);
        }
    }

    private static void triggerLevel2Effect(ServerPlayerEntity player, ServerWorld world) {
        int effect = RANDOM.nextInt(5);
        switch (effect) {
            case 0 -> {
                Vec3d lookVec = player.getRotationVec(1.0f);
                Vec3d behind = player.getPos().subtract(lookVec.multiply(5));
                BlockPos soundPos = BlockPos.ofFloored(behind);
                world.playSound(
                        null, soundPos,
                        SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 1.0f, 0.8f
                );
            }
            case 1 -> player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.BLINDNESS, 40, 0, false, false // ~2s
            ));
            case 2 -> world.playSound(
                    null, player.getBlockPos(),
                    SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 0.8f, 1.0f
            );
            case 3 -> {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 60, 0, false, false // ~3s
                ));
                player.sendMessage(Text.literal("§b§oYou feel a sudden chill..."), true);
            }
            case 4 -> player.sendMessage(Text.literal("§4§o" + player.getName().getString() + "..."), true);
        }
    }

    private static void triggerLevel3Effect(ServerPlayerEntity player, ServerWorld world) {
        int effect = RANDOM.nextInt(4);
        switch (effect) {
            case 0 -> {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.DARKNESS, 100, 0, false, false // ~5s
                ));
                world.playSound(
                        null, player.getBlockPos(),
                        SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.AMBIENT, 1.0f, 0.8f
                );
            }
            case 1 -> {
                // Non-blocking “footstep” cluster
                float basePitch = 0.7f;
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 1.2f, basePitch);
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 1.2f, basePitch + 0.12f);
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 1.2f, basePitch + 0.24f);
            }
            case 2 -> {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.NAUSEA, 100, 0, false, false // ~5s
                ));
                world.playSound(
                        null, player.getBlockPos(),
                        SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0f, 1.0f
                );
            }
            case 3 -> {
                Vec3d lookVec = player.getRotationVec(1.0f);
                Vec3d behind = player.getPos().subtract(lookVec.multiply(8));
                for (int i = 0; i < 30; i++) {
                    world.spawnParticles(
                            ParticleTypes.SMOKE,
                            behind.x, behind.y + RANDOM.nextDouble() * 2, behind.z,
                            1, 0.2, 0.0, 0.2, 0.01
                    );
                }
                world.playSound(
                        null, BlockPos.ofFloored(behind),
                        SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 0.5f, 0.5f
                );
            }
        }
    }

    private static void triggerLevel4Effect(ServerPlayerEntity player, ServerWorld world) {
        int effect = RANDOM.nextInt(3);
        switch (effect) {
            case 0 -> {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.DARKNESS, 200, 0, false, false
                ));
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS, 200, 1, false, false
                ));
                world.playSound(
                        null, player.getBlockPos(),
                        SoundEvents.ENTITY_WARDEN_AMBIENT, SoundCategory.HOSTILE, 0.8f, 0.6f
                );
                player.sendMessage(Text.literal("§4§lIT KNOWS WHERE YOU ARE"), true);
            }
            case 1 -> {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.NAUSEA, 200, 1, false, false
                ));
                world.playSound(
                        null, player.getBlockPos(),
                        SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 0.5f, 2.0f
                );
                player.sendMessage(Text.literal("§k||||||||||||||||||||"), true);
            }
            case 2 -> {
                BlockPos pos = player.getBlockPos();
                for (int i = 0; i < 50; i++) {
                    world.spawnParticles(
                            ParticleTypes.ENCHANT,
                            pos.getX() + (RANDOM.nextDouble() - 0.5) * 10,
                            pos.getY() + 10,
                            pos.getZ() + (RANDOM.nextDouble() - 0.5) * 10,
                            1, 0, -0.5, 0, 0.1
                    );
                }
                player.sendMessage(Text.literal("§4§l6 7 6 7 6 7 6 7 6 7"), true);
            }
        }
    }

    private static void triggerLevel5Effect(ServerPlayerEntity player, ServerWorld world) {
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.DARKNESS, 300, 0, false, false
        ));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.NAUSEA, 300, 1, false, false
        ));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS, 300, 2, false, false
        ));

        world.playSound(
                null, player.getBlockPos(),
                SoundEvents.ENTITY_WARDEN_ROAR, SoundCategory.HOSTILE, 1.0f, 0.5f
        );

        BlockPos pos = player.getBlockPos();
        for (int i = 0; i < 100; i++) {
            world.spawnParticles(
                    RANDOM.nextBoolean() ? ParticleTypes.SMOKE : ParticleTypes.LARGE_SMOKE,
                    pos.getX() + (RANDOM.nextDouble() - 0.5) * 15,
                    pos.getY() + RANDOM.nextDouble() * 5,
                    pos.getZ() + (RANDOM.nextDouble() - 0.5) * 15,
                    1, 0, 0, 0, 0.05
            );
        }

        player.sendMessage(Text.literal("§4§k||||§r §c§lTHE TOMB CALLS§r §4§k||||"), true);
    }
}
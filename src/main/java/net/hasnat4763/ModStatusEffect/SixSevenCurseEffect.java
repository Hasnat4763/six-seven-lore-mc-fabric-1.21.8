package net.hasnat4763.ModStatusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class SixSevenCurseEffect extends StatusEffect {
    protected SixSevenCurseEffect() {
        super(StatusEffectCategory.HARMFUL, 0xe9b8b3, ParticleTypes.SMOKE);
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).sendMessage(Text.of("You are now Cursed by the Six Seven Monster"), true);
        }
        return super.applyUpdateEffect(world, entity, amplifier);
    }
}

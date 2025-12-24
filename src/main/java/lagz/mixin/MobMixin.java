package lagz.mixin;

import lagz.PassiveHealing;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.monster.Enemy;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends @NotNull LivingEntity> entityType, net.minecraft.world.level.Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "aiStep", at = @At("TAIL"))
    private void heal(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            if (!(this instanceof Enemy || (Object) this instanceof AbstractGolem || (Object) this instanceof HappyGhast || (Object) this instanceof Allay)) {
                if (this.isAlive()) {
                    if (this.tickCount % PassiveHealing.HEALING_TICKS == 0) {
                        this.heal(1.0F);
                    }
                }
            }
        }
    }
}
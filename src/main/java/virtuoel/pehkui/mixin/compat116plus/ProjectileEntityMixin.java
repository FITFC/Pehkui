package virtuoel.pehkui.mixin.compat116plus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin
{
	@ModifyArg(method = "shouldLeaveOwner", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private Box pehkui$shouldLeaveOwner$expand(Box value)
	{
		final float width = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		final float height = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (width != 1.0F || height != 1.0F)
		{
			return value.expand(width - 1.0D, height - 1.0D, width - 1.0D);
		}
		
		return value;
	}
	
	@ModifyArg(method = "setVelocity(DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"))
	private double pehkui$setVelocity$multiply(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
	
	@Inject(at = @At("HEAD"), method = "setOwner")
	private void pehkui$setOwner(@Nullable Entity entity, CallbackInfo info)
	{
		if (entity != null)
		{
			ScaleUtils.setScaleOfProjectile((Entity) (Object) this, entity);
		}
	}
}

package com.forever.mixin;

import com.forever.OtherAnimations;
import com.forever.module.modules.Animations;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(value = {EntityLivingBase.class}, priority = 9999)
public class MixinEntityLivingBase {
    @Shadow public boolean isSwingInProgress;
    @Shadow public int swingProgressInt;
    @Shadow(aliases = {"func_70644_a"}) public boolean isPotionActive(Potion potionIn) { return false; }
    @Shadow(aliases = {"func_70660_b"}) public PotionEffect getActivePotionEffect(Potion potionIn) { return null; }
    @Inject(method = "swingItem", at = @At("HEAD"), cancellable = true)
    private void preventSwingAbort(CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            if (Animations.forceSwing) {
                Animations.forceSwing = false;
                return;
            }
            int end = this.getArmSwingAnimationEnd();
            int threshold = Math.max(1, end * 3 / 5);
            if (this.isSwingInProgress && this.swingProgressInt >= 0
                    && this.swingProgressInt < threshold) {
                ci.cancel();
            }
        }
    }

    @Overwrite
    private int getArmSwingAnimationEnd() {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            return Math.max(1, (int) (float) anim.swingSpeed.getValue());
        }
        return this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
    }
}

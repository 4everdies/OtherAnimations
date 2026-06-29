package com.forever.mixin;

import com.forever.OtherAnimations;
import com.forever.module.modules.Animations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Unique private float anim$f1 = 0.0F;
    @Unique private float anim$spin = 0.0F;
    @Shadow protected abstract void transformFirstPersonItem(float equipProgress, float swingProgress);
    @Shadow protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);
    @Shadow @Final private Minecraft mc;
    @Shadow private int equippedItemSlot;
    @Shadow private ItemStack itemToRender;
    @Shadow private float equippedProgress;
    @Shadow private float prevEquippedProgress;
    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V", ordinal = 2))
    private void anim$skipTransform(ItemRenderer instance, float equipProgress, float swingProgress) {
        Animations anim = OtherAnimations.animations;
        if (anim == null || !anim.isEnabled()) {
            this.transformFirstPersonItem(equipProgress, swingProgress);
        }
    }
    @ModifyVariable(method = "renderItemInFirstPerson", at = @At(value = "STORE"), index = 4)
    private float anim$captureF1(float f1) {
        anim$f1 = f1;
        return f1;
    }
    @ModifyArg(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V"),
               slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;performDrinking(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V"),
                              to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;doBowTransformations(FLnet/minecraft/client/entity/AbstractClientPlayer;)V")),
               index = 1)
    private float anim$useF1(float swingProgress) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            return anim$f1;
        }
        return swingProgress;
    }

    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;rotateWithPlayerRotations(Lnet/minecraft/client/entity/EntityPlayerSP;F)V"))
    private void anim$removeRotations(ItemRenderer instance, EntityPlayerSP entityPlayerSP, float v) {
        Animations anim = OtherAnimations.animations;
        if (anim == null || !anim.isEnabled() || !anim.disableHandSway.getValue()) {
            rotateWithPlayerRotations(entityPlayerSP, v);
        }
    }

    @Inject(method = "doBowTransformations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"))
    private void anim$preBowTransform(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            GlStateManager.rotate(-335.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-50.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.5F, 0.0F);
        }
    }

    @Inject(method = "doBowTransformations", at = @At("TAIL"))
    private void anim$postBowTransform(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            GlStateManager.translate(0.0F, -0.5F, 0.0F);
            GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    @Inject(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"))
    private void anim$firstPersonItemPositions(float partialTicks, CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && itemToRender != null) {
            if (itemToRender.getItem().shouldRotateAroundWhenRendering() && !(itemToRender.getItem() instanceof ItemFishingRod)) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                anim$itemTransforms();
            } else if (!(itemToRender.getItem() instanceof ItemSword)) {
                anim$itemTransforms();
            }
        }
    }

    @Unique
    private static void anim$itemTransforms() {
        float scale = 1.5F / 1.7F;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(5.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.29F, 0.149F, -0.0328F);
    }

    @ModifyConstant(method = "updateEquippedItem", constant = @Constant(floatValue = 0.4F))
    private float anim$changeEquipSpeed(float original) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            if (anim.itemSwitchMode.getValue() == 1) {
                return 0.4F;
            }
        }
        return original;
    }

    @Inject(method = "resetEquippedProgress", at = @At("HEAD"), cancellable = true)
    private void anim$disableReEquip1(CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && anim.itemSwitchMode.getValue() == 0) {
            ci.cancel();
        }
    }

    @Inject(method = "resetEquippedProgress2", at = @At("HEAD"), cancellable = true)
    private void anim$disableReEquip2(CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && anim.itemSwitchMode.getValue() == 0) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "updateEquippedItem", at = @At(value = "STORE", ordinal = 3), index = 3)
    public boolean anim$disableReEquip(boolean flag) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && anim.itemSwitchMode.getValue() == 0) {
            this.itemToRender = this.mc.thePlayer.inventory.getCurrentItem();
            this.equippedItemSlot = this.mc.thePlayer.inventory.currentItem;
            return false;
        }
        return flag;
    }

    @Inject(method = "renderItemInFirstPerson", at = @At("HEAD"))
    public void anim$applyViewmodelTransforms(float partialTicks, CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            GL11.glTranslatef(anim.posX.getValue(), anim.posY.getValue(), anim.posZ.getValue());
            GL11.glRotatef(anim.rotX.getValue(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(anim.rotY.getValue(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(anim.rotZ.getValue(), 0.0F, 0.0F, 1.0F);
        }
    }

    @Inject(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;doBlockTransformations()V"))
    public void anim$applyAnimation(float partialTicks, CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim == null || !anim.isEnabled()) return;
        float equipProgress = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer player = this.mc.thePlayer;
        float swingProgress = player.getSwingProgress(partialTicks);
        float sine = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        float sqrtSwing = MathHelper.sqrt_float(swingProgress);
        float sine1 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        String mode = anim.getCurrentMode();
        switch (mode) {
            case "EXHIBITION":
                GL11.glTranslated(0, -0.1, 0);
                this.transformFirstPersonItem(equipProgress / 2, 0.0F);
                GL11.glTranslatef(0.1F, 0.4F, -0.1F);
                GL11.glRotated(-sine * 30.0F, sine / 2, 0.0F, 9.0F);
                GL11.glRotated(-sine * 50.0F, 0.8F, sine / 2, 0F);
                break;
            case "SIGMA":
                this.transformFirstPersonItem(equipProgress * 0.5f, 0);
                GL11.glRotated(-sine * 27.5F, -8.0F, -0.0F, 9.0F);
                GL11.glRotated(-sine * 45, 1.0F, sine / 2, -0.0F);
                GL11.glTranslated(-0.1, 0.3, 0.1);
                break;
            case "VANILLA":
                GL11.glTranslated(0, 0.05, -0.1);
                this.transformFirstPersonItem(equipProgress, swingProgress);
                break;
            case "1.7":
                this.transformFirstPersonItem(equipProgress, swingProgress);
                break;
            case "SPIN":
                GL11.glRotated(this.anim$spin, 0f, 0f, -0.1f);
                this.transformFirstPersonItem(equipProgress, 0f);
                this.anim$spin = -(System.currentTimeMillis() / 2 % 360);
                break;
            case "ETB":
                GL11.glTranslated(0, -0.1, 0);
                this.transformFirstPersonItem(equipProgress, 0.0F);
                GL11.glTranslatef(0.1F, 0.4F, -0.1F);
                GL11.glRotated(-sine * 35f, -8f, -0f, 9f);
                GL11.glRotated(-sine * 70, 1.5f, -0.4f, -0f);
                break;
            case "SLIDE":
                GL11.glTranslated(-0.1D, 0.15D, 0.0D);
                this.transformFirstPersonItem(0, 0);
                float slideSine = MathHelper.sin(sqrtSwing * 2.9415927f);
                GL11.glTranslatef(-0.05f, -0.0f, 0.35f);
                GL11.glRotated(-slideSine * 30.0f, -15.0f, slideSine, 10);
                GL11.glRotated(-slideSine * 70.0, 5.0f, -slideSine, -0);
                break;
            case "SWONG":
                this.transformFirstPersonItem(equipProgress / 2.0F, 0.0F);
                GL11.glRotated(-sine * 20.0F, sine / 2.0F, -0.0F, 9.0F);
                GL11.glRotated(-sine * 30.0F, 1.0F, sine / 2.0F, -0.0F);
                break;
            case "SWANG":
                this.transformFirstPersonItem(equipProgress / 2.0F, swingProgress);
                GL11.glRotated(sine * 15.0F, -sine, -0.0F, 9.0F);
                GL11.glRotated(sine * 40.0F, 1.0F, -sine / 2.0F, -0.0F);
                break;
            case "PUNCH":
                this.transformFirstPersonItem(equipProgress, 0.0f);
                GL11.glTranslatef(0.1f, 0.2f, 0.3f);
                GL11.glRotated(-sine * 30.0f, -5.0f, 0.0f, 9.0F);
                GL11.glRotated(-sine * 10.0f, 1.0F, -0.4f, -0.5f);
                break;
            default:
                GL11.glTranslated(0, 0.05, -0.1);
                this.transformFirstPersonItem(equipProgress, swingProgress);
                break;
        }
    }
    @Inject(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V", shift = At.Shift.BEFORE))
    public void anim$applyScale(float partialTicks, CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled()) {
            double scale = (double) anim.scale.getValue() / 100.0;
            GL11.glScaled(scale, scale, scale);
        }
    }
}
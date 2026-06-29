package com.forever.mixin;

import com.forever.OtherAnimations;
import com.forever.module.modules.Animations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(value = Minecraft.class, priority = 9999)
public abstract class MixinMinecraft {

    @Shadow public PlayerControllerMP playerController;
    @Shadow public WorldClient theWorld;
    @Shadow public EntityPlayerSP thePlayer;
    @Shadow public MovingObjectPosition objectMouseOver;
    @Shadow public GameSettings gameSettings;
    @Shadow private int leftClickCounter;
    @Shadow public EffectRenderer effectRenderer;

    @Inject(method = "rightClickMouse", at = @At("HEAD"))
    private void anim$rightClickMouse(CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && anim$hasUseAction() && gameSettings.keyBindUseItem.isKeyDown()) {
            playerController.resetBlockRemoving();
        }
    }

    @Redirect(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;clickBlock(Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z"))
    private boolean anim$preventMiningWhenUsing(PlayerControllerMP instance, BlockPos pos, EnumFacing facing) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && anim$hasUseAction() && gameSettings.keyBindUseItem.isKeyDown()) {
            return false;
        }
        return instance.clickBlock(pos, facing);
    }

    @Redirect(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;swingItem()V"))
    private void anim$swingRedirect(EntityPlayerSP instance) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && anim$hasUseAction() && gameSettings.keyBindUseItem.isKeyDown()) {
            anim$clientSwing();
        } else {
            instance.swingItem();
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"), cancellable = true)
    private void anim$sendClickBlockToController(boolean leftClick, CallbackInfo ci) {
        Animations anim = OtherAnimations.animations;
        if (anim != null && anim.isEnabled() && gameSettings.keyBindUseItem.isKeyDown()) {
            if (leftClick && objectMouseOver != null
                    && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                    && thePlayer != null && thePlayer.isUsingItem()) {
                BlockPos pos = objectMouseOver.getBlockPos();
                if (theWorld != null && !theWorld.isAirBlock(pos)) {
                    effectRenderer.addBlockHitEffects(pos, objectMouseOver.sideHit);
                    anim$clientSwingVisual();
                }
            }
        }
    }

    @Unique
    private boolean anim$hasUseAction() {
        if (thePlayer == null || thePlayer.getHeldItem() == null) return false;
        ItemStack held = thePlayer.getHeldItem();
        return held.getItemUseAction() != EnumAction.NONE || held.getItem() instanceof ItemBlock;
    }

    @Unique
    private void anim$clientSwing() {
        if (thePlayer == null) return;
        Animations anim = OtherAnimations.animations;
        int animEnd = anim != null && anim.isEnabled() ? (int)(float)anim.swingSpeed.getValue() : 6;
        if (thePlayer.isSwingInProgress && thePlayer.swingProgressInt >= 0 && thePlayer.swingProgressInt < animEnd - 3) return;
        thePlayer.swingProgressInt = -1;
        thePlayer.isSwingInProgress = true;
        thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C0APacketAnimation());
    }

    @Unique
    private void anim$clientSwingVisual() {
        if (thePlayer == null) return;
        Animations anim = OtherAnimations.animations;
        int animEnd = anim != null && anim.isEnabled() ? (int)(float)anim.swingSpeed.getValue() : 6;
        if (thePlayer.isSwingInProgress && thePlayer.swingProgressInt >= 0 && thePlayer.swingProgressInt < animEnd - 3) return;
        thePlayer.swingProgressInt = -1;
        thePlayer.isSwingInProgress = true;
    }
}
package io.pinpal.blockoutlines.mixin;

import io.pinpal.blockoutlines.SecondaryOutlineRenderType;
import io.pinpal.blockoutlines.config.BlockOutlinesConfig;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    private ClientLevel level;

    @Shadow
    private static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape,
            double posX, double posY, double posZ, float colorR, float colorG, float colorB, float colorA) {
    };

    @Inject(method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"), cancellable = true)
    private void replaceHitOutlineColor(
            PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity,
            double d, double e, double f, BlockPos blockPos, BlockState blockState,
            CallbackInfo ci) {

        // Check if the block outline is enabled
        if (!BlockOutlinesConfig.ENABLED.get()) {
            return;
        }

        renderShape(poseStack, vertexConsumer,
                blockState.getShape(this.level, blockPos, CollisionContext.of(entity)),
                (double) blockPos.getX() - d, (double) blockPos.getY() - e, (double) blockPos.getZ() - f,
                BlockOutlinesConfig.innerColor.getRed(), BlockOutlinesConfig.innerColor.getGreen(),
                BlockOutlinesConfig.innerColor.getBlue(), BlockOutlinesConfig.innerColor.getAlpha());

        ci.cancel();
    }

    // TODO: Consider using https://github.com/LlamaLad7/MixinExtras to avoid capturing unused locals!
    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderBlockOutline(
            // Native Parameters
            PoseStack poseStack, float f, long l, boolean b, Camera camera,
            GameRenderer gameRenderer, LightTexture lightTexure, Matrix4f matrix4f,
            // Callback
            CallbackInfo ci,
            // Local Variable Capture
            ProfilerFiller profilerFiller, Vec3 vec3, double xPos, double yPos, double zPos, Matrix4f matrix4f2,
            boolean b2, Frustum frustum, float f2, boolean b3, boolean b4, BufferSource bufferSource,
            HitResult hitResult, BlockPos blockPos, BlockState blockState) {

        // Check if the block outline is enabled
        if (!BlockOutlinesConfig.ENABLED.get()) {
            return;
        }

        // Draw the border outline
        VertexConsumer vertexConsumer = bufferSource.getBuffer(SecondaryOutlineRenderType.SECONDARY_BLOCK_OUTLINE);
        renderShape(poseStack, vertexConsumer,
                blockState.getShape(this.level, blockPos, CollisionContext.of(camera.getEntity())),
                (double) blockPos.getX() - xPos, (double) blockPos.getY() - yPos, (double) blockPos.getZ() - zPos,
                BlockOutlinesConfig.outerColor.getRed(), BlockOutlinesConfig.outerColor.getGreen(),
                BlockOutlinesConfig.outerColor.getBlue(), BlockOutlinesConfig.outerColor.getAlpha());

        // Reset vertexConsumer to the original state for the next render call 
        vertexConsumer = bufferSource.getBuffer(RenderType.lines());
    }
}

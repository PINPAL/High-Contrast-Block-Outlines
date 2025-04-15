package com.example.examplemod.mixin;

import com.example.examplemod.CustomRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.RenderBuffers;
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
import org.lwjgl.system.Callback;
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
    private static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d,
            double d2, double d3, float f, float f2, float f3, float f4) {
    };

    @Inject(method = "renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDFFFF)V", at = @At("HEAD"), cancellable = true)
    private static void logRenderShapeCall(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape,
            double d,
            double d2, double d3, float colorR, float colorG, float colorB, float colorA, CallbackInfo ci) {
        System.out.println("Render Shape with color: " + colorR + ", " + colorG + ", " + colorB + ", " + colorA
                + " with " + vertexConsumer);
    }

    @Inject(method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"), cancellable = true)
    private void replaceHitOutlineColor(PoseStack poseStack,
            VertexConsumer vertexConsumer, Entity entity,
            double d, double e, double f, BlockPos blockPos, BlockState blockState,
            CallbackInfo ci) {

        renderShape(poseStack, vertexConsumer,
                blockState.getShape(this.level, blockPos, CollisionContext.of(entity)),
                (double) blockPos.getX() - d,
                (double) blockPos.getY() - e, (double) blockPos.getZ() - f, 1.0f, 0.0f, 0.0f,
                1.0f);

        ci.cancel();
    }

    // @Shadow
    // private void renderHitOutline(PoseStack p_109638_, VertexConsumer p_109639_,
    // Entity p_109640_, double p_109641_,
    // double p_109642_, double p_109643_, BlockPos p_109644_, BlockState p_109645_)
    // {
    // };

    // TODO: Consider using MixinExtras to avoid capturing unused locals:
    // TODO: https://github.com/LlamaLad7/MixinExtras
    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderBlockOutline(PoseStack poseStack, float f, long l, boolean b, Camera camera,
            GameRenderer gameRenderer, LightTexture lightTexure, Matrix4f matrix4f,
            // Callback
            CallbackInfo ci,
            // Local Variable Capture
            ProfilerFiller profilerFiller, Vec3 vec3, double xPos, double yPos, double zPos, Matrix4f matrix4f2,
            boolean b2,
            Frustum frustum, float f2, boolean b3, boolean b4, BufferSource bufferSource,
            HitResult hitResult, BlockPos blockPos, BlockState blockState
    // NOT CAPTURED BUT AVAILABLE:
    // Lcom/mojang/blaze3d/vertex/VertexConsumer;,
    // Lnet/minecraft/world/level/block/entity/BlockEntity;,
    // D,
    // Lnet/minecraft/client/renderer/MultiBufferSource;,
    // D,
    // I,
    // Ljava/util/SortedSet;,
    // I,
    // Lcom/mojang/blaze3d/vertex/PoseStack$Pose;,
    // Lcom/mojang/blaze3d/vertex/SheetedDecalTextureGenerator;,
    // Lnet/minecraftforge/client/model/data/ModelData;
    ) {

        VertexConsumer vertexConsumer = bufferSource.getBuffer(CustomRenderType.SECONDARY_BLOCK_OUTLINE);
        // renderHitOutline(poseStack, vertexConsumer, camera.getEntity(), xPos, yPos,
        // zPos, blockPos, blockState);
        renderShape(poseStack, vertexConsumer,
                blockState.getShape(this.level, blockPos, CollisionContext.of(camera.getEntity())),
                (double) blockPos.getX() - xPos,
                (double) blockPos.getY() - yPos, (double) blockPos.getZ() - zPos, 0.0f, 1.0f, 0.0f, 1.0f);
    }
}

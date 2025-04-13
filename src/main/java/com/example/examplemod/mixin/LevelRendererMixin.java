package com.example.examplemod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.client.multiplayer.ClientLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    private ClientLevel level;

    @Shadow
    private static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d,
            double d2, double d3, float f, float f2, float f3, float f4) {
    };

    @Inject(method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"), cancellable = true)
    private void selectivebounds$renderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity,
            double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {

        renderShape(poseStack, vertexConsumer,
                blockState.getShape(this.level, blockPos, CollisionContext.of(entity)),
                (double) blockPos.getX() - d,
                (double) blockPos.getY() - e, (double) blockPos.getZ() - f, 1.0f, 0.0f, 0.0f, 0.4f);

        // FIXME: We need to move to using a new method for injection so that we can
        // just call renderHitOutline itself
        // with different parameters (including the new vertexConsumer of
        // secondaryBlockOutline RenderType)
        renderShape(poseStack, vertexConsumer,
                blockState.getShape(this.level, blockPos, CollisionContext.of(entity)),
                (double) blockPos.getX() - (d * 2),
                (double) blockPos.getY() - (e * 2), (double) blockPos.getZ() - f, 0.0f, 0.0f, 1.0f, 1.0f);

        ci.cancel();
    }
}

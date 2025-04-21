package io.pinpal.blockoutlines.mixin;

import io.pinpal.blockoutlines.SecondaryOutlineRenderType;
import io.pinpal.blockoutlines.config.BlockOutlinesConfig;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    private ClientLevel level;

    @Shadow
    private static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape,
                                    double posX, double posY, double posZ, float colorR, float colorG, float colorB, float colorA) {
    }

    @Desc(id = "renderBlockOutline.at", value = "renderHitOutline", args = {PoseStack.class, VertexConsumer.class, Entity.class, double.class, double.class, double.class, BlockPos.class, BlockState.class})
    @Desc(id = "renderBlockOutline.method", value = "renderLevel", args = {PoseStack.class, float.class, long.class, boolean.class, Camera.class, GameRenderer.class, LightTexture.class, Matrix4f.class})
    @Redirect(method = "@Desc(renderBlockOutline.method)", at = @At(value = "INVOKE", target = "@Desc(renderBlockOutline.at)"))
    private void renderBlockOutline(
        LevelRenderer instance, PoseStack pPoseStack, VertexConsumer pConsumer, Entity pEntity,
        double pCamX, double pCamY, double pCamZ, BlockPos pPos, BlockState pState,
        @SuppressWarnings("LocalMayBeArgsOnly") @Local(ordinal = 0) BufferSource bufferSource
    ) {
        // Default color for the inner line
        float innerColorR = 0.0f, innerColorG = 0.0f, innerColorB = 0.0f;
        float innerColorA = 0.4f;

        // Check the Block Outline is enabled
        if (BlockOutlinesConfig.isEnabled) {
            // Draw the border outline
            VertexConsumer secondaryConsumer = bufferSource.getBuffer(SecondaryOutlineRenderType.SECONDARY_BLOCK_OUTLINE);
            renderShape(pPoseStack, secondaryConsumer,
                pState.getShape(this.level, pPos, CollisionContext.of(pEntity)),
                (double) pPos.getX() - pCamX, (double) pPos.getY() - pCamY, (double) pPos.getZ() - pCamZ,
                BlockOutlinesConfig.outerColor.getRed(), BlockOutlinesConfig.outerColor.getGreen(),
                BlockOutlinesConfig.outerColor.getBlue(), BlockOutlinesConfig.outerColor.getAlpha());
            // Update the inner color to match our config
            innerColorR = BlockOutlinesConfig.innerColor.getRed();
            innerColorG = BlockOutlinesConfig.innerColor.getGreen();
            innerColorB = BlockOutlinesConfig.innerColor.getBlue();
            innerColorA = BlockOutlinesConfig.innerColor.getAlpha();
        }

        // Draw the inner line
        renderShape(pPoseStack, pConsumer,
            pState.getShape(this.level, pPos, CollisionContext.of(pEntity)),
            (double) pPos.getX() - pCamX, (double) pPos.getY() - pCamY, (double) pPos.getZ() - pCamZ,
            innerColorR, innerColorG, innerColorB, innerColorA);
    }
}

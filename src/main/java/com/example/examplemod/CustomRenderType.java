package com.example.examplemod;

import java.util.OptionalDouble;

import com.example.examplemod.mixin.RenderStateShardAccessor;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.GameRenderer;

public class CustomRenderType {
    public static final RenderType SECONDARY_BLOCK_OUTLINE = RenderType.create(
            "secondary_block_outline",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeLinesShader))
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(7.0)))
                    .setLayeringState(RenderStateShardAccessor.getViewOffsetZLayering())
                    .setTransparencyState(RenderStateShardAccessor.getTranslucentTransparency())
                    .setOutputState(RenderStateShardAccessor.getItemEntityTarget())
                    .setWriteMaskState(RenderStateShardAccessor.getColorDepthWrite())
                    .setCullState(RenderStateShardAccessor.getNoCull())
                    .createCompositeState(false));
}

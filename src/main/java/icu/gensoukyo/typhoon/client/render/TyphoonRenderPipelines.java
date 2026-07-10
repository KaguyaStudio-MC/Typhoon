package icu.gensoukyo.typhoon.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import icu.gensoukyo.typhoon.Typhoon;
import net.minecraft.client.renderer.RenderPipelines;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

public final class TyphoonRenderPipelines {

    public static final RenderPipeline TYPHOON = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withLocation(Typhoon.id("pipeline/typhoon"))
            .withVertexShader("core/position_tex")
            .withFragmentShader("core/position_tex")
            .withSampler("Sampler0")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
            .build();


    @EventBusSubscriber(value = Dist.CLIENT, modid = Typhoon.MODID)
    private static final class Handler {

        @SubscribeEvent
        public static void onRegisterRenderPipelines(final RegisterRenderPipelinesEvent event) {
            event.registerPipeline(TYPHOON);
        }

    }

}

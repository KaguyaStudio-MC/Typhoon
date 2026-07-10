package icu.gensoukyo.typhoon.client.event;


import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import icu.gensoukyo.typhoon.Typhoon;
import icu.gensoukyo.typhoon.client.render.TyphoonRenderer;
import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RegisterCustomEnvironmentEffectRendererEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStoppingEvent;
import org.joml.*;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@EventBusSubscriber(value = Dist.CLIENT)
public class LevelRenderEventHandler {

    @SubscribeEvent
    public static void onRenderAfterSky(RenderLevelStageEvent.AfterSky event){
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();

        final TyphoonEntity entity = TyphoonEntity.INSTANCE;
        // todo: remove "|| true" when the typhoon is ready
        if (entity != null || true) {
            TyphoonRenderer.getInstance().render(
                    new PoseStack(),
                    modelViewStack,
                    entity
            );
        }

//        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
//        modelViewStack.pushMatrix();
//        modelViewStack.mul(poseStack.last().pose());
//        modelViewStack.translate(0.0F, 100.0F, 0.0F);
//        modelViewStack.scale(30.0F, 1.0F, 30.0F);
//        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(modelViewStack, new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness), new Vector3f(), new Matrix4f());
//        GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
//        GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
//        GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);
//
//        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky sun", color, OptionalInt.empty(), depth, OptionalDouble.empty())) {
//            renderPass.setPipeline(RenderPipelines.GUI_TEXTURED);
//            RenderSystem.bindDefaultUniforms(renderPass);
//            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
//            renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
//            renderPass.setVertexBuffer(0, this.sunBuffer);
//            renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
//            renderPass.drawIndexed(0, 0, 6, 1);
//        }

//        modelViewStack.popMatrix();
    }

    @SubscribeEvent
    public static void onClientStarted(final ClientStartedEvent event) {

        TyphoonRenderer.init(event.getClient());

    }

    @SubscribeEvent
    public static void onStoppingClient(final ClientStoppingEvent event) {

        TyphoonRenderer.cleanup();

    }
}

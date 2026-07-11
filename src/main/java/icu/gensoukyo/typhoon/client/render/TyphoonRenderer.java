package icu.gensoukyo.typhoon.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import icu.gensoukyo.typhoon.Typhoon;
import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.joml.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.Math;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class TyphoonRenderer implements AutoCloseable {

    private static final Identifier TYPHOON_LOCATION = Typhoon.id("textures/environment/cloud.png");

    private static TyphoonRenderer instance;

    // generally, it should not be null, if get it at the right time (after Minecraft init)
    public static TyphoonRenderer getInstance() {
        return instance;
    }

    public static void init(final Minecraft minecraft) {
        final var textureManager    = minecraft.getTextureManager();

        instance                    = new TyphoonRenderer(textureManager);
    }

    public static void cleanup() {
        if (instance != null) {
            instance.close();
        }
        instance = null;
    }

    private final GpuBuffer                             typhoonBuffer;
    private final AbstractTexture                       typhoonTexture;
    private final RenderSystem.AutoStorageIndexBuffer   quadIndices;

    private TyphoonRenderer(
            final TextureManager textureManager
    ) {

        this.typhoonBuffer      = this.buildTyphoonBuffer();
        this.typhoonTexture     = textureManager.getTexture(TYPHOON_LOCATION);
        this.quadIndices        = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);

    }

    public void render(
            final PoseStack     poseStack,
            final Matrix4fStack modelViewStack,
            final TyphoonEntity entity
    ) {

        final var minecraft                 = Minecraft.getInstance();
        final var camera                    = minecraft.gameRenderer.getMainCamera();
        final var offset                    = camera.position();

        // todo: get position from the typhoon
        final float tx                      = (float) entity.x;
        final float ty                      = (float) entity.height;
        final float tz                      = (float) entity.z;

        final float x                       = tx - (float) offset.x;
        final float y                       = ty - (float) offset.y;
        final float z                       = tz - (float) offset.z;

        long millis = System.currentTimeMillis();

        poseStack.translate(x, y, z);
        poseStack.mulPose(new Quaternionf().rotateY((float) (((float) (millis % 1000000) /100000)%2 * 2* Math.PI)));
        poseStack.scale((float) entity.r/2, 256.0f, (float) entity.r/2);

        modelViewStack                      .pushMatrix();
        modelViewStack                      .mul(poseStack.last().pose());

        GpuBufferSlice dynamicTransforms    = RenderSystem.getDynamicUniforms()
                                            .writeTransform(
                                                    modelViewStack,
                                                    new Vector4f(1.0F),
                                                    new Vector3f(),
                                                    new Matrix4f()
                                            );

        GpuTextureView color                = minecraft.getMainRenderTarget().getColorTextureView();
        GpuTextureView depth                = minecraft.getMainRenderTarget().getDepthTextureView();
        GpuBuffer indexBuffer               = this.quadIndices.getBuffer(6);

        try (RenderPass renderPass          = RenderSystem
                                            .getDevice()
                                            .createCommandEncoder()
                                            .createRenderPass(
                                                    () -> "Typhoon",
                                                    color,
                                                    OptionalInt.empty(),
                                                    depth,
                                                    OptionalDouble.empty()
                                            )
        ) {
            renderPass      .setPipeline(TyphoonRenderPipelines.TYPHOON);
            RenderSystem    .bindDefaultUniforms(renderPass);
            renderPass      .setUniform("DynamicTransforms", dynamicTransforms);
            renderPass      .bindTexture(
                                    "Sampler0",
                                    this.typhoonTexture.getTextureView(),
                                    this.typhoonTexture.getSampler()
                            );
            renderPass      .setVertexBuffer(0, this.typhoonBuffer);
            renderPass      .setIndexBuffer(indexBuffer, this.quadIndices.type());
            renderPass      .drawIndexed(0, 0, 12, 1);
        }

        modelViewStack.popMatrix();

    }

    private GpuBuffer buildTyphoonBuffer() {

        VertexFormat format = DefaultVertexFormat.POSITION_TEX;

        GpuBuffer buffer;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * format.getVertexSize()*2)) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, format);
            bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(0.0f, 0.0f);
            bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(1.0f, 0.0f);
            bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(1.0f, 1.0f);
            bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(0.0f, 1.0f);

            bufferBuilder.addVertex(-1.0F, 0.0F,  1.0F).setUv(0.0f, 1.0f);
            bufferBuilder.addVertex( 1.0F, 0.0F,  1.0F).setUv(1.0f, 1.0f);
            bufferBuilder.addVertex( 1.0F, 0.0F, -1.0F).setUv(1.0f, 0.0f);
            bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(0.0f, 0.0f);

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                buffer = RenderSystem.getDevice().createBuffer(() -> "Typhoon", 32, mesh.vertexBuffer());
            }
        }

        return buffer;

    }


    @Override
    public void close() {
        this.typhoonBuffer.close();
        this.typhoonTexture.close();
    }
}

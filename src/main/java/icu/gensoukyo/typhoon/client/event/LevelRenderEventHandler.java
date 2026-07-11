package icu.gensoukyo.typhoon.client.event;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import icu.gensoukyo.typhoon.Typhoon;
import icu.gensoukyo.typhoon.client.render.DebugHud;
import icu.gensoukyo.typhoon.client.render.TyphoonRenderer;
import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStoppingEvent;
import org.joml.Matrix4fStack;

import static net.neoforged.neoforge.client.gui.VanillaGuiLayers.CROSSHAIR;

@EventBusSubscriber(value = Dist.CLIENT)
public class LevelRenderEventHandler {

    @SubscribeEvent
    public static void onRenderAfterSky(RenderLevelStageEvent.AfterSky event){
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();

        final TyphoonEntity entity = TyphoonEntity.INSTANCE;

        if (entity != null) {
            TyphoonRenderer.getInstance().render(
                    new PoseStack(),
                    modelViewStack,
                    entity
            );
        }

    }

    @SubscribeEvent
    public static void onClientStarted(final ClientStartedEvent event) {

        TyphoonRenderer.init(event.getClient());

    }

    @SubscribeEvent
    public static void onStoppingClient(final ClientStoppingEvent event) {

        TyphoonRenderer.cleanup();

    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(CROSSHAIR, Typhoon.id("typhoon"), new DebugHud());
    }
}

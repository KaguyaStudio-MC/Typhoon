package icu.gensoukyo.typhoon.client.render;

import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class DebugHud implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Font font = Minecraft.getInstance().font;
        ClientLevel level = Minecraft.getInstance().level;
        int x = 10;
        int y = 10;
        if (TyphoonEntity.INSTANCE == null) {
            return;
        }
        TyphoonEntity typhoon = TyphoonEntity.INSTANCE;
        guiGraphics.text(font, "x " + typhoon.x, x, y + 10 * 0, -1, false);
        guiGraphics.text(font, "z " + typhoon.z, x, y + 10 * 1, -1, false);
        guiGraphics.text(font, "v " + typhoon.v, x, y + 10 * 2, -1, false);
        guiGraphics.text(font, "vx " + typhoon.vx, x, y + 10 * 3, -1, false);
        guiGraphics.text(font, "vz " + typhoon.vz, x, y + 10 * 4, -1, false);
        guiGraphics.text(font, "miny " + typhoon.miny, x, y + 10 * 5, -1, false);
        guiGraphics.text(font, "height " + typhoon.height, x, y + 10 * 6, -1, false);
        guiGraphics.text(font, "factor " + typhoon.factor, x, y + 10 * 7, -1, false);
        guiGraphics.text(font, "hFactor " + typhoon.hFactor, x, y + 10 * 8, -1, false);
        guiGraphics.text(font, "growSpeed " + typhoon.growSpeed, x, y + 10 * 9, -1, false);
        guiGraphics.text(font, "baseGrownFactor " + typhoon.baseGrownFactor, x, y + 10 * 10, -1, false);
        guiGraphics.text(font, "grownFactor " + typhoon.grownFactor, x, y + 10 * 11, -1, false);
        guiGraphics.text(font, "maxGrownFactor " + typhoon.maxGrownFactor, x, y + 10 * 12, -1, false);
        guiGraphics.text(font, "damage " + typhoon.damage, x, y + 10 * 13, -1, false);
        guiGraphics.text(font, "paused " + typhoon.paused, x, y + 10 * 14, -1, false);
        guiGraphics.text(font, "r " + typhoon.r, x, y + 10 * 15, -1, false);
        if (level != null && typhoon.lockedUUID != null) {
            Player playerByUUID = level.getPlayerByUUID(typhoon.lockedUUID);
            Component displayName = Component.literal("无");
            if(playerByUUID !=null) {
                displayName = playerByUUID.getDisplayName();
            }
            guiGraphics.text(font, Component.literal("locked ").append(displayName), x, y + 10 * 16, -1, false);
        }
        guiGraphics.text(font, "lockedSinceLast " + (System.currentTimeMillis() - typhoon.lastLockedTime), x, y + 10 * 17, -1, false);
    }
}

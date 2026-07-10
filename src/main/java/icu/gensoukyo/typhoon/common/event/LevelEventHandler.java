package icu.gensoukyo.typhoon.common.event;


import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber
public class LevelEventHandler {
    @SubscribeEvent
    public static void onLevelTickEvent(LevelTickEvent.Post event){

        if (TyphoonEntity.INSTANCE == null) {
            return;
        }

        Level level = event.getLevel();
        if(level.dimension() == Level.OVERWORLD) {
            TyphoonEntity.INSTANCE.tick(level);
        }
    }

    @SubscribeEvent
    public static void onLevelLoadEvent(LevelEvent.Load event){
        MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
        SavedDataStorage dataStorage;
        if (currentServer != null) {
            dataStorage = currentServer.getDataStorage();
            TyphoonEntity.INSTANCE = dataStorage.get(TyphoonEntity.ID);
        }
    }
}

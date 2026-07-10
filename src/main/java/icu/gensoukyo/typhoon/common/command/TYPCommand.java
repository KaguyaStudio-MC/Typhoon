package icu.gensoukyo.typhoon.common.command;

import icu.gensoukyo.typhoon.Typhoon;
import icu.gensoukyo.typhoon.common.config.CommonConfig;
import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber
public class TYPCommand {
    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal(Typhoon.MODID).then(
                        Commands.literal("create").executes(
                                context -> {
                                    if (TyphoonEntity.INSTANCE !=null){
                                        MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
                                        SavedDataStorage dataStorage;
                                        if (currentServer != null) {
                                            dataStorage = currentServer.getDataStorage();
                                            TyphoonEntity.INSTANCE = new TyphoonEntity(
                                                    CommonConfig.V.get()
                                                    ,CommonConfig.FACTOR.get()
                                                    ,CommonConfig.HEIGHT.get()
                                                    ,CommonConfig.MINY.get()
                                                    ,CommonConfig.R.get());

                                            dataStorage.set(TyphoonEntity.ID,TyphoonEntity.INSTANCE);
                                        }
                                    }
                                    return 0;
                                }
                        )
                ).then(
                        Commands.literal("run").executes(
                                context -> {
                                    if (TyphoonEntity.INSTANCE !=null){
                                        TyphoonEntity.INSTANCE.paused = false;
                                    }

                                    return 0;
                                }
                        )
                ).then(
                        Commands.literal("stop").executes(
                                context -> {
                                    TyphoonEntity.INSTANCE = null;
                                    return 0;
                                }
                        )
                ).then(
                        Commands.literal("pause").executes(
                                context -> {

                                    if (TyphoonEntity.INSTANCE !=null){
                                        TyphoonEntity.INSTANCE.paused = true;
                                    }
                                    return 0;
                                }
                        )
                )
        );
    }
}

package icu.gensoukyo.typhoon;

import com.mojang.logging.LogUtils;
import icu.gensoukyo.typhoon.common.config.CommonConfig;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Typhoon.MODID)
public class Typhoon {
    public static final String MODID = "typhoon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Typhoon(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    }

    public static Identifier id(String path){
        return Identifier.fromNamespaceAndPath(MODID,path);
    }
}

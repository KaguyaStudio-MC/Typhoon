package icu.gensoukyo.typhoon;

import com.mojang.logging.LogUtils;
import icu.gensoukyo.typhoon.common.config.CommonConfig;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Typhoon.MODID)
public class Typhoon {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "typhoon";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Typhoon(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    }

    public static Identifier id(String path){
        return Identifier.fromNamespaceAndPath(MODID,path);
    }
}

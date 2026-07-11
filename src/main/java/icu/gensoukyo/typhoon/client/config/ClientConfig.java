package icu.gensoukyo.typhoon.client.config;

import icu.gensoukyo.typhoon.Typhoon;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Typhoon.MODID)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DEBUG = BUILDER.define("debug", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

}

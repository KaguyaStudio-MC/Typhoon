package icu.gensoukyo.typhoon.common.config;

import icu.gensoukyo.typhoon.Typhoon;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Typhoon.MODID)
public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue FACTOR = BUILDER.defineInRange("typhoon_factor", 1.0, 0, 100.0);

    public static final ModConfigSpec.DoubleValue H_FACTOR = BUILDER.defineInRange("typhoon_h_factor", 1.0, 0, 100.0);

    public static final ModConfigSpec.DoubleValue V = BUILDER.defineInRange("typhoon_velocity", 5.0, 0, 100.0);

    public static final ModConfigSpec.DoubleValue HEIGHT = BUILDER.defineInRange("typhoon_height", 400.0, 0, 3000.0);

    public static final ModConfigSpec.DoubleValue MINY = BUILDER.defineInRange("typhoon_miny", 0, -64, 128.0);

    public static final ModConfigSpec.DoubleValue R = BUILDER.defineInRange("typhoon_radius", 2000.0, 0, 100000.0);

    public static final ModConfigSpec.DoubleValue GROW = BUILDER.defineInRange("typhoon_grow_speed", 0.0, 0, 1.0);

    public static final ModConfigSpec.DoubleValue MAX_GROWN = BUILDER.defineInRange("typhoon_max_grown", 1, 0, 100.0);

    public static final ModConfigSpec.DoubleValue DAMAGE = BUILDER.defineInRange("typhoon_damage_factor", 1.0, 0, 100.0);

    public static final ModConfigSpec SPEC = BUILDER.build();

}

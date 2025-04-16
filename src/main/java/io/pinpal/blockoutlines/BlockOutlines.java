package io.pinpal.blockoutlines;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BlockOutlines.MODID)
public class BlockOutlines {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "blockoutlines";

    public BlockOutlines(FMLJavaModLoadingContext context) {
        // Register ForgeConfigSpec to create/load config filr
        context.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}

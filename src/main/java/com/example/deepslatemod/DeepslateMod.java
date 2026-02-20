package com.example.deepslatemod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DeepslateMod.MOD_ID)
public class DeepslateMod {

    public static final String MOD_ID = "deepslatemod";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public DeepslateMod() {
        // ForgeイベントバスにWorldGenHandlerを登録
        MinecraftForge.EVENT_BUS.register(new WorldGenHandler());

        LOGGER.info("DeepslateMod: 深層岩 → 石 置換Modが読み込まれました！");
    }
}

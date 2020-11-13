package com.lzlniu.bioinformatics;

import com.lzlniu.bioinformatics.util.RegistryHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("bioinformatics")
public class Bioinformatics {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "bioinformatics";
    public Bioinformatics() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        RegistryHandler.init();
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void setup(final FMLCommonSetupEvent event) { }
    private void doClientStuff(final FMLClientSetupEvent event) { }
    public static final ItemGroup TAB = new ItemGroup("BioinformaticsTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.DNA.get());
        }
    };
}

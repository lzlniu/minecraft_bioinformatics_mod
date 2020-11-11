package com.lzlniu.bioinformatics.util;

import com.lzlniu.bioinformatics.Bioinformatics;
import com.lzlniu.bioinformatics.items.ItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bioinformatics.MOD_ID);
    public static final RegistryObject<Item> DNA = ITEMS.register("dna", ItemBase::new);
    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

package com.lzlniu.bioinformatics.util;

import com.lzlniu.bioinformatics.Bioinformatics;
import com.lzlniu.bioinformatics.blocks.BlockItemBase;
import com.lzlniu.bioinformatics.blocks.PetriDishBlock;
import com.lzlniu.bioinformatics.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bioinformatics.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bioinformatics.MOD_ID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    //items
    public static final RegistryObject<Item> DNA = ITEMS.register("dna", ItemBase::new);
    public static final RegistryObject<Item> SWAB = ITEMS.register("swab", ItemBase::new);

    //blocks
    public static final RegistryObject<Block> PETRI_DISH_BLOCK = BLOCKS.register("petri_dish_block", PetriDishBlock::new);
    //block items
    public static final RegistryObject<Item> PETRI_DISH_BLOCK_ITEM = ITEMS.register("petri_dish_block", () -> new BlockItemBase(PETRI_DISH_BLOCK.get()));
}

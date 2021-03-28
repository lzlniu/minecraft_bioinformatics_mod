package com.lzlniu.bioinformatics.items;

import com.lzlniu.bioinformatics.Bioinformatics;
//import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;

//import java.util.List;

public class ItemBase extends Item {
    public ItemBase() {
        super(new Item.Properties().group(Bioinformatics.TAB));
    }
    /*
    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemstack, world, list, flag);
        list.add(new StringTextComponent("Line 1"));
        list.add(new StringTextComponent("Line 2"));
    }
    */
}

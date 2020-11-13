package com.lzlniu.bioinformatics.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.stream.Stream;

public class PetriDishBlock extends Block {

    public static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(4,0,4,12,2,12),
            Block.makeCuboidShape(6,1,12,10,2,13),
            Block.makeCuboidShape(6,1,3,10,2,4),
            Block.makeCuboidShape(12,1,6,13,2,10),
            Block.makeCuboidShape(3,1,6,4,2,10)
    ).reduce((v1, v2)->{return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    public PetriDishBlock(){
        super(AbstractBlock.Properties.create(Material.MISCELLANEOUS)
        .hardnessAndResistance(0.0f,3.0f)
        .sound(SoundType.GLASS)
        .harvestLevel(0));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.99f;
    }
}

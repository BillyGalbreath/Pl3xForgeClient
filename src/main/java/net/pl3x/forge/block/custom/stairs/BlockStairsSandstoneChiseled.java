package net.pl3x.forge.block.custom.stairs;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.pl3x.forge.block.ModBlocks;

public class BlockStairsSandstoneChiseled extends BlockStairs {
    public BlockStairsSandstoneChiseled() {
        super(Material.ROCK, "stairs_sandstone_chiseled");
        setSoundType(SoundType.STONE);
        setHardness(1F);

        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        ModBlocks.blocks.add(this);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.SAND;
    }
}
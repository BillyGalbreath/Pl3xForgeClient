package net.pl3x.forge.client.block.custom;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.pl3x.forge.client.ModGuiHandler;
import net.pl3x.forge.client.Pl3xForgeClient;
import net.pl3x.forge.client.block.BlockTileEntity;
import net.pl3x.forge.client.block.enchantmentsplitter.TileEntityEnchantmentSplitter;

import javax.annotation.Nullable;

public class BlockEnchantmentSplitter extends BlockTileEntity<TileEntityEnchantmentSplitter> {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockEnchantmentSplitter() {
        super(Material.ROCK, "enchantment_splitter");
        setHardness(6.0F);
        setResistance(2500.0F);
        setLightOpacity(0);
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing) {
        return facing == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(Pl3xForgeClient.instance, ModGuiHandler.ENCHANTMENT_SPLITTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityEnchantmentSplitter) {
                ((TileEntityEnchantmentSplitter) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public Class<TileEntityEnchantmentSplitter> getTileEntityClass() {
        return TileEntityEnchantmentSplitter.class;
    }

    @Nullable
    @Override
    public TileEntityEnchantmentSplitter createTileEntity(World world, IBlockState state) {
        return new TileEntityEnchantmentSplitter();
    }

    // Below is from BlockContainer.java

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof IWorldNameable && ((IWorldNameable) te).hasCustomName()) {
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.005F);

            if (worldIn.isRemote) {
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            Item item = getItemDropped(state, worldIn.rand, i);

            if (item == Items.AIR) {
                return;
            }

            ItemStack itemstack = new ItemStack(item, quantityDropped(worldIn.rand));
            itemstack.setStackDisplayName(((IWorldNameable) te).getName());
            spawnAsEntity(worldIn, pos, itemstack);
        } else {
            super.harvestBlock(worldIn, player, pos, state, null, stack);
        }
    }

    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }
}
package balkondeuralpha.freerunner.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockEdge extends Block {
	public BlockEdge() {
		super(Material.circuits);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (world.getBlock(i - 1, j, k).isNormalCube(world, i - 1, j, k)) {
			return true;
		}
		if (world.getBlock(i + 1, j, k).isNormalCube(world, i + 1, j, k)) {
			return true;
		}
		if (world.getBlock(i, j, k - 1).isNormalCube(world, i, j, k - 1)) {
			return true;
		}
		return world.getBlock(i, j, k + 1).isNormalCube(world, i, j, k + 1);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		float f = 0.125F;
		if (l == 2) {
			setBlockBounds(0.0F, 0.7F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		}
		if (l == 3) {
			setBlockBounds(0.0F, 0.7F, 0.0F, 1.0F, 1.0F, f);
		}
		if (l == 4) {
			setBlockBounds(1.0F - f, 0.7F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		if (l == 5) {
			setBlockBounds(0.0F, 0.7F, 0.0F, f, 1.0F, 1.0F);
		}
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public int getRenderType() {
		return 8;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		float f = 0.125F;
		if (l == 2) {
			setBlockBounds(0.0F, 0.7F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		}
		if (l == 3) {
			setBlockBounds(0.0F, 0.7F, 0.0F, 1.0F, 1.0F, f);
		}
		if (l == 4) {
			setBlockBounds(1.0F - f, 0.7F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		if (l == 5) {
			setBlockBounds(0.0F, 0.7F, 0.0F, f, 1.0F, 1.0F);
		}
		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
		int i1 = world.getBlockMetadata(i, j, k);
		boolean flag = false;
		if (i1 == 2 && world.getBlock(i, j, k + 1).isNormalCube(world, i, j, k + 1)) {
			flag = true;
		}
		if (i1 == 3 && world.getBlock(i, j, k - 1).isNormalCube(world, i, j, k - 1)) {
			flag = true;
		}
		if (i1 == 4 && world.getBlock(i + 1, j, k).isNormalCube(world, i + 1, j, k)) {
			flag = true;
		}
		if (i1 == 5 && world.getBlock(i - 1, j, k).isNormalCube(world, i - 1, j, k)) {
			flag = true;
		}
		if (!flag) {
			dropBlockAsItem(world, i, j, k, i1, 0);
			world.setBlockToAir(i, j, k);
		}
		super.onNeighborBlockChange(world, i, j, k, l);
	}

	@Override
	public void onPostBlockPlaced(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMetadata(i, j, k);
		if ((i1 == 0 || l == 2) && world.getBlock(i, j, k + 1).isNormalCube(world, i, j, k + 1)) {
			i1 = 2;
		}
		if ((i1 == 0 || l == 3) && world.getBlock(i, j, k - 1).isNormalCube(world, i, j, k - 1)) {
			i1 = 3;
		}
		if ((i1 == 0 || l == 4) && world.getBlock(i + 1, j, k).isNormalCube(world, i + 1, j, k)) {
			i1 = 4;
		}
		if ((i1 == 0 || l == 5) && world.getBlock(i - 1, j, k).isNormalCube(world, i - 1, j, k)) {
			i1 = 5;
		}
		world.setBlockMetadataWithNotify(i, j, k, i1, 3);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}

package balkondeuralpha.freerunner;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockWoodBar extends Block {
	public static int barWoodModel;

	protected BlockWoodBar() {
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        Block test = world.getBlock(i - 1, j, k);
		if (test == this || test.isNormalCube(world, i - 1, j, k)) {
			return true;
		}
        test = world.getBlock(i + 1, j, k);
		if (test == this || test.isNormalCube(world, i + 1, j, k)) {
			return true;
		}
        test = world.getBlock(i, j, k - 1);
		if (test == this || test.isNormalCube(world, i, j, k - 1)) {
			return true;
		}
        test = world.getBlock(i, j, k + 1);
		if (test == this || test.isNormalCube(world, i, j, k + 1)) {
			return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		float f = 1.0F;
		if (l == 2) {
			setBlockBounds(0.4F, 0.8F, 1.0F - f, 0.6F, 1.0F, 1.0F);
		} else if (l == 3) {
			setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 1.0F, f);
		} else if (l == 4) {
			setBlockBounds(1.0F - f, 0.8F, 0.4F, 1.0F, 1.0F, 0.6F);
		} else if (l == 5) {
			setBlockBounds(0.0F, 0.8F, 0.4F, f, 1.0F, 0.6F);
		}
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public int getRenderType() {
		return barWoodModel;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
		int i1 = world.getBlockMetadata(i, j, k);
		boolean flag = false;
        Block test = world.getBlock(i, j, k + 1);
		if (i1 == 2 && (test == this || test.isNormalCube(world, i, j, k + 1))) {
			flag = true;
		}
        test = world.getBlock(i, j, k - 1);
		if (i1 == 3 && (test == this || test.isNormalCube(world, i, j, k - 1))) {
			flag = true;
		}
        test = world.getBlock(i + 1, j, k);
		if (i1 == 4 && (test == this || test.isNormalCube(world, i + 1, j, k))) {
			flag = true;
		}
        test = world.getBlock(i - 1, j, k);
		if (i1 == 5 && (test == this || test.isNormalCube(world, i - 1, j, k))) {
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
        Block test = world.getBlock(i, j, k + 1);
		if ((i1 == 0 || l == 2) && (test == this || test.isNormalCube(world, i, j, k + 1))) {
			i1 = 2;
		}
        test = world.getBlock(i, j, k - 1);
		if ((i1 == 0 || l == 3) && (test == this || test.isNormalCube(world, i, j, k - 1))) {
			i1 = 3;
		}
        test = world.getBlock(i + 1, j, k);
		if ((i1 == 0 || l == 4) && (test == this || test.isNormalCube(world, i + 1, j, k))) {
			i1 = 4;
		}
        test = world.getBlock(i - 1, j, k);
		if ((i1 == 0 || l == 5) && (test == this || test.isNormalCube(world, i - 1, j, k))) {
			i1 = 5;
		}
		world.setBlockMetadataWithNotify(i, j, k, i1, 3);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}

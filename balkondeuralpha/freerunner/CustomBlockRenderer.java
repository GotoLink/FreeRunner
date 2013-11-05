package balkondeuralpha.freerunner;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomBlockRenderer implements ISimpleBlockRenderingHandler {
	@Override
	public int getRenderId() {
		return FRCommonProxy.properties.barWoodModel;
	}

	@Override
	public void renderInventoryBlock(Block block, int i, int j, RenderBlocks renderblocks) {
		int k = block.getRenderType();
		if (k == FRCommonProxy.properties.barWoodModel) {
			Tessellator tess = Tessellator.instance;
			float f4 = 1.2F;
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tess.startDrawingQuads();
			{
				tess.setNormal(0.0F, -1.0F, 0.0F);
				renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
				tess.setNormal(0.0F, 1.0F, 0.0F);
				renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
				tess.setNormal(0.0F, 0.0F, -1.0F);
				renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
				tess.setNormal(0.0F, 0.0F, 1.0F);
				renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
				tess.setNormal(-1.0F, 0.0F, 0.0F);
				renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
				tess.setNormal(1.0F, 0.0F, 0.0F);
				renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
			}
			tess.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int i, int j, int k, Block block, int m, RenderBlocks renderblocks) {
		if (m == FRCommonProxy.properties.barWoodModel) {
			float f = 1.0F;
			int l = blockAccess.getBlockMetadata(i, j, k);
			if (l == 2) {
				block.setBlockBounds(0.4F, 0.8F, 1.0F - f, 0.6F, 1.0F, 1.0F);
			} else if (l == 3) {
				block.setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 1.0F, f);
			} else if (l == 4) {
				block.setBlockBounds(1.0F - f, 0.8F, 0.4F, 1.0F, 1.0F, 0.6F);
			} else if (l == 5) {
				block.setBlockBounds(0.0F, 0.8F, 0.4F, f, 1.0F, 0.6F);
			}
			renderblocks.renderStandardBlock(block, i, j, k);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}
}

package balkondeuralpha.freerunner;

import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@Mod(modid = "FreeRunMod",name="Free Runner Mod",version="1.0")
@NetworkMod
public class FreeRun implements ITickHandler
{	
@Init
	public void initFreeRun(FMLInitializationEvent event)
	{
		instance = this;
		properties = new FR_Properties();
		properties.loadAllProperties();
		registerItemsAndBlocks();
		
		mc = Minecraft.getMinecraft();
		keyForward = mc.gameSettings.keyBindForward.keyCode;
		keyBackward = mc.gameSettings.keyBindBack.keyCode;
		keyLeft = mc.gameSettings.keyBindLeft.keyCode;
		keyRight = mc.gameSettings.keyBindRight.keyCode;
		
		PlayerAPI.register("Freerun", FR_FreerunPlayer.class);
		RenderPlayerAPI.register("Freerun", FR_Animator.class);
		TickRegistry.registerTickHandler(this, Side.CLIENT);
		RenderingRegistry.registerBlockHandler(customBlockRenderer);
	}
	
	public void setFreerunPlayer(FR_FreerunPlayer freerun)
	{
		this.freerun = freerun;
	}
	
	public void setAnimator(FR_Animator animator)
	{
		this.animator = animator;
		animator.setFreerunPlayer(freerun);
	}
	
	private void registerItemsAndBlocks()
	{
		if (properties.enableEdgeWood)
		{
			edgeWood = new FR_BlockEdge(properties.edgeWoodId, 0).setHardness(1.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setBlockName("edgeWood").setTextureFile("/balkondeuralpha/blocksanditem.png");
			
			GameRegistry.registerBlock(edgeWood, "Wooden Edge");
			LanguageRegistry.addName(edgeWood, "Wooden Edge");
			GameRegistry.addRecipe(new ItemStack(edgeWood, 2), new Object[] { "#X", Character.valueOf('#'), Block.wood, Character.valueOf('X'), Item.stick });
		}
		
		if (properties.enableEdgeStone)
		{
			edgeStone = new FR_BlockEdge(properties.edgeStoneId, 1).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundStoneFootstep).setBlockName("edgeStone").setTextureFile("/balkondeuralpha/blocksanditem.png");
			
			GameRegistry.registerBlock(edgeStone, "Stone Edge");
			LanguageRegistry.addName(edgeStone, "Stone Edge");
			GameRegistry.addRecipe(new ItemStack(edgeStone, 2), new Object[] { "#X", Character.valueOf('#'), Block.stone, Character.valueOf('X'), Block.cobblestone });
		}
		
		if (properties.enableHayStack)
		{
			materialHay = new FR_MaterialHay(MapColor.woodColor);
			
			hayStack = new FR_BlockHayStack(properties.hayStackId, 2).setHardness(0.5F).setStepSound(Block.soundGrassFootstep).setBlockName("hayStack").setTextureFile("/balkondeuralpha/blocksanditem.png");
			
			GameRegistry.registerBlock(hayStack, "Haystack");
			LanguageRegistry.addName(hayStack, "Haystack");
			GameRegistry.addRecipe(new ItemStack(hayStack, 4), new Object[] { " # ", "###", Character.valueOf('#'), Item.wheat });
			GameRegistry.addRecipe(new ItemStack(Item.wheat, 1), new Object[] { "#", Character.valueOf('#'), FreeRun.hayStack });
			try
			{
				setHaystackBurnable();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if (properties.enableBarWood)
		{
			barWood = new FR_BlockWoodBar(properties.barWoodId, 4).setHardness(2.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setBlockName("barWood");
			
			GameRegistry.registerBlock(barWood, "Wooden Bar");
			LanguageRegistry.addName(barWood, "Wooden Bar");
			GameRegistry.addRecipe(new ItemStack(barWood, 4), new Object[] { "###", Character.valueOf('#'), Block.wood });
		}
		
		if (properties.enableClimbingGlove)
		{
			climbGlove = new Item(properties.climbGloveId).setMaxStackSize(1).setIconIndex(3).setItemName("climbGlove").setTextureFile("/balkondeuralpha/blocksanditem.png");
			
			ModLoader.addName(climbGlove, "Climbing Glove");
			GameRegistry.addRecipe(new ItemStack(climbGlove, 1), new Object[] { " # ", "#X#", Character.valueOf('#'), Item.leather, Character.valueOf('X'), Item.ingotIron });
		}
	}
	
	private void setHaystackBurnable() throws Exception
	{
		Object o = null;
		Object o1 = null;
		o = ModLoader.getPrivateValue(BlockFire.class, Block.fire, 0);
		o1 = ModLoader.getPrivateValue(BlockFire.class, Block.fire, 1);
		
		if (o instanceof int[])
		{
			int[] chanceToEncourageFire = (int[]) o;
			chanceToEncourageFire[hayStack.blockID] = 30;
		} else
		{
			throw new Exception("Invalid field: changeToEncourageFire");
		}
		if (o1 instanceof int[])
		{
			int[] abilityToCatchFire = (int[]) o1;
			abilityToCatchFire[hayStack.blockID] = 100;
		} else
		{
			throw new Exception("Invalid field: abilityToCatchFire");
		}
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (mc.thePlayer == null || mc.theWorld == null) return;
		if (properties.enableAnimations && tickData[0]!=null)
		{
			freerun.updateAnimations((Float) tickData[0]);
			animator.setRenderTime((Float) tickData[0]);
		}
	}
	@Override
	public EnumSet<TickType> ticks() {
		return types;
	}
	@Override
	public String getLabel() {
		return "FreeRunTick";
	}
	
	@SideOnly(Side.CLIENT)
	public class CustomBlockRenderer implements ISimpleBlockRenderingHandler{
	@Override
	public boolean renderWorldBlock( IBlockAccess blockAccess, int i, int j, int k, Block block, int m, RenderBlocks renderblocks)
	{
		if (m == properties.barWoodModel)
		{
			float f = 1.0F;
			int l = blockAccess.getBlockMetadata(i, j, k);
			if (l == 2)
			{
				block.setBlockBounds(0.4F, 0.8F, 1.0F - f, 0.6F, 1.0F, 1.0F);
			} else if (l == 3)
			{
				block.setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 1.0F, f);
			} else if (l == 4)
			{
				block.setBlockBounds(1.0F - f, 0.8F, 0.4F, 1.0F, 1.0F, 0.6F);
			} else if (l == 5)
			{
				block.setBlockBounds(0.0F, 0.8F, 0.4F, f, 1.0F, 0.6F);
			}
			renderblocks.renderStandardBlock(block, i, j, k);
			return true;
		}
		return false;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int i, int j, RenderBlocks renderblocks) 
	{
		int k = block.getRenderType();
		if (k == properties.barWoodModel)
		{
			Tessellator tess = Tessellator.instance;
			float f4 = 1.2F;
			
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			tess.startDrawingQuads();
			{
				tess.setNormal(0.0F, -1.0F, 0.0F);
				renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
				
				tess.setNormal(0.0F, 1.0F, 0.0F);
				renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
				
				tess.setNormal(0.0F, 0.0F, -1.0F);
				renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
				
				tess.setNormal(0.0F, 0.0F, 1.0F);
				renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
				
				tess.setNormal(-1.0F, 0.0F, 0.0F);
				renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
				
				tess.setNormal(1.0F, 0.0F, 0.0F);
				renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
			}
			tess.draw();
			
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
	}
	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}
	@Override
	public int getRenderId() {
		return properties.barWoodModel;
	}
	}
	public FR_Properties		properties;
	public FR_FreerunPlayer		freerun;
	public FR_Animator			animator;
	
	public int					keyForward,keyBackward,keyLeft,keyRight;
	@Instance("FreeRunMod")
	public static FreeRun		instance;
	public static Block			edgeWood,edgeStone,hayStack,barWood;
	public static Item			climbGlove;
	public static Material		materialHay;
	private ISimpleBlockRenderingHandler customBlockRenderer=new CustomBlockRenderer();
	private Minecraft mc;
	private final EnumSet<TickType> types = EnumSet.of(TickType.RENDER);    
	
}

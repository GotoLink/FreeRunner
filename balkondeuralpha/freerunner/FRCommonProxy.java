package balkondeuralpha.freerunner;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class FRCommonProxy {
	public static Block edgeWood, edgeStone, hayStack, barWood;
	public static Item climbGlove;
	public static Material materialHay;
	public static FR_Properties properties;

	public void registerThings(FMLPreInitializationEvent event) {
		properties = new FR_Properties();
		properties.loadAllProperties(event.getSuggestedConfigurationFile());
		if (properties.enableEdgeWood) {
			edgeWood = new BlockEdge().setHardness(1.0F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("edgeWood")
					.setBlockTextureName("freerun:wood-edge");
			GameRegistry.registerBlock(edgeWood, "Wooden Edge");
			GameRegistry.addRecipe(new ItemStack(edgeWood, 2), "#X", '#', Blocks.log, 'X', Items.stick);
		}
		if (properties.enableEdgeStone) {
			edgeStone = new BlockEdge().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("edgeStone")
					.setBlockTextureName("freerun:stone-edge");
			GameRegistry.registerBlock(edgeStone, "Stone Edge");
			GameRegistry.addRecipe(new ItemStack(edgeStone, 2), "#X", '#', Blocks.stone, 'X', Blocks.cobblestone);
		}
		if (properties.enableHayStack) {
			materialHay = new MaterialHay(MapColor.woodColor);
			hayStack = new BlockHayStack().setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("hayStack").setBlockTextureName("freerun:hay");
            GameRegistry.registerBlock(hayStack, "Haystack");
            Blocks.fire.setFireInfo(hayStack, 30, 100);
            GameRegistry.addRecipe(new ItemStack(hayStack, 4), " # ", "###", '#', Items.wheat);
			GameRegistry.addRecipe(new ItemStack(Items.wheat, 1), "#", '#', hayStack);
		}
		if (properties.enableBarWood) {
			barWood = new BlockWoodBar().setHardness(2.0F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("barWood").setBlockTextureName("planks");
			GameRegistry.registerBlock(barWood, "Wooden Bar");
			GameRegistry.addRecipe(new ItemStack(barWood, 4), "###", '#', Blocks.log);
		}
		if (properties.enableClimbingGlove) {
			climbGlove = new Item().setMaxStackSize(1).setUnlocalizedName("climbGlove").setTextureName("freerun:climbglove").setCreativeTab(CreativeTabs.tabTools);
			GameRegistry.registerItem(climbGlove, "Climbing Glove");
			GameRegistry.addRecipe(new ItemStack(climbGlove, 1), " # ", "#X#", '#', Items.leather, 'X', Items.iron_ingot);
		}
		PlayerEventHandler playerEvent = new PlayerEventHandler();
		MinecraftForge.EVENT_BUS.register(playerEvent);
		FMLCommonHandler.instance().bus().register(playerEvent);
	}
}

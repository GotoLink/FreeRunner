package balkondeuralpha.freerunner;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.IPlayerTracker;
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
			edgeWood = new BlockEdge(properties.edgeWoodId).setHardness(1.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("edgeWood")
					.setTextureName("freerun:wood-edge");
			GameRegistry.registerBlock(edgeWood, "Wooden Edge");
			GameRegistry.addRecipe(new ItemStack(edgeWood, 2), "#X", Character.valueOf('#'), Block.wood, Character.valueOf('X'), Item.stick);
		}
		if (properties.enableEdgeStone) {
			edgeStone = new BlockEdge(properties.edgeStoneId).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("edgeStone")
					.setTextureName("freerun:stone-edge");
			GameRegistry.registerBlock(edgeStone, "Stone Edge");
			GameRegistry.addRecipe(new ItemStack(edgeStone, 2), "#X", Character.valueOf('#'), Block.stone, Character.valueOf('X'), Block.cobblestone);
		}
		if (properties.enableHayStack) {
			materialHay = new MaterialHay(MapColor.woodColor);
			hayStack = new BlockHayStack(properties.hayStackId).setHardness(0.5F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("hayStack").setTextureName("freerun:hay");
			Block.setBurnProperties(hayStack.blockID, 30, 100);
			GameRegistry.registerBlock(hayStack, "Haystack");
			GameRegistry.addRecipe(new ItemStack(hayStack, 4), " # ", "###", Character.valueOf('#'), Item.wheat);
			GameRegistry.addRecipe(new ItemStack(Item.wheat, 1), "#", Character.valueOf('#'), hayStack);
		}
		if (properties.enableBarWood) {
			barWood = new BlockWoodBar(properties.barWoodId).setHardness(2.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("barWood").setTextureName("planks");
			GameRegistry.registerBlock(barWood, "Wooden Bar");
			GameRegistry.addRecipe(new ItemStack(barWood, 4), "###", Character.valueOf('#'), Block.wood);
		}
		if (properties.enableClimbingGlove) {
			climbGlove = new Item(properties.climbGloveId).setMaxStackSize(1).setUnlocalizedName("climbGlove").setTextureName("freerun:climbglove").setCreativeTab(CreativeTabs.tabTools);
			GameRegistry.registerItem(climbGlove, "Climbing Glove");
			GameRegistry.addRecipe(new ItemStack(climbGlove, 1), " # ", "#X#", Character.valueOf('#'), Item.leather, Character.valueOf('X'), Item.ingotIron);
		}
		IPlayerTracker playerEvent = new PlayerEventHandler();
		MinecraftForge.EVENT_BUS.register(playerEvent);
		GameRegistry.registerPlayerTracker(playerEvent);
	}
}

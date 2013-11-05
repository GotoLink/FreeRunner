package balkondeuralpha.freerunner;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class FRCommonProxy {
	public static Block edgeWood, edgeStone, hayStack, barWood;
	public static Item climbGlove;
	public static Material materialHay;
	public static FR_Properties properties;

	public void registerThings() {
		properties = new FR_Properties();
		properties.loadAllProperties();
		if (properties.enableEdgeWood) {
			edgeWood = new BlockEdge(properties.edgeWoodId).setHardness(1.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("edgeWood");
			GameRegistry.registerBlock(edgeWood, "Wooden Edge");
			LanguageRegistry.addName(edgeWood, "Wooden Edge");
			GameRegistry.addRecipe(new ItemStack(edgeWood, 2), new Object[] { "#X", Character.valueOf('#'), Block.wood, Character.valueOf('X'), Item.stick });
		}
		if (properties.enableEdgeStone) {
			edgeStone = new BlockEdge(properties.edgeStoneId).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("edgeStone");
			GameRegistry.registerBlock(edgeStone, "Stone Edge");
			LanguageRegistry.addName(edgeStone, "Stone Edge");
			GameRegistry.addRecipe(new ItemStack(edgeStone, 2), new Object[] { "#X", Character.valueOf('#'), Block.stone, Character.valueOf('X'), Block.cobblestone });
		}
		if (properties.enableHayStack) {
			materialHay = new MaterialHay(MapColor.woodColor);
			hayStack = new BlockHayStack(properties.hayStackId).setHardness(0.5F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("hayStack");
			Block.setBurnProperties(hayStack.blockID, 30, 100);
			GameRegistry.registerBlock(hayStack, "Haystack");
			LanguageRegistry.addName(hayStack, "Haystack");
			GameRegistry.addRecipe(new ItemStack(hayStack, 4), new Object[] { " # ", "###", Character.valueOf('#'), Item.wheat });
			GameRegistry.addRecipe(new ItemStack(Item.wheat, 1), new Object[] { "#", Character.valueOf('#'), hayStack });
		}
		if (properties.enableBarWood) {
			barWood = new BlockWoodBar(properties.barWoodId).setHardness(2.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("barWood");
			GameRegistry.registerBlock(barWood, "Wooden Bar");
			LanguageRegistry.addName(barWood, "Wooden Bar");
			GameRegistry.addRecipe(new ItemStack(barWood, 4), new Object[] { "###", Character.valueOf('#'), Block.wood });
		}
		if (properties.enableClimbingGlove) {
			climbGlove = new Item(properties.climbGloveId).setMaxStackSize(1).setUnlocalizedName("climbGlove");
			LanguageRegistry.addName(climbGlove, "Climbing Glove");
			GameRegistry.addRecipe(new ItemStack(climbGlove, 1), new Object[] { " # ", "#X#", Character.valueOf('#'), Item.leather, Character.valueOf('X'), Item.ingotIron });
		}
	}
}

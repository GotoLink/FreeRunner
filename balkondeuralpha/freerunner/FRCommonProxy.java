package balkondeuralpha.freerunner;

import balkondeuralpha.freerunner.blocks.BlockEdge;
import balkondeuralpha.freerunner.blocks.BlockHayStack;
import balkondeuralpha.freerunner.blocks.BlockWoodBar;
import balkondeuralpha.freerunner.moves.Move;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
	public static FR_Properties properties;
    protected SimpleNetworkWrapper wrapper;

	public void registerThings(FMLPreInitializationEvent event) {
		properties = new FR_Properties(event.getSuggestedConfigurationFile());
		if (properties.enableEdgeWood) {
			edgeWood = new BlockEdge().setHardness(1.0F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("edgeWood")
					.setBlockTextureName("freerun:wood-edge");
			GameRegistry.registerBlock(edgeWood, "WoodenEdge");
			GameRegistry.addRecipe(new ItemStack(edgeWood, 2), "#X", '#', Blocks.log, 'X', Items.stick);
		}
		if (properties.enableEdgeStone) {
			edgeStone = new BlockEdge().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("edgeStone")
					.setBlockTextureName("freerun:stone-edge");
			GameRegistry.registerBlock(edgeStone, "StoneEdge");
			GameRegistry.addRecipe(new ItemStack(edgeStone, 2), "#X", '#', Blocks.stone, 'X', Blocks.cobblestone);
		}
		if (properties.enableHayStack) {
			hayStack = new BlockHayStack().setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("hayStack").setBlockTextureName("freerun:hay");
            GameRegistry.registerBlock(hayStack, "Haystack");
            Blocks.fire.setFireInfo(hayStack, 30, 100);
            GameRegistry.addRecipe(new ItemStack(hayStack, 4), " # ", "###", '#', Items.wheat);
			GameRegistry.addRecipe(new ItemStack(Items.wheat, 1), "#", '#', hayStack);
		}
		if (properties.enableBarWood) {
			barWood = new BlockWoodBar().setHardness(2.0F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("barWood").setBlockTextureName("planks");
			GameRegistry.registerBlock(barWood, "WoodenBar");
			GameRegistry.addRecipe(new ItemStack(barWood, 4), "###", '#', Blocks.log);
		}
		if (properties.enableClimbingGlove) {
			climbGlove = new Item().setMaxStackSize(1).setUnlocalizedName("climbGlove").setTextureName("freerun:climbglove").setCreativeTab(CreativeTabs.tabTools);
			GameRegistry.registerItem(climbGlove, "ClimbingGlove");
			GameRegistry.addRecipe(new ItemStack(climbGlove, 1), " # ", "#X#", '#', Items.leather, 'X', Items.iron_ingot);
		}
		PlayerEventHandler playerEvent = new PlayerEventHandler();
		MinecraftForge.EVENT_BUS.register(playerEvent);
		FMLCommonHandler.instance().bus().register(playerEvent);
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("FreeRun");
        wrapper.registerMessage(NetworkHandler.class, NetworkHandler.SetFreeRun.class, 0, Side.SERVER);
	}

    public void sendFreeRunPacket(boolean b){
        wrapper.sendToServer(new NetworkHandler.SetFreeRun(b));
    }

    public EntityPlayer getPlayer(MessageContext context){
        return context.getServerHandler().playerEntity;
    }

    public void setAnimation(Move move) {
    }

    public void startAnimation(Move move) {
    }
}

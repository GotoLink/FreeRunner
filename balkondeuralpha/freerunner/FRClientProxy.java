package balkondeuralpha.freerunner;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FRClientProxy extends FRCommonProxy {
	@Override
	public void registerThings(FMLPreInitializationEvent event) {
		super.registerThings(event);
		FMLCommonHandler.instance().bus().register(new RunnerTickHandler());
		BlockWoodBar.barWoodModel = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new CustomBlockRenderer());
        if (properties.enableAnimations)
		    MinecraftForge.EVENT_BUS.register(new Animator());
	}
}

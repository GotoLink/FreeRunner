package balkondeuralpha.freerunner;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class FRClientProxy extends FRCommonProxy {
	@Override
	public void registerThings(FMLPreInitializationEvent event) {
		super.registerThings(event);
		FreerunPlayer free = new FreerunPlayer(Minecraft.getMinecraft().thePlayer);
		TickRegistry.registerTickHandler(new RenderTickHandler(free), Side.CLIENT);
		BlockWoodBar.barWoodModel = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new CustomBlockRenderer());
		MinecraftForge.EVENT_BUS.register(new Animator(free));
		KeyBindingRegistry.registerKeyBinding(new FreeRunKeyHandler());
	}
}

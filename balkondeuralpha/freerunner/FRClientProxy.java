package balkondeuralpha.freerunner;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class FRClientProxy extends FRCommonProxy {
	@Override
	public void registerThings() {
		super.registerThings();
		FreerunPlayer free = new FreerunPlayer();
		TickRegistry.registerTickHandler(new RenderTickHandler(free), Side.CLIENT);
		RenderingRegistry.registerBlockHandler(new CustomBlockRenderer());
		MinecraftForge.EVENT_BUS.register(new Animator(free));
	}
}

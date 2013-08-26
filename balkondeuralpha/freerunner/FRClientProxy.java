package balkondeuralpha.freerunner;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class FRClientProxy extends FRCommonProxy{
	@Override
	public void registerThings() {
		TickRegistry.registerTickHandler(new RenderTickHandler(), Side.CLIENT);
		RenderingRegistry.registerBlockHandler(new CustomBlockRenderer());
	}
}

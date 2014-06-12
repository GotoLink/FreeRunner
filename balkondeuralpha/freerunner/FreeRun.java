package balkondeuralpha.freerunner;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "freerun", name = "Free Runner Mod", version = "1.0")
public class FreeRun {
	@SidedProxy(clientSide = "balkondeuralpha.freerunner.FRClientProxy", serverSide = "balkondeuralpha.freerunner.FRCommonProxy")
	public static FRCommonProxy proxy;

	@EventHandler
	public void initFreeRun(FMLPreInitializationEvent event) {
		proxy.registerThings(event);
	}

}

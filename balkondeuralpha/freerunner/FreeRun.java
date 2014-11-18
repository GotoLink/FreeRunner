package balkondeuralpha.freerunner;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;

@Mod(modid = "freerun", name = "Free Runner Mod", useMetadata = true)
public class FreeRun {
	@SidedProxy(clientSide = "balkondeuralpha.freerunner.client.FRClientProxy", serverSide = "balkondeuralpha.freerunner.FRCommonProxy")
	public static FRCommonProxy proxy;

	@EventHandler
	public void initFreeRun(FMLPreInitializationEvent event) {
		proxy.registerThings(event);
	}

    @EventHandler
    public void remap(FMLMissingMappingsEvent event){
        for(FMLMissingMappingsEvent.MissingMapping missingMapping:event.get()){
            switch(missingMapping.type){
                case ITEM:
                    missingMapping.remap(GameData.getItemRegistry().getObject(missingMapping.name.replace(" ", "")));
                    break;
                case BLOCK:
                    missingMapping.remap(GameData.getBlockRegistry().getObject(missingMapping.name.replace(" ", "")));
                    break;
            }
        }
    }
}

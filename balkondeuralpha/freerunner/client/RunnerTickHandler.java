package balkondeuralpha.freerunner.client;

import balkondeuralpha.freerunner.FRCommonProxy;
import balkondeuralpha.freerunner.FreeRun;
import balkondeuralpha.freerunner.FreerunPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class RunnerTickHandler {
    private final KeyBinding key;
    private final Minecraft mc;
	private FreerunPlayer runner;
	public RunnerTickHandler() {
        mc = Minecraft.getMinecraft();
        key = mc.gameSettings.keyBindSprint;
	}

    @SubscribeEvent
    public void keyEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && mc.thePlayer != null) {
            if(runner==null)
                runner = FreerunPlayer.get(mc.thePlayer);
            if (key.getIsKeyPressed()) {
                runner.freeRunning = FRCommonProxy.properties.enableFreerunToggle?!runner.freeRunning:true;
                FreeRun.proxy.sendFreeRunPacket(runner.freeRunning);
            }else if(!FRCommonProxy.properties.enableFreerunToggle){
                runner.freeRunning = false;
                FreeRun.proxy.sendFreeRunPacket(false);
            }
        }
    }
}

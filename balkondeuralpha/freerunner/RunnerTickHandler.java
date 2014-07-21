package balkondeuralpha.freerunner;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class RunnerTickHandler {
    private static final KeyBinding key = Minecraft.getMinecraft().gameSettings.keyBindSprint;
	public FreerunPlayer runner;
	public RunnerTickHandler() {
	}

    @SubscribeEvent
    public void keyEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            if(runner==null)
                runner = FreerunPlayer.get(Minecraft.getMinecraft().thePlayer);
            if (key.getIsKeyPressed() && !FRCommonProxy.properties.enableFreerunToggle) {
                runner.freeRunning = true;
            }else if(!key.getIsKeyPressed())
                runner.freeRunning = FRCommonProxy.properties.enableFreerunToggle && !runner.freeRunning;
        }
    }
}

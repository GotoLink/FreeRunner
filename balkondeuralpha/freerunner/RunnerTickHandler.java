package balkondeuralpha.freerunner;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class RunnerTickHandler {
    private static final KeyBinding key = new KeyBinding("Freerun", Keyboard.getKeyIndex("LCONTROL"), "key.categories.movement");
	public FreerunPlayer runner;
	public RunnerTickHandler() {
        ClientRegistry.registerKeyBinding(key);
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

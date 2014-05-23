package balkondeuralpha.freerunner;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class RunnerTickHandler {
    private static final KeyBinding key = new KeyBinding("Freerun", Keyboard.getKeyIndex("LCONTROL"), "key.categories.movement");
	public final FreerunPlayer runner;

	public RunnerTickHandler(FreerunPlayer freerun) {
		this.runner = freerun;
        ClientRegistry.registerKeyBinding(key);
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null)
			return;
		if (FRCommonProxy.properties.enableAnimations) {
			runner.updateAnimations(event.renderTickTime);
		}
	}

    @SubscribeEvent
    public void keyEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            if (key.getIsKeyPressed() && !FRCommonProxy.properties.enableFreerunToggle) {
                runner.freeRunning = true;
            }else if(!key.getIsKeyPressed())
                runner.freeRunning = FRCommonProxy.properties.enableFreerunToggle && !runner.freeRunning;
        }
    }
}

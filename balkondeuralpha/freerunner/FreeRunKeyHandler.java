package balkondeuralpha.freerunner;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class FreeRunKeyHandler extends KeyHandler {
	public FreeRunKeyHandler() {
		super(new KeyBinding[] { new KeyBinding("Freerun", Keyboard.getKeyIndex("LCONTROL")) }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (tickEnd && Minecraft.getMinecraft().thePlayer != null) {
			if (!FRCommonProxy.properties.enableFreerunToggle) {
				PlayerEventHandler.freeRunners.get(Minecraft.getMinecraft().thePlayer.username).freeRunning = true;
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if (tickEnd && Minecraft.getMinecraft().thePlayer != null) {
			FreerunPlayer runner = PlayerEventHandler.freeRunners.get(Minecraft.getMinecraft().thePlayer.username);
			if (!FRCommonProxy.properties.enableFreerunToggle) {
				runner.freeRunning = false;
			} else {
				runner.freeRunning = !runner.freeRunning;
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
}

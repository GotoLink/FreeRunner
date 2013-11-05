package balkondeuralpha.freerunner;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class RenderTickHandler implements ITickHandler {
	public final FreerunPlayer freerun;
	private final EnumSet<TickType> types = EnumSet.of(TickType.RENDER);

	public RenderTickHandler(FreerunPlayer freerun) {
		this.freerun = freerun;
	}

	@Override
	public String getLabel() {
		return "FreeRunRenderTick";
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null)
			return;
		if (FRCommonProxy.properties.enableAnimations && tickData[0] != null) {
			freerun.updateAnimations((Float) tickData[0]);
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return types;
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}
}

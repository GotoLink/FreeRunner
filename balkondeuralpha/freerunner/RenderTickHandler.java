package balkondeuralpha.freerunner;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class RenderTickHandler implements ITickHandler{

	public FreerunPlayer		freerun;
	public Animator			animator;

	public void setFreerunPlayer(FreerunPlayer freerun)
	{
		this.freerun = freerun;
	}
	
	public void setAnimator(Animator animator)
	{
		this.animator = animator;
		animator.setFreerunPlayer(freerun);
	}
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;
		if (FreeRun.properties.enableAnimations && tickData[0]!=null)
		{
			freerun.updateAnimations((Float) tickData[0]);
			animator.setRenderTime((Float) tickData[0]);
		}
	}
	@Override
	public EnumSet<TickType> ticks() {
		return types;
	}
	@Override
	public String getLabel() {
		return "FreeRunTick";
	}
	private final EnumSet<TickType> types = EnumSet.of(TickType.RENDER); 
}

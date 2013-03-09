package balkondeuralpha.freerunner;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.RenderPlayerBase;
@SideOnly(Side.CLIENT)
public class FR_Animator extends RenderPlayerBase
{
	public FR_Animator(RenderPlayerAPI renderplayerapi)
	{
		super(renderplayerapi);
		freerun = null;
		renderTime = 0F;
		FreeRun.instance.setAnimator(this);
	}
	
	public void setFreerunPlayer(FR_FreerunPlayer freerun)
	{
		this.freerun = freerun;
	}
	
	public void setRenderTime(float f)
	{
		renderTime = f;
	}
	
	public void onRender(ModelBiped model, Entity entity)
	{
		if (!FreeRun.instance.properties.enableAnimations || freerun == null /*|| freerun.player != entity*/)//FIXME
		{
			return;
		}
		FR_Move move = freerun.move;
		if (move != null)
		{
			FR_Animation anim = freerun.move.getAnimation();
			if (anim != null)
			{
				System.out.println(renderTime);
				anim.doAnimate(model, move.prevAnimProgress + (move.animProgress - move.prevAnimProgress) * renderTime, renderTime);
			}
		}
	}
	
	private FR_FreerunPlayer	freerun;
	private float				renderTime;
}

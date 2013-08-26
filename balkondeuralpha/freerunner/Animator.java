package balkondeuralpha.freerunner;

import balkondeuralpha.freerunner.moves.Move;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class Animator extends RenderPlayerBase
{
	public Animator(RenderPlayerAPI renderplayerapi)
	{
		super(renderplayerapi);
		freerun = null;
		renderTime = 0F;
		FreeRun.instance.setAnimator(this);
	}
	
	public void setFreerunPlayer(FreerunPlayer freerun)
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
		Move move = freerun.move;
		if (move != null)
		{
			Animation anim = freerun.move.getAnimation();
			if (anim != null)
			{
				System.out.println(renderTime);
				anim.doAnimate(model, move.prevAnimProgress + (move.animProgress - move.prevAnimProgress) * renderTime, renderTime);
			}
		}
	}
	
	private FreerunPlayer	freerun;
	private float				renderTime;
}

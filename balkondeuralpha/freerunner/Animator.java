package balkondeuralpha.freerunner;

import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.ForgeSubscribe;
import balkondeuralpha.freerunner.moves.Move;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Animator {
	private final FreerunPlayer freerun;

	public Animator(FreerunPlayer freerun) {
		this.freerun = freerun;
	}

	@ForgeSubscribe
	public void onRender(RenderPlayerEvent.Post event) {
		if (!FRCommonProxy.properties.enableAnimations || freerun == null || freerun.player != event.entity) {
			return;
		}
		Move move = freerun.move;
		if (move != null) {
			Animation anim = move.getAnimation();
			if (anim != null) {
				anim.doAnimate(event.renderer.modelBipedMain, move.prevAnimProgress + (move.animProgress - move.prevAnimProgress) * event.partialRenderTick, event.partialRenderTick);
			}
		}
	}
}

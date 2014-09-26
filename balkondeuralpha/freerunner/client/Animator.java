package balkondeuralpha.freerunner.client;

import balkondeuralpha.freerunner.FreerunPlayer;
import balkondeuralpha.freerunner.animations.Animation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import balkondeuralpha.freerunner.moves.Move;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Animator {
	public Animator() {
	}

	@SubscribeEvent
	public void onRender(RenderPlayerEvent.Post event) {
		Move move = FreerunPlayer.get(event.entityPlayer).move;
		if (move != null) {
			Animation anim = move.getAnimation();
            try{
                if (anim != null) {
                    anim.doAnimate(event.renderer.modelBipedMain, move.prevAnimProgress + (move.animProgress - move.prevAnimProgress) * event.partialRenderTick, event.partialRenderTick);
                }
            }catch(Exception ignored){

            }
		}
	}
}

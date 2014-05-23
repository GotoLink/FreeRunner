package balkondeuralpha.freerunner;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.event.ForgeSubscribe;
import balkondeuralpha.freerunner.moves.Move;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public class Animator {
	private final FreerunPlayer freerun;
    private final Field model = RenderPlayer.class.getDeclaredFields()[1];

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
            try{
                if (anim != null) {
                    anim.doAnimate((ModelBiped) model.get(event.renderer), move.prevAnimProgress + (move.animProgress - move.prevAnimProgress) * event.partialRenderTick, event.partialRenderTick);
                }
            }catch(Exception ignored){

            }
		}
	}
}

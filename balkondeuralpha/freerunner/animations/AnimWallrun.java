package balkondeuralpha.freerunner.animations;

import balkondeuralpha.freerunner.animations.Animation;
import balkondeuralpha.freerunner.moves.Move;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;

public class AnimWallrun implements Animation
{
    @Override
    public void set(Move move){

    }

	@Override
    @SideOnly(Side.CLIENT)
	public void doAnimate(ModelBiped model, float progress, float rendertime){
		/*
		float f = progress * 3F;
		if (progress > 0.6F)
		{
			f -= (progress - 0.6F) * 6F;
		}
		model.bipedRightLeg.rotateAngleX = -f;
		*/
	}
}

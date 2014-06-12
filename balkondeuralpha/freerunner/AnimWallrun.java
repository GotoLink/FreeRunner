package balkondeuralpha.freerunner;

import net.minecraft.client.model.ModelBiped;

public class AnimWallrun implements Animation
{
	@Override
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

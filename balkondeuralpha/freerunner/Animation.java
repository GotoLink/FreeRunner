package balkondeuralpha.freerunner;

import net.minecraft.client.model.ModelBiped;

public abstract class Animation
{
	public abstract void doAnimate(ModelBiped model, float progress, float renderTime);
}

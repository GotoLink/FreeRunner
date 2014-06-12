package balkondeuralpha.freerunner;

import net.minecraft.client.model.ModelBiped;

public interface Animation{
	public void doAnimate(ModelBiped model, float progress, float renderTime);
}

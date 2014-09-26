package balkondeuralpha.freerunner.animations;

import balkondeuralpha.freerunner.moves.Move;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;

public interface Animation{
    public void set(Move move);

    @SideOnly(Side.CLIENT)
	public void doAnimate(ModelBiped model, float progress, float renderTime);
}

package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.AnimRoll;
import balkondeuralpha.freerunner.FreerunPlayer;
import net.minecraft.util.MathHelper;

public class MoveRoll extends Move{
    private boolean		moved;
    private int			progress;
    private final int	finished	= 16;
	public MoveRoll(FreerunPlayer freerunhandler){
		super(freerunhandler);
        animation = new AnimRoll(getPlayer());
        animProgress = 1F;
	}
	
	@Override
	public void updateMove(){
        super.updateMove();
        if (animProgress < 1F) {
            animProgress += 0.05F;
        }
		if (!moved){
			float f = 1.0F;
			double d = -MathHelper.sin((getPlayer().rotationYaw / 180F) * 3.141593F) * f;
			double d1 = MathHelper.cos((getPlayer().rotationYaw / 180F) * 3.141593F) * f;
            getPlayer().setVelocity(d, 0, d1);
			moved = true;
		}
		if (progress > finished){
			moveDone();
		}
		progress++;
	}
	
	@Override
	public void moveDone(){
		moved = false;
		progress = 0;
		super.moveDone();
	}
	
	@Override
	public float getAnimationProgress()	{
		float f = (float) progress / (float) finished;
		if (f > 1.0F){
			f = 1.0F;
		}
		if (f < 0.0F){
			f = 0.0F;
		}
		return f;
	}

    public void start(){
        animProgress = 0F;
        ((AnimRoll)animation).set(this);
    }
}

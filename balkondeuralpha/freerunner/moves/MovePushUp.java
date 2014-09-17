package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.FreerunPlayer;
import balkondeuralpha.freerunner.Situation;

public class MovePushUp extends Move{
    private float		yLimit;
    private final float	distance	= 1.0F;
    private final float	speed		= 0.01F;
    private final float	limitSpeed	= 0.1F;
	public MovePushUp(FreerunPlayer freerunhandler)
	{
		super(freerunhandler);
	}
	
	@Override
	public void updateMove(){
		super.updateMove();
		if (getPlayer().boundingBox.minY <= yLimit + 1.0F){
			nextMotionY += speed;
			doMoves();
		} else{
			//moveDone();
		}
		
		if (lookDirection == FreerunPlayer.LOOK_WEST){
			if (getPlayer().posZ - startPosZ <= distance){
				nextMotionZ += speed;
			} else{
				moveDone();
			}
			
		} else if (lookDirection == FreerunPlayer.LOOK_NORTH){
			if (getPlayer().posX - startPosX >= -distance){
				nextMotionX -= speed;
			} else{
				moveDone();
			}
			
		} else if (lookDirection == FreerunPlayer.LOOK_EAST){
			if (getPlayer().posZ - startPosZ >= -distance){
				nextMotionZ -= speed;
			} else{
				moveDone();
			}
			
		} else if (lookDirection == FreerunPlayer.LOOK_SOUTH){
			if (getPlayer().posX - startPosX <= distance){
				nextMotionX += speed;
			} else{
				moveDone();
			}
		}
		
		if (nextMotionX > limitSpeed){
			nextMotionX = limitSpeed;
		} else if (nextMotionX < -limitSpeed){
			nextMotionX = -limitSpeed;
		}
		
		if (nextMotionY > limitSpeed){
			nextMotionY = limitSpeed;
		}
		
		if (nextMotionZ > limitSpeed){
			nextMotionZ = limitSpeed;
		} else if (nextMotionZ < -limitSpeed){
			nextMotionZ = -limitSpeed;
		}
	}
	
	@Override
	protected void doMoves(){
        getPlayer().motionX = nextMotionX;
        getPlayer().motionY = nextMotionY;
        getPlayer().motionZ = nextMotionZ;
	}
	
	public void performMove(int lookdirection, float y)	{
		yLimit = y;
		performMove(lookdirection);
	}

    @Override
    public boolean canPerform(Situation situation){
        return situation.canPushUp()!=0;
    }

    @Override
    public void performMove(int look){
        super.performMove(look);
        getPlayer().addExhaustion(0.3F);
    }
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
}

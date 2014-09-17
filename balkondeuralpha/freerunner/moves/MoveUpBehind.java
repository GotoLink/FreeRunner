package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.FreerunPlayer;
import balkondeuralpha.freerunner.Situation;

public class MoveUpBehind extends Move{
    private final float	limitSpeed	= 0.5F;
    private boolean		yDone;
    private boolean		xzDone;
	public MoveUpBehind(FreerunPlayer freerunhandler){
		super(freerunhandler);
		yDone = xzDone = false;
	}
	
	@Override
	public void updateMove(){
		super.updateMove();
		float yspeed = 0.05F;
		float xzspeed = 0.05F;
		if (!yDone)	{
			if (getPlayer().posY - startPosY <= 1.4F){
				nextMotionY += yspeed;
			} else{
				yDone = true;
			}
		}
		
		if (!xzDone){
			if (lookDirection == FreerunPlayer.LOOK_WEST){
				if (getPlayer().posZ - startPosZ >= -1.0F){
					nextMotionZ -= xzspeed;
				} else{
					xzDone = true;
				}
			} else if (lookDirection == FreerunPlayer.LOOK_NORTH){
				if (getPlayer().posX - startPosX <= 1.0F){
					nextMotionX += xzspeed;
				} else{
					xzDone = true;
				}
			} else if (lookDirection == FreerunPlayer.LOOK_EAST){
				if (getPlayer().posZ - startPosZ <= 1.0F){
					nextMotionZ += xzspeed;
				} else{
					xzDone = true;
				}
			} else if (lookDirection == FreerunPlayer.LOOK_SOUTH){
				if (getPlayer().posX - startPosX >= -1.0F){
					nextMotionX -= xzspeed;
				} else{
					xzDone = true;
				}
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
		
		if (yDone){
			nextMotionY = 0D;
		}
		if (xzDone)	{
			nextMotionX = 0D;
			nextMotionZ = 0D;
			if (yDone){
				moveDone();
			}
		}
		doMoves();
	}

    @Override
    public boolean canPerform(Situation situation){
        return situation.hasEdgeUpBehind();
    }

    @Override
    public void performMove(int look){
        super.performMove(look);
        getPlayer().addExhaustion(0.3F);
    }
	
	@Override
	public void moveDone(){
		xzDone = false;
		yDone = false;
		freerunEngine.tryGrabLedge();
		super.moveDone();
	}
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
}

package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.FreerunPlayer;
import balkondeuralpha.freerunner.Situation;

public class MoveClimb extends Move{
    protected int			direction;
    protected float			distance;
    protected final float	speed			= 0.03F;
    protected final float	limitSpeed		= 0.1F;
	public MoveClimb(FreerunPlayer freerunhandler, int direction, float distance){
		super(freerunhandler);
		this.direction = direction;
		this.distance = distance;
	}
	
	@Override
	public void updateMove(){
		super.updateMove();
		if (direction == DIRECTION_UP)
		{
			if (getPlayer().posY - startPosY <= distance)
			{
				nextMotionY += speed;
				doMoves();
			} else
			{
				moveDone();
			}
		} else if (direction == DIRECTION_DOWN)
		{
			if (getPlayer().posY - startPosY >= -distance)
			{
				nextMotionY -= speed;
				doMoves();
			} else
			{
				moveDone();
			}
		} else if (direction == DIRECTION_LEFT)
		{
			if (lookDirection == FreerunPlayer.LOOK_WEST)
			{
				if (getPlayer().posX - startPosX <= distance)
				{
					nextMotionX += speed;
					doMoves();
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FreerunPlayer.LOOK_NORTH)
			{
				if (getPlayer().posZ - startPosZ <= distance)
				{
					nextMotionZ += speed;
					doMoves();
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FreerunPlayer.LOOK_EAST)
			{
				if (getPlayer().posX - startPosX >= -distance)
				{
					nextMotionX -= speed;
					doMoves();
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FreerunPlayer.LOOK_SOUTH)
			{
				if (getPlayer().posZ - startPosZ >= -distance)
				{
					nextMotionZ -= speed;
					doMoves();
				} else
				{
					moveDone();
				}
			}
		} else if (direction == DIRECTION_RIGHT)
		{
			if (lookDirection == FreerunPlayer.LOOK_WEST)
			{
				if (getPlayer().posX - startPosX >= -distance)
				{
					nextMotionX -= speed;
					doMoves();
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FreerunPlayer.LOOK_NORTH)
			{
				if (getPlayer().posZ - startPosZ >= -distance)
				{
					nextMotionZ -= speed;
					doMoves();
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FreerunPlayer.LOOK_EAST)
			{
				if (getPlayer().posX - startPosX <= distance)
				{
					nextMotionX += speed;
					doMoves();
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FreerunPlayer.LOOK_SOUTH)
			{
				if (getPlayer().posZ - startPosZ <= distance)
				{
					nextMotionZ += speed;
					doMoves();
				} else
				{
					moveDone();
				}
			}
		}
		
		if (nextMotionX > limitSpeed)
		{
			nextMotionX = limitSpeed;
		} else if (nextMotionX < -limitSpeed)
		{
			nextMotionX = -limitSpeed;
		}
		
		if (nextMotionY > limitSpeed)
		{
			nextMotionY = limitSpeed;
		}
		
		if (nextMotionZ > limitSpeed)
		{
			nextMotionZ = limitSpeed;
		} else if (nextMotionZ < -limitSpeed)
		{
			nextMotionZ = -limitSpeed;
		}
	}

    @Override
    public boolean canPerform(Situation situation){
        if(direction == DIRECTION_UP)
            situation.canClimbUp();
        else if(direction == DIRECTION_DOWN)
            situation.canClimbDown();
        else if(direction == DIRECTION_LEFT)
            situation.canClimbLeft();
        else if(direction == DIRECTION_RIGHT)
            situation.canClimbRight();
        return false;
    }
	
	@Override
	public void moveDone(){
		int i = direction > 2 ? 5 : 10;
		addMovementPause(i);
		super.moveDone();
	}
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
}

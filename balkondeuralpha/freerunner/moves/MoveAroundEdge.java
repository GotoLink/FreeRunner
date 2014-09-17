package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.FreerunPlayer;
import balkondeuralpha.freerunner.Situation;

public class MoveAroundEdge extends MoveClimb{
    private boolean		sideDone;
    private boolean		forwardDone;
    private final float	speed		= 0.05F;
    private final float	limitSpeed	= 0.1F;
	public MoveAroundEdge(FreerunPlayer freerunhandler, int direction, float distance){
		super(freerunhandler, direction, distance);
		sideDone = forwardDone = false;
	}
	
	@Override
	public void updateMove(){
		if (direction == DIRECTION_LEFT){
			if (lookDirection == FreerunPlayer.LOOK_WEST){
                getPlayer().setPosition(getPlayer().posX + 2D, getPlayer().posY, getPlayer().posZ + 2D);
			} else if (lookDirection == FreerunPlayer.LOOK_NORTH){
                getPlayer().setPosition(getPlayer().posX - 2D, getPlayer().posY, getPlayer().posZ + 2D);
			} else if (lookDirection == FreerunPlayer.LOOK_EAST){
                getPlayer().setPosition(getPlayer().posX - 2D, getPlayer().posY, getPlayer().posZ - 2D);
			} else if (lookDirection == FreerunPlayer.LOOK_SOUTH){
                getPlayer().setPosition(getPlayer().posX + 2D, getPlayer().posY, getPlayer().posZ - 2D);
			}
		} else if (direction == DIRECTION_RIGHT){
			if (lookDirection == FreerunPlayer.LOOK_WEST){
                getPlayer().setPosition(getPlayer().posX - 2D, getPlayer().posY, getPlayer().posZ - 1D);
			} else if (lookDirection == FreerunPlayer.LOOK_NORTH){
                getPlayer().setPosition(getPlayer().posX + 1D, getPlayer().posY, getPlayer().posZ - 2D);
			} else if (lookDirection == FreerunPlayer.LOOK_EAST){
                getPlayer().setPosition(getPlayer().posX + 2D, getPlayer().posY, getPlayer().posZ + 1D);
			} else if (lookDirection == FreerunPlayer.LOOK_SOUTH){
                getPlayer().setPosition(getPlayer().posX - 1D, getPlayer().posY, getPlayer().posZ + 2D);
			}
		}
		moveDone();
		
		/*
		if (!sideDone)
		{
			if (direction == FR_MoveClimb.DIRECTION_LEFT)
			{
				if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
				{
					if (player.posX - startPosX <= distance)
					{
						nextMotionX += speed;
					} else
					{
						sideDone = true;
					}
					player.setPosition(player.posX + 1D, player.posY, player.posZ + 1D);
					moveDone();
				} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
				{
					if (player.posZ - startPosZ <= distance)
					{
						nextMotionZ += speed;
					} else
					{
						sideDone = true;
					}
					
				} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
				{
					if (player.posX - startPosX >= -distance)
					{
						nextMotionX -= speed;
					} else
					{
						sideDone = true;
					}
					
				} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
				{
					if (player.posZ - startPosZ >= -distance)
					{
						nextMotionZ -= speed;
					} else
					{
						sideDone = true;
					}
					
				}
			} else if (direction == FR_MoveClimb.DIRECTION_RIGHT)
			{
				if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
				{
					if (player.posX - startPosX >= -distance)
					{
						nextMotionX -= speed;
					} else
					{
						sideDone = true;
					}
				} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
				{
					if (player.posZ - startPosZ >= -distance)
					{
						nextMotionZ -= speed;
					} else
					{
						sideDone = true;
					}
				} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
				{
					if (player.posX - startPosX <= distance)
					{
						nextMotionX += speed;
					} else
					{
						sideDone = true;
					}
				} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
				{
					if (player.posZ - startPosZ <= distance)
					{
						nextMotionZ += speed;
					} else
					{
						sideDone = true;
					}
				}
			}
		}
		
		if (!forwardDone)
		{
			if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
			{
				if (player.posZ - startPosZ <= distance)
				{
					nextMotionZ += speed;
				} else
				{
					forwardDone = true;
				}
				
			} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
			{
				if (player.posX - startPosX >= -distance)
				{
					nextMotionX -= speed;
				} else
				{
					forwardDone = true;
				}
				
			} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
			{
				if (player.posZ - startPosZ >= -distance)
				{
					nextMotionZ -= speed;
				} else
				{
					forwardDone = true;
				}
				
			} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
			{
				if (player.posX - startPosX <= distance)
				{
					nextMotionX += speed;
				} else
				{
					forwardDone = true;
				}
			}
		}*/
		/*
		if (nextMotionX > limitSpeed)
		{
			nextMotionX = limitSpeed;
		} else if (nextMotionX < -limitSpeed)
		{
			nextMotionX = -limitSpeed;
		}
		
		if (nextMotionZ > limitSpeed)
		{
			nextMotionZ = limitSpeed;
		} else if (nextMotionZ < -limitSpeed)
		{
			nextMotionZ = -limitSpeed;
		}
		
		if (sideDone)
		{
			if (lookDirection == FR_FreerunPlayer.LOOK_WEST || lookDirection == FR_FreerunPlayer.LOOK_EAST)
			{
				nextMotionX = 0D;
			} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH || lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
			{
				nextMotionZ = 0D;
			}
		}
		
		if (forwardDone)
		{
			if (lookDirection == FR_FreerunPlayer.LOOK_WEST || lookDirection == FR_FreerunPlayer.LOOK_EAST)
			{
				nextMotionZ = 0D;
			} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH || lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
			{
				nextMotionX = 0D;
			}
			
			if (sideDone)
			{
				moveDone();
			}
		}
		*/
		
		//doMoves(player);
	}

    @Override
    public boolean canPerform(Situation situation){
        if(direction==DIRECTION_LEFT)
            return situation.canClimbAroundEdgeLeft();
        else if(direction==DIRECTION_RIGHT)
            return situation.canClimbAroundEdgeRight();
        return false;
    }
	
	@Override
	public void moveDone(){
		sideDone = false;
		forwardDone = false;
		if (direction == DIRECTION_LEFT){
            getPlayer().rotationYaw += 90F;
		} else if (direction == DIRECTION_RIGHT){
            getPlayer().rotationYaw -= 90F;
		}
		freerunEngine.tryGrabLedge();
		super.moveDone();
	}
	
	@Override
	public void doMoves(){
        getPlayer().motionX = nextMotionX;
        getPlayer().motionZ = nextMotionZ;
	}
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
}

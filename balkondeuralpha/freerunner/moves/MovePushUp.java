package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.FreerunPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class MovePushUp extends Move
{
	protected MovePushUp(FreerunPlayer freerunhandler)
	{
		super(freerunhandler);
	}
	
	@Override
	public void updateMove()
	{
		super.updateMove();
		if (player.boundingBox.minY <= yLimit + 1.0F)
		{
			nextMotionY += speed;
			doMoves(player);
		} else
		{
			//moveDone();
		}
		
		if (lookDirection == FreerunPlayer.LOOK_WEST)
		{
			if (player.posZ - startPosZ <= distance)
			{
				nextMotionZ += speed;
			} else
			{
				moveDone();
			}
			
		} else if (lookDirection == FreerunPlayer.LOOK_NORTH)
		{
			if (player.posX - startPosX >= -distance)
			{
				nextMotionX -= speed;
			} else
			{
				moveDone();
			}
			
		} else if (lookDirection == FreerunPlayer.LOOK_EAST)
		{
			if (player.posZ - startPosZ >= -distance)
			{
				nextMotionZ -= speed;
			} else
			{
				moveDone();
			}
			
		} else if (lookDirection == FreerunPlayer.LOOK_SOUTH)
		{
			if (player.posX - startPosX <= distance)
			{
				nextMotionX += speed;
			} else
			{
				moveDone();
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
	protected void doMoves(EntityPlayer entityplayer)
	{
		entityplayer.motionX = nextMotionX;
		entityplayer.motionY = nextMotionY;
		entityplayer.motionZ = nextMotionZ;
	}
	
	public void performMove(EntityPlayer entityplayer, int lookdirection, float y)
	{
		yLimit = y;
		performMove(entityplayer, lookdirection);
	}
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
	
	private float		yLimit;
	private final float	distance	= 1.0F;
	private final float	speed		= 0.01F;
	private final float	limitSpeed	= 0.1F;
}

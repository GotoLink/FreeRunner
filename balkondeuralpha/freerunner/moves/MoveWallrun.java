package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.AnimWallrun;
import balkondeuralpha.freerunner.FreerunPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class MoveWallrun extends Move
{
	protected MoveWallrun(FreerunPlayer freerunhandler)
	{
		super(freerunhandler);
		animation = new AnimWallrun();
	}
	
	@Override
	public void updateMove()
	{
		super.updateMove();
		if (player.posY - startPosY <= distance)
		{
			nextMotionY += speed;
			doMoves(player);
		} else
		{
			moveDone();
		}
		
		if (nextMotionY > limitSpeed)
		{
			nextMotionY = limitSpeed;
		}
	}
	
	public void performMove(EntityPlayer entityplayer, int lookdirection, float distance)
	{
		performMove(entityplayer, lookdirection);
		this.distance = distance;
	}
	
	@Override
	protected void doMoves(EntityPlayer entityplayer)
	{
		entityplayer.motionY = nextMotionY;
	}
	
	@Override
	public void moveDone()
	{
		super.moveDone();
		freerunEngine.tryGrabLedge();
	}
	
	@Override
	public float getAnimationProgress()
	{
		double d = player.posY - startPosY;
		return (float) (d / distance);
	}
	
	protected float		distance;
	private final float	speed		= 0.03F;
	private final float	limitSpeed	= 0.2F;
}

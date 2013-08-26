package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.Animation;
import balkondeuralpha.freerunner.FreerunPlayer;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Move
{
	protected Move(FreerunPlayer freerunhandler)
	{
		freerunEngine = freerunhandler;
		lookDirection = 0;
		blockings = 0;
		nextMotionX = nextMotionY = nextMotionZ = 0D;
		animProgress = prevAnimProgress = 0F;
		deltaPos = new double[3];
		prevDeltaPos = new double[3];
	}
	
	public void updateMove()
	{
		prevAnimProgress = animProgress;
		animProgress = Math.min(Math.max(getAnimationProgress(), 0F), 1F);
	}
	
	@Deprecated
	protected boolean canMoveFurther(EntityPlayer entityplayer)
	{
		for (int i = 0; i < 3; i++)
		{
			prevDeltaPos[i] = deltaPos[i];
			if (i == 0)
			{
				deltaPos[i] = entityplayer.posX - startPosX;
			} else if (i == 1)
			{
				deltaPos[i] = entityplayer.posY - startPosY;
			} else
			{
				deltaPos[i] = entityplayer.posZ - startPosZ;
			}
			
			if (deltaPos[i] != prevDeltaPos[i])
			{
				continue;
			} else
			{
				blockings++;
			}
		}
		return blockings < 15;
	}
	
	protected void doMoves(EntityPlayer entityplayer)
	{
		player.motionX = nextMotionX;
		player.motionY = nextMotionY;
		player.motionZ = nextMotionZ;
	}
	
	public final void performMove(EntityPlayer entityplayer, int lookdirection)
	{
		if (Move.paused)
		{
			return;
		}
		lookDirection = lookdirection;
		player = entityplayer;
		startPosX = player.posX;
		startPosY = player.posY;
		startPosZ = player.posZ;
		freerunEngine.setMove(this);
	}
	
	public void moveDone()
	{
		abortMove();
	}
	
	private void abortMove()
	{
		blockings = 0;
		nextMotionX = 0D;
		nextMotionY = 0D;
		nextMotionZ = 0D;
		if (equals(freerunEngine.move))
		{
			freerunEngine.setMove(null);
		}
	}
	
	public double distanceFromStart(double x, double y, double z)
	{
		double dx = x - startPosX;
		double dy = y - startPosY;
		double dz = z - startPosZ;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	protected abstract float getAnimationProgress();
	
	public final Animation getAnimation()
	{
		return animation;
	}
	
	public double				startPosX,startPosY,startPosZ;
	protected Animation		animation;
	public float				animProgress,prevAnimProgress;
	protected int				lookDirection;
	protected FreerunPlayer	freerunEngine;
	protected EntityPlayer			player;
	protected double			nextMotionX,nextMotionY,nextMotionZ;
	protected int				blockings;
	protected double[]			deltaPos,prevDeltaPos;
	
	public static void addMovementPause(int ticks)
	{
		Move.paused = true;
		Move.pauseTimer = ticks;
	}
	
	public static void onUpdate(FreerunPlayer freerunengine)
	{
		if (!Move.paused)
		{
			if (freerunengine.move != null)
			{
				freerunengine.move.updateMove();
			}
		}
		
		if (Move.pauseTimer > 0)
		{
			Move.pauseTimer--;
		} else
		{
			Move.paused = false;
		}
	}
	
	public static void addAllMoves(FreerunPlayer freerunengine)
	{
		Move.climbUp = new MoveClimb(freerunengine, MoveClimb.DIRECTION_UP, 0.8F);
		Move.climbDown = new MoveClimb(freerunengine, MoveClimb.DIRECTION_DOWN, 1.0F);
		Move.climbLeft = new MoveClimb(freerunengine, MoveClimb.DIRECTION_LEFT, 1.0F);
		Move.climbRight = new MoveClimb(freerunengine, MoveClimb.DIRECTION_RIGHT, 1.0F);
		
		Move.climbAroundLeft = new MoveAroundEdge(freerunengine, MoveClimb.DIRECTION_LEFT, 2.0F);
		Move.climbAroundRight = new MoveAroundEdge(freerunengine, MoveClimb.DIRECTION_RIGHT, 2.0F);
		
		Move.ejectUp = new MoveEject(freerunengine, MoveClimb.DIRECTION_UP);
		Move.ejectBack = new MoveEject(freerunengine, MoveClimb.DIRECTION_DOWN);
		Move.ejectLeft = new MoveEject(freerunengine, MoveClimb.DIRECTION_LEFT);
		Move.ejectRight = new MoveEject(freerunengine, MoveClimb.DIRECTION_RIGHT);
		
		Move.wallrun = new MoveWallrun(freerunengine);
		Move.pushUp = new MovePushUp(freerunengine);
		Move.upBehind = new MoveUpBehind(freerunengine);
		
		Move.roll = new MoveRoll(freerunengine);
	}
	
	private static int				pauseTimer;
	protected static boolean		paused;
	public static MoveWallrun	wallrun;
	public static MoveClimb		climbUp,climbDown,climbLeft,climbRight;
	public static MoveClimb		climbAroundLeft,climbAroundRight;
	public static MoveEject		ejectUp,ejectBack,ejectLeft,ejectRight;
	public static MovePushUp		pushUp;
	public static MoveUpBehind	upBehind;
	public static MoveRoll		roll;
}

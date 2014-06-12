package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.Animation;
import balkondeuralpha.freerunner.FreerunPlayer;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Move {
	public double startPosX, startPosY, startPosZ;
	protected Animation animation;
	public float animProgress, prevAnimProgress;
	protected int lookDirection;
	protected FreerunPlayer freerunEngine;
	protected double nextMotionX, nextMotionY, nextMotionZ;
	protected int blockings;
	protected double[] deltaPos, prevDeltaPos;
    public static final int	DIRECTION_UP	= 1;
    public static final int	DIRECTION_DOWN	= 2;
    public static final int	DIRECTION_LEFT	= 3;
    public static final int	DIRECTION_RIGHT	= 4;

	protected Move(FreerunPlayer freerunhandler) {
		freerunEngine = freerunhandler;
		lookDirection = 0;
		blockings = 0;
		nextMotionX = nextMotionY = nextMotionZ = 0D;
		animProgress = prevAnimProgress = 0F;
		deltaPos = new double[3];
		prevDeltaPos = new double[3];
	}

	public double distanceFromStart(double x, double y, double z) {
		double dx = x - startPosX;
		double dy = y - startPosY;
		double dz = z - startPosZ;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public final Animation getAnimation() {
		return animation;
	}

	public abstract float getAnimationProgress();

	public void moveDone() {
		abortMove();
	}

	public final void performMove(int lookdirection) {
		if (freerunEngine.paused) {
			return;
		}
		lookDirection = lookdirection;
		startPosX = getPlayer().posX;
		startPosY = getPlayer().posY;
		startPosZ = getPlayer().posZ;
		freerunEngine.setMove(this);
	}

	public void updateMove() {
		prevAnimProgress = animProgress;
		animProgress = Math.min(Math.max(getAnimationProgress(), 0F), 1F);
	}

	@Deprecated
	protected boolean canMoveFurther(EntityPlayer entityplayer) {
		for (int i = 0; i < 3; i++) {
			prevDeltaPos[i] = deltaPos[i];
			if (i == 0) {
				deltaPos[i] = entityplayer.posX - startPosX;
			} else if (i == 1) {
				deltaPos[i] = entityplayer.posY - startPosY;
			} else {
				deltaPos[i] = entityplayer.posZ - startPosZ;
			}
			if (deltaPos[i] != prevDeltaPos[i]) {
				continue;
			} else {
				blockings++;
			}
		}
		return blockings < 15;
	}

	protected void doMoves() {
        getPlayer().motionX = nextMotionX;
        getPlayer().motionY = nextMotionY;
        getPlayer().motionZ = nextMotionZ;
	}

	private void abortMove() {
		blockings = 0;
		nextMotionX = 0D;
		nextMotionY = 0D;
		nextMotionZ = 0D;
		if (equals(freerunEngine.move)) {
			freerunEngine.setMove(null);
		}
	}

	public void addMovementPause(int ticks) {
        freerunEngine.paused = true;
        freerunEngine.pauseTimer = ticks;
	}

    public EntityPlayer getPlayer(){
        return freerunEngine.player;
    }
}

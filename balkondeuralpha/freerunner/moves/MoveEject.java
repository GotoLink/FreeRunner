package balkondeuralpha.freerunner.moves;

import net.minecraft.util.MathHelper;
import balkondeuralpha.freerunner.FRCommonProxy;
import balkondeuralpha.freerunner.FreerunPlayer;

public class MoveEject extends Move {
	private float power;
	private int direction;
	public MoveEject(FreerunPlayer freerunhandler, int direction) {
		super(freerunhandler);
		this.direction = direction;
		power = 1F;
	}

	@Override
	public float getAnimationProgress() {
		return 0F;
	}

	@Override
	public void moveDone() {
		power = 1.0F;
		super.moveDone();
	}

	public void performMove(int lookdirection, float power) {
		this.power = power;
		performMove(lookdirection);
	}

	@Override
	public void updateMove() {
		super.updateMove();
		if (getPlayer().motionY != 0D && freerunEngine.isClimbing) {
			return;
		}
		if (direction != DIRECTION_UP) {
			if (direction == DIRECTION_LEFT) {
                getPlayer().rotationYaw -= 90F;
			} else if (direction == DIRECTION_RIGHT) {
                getPlayer().rotationYaw += 90F;
			} else {
                getPlayer().rotationYaw += 180F;
			}
			freerunEngine.isClimbing = false;
			float f = 0.35F * power;
			double d = -MathHelper.sin((getPlayer().rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
			double d1 = MathHelper.cos((getPlayer().rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
            getPlayer().addVelocity(d, f, d1);
		} else {
			if (!getPlayer().worldObj.isRemote) {
				if (getPlayer().getCurrentEquippedItem() != null && getPlayer().getCurrentEquippedItem().getItem() == FRCommonProxy.climbGlove) {
					freerunEngine.isClimbing = false;
                    getPlayer().addVelocity(0D, 0.5D, 0D);
				}
			} else if (FRCommonProxy.properties.fixedGloveInSMP) {
				freerunEngine.isClimbing = false;
                getPlayer().addVelocity(0D, 0.5D, 0D);
			}
		}
		addMovementPause(15);
		moveDone();
	}
}

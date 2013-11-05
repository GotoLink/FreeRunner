package balkondeuralpha.freerunner.moves;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import balkondeuralpha.freerunner.FRCommonProxy;
import balkondeuralpha.freerunner.FreerunPlayer;

public class MoveEject extends Move {
	private float power;
	private int direction;

	protected MoveEject(FreerunPlayer freerunhandler, int direction) {
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

	public void performMove(EntityPlayer entityplayer, int lookdirection, float power) {
		this.power = power;
		super.performMove(entityplayer, lookdirection);
	}

	@Override
	public void updateMove() {
		super.updateMove();
		if (player.motionY != 0D && freerunEngine.isClimbing) {
			return;
		}
		if (direction != MoveClimb.DIRECTION_UP) {
			if (direction == MoveClimb.DIRECTION_LEFT) {
				player.rotationYaw -= 90F;
			} else if (direction == MoveClimb.DIRECTION_RIGHT) {
				player.rotationYaw += 90F;
			} else {
				player.rotationYaw += 180F;
			}
			freerunEngine.isClimbing = false;
			float f = 0.35F * power;
			double d = -MathHelper.sin((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
			double d1 = MathHelper.cos((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
			player.addVelocity(d, f, d1);
		} else {
			if (!player.worldObj.isRemote) {
				if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == FRCommonProxy.climbGlove.itemID) {
					freerunEngine.isClimbing = false;
					player.addVelocity(0D, 0.5D, 0D);
				}
			} else if (FRCommonProxy.properties.fixedGloveInSMP) {
				freerunEngine.isClimbing = false;
				player.addVelocity(0D, 0.5D, 0D);
			}
		}
		Move.addMovementPause(15);
		moveDone();
	}
}

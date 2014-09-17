package balkondeuralpha.freerunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import balkondeuralpha.freerunner.moves.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitiesClassAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class FreerunPlayer implements IExtendedEntityProperties{
	public Move move;
	public Situation situation;
    public int pauseTimer;
    public boolean paused;
	public double startPosY, startPosX, startPosZ;
	public double horizontalSpeed;
	public boolean isClimbing, wallRunning, freeRunning;
	public EntityPlayer player;
    //Cached available moves
    private Move wallrun, pushUp, upBehind;
    private MoveClimb climbUp, climbDown, climbLeft, climbRight;
    private MoveClimb climbAroundLeft, climbAroundRight;
    private MoveEject ejectUp, ejectBack, ejectLeft, ejectRight;
    private MoveRoll roll;

	public static final int LOOK_WEST = 0, LOOK_NORTH = 1, LOOK_EAST = 2, LOOK_SOUTH = 3;

	public FreerunPlayer(EntityPlayer player) {
		this.player = player;
		freeRunning = false;
		horizontalSpeed = 0D;
        addAllMoves();
	}

    public void addAllMoves() {
        climbUp = new MoveClimb(this, Move.DIRECTION_UP, 0.8F);
        climbDown = new MoveClimb(this, Move.DIRECTION_DOWN, 1.0F);
        climbLeft = new MoveClimb(this, Move.DIRECTION_LEFT, 1.0F);
        climbRight = new MoveClimb(this, Move.DIRECTION_RIGHT, 1.0F);
        climbAroundLeft = new MoveAroundEdge(this, Move.DIRECTION_LEFT, 2.0F);
        climbAroundRight = new MoveAroundEdge(this, Move.DIRECTION_RIGHT, 2.0F);
        ejectUp = new MoveEject(this, Move.DIRECTION_UP);
        ejectBack = new MoveEject(this, Move.DIRECTION_DOWN);
        ejectLeft = new MoveEject(this, Move.DIRECTION_LEFT);
        ejectRight = new MoveEject(this, Move.DIRECTION_RIGHT);
        wallrun = new MoveWallrun(this);
        pushUp = new MovePushUp(this);
        upBehind = new MoveUpBehind(this);
        roll = new MoveRoll(this);
    }

    public static FreerunPlayer get(EntityPlayer entityPlayer){
        return (FreerunPlayer) entityPlayer.getExtendedProperties("FreeRun");
    }

	public int canHopOver() {
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(true);
		if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			if (isSelectedBlockClose(movingobjectposition, 2.0F) && isSelectedBlockOnLevel(movingobjectposition, 0)) {
				Block b = getSelectedBlock(movingobjectposition);
				Material m = getSelectedBlockMaterial(movingobjectposition);
				AxisAlignedBB boundingbox = b.getCollisionBoundingBoxFromPool(player.worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
				if (m.isSolid() && isBlockAboveAir(2, false, isClimbing)) {
					if (boundingbox != null && boundingbox.maxY - movingobjectposition.blockY > 1.0F) {
						return 2;
					} else if (b.getBlockBoundsMaxY() > player.stepHeight && b.getBlockBoundsMaxY() <= 1.0F && b != Blocks.oak_stairs
							&& b != Blocks.stone_stairs) {
						return 1;
					}
				}
			}
		}
		if (movingobjectposition == null || movingobjectposition.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || movingobjectposition.entityHit == null) {
			return 0;
		}
		if (isSelectedEntityClose(movingobjectposition.entityHit, 2.0F) && isSelectedEntityOnLevel(movingobjectposition.entityHit, 0)) {
			if (movingobjectposition.entityHit.boundingBox.maxY - movingobjectposition.entityHit.boundingBox.minY <= 1.5D) {
				return 3;
			}
		}
		return 0;
	}

	public int canJumpOverGap() {
		int j = MathHelper.floor_double(player.boundingBox.minY - 0.1F);
		if (player.onGround && !player.worldObj.getBlock(MathHelper.floor_double(player.posX), j, MathHelper.floor_double(player.posZ)).getMaterial().isSolid()) {
            double d = -MathHelper.sin((player.rotationYaw / 180F) * (float) Math.PI);
            double d1 = MathHelper.cos((player.rotationYaw / 180F) * (float) Math.PI);
            if (player.worldObj.getBlock(MathHelper.floor_double(player.posX + d), j, MathHelper.floor_double(player.posZ + d1)).getMaterial().isSolid())
                return 1;
            else if (player.worldObj.getBlock(MathHelper.floor_double(player.posX + 2 * d), j, MathHelper.floor_double(player.posZ + 2 * d1)).getMaterial().isSolid())
                return 2;
        }
		return 0;
	}

	public EntityLiving canLandOnMob(List list) {
		for (Object ent : list) {
			if (ent instanceof EntityLiving && isSelectedEntityClose((EntityLiving)ent, 3.0F, 0D, player.motionY, 0D)) {
                return (EntityLiving) ent;
            }
		}
		return null;
	}

	public boolean canWallrun() {
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(true);
		if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			if (isSelectedBlockClose(movingobjectposition, 2.0F)) {
				Material m = getSelectedBlockMaterial(movingobjectposition);
				Material m1 = getSelectedBlockMaterial(movingobjectposition, 0, 1, 0);
				if (isSelectedBlockOnLevel(movingobjectposition, 1)) {
					m = getSelectedBlockMaterial(movingobjectposition);
					m1 = getSelectedBlockMaterial(movingobjectposition, 0, -1, 0);
				}
				if (!m1.isSolid()) {
					if (situation.canPushUp() != 0) {
						return m.isSolid() && !EntitiesClassAccess.isJumping(player);
					}
				}
				return m.isSolid() && m1.isSolid() && !EntitiesClassAccess.isJumping(player);
			}
		}
		return false;
	}

	public int getLookDirection() {
		return (MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 0.5D) & 3);
	}

	public Block getSelectedBlock(MovingObjectPosition movingobjectposition) {
		return getSelectedBlock(movingobjectposition, 0, 0, 0);
	}

	public Block getSelectedBlock(MovingObjectPosition movingobjectposition, int addX, int addY, int addZ) {
		return player.worldObj.getBlock(movingobjectposition.blockX + addX, movingobjectposition.blockY + addY, movingobjectposition.blockZ + addZ);
	}

	public Material getSelectedBlockMaterial(MovingObjectPosition movingobjectposition) {
		return getSelectedBlockMaterial(movingobjectposition, 0, 0, 0);
	}

	public Material getSelectedBlockMaterial(MovingObjectPosition movingobjectposition, int addX, int addY, int addZ) {
		return getSelectedBlock(movingobjectposition, addX, addY, addZ).getMaterial();
	}

	public void handleThings() {
		handleFreerunning();
		handleMoves();
	}

	public boolean hasBlockInFront() {
		double d = -MathHelper.sin((player.rotationYaw / 180F) * (float) Math.PI);
		double d1 = MathHelper.cos((player.rotationYaw / 180F) * (float) Math.PI);
		int i = MathHelper.floor_double(player.posX + d);
		int j = MathHelper.floor_double(player.boundingBox.minY);
		int k = MathHelper.floor_double(player.posZ + d1);
		return player.worldObj.getBlock(i, j, k).getMaterial().isSolid();
	}

	public boolean isBlockAboveAir(int l, boolean blockAboveBlockIsSolid, boolean climbing) {
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(true);
		if (movingobjectposition == null || movingobjectposition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
			return false;
		}
		int i = movingobjectposition.blockX;
		int j = movingobjectposition.blockY + 1;
		int k = movingobjectposition.blockZ;
		Material m = player.worldObj.getBlock(i, j, k).getMaterial();
		Material m1 = player.worldObj.getBlock(i, j + 1, k).getMaterial();
		Material m2 = player.worldObj.getBlock(i, j + 2, k).getMaterial();
		if (l == 1) {
			if (blockAboveBlockIsSolid) {
				return !m.isSolid() && m1.isSolid();
			} else {
				return !m.isSolid();
			}
		} else if (l == 2) {
			if (blockAboveBlockIsSolid) {
				return !m.isSolid() && !m1.isSolid() && m2.isSolid();
			} else {
				return !m.isSolid() && !m1.isSolid();
			}
		} else if (l == 3) {
			return !m.isSolid() && !m1.isSolid() && !m2.isSolid();
		} else {
			return !m.isSolid();
		}
	}

	public boolean isHangingStill() {
		return isClimbing && move == null;
	}

	public boolean isMoving() {
		return move != null;
	}

	public boolean isMovingBackwards() {
		return player.moveForward < 0;
	}

	public boolean isMovingForwards() {
		return player.moveForward > 0;
	}

	public boolean isMovingLeft() {
		return player.moveStrafing > 0;
	}

	public boolean isMovingRight() {
		return player.moveStrafing < 0;
	}

	public boolean isOnCertainBlock(Block blockID) {
		int i = MathHelper.floor_double(player.posX);
		int j = MathHelper.floor_double(player.boundingBox.minY - 0.1F + player.motionY);
		int k = MathHelper.floor_double(player.posZ);
		return player.worldObj.getBlock(i, j, k) == blockID;
	}

	public boolean isRolling() {
		return roll.animProgress < 1F;
	}

	public boolean isSelectedBlockClose(MovingObjectPosition movingobjectposition, float f) {
		return isSelectedBlockClose(movingobjectposition, f, 0D, 0D, 0D);
	}

	public boolean isSelectedBlockClose(MovingObjectPosition movingobjectposition, float f, double addX, double addY, double addZ) {
		double d1 = Math.sqrt(Math.pow((movingobjectposition.blockX + 0.5D) + addX - player.posX, 2));
		double d2 = Math.sqrt(Math.pow((movingobjectposition.blockY + 0.5D) + addY - player.posY, 2));
		double d3 = Math.sqrt(Math.pow((movingobjectposition.blockZ + 0.5D) + addZ - player.posZ, 2));
		double dXYZ = Math.sqrt((d1 * d1) + (d2 * d2) + (d3 * d3));
		return dXYZ <= f;
	}

	public boolean isSelectedBlockOnLevel(MovingObjectPosition movingobjectposition, int i) {
		int j = MathHelper.floor_double(player.boundingBox.minY) + i;
		return j == movingobjectposition.blockY;
	}

	public boolean isSelectedEntityClose(Entity ent, float f) {
		return isSelectedEntityClose(ent, f, 0D, 0D, 0D);
	}

	public boolean isSelectedEntityClose(Entity ent, float f, double addX, double addY, double addZ) {
		double d1 = Math.sqrt(Math.pow((ent.posX + 0.5D) + addX - player.posX, 2));
		double d2 = Math.sqrt(Math.pow((ent.posY + 0.5D) + addY - player.posY, 2));
		double d3 = Math.sqrt(Math.pow((ent.posZ + 0.5D) + addZ - player.posZ, 2));
		double dXYZ = Math.sqrt((d1 * d1) + (d2 * d2) + (d3 * d3));
		return dXYZ <= f;
	}

	public boolean isSelectedEntityOnLevel(Entity ent, int i) {
		int j = MathHelper.floor_double(player.boundingBox.minY) + i;
		return j == MathHelper.floor_double(ent.boundingBox.minY);
	}

	public boolean isTooHungry() {
		return player.getFoodStats().getFoodLevel() <= 6;
	}

	public boolean isWallrunning() {
		return move instanceof MoveWallrun;
	}

	public MoveRoll getRoll() {
		return roll;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public void stopMove() {
		if (isMoving()) {
			move.moveDone();
		}
	}

	public void tryGrabLedge() {
		if (!player.onGround && situation.canHangStill() && !isWallrunning()) {
			isClimbing = true;
			Vec3 vec3d = situation.getHangPositions();
			player.motionX = vec3d.xCoord - player.posX;
			player.motionY = vec3d.yCoord - player.posY;
			player.motionZ = vec3d.zCoord - player.posZ;
		}
	}

	protected MovingObjectPosition getMovingObjectPositionFromPlayer(boolean par3) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f + (player.worldObj.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight());
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		if (player instanceof EntityPlayerMP) {
			d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
		}
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return player.worldObj.func_147447_a(vec3, vec31, par3, !par3, false);
	}

	private void handleFreerunning() {
		if (isTooHungry()) {
			stopMove();
			return;
		}
		//Not in water
		if (!player.isInWater() && !player.handleLavaMovement()) {
			if (player.onGround || player.isSneaking() || player.isOnLadder()) {
				isClimbing = false;
				stopMove();
			}
			//Climbing
			if (isClimbing) {
				player.addExhaustion(0.001F);
				player.fallDistance = 0.0F;
				player.motionX = player.motionY = player.motionZ = 0D;
				if (!situation.canHangStill() && !(move instanceof MoveAroundEdge)) {
					isClimbing = false;
				} else if (isHangingStill()) {
					Vec3 vec3d = situation.getHangPositions();
					player.motionX = vec3d.xCoord - player.posX;
					player.motionY = vec3d.yCoord - player.posY;
					player.motionZ = vec3d.zCoord - player.posZ;
					int lookdirection = situation.lookDirection;
                    List<Move> moves = getMovesHangingStill();
                    for(Move mvoe:moves) {
                        if (mvoe!=null && mvoe.canPerform(situation)) {
                            mvoe.performMove(lookdirection);
                            break;
                        }
                    }
				}
				if (freeRunning && EntitiesClassAccess.isJumping(player) && isHangingStill()) {
					if (isMovingForwards() && !isWallrunning()) {
						ejectUp.performMove(situation.lookDirection);
					} else {
						getEject().performMove(situation.lookDirection);
					}
				}
				return;
			}
			//Not climbing
			if (freeRunning) {
				tryGrabLedge();
				//Wallkick
				if (!player.onGround) {
					if (FRCommonProxy.properties.enableWallKick && isWallrunning() && EntitiesClassAccess.isJumping(player) && move.getAnimationProgress() > 0.3F) {
						stopMove();
						getEject().performMove(situation.lookDirection, 0.8F);
					}
					return;
				}
				if ((isMovingForwards() || player.isCollidedHorizontally) && !isRolling()) {
					int i = canJumpOverGap();
					int j = canHopOver();
					if (!EntitiesClassAccess.isJumping(player)) {
						if (i == 1) {
							player.addVelocity(0D, 0.35D, 0D);
                            EntitiesClassAccess.setJumping(player);
							player.addExhaustion(0.1F);
						} else if (i == 2) {
                            player.jump();
						}
					}
					if (j == 1 && !EntitiesClassAccess.isJumping(player)) {
                        player.jump();
					} else if (j > 1 || canWallrun()) {
						wallrun.performMove(getLookDirection());
					}
				}
			}
			if (isWallrunning()) {
				if (player.isCollidedVertically && !player.onGround) {
					move.moveDone();
				}
				tryGrabLedge();
			}
			return;
		}
	}

    private List<Move> getMovesHangingStill() {
        if(isMovingForwards()){
            return Arrays.asList(upBehind, climbUp, pushUp);
        }else if(isMovingBackwards()){
            return Arrays.asList((Move)climbDown);
        }else if(isMovingLeft()){
            return Arrays.asList((Move)climbLeft, climbAroundLeft);
        }else if(isMovingRight()){
            return Arrays.asList((Move)climbRight, climbAroundRight);
        }
        return new ArrayList<Move>(0);
    }

    private MoveEject getEject(){
        if (isMovingLeft()) {
            return ejectLeft;
        } else if (isMovingRight()) {
            return ejectRight;
        } else {
            return ejectBack;
        }
    }

    private void handleMoves() {
        if (!paused) {
            if (move != null) {
                move.updateMove();
            }
        }
        if (pauseTimer > 0) {
            pauseTimer--;
        } else {
            paused = false;
        }
	}

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound tags = new NBTTagCompound();
        compound.setTag("FreeRun", tags);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound tags = compound.getCompoundTag("FreeRun");
    }

    @Override
    public void init(Entity entity, World world) {

    }
}

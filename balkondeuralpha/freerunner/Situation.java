package balkondeuralpha.freerunner;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Situation {
	private World worldObj;
	public float blockHeight = 1.0F;
	public int lookDirection;
	private int posX, posY, posZ, nextPosX, nextPosY, nextPosZ;

	private Situation(double x, double y, double z, int lookdirection, World world) {
		nextPosX = posX = MathHelper.floor_double(x);
		nextPosY = posY = (int) Math.ceil(y);
		nextPosZ = posZ = MathHelper.floor_double(z);
		lookDirection = lookdirection;
		worldObj = world;
	}

	public boolean canClimbAroundEdgeLeft() {
		boolean flag = false;
		int i = lookDirection;
		lookDirection = (lookDirection + 1) % 4;
		if (i == FreerunPlayer.LOOK_WEST) {
			nextPosX = posX + 1;
			nextPosZ = posZ + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		} else if (i == FreerunPlayer.LOOK_NORTH) {
			nextPosZ = posZ + 1;
			nextPosX = posX - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		} else if (i == FreerunPlayer.LOOK_EAST) {
			nextPosX = posX - 1;
			nextPosZ = posZ - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		} else if (i == FreerunPlayer.LOOK_SOUTH) {
			nextPosZ = posZ - 1;
			nextPosX = posX + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		}
		lookDirection = i;
		return flag;
	}

	public boolean canClimbAroundEdgeRight() {
		boolean flag = false;
		int i = lookDirection;
		lookDirection = (lookDirection - 1) % 4;
		if (i == FreerunPlayer.LOOK_WEST) {
			nextPosX = posX + 1;
			nextPosZ = posZ + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		} else if (i == FreerunPlayer.LOOK_NORTH) {
			nextPosZ = posZ + 1;
			nextPosX = posX - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		} else if (i == FreerunPlayer.LOOK_EAST) {
			nextPosX = posX - 1;
			nextPosZ = posZ - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		} else if (i == FreerunPlayer.LOOK_SOUTH) {
			nextPosZ = posZ - 1;
			nextPosX = posX + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		}
		lookDirection = i;
		return flag;
	}

	public boolean canClimbDown() {
		return hasEdgeDown();
	}

	public boolean canClimbLeft() {
		return hasEdgeLeft();
	}

	public boolean canClimbRight() {
		return hasEdgeRight();
	}

	public boolean canClimbUp() {
		return hasEdgeUp();
	}

	public boolean canHangStill() {
		return hasEdgeOnLocation(posX, posY, posZ);
	}

	public boolean canJumpUpBehind() {
		return hasEdgeUpBehind();
	}

	public float canPushUp() {
		if (canClimbUp() || !canHangStill()) {
			return 0F;
		}
		int x = posX;
		int y = posY - 1;
		int z = posZ;
		boolean flag = hasAirAbove(x, y, z, 2);
		if (lookDirection == FreerunPlayer.LOOK_WEST) {
			z++;
		} else if (lookDirection == FreerunPlayer.LOOK_NORTH) {
			x--;
		} else if (lookDirection == FreerunPlayer.LOOK_EAST) {
			z--;
		} else if (lookDirection == FreerunPlayer.LOOK_SOUTH) {
			x++;
		}
		Material material = worldObj.getBlockMaterial(x, y, z);
		//if (material.isSolid())
		{
			if (hasAirAbove(x, y, z, 2) && flag) {
				float f = y;
				Block block = Block.blocksList[worldObj.getBlockId(x, y, z)];
				if (block != null) {
					AxisAlignedBB bb = block.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
					if (bb != null) {
						f += bb.maxY - y - 1F;
					}
				}
				return f;
			}
		}
		return 0F;
	}

	public Vec3 getHangPositions() {
		double x = posX + 0.5D;
		double y = posY - 0.1D - (1.0F - blockHeight);
		double z = posZ + 0.5D;
		return Vec3.createVectorHelper(x, y, z);
	}

	private boolean hasAirAbove(int x, int y, int z, int i) {
		if (i >= 1) {
			Material material = worldObj.getBlockMaterial(x, y + 1, z);
			if (i >= 2) {
				Material material1 = worldObj.getBlockMaterial(x, y + 2, z);
				return !material.isSolid() && !material1.isSolid();
			}
			return !material.isSolid();
		}
		return false;
	}

	private boolean hasEdgeDown() {
		nextPosY = posY - 1;
		return hasEdgeOnLocation(posX, nextPosY, posZ);
	}

	private boolean hasEdgeLeft() {
		boolean b = false;
		if (lookDirection == FreerunPlayer.LOOK_WEST) {
			nextPosX = posX + 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FreerunPlayer.LOOK_NORTH) {
			nextPosZ = posZ + 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		} else if (lookDirection == FreerunPlayer.LOOK_EAST) {
			nextPosX = posX - 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FreerunPlayer.LOOK_SOUTH) {
			nextPosZ = posZ - 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		}
		return b;
	}

	private boolean hasEdgeOnLocation(int x, int y, int z) {
		int b = worldObj.getBlockId(x, y - 1, z);
		int b1 = worldObj.getBlockId(x, y, z);
		int md = worldObj.getBlockMetadata(x, y - 1, z);
		int md1 = Situation.getMetaData(lookDirection);
		if (b1 == Block.vine.blockID || md == 0 || md == md1) {
			if (FreerunPlayer.climbableInside.contains(b)) {
				return true;
			} else if (worldObj.getBlockMaterial(x, y, z).isSolid() || worldObj.getBlockMaterial(x, y - 1, z).isSolid()) {
				return false;
			}
		}
		if (lookDirection == FreerunPlayer.LOOK_WEST) {
			z++;
		} else if (lookDirection == FreerunPlayer.LOOK_NORTH) {
			x--;
		} else if (lookDirection == FreerunPlayer.LOOK_EAST) {
			z--;
		} else if (lookDirection == FreerunPlayer.LOOK_SOUTH) {
			x++;
		}
		b = worldObj.getBlockId(x, y - 1, z);
		b1 = worldObj.getBlockId(x, y, z);
		if (FreerunPlayer.climbableInside.contains(b)) {
			return false;
		}
		if (FreerunPlayer.climbableBlocks.contains(b)) {
			blockHeight = (float) (Block.blocksList[b].getBlockBoundsMaxY());
			return true;
		}
		if (worldObj.getBlockMaterial(x, y - 1, z).isSolid()) {
			if (b != b1) {
				blockHeight = (float) (Block.blocksList[b].getBlockBoundsMaxY());
				return !(((b == Block.stone.blockID || b == 14 || b == 15 || b == 16 || b == 21 || b == 56 || b == 73 || b == 74) && (b1 == Block.stone.blockID || b1 == 14 || b1 == 15 || b1 == 16
						|| b1 == 21 || b1 == 56 || b1 == 73 || b1 == 74))
						|| ((b == Block.dirt.blockID || b == Block.grass.blockID) && (b1 == Block.dirt.blockID || b1 == Block.grass.blockID))
						|| ((b == Block.cobblestone.blockID || b == Block.cobblestoneMossy.blockID || b == Block.stairsCobblestone.blockID) && (b1 == Block.cobblestone.blockID
								|| b1 == Block.cobblestoneMossy.blockID || b1 == Block.stairsCobblestone.blockID)) || ((b == Block.planks.blockID || b == Block.stairsBrick.blockID) && (b1 == Block.planks.blockID || b1 == Block.stairsBrick.blockID)));
			}
		}
		blockHeight = 1.0F;
		return false;
	}

	private boolean hasEdgeRight() {
		boolean b = false;
		if (lookDirection == FreerunPlayer.LOOK_WEST) {
			nextPosX = posX - 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FreerunPlayer.LOOK_NORTH) {
			nextPosZ = posZ - 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		} else if (lookDirection == FreerunPlayer.LOOK_EAST) {
			nextPosX = posX + 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FreerunPlayer.LOOK_SOUTH) {
			nextPosZ = posZ + 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		}
		return b;
	}

	private boolean hasEdgeUp() {
		nextPosY = posY + 1;
		return hasEdgeOnLocation(posX, nextPosY, posZ);
	}

	private boolean hasEdgeUpBehind() {
		boolean b = false;
		nextPosY = posY + 2;
		if (lookDirection == FreerunPlayer.LOOK_WEST) {
			nextPosZ = posZ - 1;
			b = hasEdgeOnLocation(posX, nextPosY, nextPosZ);
		} else if (lookDirection == FreerunPlayer.LOOK_NORTH) {
			nextPosX = posX + 1;
			b = hasEdgeOnLocation(nextPosX, nextPosY, posZ);
		} else if (lookDirection == FreerunPlayer.LOOK_EAST) {
			nextPosZ = posZ + 1;
			b = hasEdgeOnLocation(posX, nextPosY, nextPosZ);
		} else if (lookDirection == FreerunPlayer.LOOK_SOUTH) {
			nextPosX = posX - 1;
			b = hasEdgeOnLocation(nextPosX, nextPosY, posZ);
		}
		return b;
	}

	public static int getMetaData(int lookdirection) {
		if (lookdirection == FreerunPlayer.LOOK_WEST) {
			return 2;
		} else if (lookdirection == FreerunPlayer.LOOK_NORTH) {
			return 5;
		} else if (lookdirection == FreerunPlayer.LOOK_EAST) {
			return 3;
		} else if (lookdirection == FreerunPlayer.LOOK_SOUTH) {
			return 4;
		}
		return 0;
	}

	public static Situation getSituation(EntityPlayer player, int lookdirection, World world) {
		return new Situation(player.posX, player.posY, player.posZ, lookdirection, world);
	}
}

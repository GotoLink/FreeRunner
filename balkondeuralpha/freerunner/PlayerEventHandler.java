package balkondeuralpha.freerunner;

import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PlayerEventHandler {
	private static final UUID freerunSpeed = UUID.fromString("7f4a5bc2-c657-457b-9ba1-7960db31e814");

	@SubscribeEvent
	public void afterDamageEntity(LivingHurtEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
            FreerunPlayer.get(((EntityPlayer) event.entityLiving)).isClimbing = false;
		}
	}

	@SubscribeEvent
	public void beforeFall(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			FreerunPlayer freeRunner = FreerunPlayer.get(player);
			if (freeRunner.freeRunning) {
				int i = MathHelper.floor_double(player.posX);
				int j = MathHelper.floor_double(player.boundingBox.minY);
				int k = MathHelper.floor_double(player.posZ);
				Block b = player.worldObj.getBlock(i, j, k);
				j = MathHelper.floor_double(player.boundingBox.minY + player.motionY);
				Block b1 = player.worldObj.getBlock(i, j, k);
				Material material = b1.getMaterial();
				List list = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(1.0, 2.0, 1.0));
				float f = event.distance;
				if (f > 3.0F && list != null && !list.isEmpty()) {
					EntityLiving entityliving = freeRunner.canLandOnMob(list);
					if (entityliving != null) {
						entityliving.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.0F);
						entityliving.setAttackTarget(player);
                        if(event.entity.worldObj.isRemote)
						    entityliving.performHurtAnimation();
						player.motionY *= 0.1F;
						event.distance = 0F;
					}
				} else if (b != b1 && material.isSolid() && material != Material.water && material != Material.lava) {
					event.distance = roll(player, freeRunner, f);
				}
			}
		}
	}

    private float roll(EntityPlayer player, FreerunPlayer freeRunner, float f) {
        if (!freeRunner.freeRunning || f < 3F) {
            return f;
        }
        float maxFall = 6F;
        if (f < maxFall) {
            f /= 2F;
            return f;
        }
        int i = MathHelper.floor_double(player.posX);
        int j = MathHelper.floor_double(player.boundingBox.minY - 1.1F + player.motionY);
        int k = MathHelper.floor_double(player.posZ);
        Block b = player.worldObj.getBlock(i, j, k);
        if (b == Blocks.fence || b == Blocks.nether_brick_fence || freeRunner.isOnCertainBlock(Blocks.leaves) || freeRunner.isOnCertainBlock(FRCommonProxy.barWood) || player.isInWater()) {
            f /= 2F;
            return f;
        }
        if (!freeRunner.isMovingForwards()) {
            f *= 0.8F;
            return f;
        }
        double d = -MathHelper.sin((player.rotationYaw / 180F) * (float) Math.PI);
        double d1 = MathHelper.cos((player.rotationYaw / 180F) * (float) Math.PI);
        freeRunner.getRoll().start();
        player.setVelocity(d, 0, d1);
        player.addExhaustion(0.3F);
        f /= 2F;
        return f;
    }

    @SubscribeEvent
	public void beforeOnUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			FreerunPlayer freeRunner = FreerunPlayer.get(player);
			freeRunner.situation = Situation.getSituation(player, freeRunner.getLookDirection());
			freeRunner.horizontalSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
			freeRunner.handleThings();
            IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            AttributeModifier mod = new AttributeModifier(freerunSpeed, "FreeRunSpeed", getSpeedModifier(player, freeRunner) - 1, 2);
            if (mod.getAmount() > -1) {
                if (atinst.getModifier(freerunSpeed) == null) {
                    atinst.applyModifier(mod);
                }
            } else if (atinst.getModifier(freerunSpeed) != null) {
                atinst.removeModifier(mod);
            }
		}
	}

	public float getSpeedModifier(EntityPlayer player, FreerunPlayer freeRunner) {
		if (player.isSprinting() || freeRunner.isTooHungry()) {
			return -1F;
		}
		if (freeRunner.freeRunning) {
			return FRCommonProxy.properties.speedMultiplier;
		}
		if (!player.isInWater() && !player.handleLavaMovement() && player.onGround) {
			if (!freeRunner.isClimbing && FRCommonProxy.barWood != null && freeRunner.isOnCertainBlock(FRCommonProxy.barWood)) {
				return 0.5F;
			}
		}
		return -1F;
	}

    @SubscribeEvent
    public void onPlayerConstruction(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            IExtendedEntityProperties runner = FreerunPlayer.get((EntityPlayer) event.entity);
            if (runner == null) {
                runner = new FreerunPlayer((EntityPlayer) event.entity);
                event.entity.registerExtendedProperties("FreeRun", runner);
            }
        }
    }
}

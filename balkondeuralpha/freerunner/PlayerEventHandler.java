package balkondeuralpha.freerunner;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.IPlayerTracker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PlayerEventHandler {
	private static final UUID freerunSpeed = UUID.randomUUID();
	public static HashMap<String, FreerunPlayer> freeRunners = new HashMap<String, FreerunPlayer>();

	@SubscribeEvent
	public void afterDamageEntity(LivingHurtEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			freeRunners.get(((EntityPlayer) event.entityLiving).username).isClimbing = false;
		}
	}

	@SubscribeEvent
	public void beforeFall(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			FreerunPlayer freeRunner = freeRunners.get(player.username);
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
						entityliving.performHurtAnimation();
						player.motionY *= 0.1F;
						event.distance = 0F;
					}
				} else if (b != b1 && material.isSolid() && material != Material.water && material != Material.lava) {
					event.distance = freeRunner.roll(f);
				}
			}
		}
	}

	@SubscribeEvent
	public void beforeOnUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			FreerunPlayer freeRunner = freeRunners.get(player.username);
			freeRunner.situation = Situation.getSituation(player, freeRunner.getLookDirection(), player.worldObj);
			freeRunner.horizontalSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
			freeRunner.prevRollAnimation = freeRunner.rollAnimation;
			if (freeRunner.isRolling()) {
				freeRunner.rollAnimation += 0.05F;
			}
			/*
			 * if (player.prevPosX != 0D && player.prevPosY != 0D &&
			 * player.prevPosZ != 0D) { handleStats(player.posX -
			 * player.prevPosX, player.posY - player.prevPosY, player.posZ -
			 * player.prevPosZ); }
			 */
			freeRunner.handleThings();
			AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
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
	public void onPlayerLogin(EntityPlayer player) {
		freeRunners.put(player.username, new FreerunPlayer(player));
	}

	@SubscribeEvent
	public void onPlayerLogout(EntityPlayer player) {
		freeRunners.remove(player.username);
	}
}

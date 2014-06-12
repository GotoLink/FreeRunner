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
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PlayerEventHandler {
	private static final UUID freerunSpeed = UUID.randomUUID();

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

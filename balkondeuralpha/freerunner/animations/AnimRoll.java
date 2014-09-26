package balkondeuralpha.freerunner.animations;

import balkondeuralpha.freerunner.FreerunPlayer;
import balkondeuralpha.freerunner.animations.Animation;
import balkondeuralpha.freerunner.moves.Move;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntitiesClassAccess;
import net.minecraft.entity.player.EntityPlayer;

public class AnimRoll implements Animation {
    private final EntityPlayer player;
    private float startRollingYaw, startRollingPitch;
    public AnimRoll(EntityPlayer player){
        this.player = player;
    }

    @Override
    public void set(Move roll){
        startRollingYaw = roll.getPlayer().rotationYaw;
        startRollingPitch = roll.getPlayer().rotationPitch;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doAnimate(ModelBiped model, float f, float renderTick) {
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && FreerunPlayer.get(player).isRolling()) {
            float yaw = (startRollingYaw + 360F * f);
            float pitch = (startRollingPitch + 360F * f);
            while (yaw >= 360F) {
                yaw -= 360F;
            }
            if (pitch >= 180F) {
                pitch -= 270F;
            }
            EntitiesClassAccess.setRotation(player, yaw, pitch);
        }
    }
}

package net.minecraft.entity;

public class EntitiesClassAccess {

    public static boolean isJumping(EntityLivingBase living){
        return living.isJumping;
    }

    public static void setJumping(EntityLivingBase living){
        living.isJumping = true;
    }

    public static void setRotation(Entity ent, float yaw, float pitch){
        ent.setRotation(yaw, pitch);
    }
}

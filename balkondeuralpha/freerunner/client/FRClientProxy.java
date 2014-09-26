package balkondeuralpha.freerunner.client;

import balkondeuralpha.freerunner.FRCommonProxy;
import balkondeuralpha.freerunner.animations.AnimRoll;
import balkondeuralpha.freerunner.animations.AnimWallrun;
import balkondeuralpha.freerunner.animations.Animation;
import balkondeuralpha.freerunner.blocks.BlockWoodBar;
import balkondeuralpha.freerunner.moves.Move;
import balkondeuralpha.freerunner.moves.MoveRoll;
import balkondeuralpha.freerunner.moves.MoveWallrun;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FRClientProxy extends FRCommonProxy {
	@Override
	public void registerThings(FMLPreInitializationEvent event) {
		super.registerThings(event);
		FMLCommonHandler.instance().bus().register(new RunnerTickHandler());
        if(properties.enableBarWood) {
            BlockWoodBar.barWoodModel = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new CustomBlockRenderer());
        }
        if (properties.enableAnimations)
		    MinecraftForge.EVENT_BUS.register(new Animator());
	}

    @Override
    public EntityPlayer getPlayer(MessageContext context){
        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    @Override
    public void setAnimation(Move move){
        if(move instanceof MoveRoll)
            move.setAnimation(new AnimRoll(move.getPlayer()));
        else if(move instanceof MoveWallrun)
            move.setAnimation(new AnimWallrun());
    }

    @Override
    public void startAnimation(Move move){
        Animation anim = move.getAnimation();
        if(anim!=null)
            anim.set(move);
    }
}

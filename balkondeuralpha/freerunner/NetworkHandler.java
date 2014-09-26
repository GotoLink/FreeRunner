package balkondeuralpha.freerunner;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Olivier on 10/08/2014.
 */
public class NetworkHandler implements IMessageHandler<NetworkHandler.SetFreeRun, NetworkHandler.SetFreeRun> {
    private IMessage onPlayerMessage(PlayerMessage message, MessageContext ctx) {
        return message.handle(FreeRun.proxy.getPlayer(ctx), ctx.side);
    }

    @Override
    public SetFreeRun onMessage(SetFreeRun message, MessageContext ctx) {
        return message.handle(FreeRun.proxy.getPlayer(ctx), ctx.side);
    }

    public static abstract class PlayerMessage implements IMessage{
        public String user;
        public PlayerMessage(){}
        public PlayerMessage(String player){
            this.user = player;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.user = ByteBufUtils.readUTF8String(buf);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, user);
        }

        public abstract IMessage handle(EntityPlayer receiver, Side side);
    }

    public static class SetFreeRun implements IMessage{
        boolean set;
        public SetFreeRun(){}
        public SetFreeRun(boolean set){
            this.set = set;
        }
        @Override
        public void fromBytes(ByteBuf buf){
            this.set = buf.readBoolean();
        }
        @Override
        public void toBytes(ByteBuf buf){
            buf.writeBoolean(set);
        }

        public SetFreeRun handle(EntityPlayer player, Side side) {
            if(player!=null) {
                FreerunPlayer.get(player).freeRunning = set;
            }
            return null;
        }
    }
}

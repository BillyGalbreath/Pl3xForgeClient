package net.pl3x.forge.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.pl3x.forge.tileentity.TileEntityPlate;

public class PlateRequestUpdatePacket implements IMessage {
    private BlockPos pos;
    private int dimension;

    public PlateRequestUpdatePacket() {
    }

    public PlateRequestUpdatePacket(TileEntityPlate te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PlateRequestUpdatePacket(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PlateRequestUpdatePacket, IMessage> {
        @Override
        public IMessage onMessage(PlateRequestUpdatePacket packet, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(packet.dimension);
            TileEntityPlate te = (TileEntityPlate) world.getTileEntity(packet.pos);
            if (te != null) {
                return new PlateUpdatePacket(te);
            }
            return null;
        }
    }
}

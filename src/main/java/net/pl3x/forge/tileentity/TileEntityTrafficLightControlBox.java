package net.pl3x.forge.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.pl3x.forge.network.PacketHandler;
import net.pl3x.forge.network.TrafficLightControlBoxUpdatePacket;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityTrafficLightControlBox extends TileEntity implements ITickable {
    public int tick = 0;
    public IntersectionState intersectionState = IntersectionState.EW_GREEN_NS_RED;

    public TileEntityTrafficLightControlBox() {
        tick = ThreadLocalRandom.current().nextInt(IntersectionState.EW_GREEN_NS_RED.ticks);
    }

    @Override
    public void update() {
        // tick the logic
        tick++;
        if (tick > intersectionState.ticks) {
            tick = 0;
            int next = intersectionState.ordinal() + 1;
            if (next >= IntersectionState.values().length) {
                next = 0;
            }
            intersectionState = IntersectionState.values()[next];
            updateClients();
        }
    }

    public void updateClients() {
        if (!world.isRemote) {
            PacketHandler.INSTANCE.sendToAllAround(new TrafficLightControlBoxUpdatePacket(pos, tick, intersectionState),
                    new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 128));
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    public enum IntersectionState {
        EW_GREEN_NS_RED(100),
        EW_YELLOW_NS_RED(20),
        EW_RED_NS_GREEN(100),
        EW_RED_NS_YELLOW(20);

        public int ticks;

        IntersectionState(int ticks) {
            this.ticks = ticks;
        }
    }
}
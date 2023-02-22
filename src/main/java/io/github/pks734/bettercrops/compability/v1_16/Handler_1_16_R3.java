package io.github.pks734.bettercrops.compability.v1_16;

import io.github.pks734.bettercrops.compability.VersionHandler;
import net.minecraft.server.v1_16_R3.BlockCrops;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.IBlockAccess;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.block.Block;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Handler_1_16_R3 implements VersionHandler {
    @Override
    public float getGrowthChance(Block block) {
        float points = 0.0F;
        BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
        WorldServer worldServer = ((CraftWorld) block.getWorld()).getHandle();
        BlockCrops blockCrops = (BlockCrops) worldServer.getType(position).getBlock();

        if (worldServer.getLightLevel(position, 0) < 9) {
            return 0.0F;
        }


        try {
            Method method = blockCrops.getClass().getDeclaredMethod("a", net.minecraft.server.v1_16_R3.Block.class, net.minecraft.server.v1_16_R3.IBlockAccess.class, net.minecraft.server.v1_16_R3.BlockPosition.class);
            method.setAccessible(true);
            points = (float) method.invoke(null, blockCrops, (IBlockAccess)worldServer, position);
            System.out.println(points); //TODO: REMOVE
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return (1.0F / ((int)(25.0F / points) + 1));
    }

    @Override
    public boolean canReedGrow(Block block) {
        return false;
    }
}

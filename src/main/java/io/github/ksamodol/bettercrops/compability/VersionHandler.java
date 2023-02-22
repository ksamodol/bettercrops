package io.github.ksamodol.bettercrops.compability;

import org.bukkit.block.Block;

public interface VersionHandler {
    float getGrowthChance(Block block);
    boolean canReedGrow(Block block);
}

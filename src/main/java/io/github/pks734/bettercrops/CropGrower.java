package io.github.pks734.bettercrops;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.Optional;

public class CropGrower implements Runnable{
    private BetterCrops plugin;
    private Long lastUnloadedTime;
    private ArrayList<CropInfo> list;
    private Chunk chunk;


    public CropGrower(BetterCrops plugin, ChunkDefinition chunkDefinition, Long lastUnloadedTime, ArrayList<CropInfo> list) {
        this.plugin = plugin;
        this.lastUnloadedTime = lastUnloadedTime;
        this.list = list;
        this.chunk = Bukkit.getWorld(chunkDefinition.getWorldName()).getChunkAt(chunkDefinition.getX(), chunkDefinition.getZ());
    }

    @Override
    public void run() {
        if(chunk.getWorld().getFullTime() - lastUnloadedTime < 0){ //TODO: WHAT TO DO?
            plugin.getLogger().warning("Time difference for chunk X:" + chunk.getX() + " Z:" + chunk.getZ() + " is negative! Skipping!");
            return;
        }
        for(CropInfo i : list){
            Point3D point = i.getLocation();
            Block block = chunk.getBlock(point.getX(), point.getY(), point.getZ());

            updateBlock(block);

        }

    }


    private int calculateNewAge(Block block){
        float growthChance = calculateGrowthChance(block);
        Ageable crop = (Ageable)block.getBlockData();
        int maximumAge = crop.getMaximumAge();
        int currentAge = crop.getAge();

        if(Math.abs(growthChance - 0) < 0.00001){ //checks if chance is 0
            return currentAge;
        }
        Long timeDifference = chunk.getWorld().getFullTime() - lastUnloadedTime;
        //after this many ticks the chance of even the worst crop not being fully grown is negligible
        //safe to return maximumAge and avoid potentially risky long conversions
        if (timeDifference > 2000000){
            return maximumAge;
        }

        int secondsPassed = (timeDifference.intValue() / 20);
        if (secondsPassed == 0){
            return currentAge;
        }
        int randomTickSpeed = Optional.ofNullable(chunk.getWorld().getGameRuleValue(GameRule.RANDOM_TICK_SPEED)).orElse(3);
        double probabilityOfTickBeingRandomTick = (double)randomTickSpeed/4096;
        double probabilityOfSecondHavingRandomTick = 1 - Math.pow(1-probabilityOfTickBeingRandomTick,20);

        int randomTicks = getBinomialDistributionSample(secondsPassed, probabilityOfSecondHavingRandomTick);

        if(randomTicks == 0){
            return currentAge;
        }
        if(crop.getMaterial() == Material.BEETROOTS){ //TODO: binomial?
            growthChance *= (2.0F/3.0F); //beetroot have 1/3 chance of a tick not advancing growth
        }

        int successfulGrowths = getBinomialDistributionSample(randomTicks, growthChance);

        return Math.min(currentAge + successfulGrowths, maximumAge); //to make sure newAge < maximumAge
    }

    private float calculateGrowthChance(Block block){
        return plugin.getVersionHandler().getGrowthChance(block);
    }

    public static int getBinomialDistributionSample(int trials, double probability){
        return new BinomialDistribution(trials, probability).sample();
    }


    private void updateBlock(Block block){
        BlockData blockData = block.getBlockData();
        int newAge = calculateNewAge(block);
        if(((Ageable)blockData).getAge() < newAge){
            BlockData newBlockData = blockData.getMaterial().createBlockData();
            ((Ageable)newBlockData).setAge(newAge);
            block.setBlockData(newBlockData);
            plugin.getLogger().info("Setting block at x:" + block.getX() + ", y:" + block.getY() + ", z:" + block.getZ() + " to age:" + newAge + ".");
        }else{
            plugin.getLogger().info("Block at x:" + block.getX() + ", y:" + block.getY() + ", z:" + block.getZ() + " did not grow while the chunk was unloaded");
        }

    }


}

package io.github.pks734.bettercrops;

import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;


public class AsyncCropExtractor implements Runnable {

    private BetterCrops plugin;
    private ChunkSnapshot chunkSnapshot;
    private ArrayList<CropInfo> list;
    private Long lastUnloadedTime;

    public AsyncCropExtractor(BetterCrops plugin, ChunkSnapshot chunkSnapshot, Long lastUnloadedTime) {
        this.plugin = plugin;
        this.chunkSnapshot = chunkSnapshot;
        this.list = new ArrayList<>();
        this.lastUnloadedTime = lastUnloadedTime;
    }

    @Override
    public void run() {
        int maxHeight = 256;

        for(int x = 0; x < 16; x++){
            for(int y = 0; y < maxHeight; y++){
                for(int z = 0; z < 16; z++){
                    BlockData blockData = chunkSnapshot.getBlockData(x,y,z);
                    if(isCrop(blockData)){
                        CropInfo cropInfo = new CropInfo(new Point3D(x, y, z), blockData.getMaterial(), ((Ageable)blockData).getAge());
                        list.add(cropInfo);
                    }
                }
            }
        }
        if(!list.isEmpty()){
            ChunkDefinition chunkDefinition = new ChunkDefinition(chunkSnapshot.getX(), chunkSnapshot.getZ(), chunkSnapshot.getWorldName());
            plugin.getLogger().info("Found " + list.size() + " crops at chunk X:" + chunkSnapshot.getX() + ", Z:" + chunkDefinition.getZ());
            Bukkit.getScheduler().runTask(plugin, new CropGrower(plugin, chunkDefinition, lastUnloadedTime, list));
        }

    }

    private boolean isCrop(BlockData blockData){
        Material material = blockData.getMaterial();
        if(material == Material.WHEAT || material == Material.POTATOES || material == Material.CARROTS || material == Material.BEETROOTS){
            return true;
        }
        return false;
    }

}

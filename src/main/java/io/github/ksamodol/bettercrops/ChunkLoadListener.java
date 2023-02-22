package io.github.ksamodol.bettercrops;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class ChunkLoadListener implements Listener {
    private final BetterCrops plugin;

    public ChunkLoadListener(BetterCrops plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        if(event.isNewChunk())
            return;

        PersistentDataContainer pdc = event.getChunk().getPersistentDataContainer();
        if(!pdc.has(key("lastUnloadedTime"), PersistentDataType.LONG)) {
            return;
        }

        ChunkSnapshot chunkSnapshot = event.getChunk().getChunkSnapshot();

        Long lastUnloadedTime = pdc.get(key("lastUnloadedTime"), PersistentDataType.LONG);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, new AsyncCropExtractor(plugin, chunkSnapshot, lastUnloadedTime));

        //Bukkit.broadcastMessage("Wheat in chunk at" + chunk.getX() + ", "+ chunk.getZ() + "!!");
        //plugin.getLogger().info("lastUnloadedTime =" + lastUnloadedTime.toString() + ", currentTime =" + currentTime.toString() + ", difference =" + timeDifference.toString());
    }

    private NamespacedKey key(String key) {
        return new NamespacedKey(plugin, key);
    }


}

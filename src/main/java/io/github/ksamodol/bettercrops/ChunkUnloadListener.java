package io.github.ksamodol.bettercrops;


import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class ChunkUnloadListener implements Listener {
    private final BetterCrops plugin;
    private final NamespacedKey key;

    public ChunkUnloadListener(BetterCrops plugin){
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "lastUnloadedTime");
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        PersistentDataContainer pdc = event.getChunk().getPersistentDataContainer();
        Long currentTime = event.getWorld().getFullTime();
        pdc.set(key, PersistentDataType.LONG, currentTime);

    }


}

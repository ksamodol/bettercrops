package io.github.ksamodol.bettercrops;

import io.github.ksamodol.bettercrops.compability.VersionHandler;
import io.github.ksamodol.bettercrops.compability.v1_16.Handler_1_16_R2;
import io.github.ksamodol.bettercrops.compability.v1_16.Handler_1_16_R3;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterCrops extends JavaPlugin {

    private VersionHandler versionHandler;

    @Override
    public void onEnable() {
        try {
            initializeVersionHandler();
        } catch (Exception e) {
            getLogger().warning(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new ChunkUnloadListener(this), this);
        getServer().getPluginManager().registerEvents(new ChunkLoadListener(this), this);

        getLogger().info("Plugin successfully enabled!");
    }
    @Override
    public void onDisable() {

        getLogger().info("Plugin is disabled!");
    }

    private void initializeVersionHandler() throws Exception {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String nmsVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
        switch(nmsVersion){
            case "v1_16_R2":
                versionHandler = new Handler_1_16_R2();
                break;
            case "v1_16_R3":
                versionHandler = new Handler_1_16_R3();
                break;
            default:
                throw new Exception("This version of NMS is not supported! Your NMS version: " + nmsVersion);
        }
    }

    public VersionHandler getVersionHandler(){
        return versionHandler;
    }

}

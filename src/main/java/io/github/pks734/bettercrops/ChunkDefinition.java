package io.github.pks734.bettercrops;

public class ChunkDefinition {
    private int x;
    private int z;
    private String worldName;

    public ChunkDefinition(int x, int z, String worldName) {
        this.x = x;
        this.z = z;
        this.worldName = worldName;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }
}

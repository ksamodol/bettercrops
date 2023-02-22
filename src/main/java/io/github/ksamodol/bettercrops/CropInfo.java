package io.github.ksamodol.bettercrops;

import org.bukkit.Material;

public class CropInfo {
    private Point3D location;
    private Material cropType;
    private int age;

    public CropInfo(Point3D location, Material cropType, int age) {
        this.location = location;
        this.cropType = cropType;
        this.age = age;
    }

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D location) {
        this.location = location;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

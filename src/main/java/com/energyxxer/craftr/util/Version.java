package com.energyxxer.craftr.util;

/**
 * Created by User on 1/21/2017.
 */
public class Version {
    public final int major;
    public final int minor;
    public final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int compare(Version v) {
        if(this.major - v.major != 0) return this.major - v.major;
        if(this.minor - v.minor != 0) return this.minor - v.minor;
        return this.patch - v.patch;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d",major,minor,patch);
    }
}

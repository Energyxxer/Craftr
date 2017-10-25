package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.coordinates;

public class CoordinateSet {

    private Coordinate x;
    private Coordinate y;
    private Coordinate z;

    public CoordinateSet(Coordinate x, Coordinate y, Coordinate z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordinateSet(double x, double y, double z) {
        this(new Coordinate(x), new Coordinate(y), new Coordinate(z));
    }

    public CoordinateSet(double x, double y, double z, Coordinate.Type type) {
        this(new Coordinate(type, x), new Coordinate(type, y), new Coordinate(type, z));
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}

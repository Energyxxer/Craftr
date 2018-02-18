package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.coordinates;

public class CoordinateSet implements CoordinateModifier {

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

    @Override
    public String getSubCommand() {
        return "at " + x + " " + y + " " + z;
    }

    @Override
    public boolean isIdempotent() {
        return x.isIdempotent() && y.isIdempotent() && z.isIdempotent();
    }

    @Override
    public boolean isSignificant() {
        return x.isSignificant() || y.isSignificant() || z.isSignificant();
    }

    @Override
    public boolean isAbsolute() {
        return x.getType() == Coordinate.Type.ABSOLUTE &&
                y.getType() == Coordinate.Type.ABSOLUTE &&
                z.getType() == Coordinate.Type.ABSOLUTE;
    }
}

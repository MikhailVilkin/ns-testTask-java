class Point {
    private double x;
    private double y;
    private double z;

    Point() {
        x = 0;
        y = 0;
        z = 0;
    }

    Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        // checkForTails();
    }

    Point(Vector3 v) {
        x = v.getX();
        y = v.getY();
        z = v.getZ();

        //checkForTails();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void checkForTails() {
        if (Math.abs(x) - Math.abs((int)x) < 1E-7 && Math.abs(x) - Math.abs((int)x) > 0)
            x = x - (x > 0 ? Math.abs(x) - Math.abs((int)x) : -(Math.abs(x) - Math.abs((int)x)));
        if (Math.abs(y) - Math.abs((int)y) < 1E-7 && Math.abs(y) - Math.abs((int)y) > 0)
            y = y - (y > 0 ? Math.abs(y) - Math.abs((int)y) : -(Math.abs(y) - Math.abs((int)y)));
        if (Math.abs(z) - Math.abs((int)z) < 1E-7  && Math.abs(z) - Math.abs((int)z) > 0)
            z = z - (z > 0 ? Math.abs(z) - Math.abs((int)z) : -(Math.abs(z) - Math.abs((int)z)));
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
class Sphere {
    private Point center;
    private double radius;

    Sphere() {
        center = new Point();
        radius = 10;
    }

    Sphere(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
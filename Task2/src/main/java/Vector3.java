public class Vector3 {

    private double x;
    private double y;
    private double z;

    Vector3() {
        x = 0;
        y = 0;
        z = 0;
    }

    Vector3(Point a, Point b) {
        this.x = a.getX() - b.getX();
        this.y = a.getY() - b.getY();
        this.z = a.getZ() - b.getZ();
    }

    Vector3(Line l) {
        this.x = l.getPoint2().getX() - l.getPoint1().getX();
        this.y = l.getPoint2().getY() - l.getPoint1().getY();
        this.z = l.getPoint2().getZ() - l.getPoint1().getZ();
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double abs() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    Vector3 normalize() {
        return this.divide(this.abs());
    }

    Vector3 divide(double scalarValue) {
        if (scalarValue == 0)
            return this;
        return new Vector3(x / scalarValue, y / scalarValue, z / scalarValue);
    }

    Vector3 multiply(double scalarValue) {
        return new Vector3(x * scalarValue, y * scalarValue, z * scalarValue);
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


    static Vector3 vectorProduct(Vector3 v1, Vector3 v2) {

        return new Vector3(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }

    static double scalarProduct(Vector3 v1, Vector3 v2) {

        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    static Vector3 subtract(Vector3 v1, Vector3 v2) {

        return new Vector3(v1.getX() - v2.getX(),
                v1.getY() - v2.getY(),
                v1.getZ() - v2.getZ());
    }

    static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    static double distanceFromPointToLine(Point P, Line l) {

        // радиус-вектор точки
        Vector3 r0 = new Vector3(P.getX(), P.getY(), P.getZ());

        // радиус-вектор точки на прямой
        Vector3 r1 = new Vector3(l.getPoint1().getX(), l.getPoint1().getY(), l.getPoint1().getZ());

        // направляющий вектор прямой
        Vector3 s = new Vector3(l);

        // расстояние от точки до прямой равно
        // отношению модуля веторного произведения векторов (r0 - r1) и s
        // к длине вектора s
        return vectorProduct(subtract(r0, r1), s).abs() / s.abs();
    }

    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

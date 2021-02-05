import java.util.ArrayList;
import java.util.Scanner;

public class SphereAndLine {

    Sphere sphere;
    Line line;

    String coordinates;

    SphereAndLine(String coordinates) {
        this.coordinates = coordinates;
        parseCoordinatesString();
    }

    ArrayList<Point> getIntersectionPoints() {

        // Радиус-вектор точки центра сферы
        Vector3 O = new Vector3(sphere.getCenter().getX(), sphere.getCenter().getY(), sphere.getCenter().getZ());

        // Вектор V нормали к плоскости ABO (AB - отрезок, О - центр сферы)
        Vector3 V = Vector3.vectorProduct(new Vector3(line.getPoint2(), sphere.getCenter()),
                                          new Vector3(line.getPoint1(), sphere.getCenter()));

        // Длина отрезка AB
        double AB = new Vector3(line.getPoint2(), line.getPoint1()).abs();
        // Расстояние h от центра сферы до отрезка AB
        double h = Vector3.distanceFromPointToLine(sphere.getCenter(), line);

        // Единичный вектор OP перпендикуляра из центра сферы О на прямую AB через векторное произведение векторов V и AB
        Vector3 OP = Vector3.vectorProduct(V, new Vector3(line.getPoint2(), line.getPoint1()));
        // Нормализация вектора OP
        OP = OP.normalize();

        // точка P - основание перпендикуляра из центра О на прямую AB
        Vector3 P = Vector3.add(O, OP.multiply(h));

        // Расстояние d от точки P до точек пересечения сферы и прямой (по т. Пифагора)
        // Если d - комплексное, то сфера и прямая не пересекаются
        double d;
        if (sphere.getRadius() * sphere.getRadius() - h * h >= 0)
            d = Math.sqrt(sphere.getRadius() * sphere.getRadius() - h * h);
        else
            return null;

        // Вектор Pd на прямой AB, имующий длину d
        Vector3 Pd = new Vector3(line.getPoint2(), line.getPoint1()).divide(AB).multiply(d);

        // Искомые точки
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(Vector3.add(P, Pd)));
        points.add(new Point(Vector3.subtract(P, Pd)));
        return points;
    }

    private void parseCoordinatesString() {

        String sphereCenter = getSphereCenter();
        String[] centerCoordinates = sphereCenter.split(", ");
        Point center = new Point(
                Integer.parseInt(centerCoordinates[0]),
                Integer.parseInt(centerCoordinates[1]),
                Integer.parseInt(centerCoordinates[2]));

        String sphereRadius = getSphereRadius();
        double radius = Double.parseDouble(sphereRadius);
        sphere = new Sphere(center, radius);


        String lineCoordinates = getLineCoordinates();
        lineCoordinates = lineCoordinates.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        Point linePoints[] = new Point[2];
        String[] coordinates = lineCoordinates.split(", ");
        for (int i = 0; i < 6; i+=3) {
            linePoints[i / 3] = new Point(
                    Double.parseDouble(coordinates[i]),
                    Double.parseDouble(coordinates[i+1]),
                    Double.parseDouble(coordinates[i+2]));
        }
        line = new Line(linePoints[0], linePoints[1]);

    }

    private String getSphereCenter() {

        int sphereIndex = coordinates.indexOf("sphere");
        String sphereCenterString = coordinates.substring(
                sphereIndex + coordinates.substring(sphereIndex).indexOf("center"));

        return sphereCenterString.substring(
                sphereCenterString.indexOf("[") + 1,
                sphereCenterString.indexOf("]"));
    }

    private String getSphereRadius() {

        int sphereIndex = coordinates.indexOf("sphere");

        String sphereRadiusString = coordinates.substring(
                sphereIndex + coordinates.substring(sphereIndex).indexOf("radius") + 8);

        int endOfSphereRadiusCoordinates = sphereRadiusString.indexOf(",");
        if (endOfSphereRadiusCoordinates == -1)
            endOfSphereRadiusCoordinates = sphereRadiusString.indexOf("}");

        String sphereRadius = sphereRadiusString.substring(0, endOfSphereRadiusCoordinates);
        if (sphereRadius.contains("}"))
            sphereRadius = sphereRadius.substring(0, sphereRadius.length() - 1);

        return sphereRadius;
    }

    private String getLineCoordinates() {

        String lineString = coordinates.substring(coordinates.indexOf("line"));

        return lineString.substring(
                lineString.indexOf("["),
                lineString.indexOf("}"));
    }
}

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //String path = "/Users/mikhail/Documents/IdeaProjects/nsTask2/input.txt";
        String path = args[0];
        try {

            FileScanner fileScanner = new FileScanner(path);
            ArrayList<String> contents = fileScanner.getFileContents();

            for (String line : contents) {

                System.out.println();
                System.out.println(line);
                SphereAndLine sphereAndLine = new SphereAndLine(line);
                ArrayList<Point> points = sphereAndLine.getIntersectionPoints();
                if (points == null) {
                    System.out.println("Коллизий не найдено");
                } else {
                    for (Point point : points) {
                        point.checkForTails();
                        System.out.println(point);
                    }
                }

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
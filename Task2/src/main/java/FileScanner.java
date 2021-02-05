import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileScanner {

    File file;

    FileScanner(String path) {
        file = new File(path);
    }

    ArrayList<String> getFileContents() throws FileNotFoundException {

        try(Scanner scanner = new Scanner(file)) {

            ArrayList<String> contents = new ArrayList<>();

            while (scanner.hasNext()) {
                contents.add(scanner.nextLine());
            }
            return contents;
        }
    }


}

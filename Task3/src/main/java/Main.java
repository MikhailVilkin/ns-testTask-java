import com.opencsv.CSVWriter;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String logPath;
        Instant timeStart;
        Instant timeEnd;

        if (args.length == 3) {
            logPath = args[0];
            try {
                // Парсинг даты
                timeStart = LocalDateTime.parse(args[1]).toInstant(ZoneOffset.UTC);
                timeEnd = LocalDateTime.parse(args[2]).toInstant(ZoneOffset.UTC);

                // Получаем массив логов
                ArrayList<LogInstance> instances = parseLogs(logPath);

                // Вычисляем ответы на вопросы
                String[] answers = getAnswers(instances, timeStart, timeEnd);

                // Помещаем ответы в файл
                createCsvFileWithAnswers(answers);

                return;

            } catch (DateTimeParseException e) {
                usage();
                e.printStackTrace();
            }
        }

        usage();
    }

    private static void usage() {
        System.out.println("Usage: Пример строки запуска: java –jar nsTask3 ./log.log 2021-02-03T12:00:00 2021-02-03T16:00:00");
    }

    // вместимость бочки
    static int capacity = 0;

    // Функция читает файл логов и помещает данные в массив объектов LogInstance
    private static ArrayList<LogInstance> parseLogs(String logPath) {

        int currentAmount;
        ArrayList<LogInstance> instances = new ArrayList<>();

        try {
            File file = new File(logPath);
            Scanner scanner = new Scanner(file);

            scanner.nextLine();
            String secondLine = scanner.nextLine();
            capacity = Integer.parseInt(secondLine.split(" ")[0]);

            String thirdLine = scanner.nextLine();
            currentAmount = Integer.parseInt(thirdLine.split(" ")[0]);

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();

                try {
                    instances.add(new LogInstance(data, currentAmount));
                    currentAmount = instances.get(instances.size() - 1).getCurrentAmount();

                } catch (DateTimeParseException e) {
                    System.out.println("Error reading logs");
                    e.printStackTrace();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

        return instances;
    }

    // Функция ищщет ответы на вопросы в логах
    // Массив хранит ответы:
    // 0 - какое количество попыток налить воду в бочку было за указанный период?
    // 1 - какой процент ошибок был допущен за указанный период?
    // 2 - какой объем воды был налит в бочку за указанный период?
    // 3 - какой объем воды был не налит в бочку за указанный период?
    // 4 - какое количество попыток забрать воду в бочку было за указанный период?
    // 5 - какой процент ошибок был допущен за указанный период?
    // 6 - какой объем воды был забран из бочки за указанный период?
    // 7 - какой объем воды был не забран из бочки за указанный период?
    // 8 - какой объем воды был в бочке в начале указанного периода?
    // 9 - Какой в конце указанного периода?
    //
    // Переменная started - флаг, указывющий, что заданный период начался
    // В лог файле данные отсортированы по возрастанию даты
    //
    // Функция возвращает массив строк с ответами
    //
    static String[] getAnswers(ArrayList<LogInstance> instances, Instant timeStart, Instant timeEnd) {
        int[] answers = new int[10];
        boolean started = false;
        for (LogInstance instance : instances) {

            if (!started && instance.getTime().isAfter(timeStart) && instance.getTime().isBefore(timeEnd)) {
                answers[8] = instance.getPreviousAmount();
                started = true;
            }

            if (started && instance.getTime().isAfter(timeEnd)) {
                answers[9] = instance.getPreviousAmount();
                started = false;
            }

            if (started) {
                if (instance.isAction()) {
                    answers[0]++;
                    if (!instance.isSuccess()) {
                        answers[1]++;
                        answers[3]+=instance.getLitres();
                    }
                    else
                        answers[2]+=instance.getLitres();
                }
                else {
                    answers[4]++;
                    if (!instance.isSuccess()) {
                        answers[5]++;
                        answers[7]+=instance.getLitres();
                    }
                    else
                        answers[6]+=instance.getLitres();
                }
            }
        }
        if (answers[9] == 0)
            answers[9] = instances.get(instances.size() - 1).getCurrentAmount();

        String[] result = new String[10];
        for (int i = 0; i < 10; i++)
            result[i] = answers[i] + "";
        result[1] = String.format("%.2f", ((float)answers[1] / answers[0]) * 100) + "%";
        result[5] = String.format("%.2f", ((float)answers[5] / answers[4]) * 100) + "%";

        return result;
    }

    // Создаем файл CSV и помещаем туда ответы
    static void createCsvFileWithAnswers(String[] answers) {

        String[] questions = {
                "какое количество попыток налить воду в бочку было за указанный период?",
                "какой процент ошибок был допущен за указанный период?",
                "какой объем воды был налит в бочку за указанный период?",
                "какой объем воды был не налит в бочку за указанный период?",
                "какое количество попыток забрать воду в бочку было за указанный период?",
                "какой процент ошибок был допущен за указанный период?",
                "какой объем воды был забран из бочки за указанный период?",
                "какой объем воды был не забран из бочки за указанный период?",
                "какой объем воды был в бочке в начале указанного периода?",
                "Какой в конце указанного периода?" };

        try {

            FileWriter outputFile = new FileWriter("./result.csv");
            CSVWriter writer = new CSVWriter(outputFile);

            writer.writeNext(questions);
            writer.writeNext(answers);

            writer.close();

        } catch (IOException e) {
            System.out.print("Не удалось создать файл");
            e.printStackTrace();
        }


    }
}

class LogInstance {

    private Instant time;
    private String user;

    // true если наливают, false, если забирают воду
    private boolean action;

    private int litres;
    private boolean success;

    private int previousAmount;
    private int currentAmount;

    LogInstance(String log, int previousAmount) throws DateTimeParseException {

        this.previousAmount = previousAmount;

        String[] substrings = log.split(" ");

        time = LocalDateTime.parse(substrings[0].substring(0, 26)).toInstant(ZoneOffset.UTC);
        user = substrings[2].replaceAll("\\[", "").replaceAll("]", "");
        if (log.contains("wanna top up")) {
            action = true;
            litres = Integer.parseInt(substrings[7].replace("l", ""));
            success = substrings[8].contains("у");

            if (success)
                currentAmount = previousAmount + litres;
            else
                currentAmount = previousAmount;
        }
        else {
            action = false;
            litres = Integer.parseInt(substrings[6].replace("l", ""));
            success = substrings[7].contains("у");

            if (success)
                currentAmount = previousAmount - litres;
            else
                currentAmount = previousAmount;
        }
    }

    int getCurrentAmount() {
        return currentAmount;
    }

    int getPreviousAmount() {
        return previousAmount;
    }

    public Instant getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public boolean isAction() {
        return action;
    }

    public int getLitres() {
        return litres;
    }

    public boolean isSuccess() {
        return success;
    }

    public String toString() {
        return time.toString() + " " + user + " " + litres + " " + (success ? "успех" : "фейл");
    }
}

class LogCreator {

    final static int maxCapacity = 300;
    static int currentValue;

    static void createLog(String pathname) {

        currentValue = 44;

        File file = new File(pathname);

        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname), "utf-8"))) {

            writer.write("META DATA:\n");
            writer.write(maxCapacity + " (объем бочки)\n");
            writer.write(currentValue + " (текущий объем воды в бочке)\n");

            for (int i = 0; i < 14000; i++) {
                String time = Clock.systemUTC().instant().toString();
                if (time.length() != 27) {
                    continue;
                }
                writer.write(time + " - ");
                writer.write("[username" + i % 10 + "] - wanna ");

                int litres = (int)((Math.random() * ((200 - 10) + 1)) + 10);

                boolean topScoop = false, success = false;
                if ((int)((Math.random()*10)%2) == 0) {
                    writer.write("top up ");
                    topScoop = true;
                }
                else
                    writer.write("scoop ");

                writer.write(litres + "");
                if (topScoop) {

                    if (currentValue + litres <= maxCapacity) {
                        currentValue += litres;
                        writer.write("l (успех)\n");
                    }
                    else {
                        writer.write("l (фейл)\n");
                    }

                } else {
                    if (currentValue - litres >= 0) {
                        currentValue -= litres;
                        writer.write("l (успех)\n");
                    }
                    else {
                        writer.write("l (фейл)\n");
                    }
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
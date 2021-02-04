/*
на mac os при запуске из терминала исполняемого .jar не принимает * в аргументах
пишет: zsh: no matches found: a*
возможно на линуксе похожее что-то, не проверял
все ок, если перед * вводить /
 */

public class Main {

    public static void main(String[] args) {
        if (args.length != 2)
            return;

        if (compare(args[0], args[1]))
            System.out.println("ОК");
        else
            System.out.println("КО");
    }

    private static boolean compare(String str1, String str2) {

        // убираем повторяющиеся *
        str2 = str2.replaceAll("\\*+", "*");

        // делим строку на подстроки между *
        String[] partsOfStr2 = str2.split("\\*");

        // ищем позиции подстрок в первой строке
        // если очередная подстрока не найдена, то функция indexOf вернет -1, значит строки разные
        // сохраняем в переменной currentIndex индекс в строке, до которого мы дошли
        int currentIndex = 0;
        for (String part : partsOfStr2) {
            if (str1.indexOf(part, currentIndex) == -1)
                return false;
            currentIndex = str1.indexOf(part, currentIndex) + part.length();
        }

        // если дошли до сюда, значит все части второй строки были найдены в первой,
        // и исходные строки считаем одинаковыми
        return true;
    }
}

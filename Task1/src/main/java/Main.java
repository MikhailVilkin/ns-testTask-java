import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            usage();
            return;
        }

        String result = "null";
        if (args.length == 2) {
            if (!isInteger(args[0])) {
                usage();
                return;
            }

            try {
                result = itoBase(Integer.parseInt(args[0]), args[1].toLowerCase());
            } catch (ConvertationException e) {
                e.getMessage();
                usage();
                return;
            } catch (NumberFormatException e) {
                usage();
            }

        } else {
            try {
                result = itoBase(args[0].toLowerCase(), args[1].toLowerCase(), args[2].toLowerCase());
            } catch (ConvertationException e) {
                e.getMessage();
                usage();
            }
        }

        System.out.println("Результат преобразования: " + result);
    }


    static String itoBase(int nb, String base) throws ConvertationException {

        // если новое основание системы счисления - это число,
        // то просто преобразуем из десятичной системы в заданную
        if (isInteger(base))
            return convertFromDecimal(nb, Integer.parseInt(base));

        // если заданное основание не является числом
        // производим магическое преобразование в такую сс
        ArrayList<Character> alphabet = createAlphabet(base);
        if (alphabet.isEmpty() || alphabet.size() > 36)
            throw new ConvertationException("Empty alphabet");

        // Переводим в новую систему счисления
        String converted = convertFromDecimal(nb, alphabet.size());
        String result = "";
        for (char digit : converted.toCharArray()) {
            if (digit >= '0' && digit <= '9') {
                result += alphabet.get(digit - '0');
                continue;
            } else if (digit >= 'a' && digit <= 'z') {
                result += alphabet.get(digit - 'a');
                continue;
            }
        }

        return result;
    }


    static String itoBase(String nb, String baseSrc, String baseDst) throws ConvertationException {

        if (baseSrc.equals("10") && isInteger(baseDst)) {
            return convertFromDecimal(Integer.parseInt(nb), Integer.parseInt(baseDst));
        }
        if(isInteger(baseSrc) && baseDst.equals("10")) {
            return convertToDecimal(nb, Integer.parseInt(baseSrc)) + "";
        }
        if(isInteger(baseDst) && isInteger(baseSrc)) {
            return convertFromDecimal(convertToDecimal(nb, Integer.parseInt(baseSrc)), Integer.parseInt(baseDst));
        }

        int nbInDec;
        ArrayList<Character> alphabet = createAlphabet(baseSrc);
        if (alphabet.isEmpty() || alphabet.size() > 36)
            throw new ConvertationException("Empty alphabet");

        String nbInNormalForm = "";
        for (char digit : nb.toCharArray()) {
            // если число содержит символы не из алфавита
            if (!alphabet.contains(digit))
                return null;

            int index = alphabet.indexOf(digit);
            if (index < 10) {
                nbInNormalForm += index;
            } else {
                nbInNormalForm += (index - 10 + 'A');
            }
        }
        nbInDec = convertToDecimal(nbInNormalForm, alphabet.size());

        return itoBase(nbInDec, baseDst);
    }


    static String convertFromDecimal(int number, int newBase) {

        ArrayList<Character> converted = new ArrayList<>();
        while (number / newBase > 0) {
            int newDigit = number % newBase;
            char newDigitChar;
            if (newDigit > 9) {
                newDigitChar = (char)(newDigit - 10 + 'a');
            } else {
                newDigitChar = (char)(newDigit + '0');
            }
            converted.add(newDigitChar);
            number /= newBase;
        }
        if (number > 9) {
            converted.add((char)(number % newBase - 10 + 'a'));
        } else {
            converted.add((char)(number % newBase + '0'));
        }

        converted.add((char)(number % newBase));

        StringBuilder builder = new StringBuilder(converted.size());
        for(Character ch: converted)
        {
            builder.append(ch);
        }

        return builder.reverse().toString();
    }


    static int convertToDecimal(String number, int base) {

        ArrayList<Integer> numberArray = new ArrayList<>();
        for (char digit : number.toCharArray()) {
            if (isInteger(Character.toString(digit))) {
                numberArray.add(digit - '0');
            } else if (digit >= 'a' && digit <= 'z'){
                numberArray.add(digit - 'a' + 10);
            }
        }

        int result = 0;
        for (int i = numberArray.size() - 1; i >= 0; i--) {
            result += numberArray.get(i) * Math.pow(base, numberArray.size() - 1 -i);
        }

        return result;
    }


    // Идея в том, чтобы котики тоже были системой счисления
    // Берем все уникальные символы из строки
    // Их вес будет рассчитан по порядку в данной строке
    // Получаем алфавит для нашей системы счисления
    static ArrayList<Character> createAlphabet(String base) {
        ArrayList<Character> alphabet = new ArrayList<>();

        for (char ch : base.toCharArray()) {
            if (alphabet.contains(ch))
                continue;
            alphabet.add(ch);
        }

        return alphabet;
    }


    // Вывод usage
    static void usage() {
        System.out.println("Usage: необходимо ввести либо десятичное число и систему счисления в которую нужно перевести");
        System.out.println("либо число, его систему счисления и систему счисления в которую нужно перевести");
        System.out.println("десятичное представление числа должно быть не больше Integer.MAX_VALUE");
        System.out.println("Мощность алфавита системы счисления должна быть не более 36");
    }


    // Функция проверяет, что строка - это целое число
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}


class ConvertationException extends Exception {

    public ConvertationException() {}

    public ConvertationException(String message)
    {
        super(message);
    }
}
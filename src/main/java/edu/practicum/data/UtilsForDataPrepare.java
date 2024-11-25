package edu.practicum.data;

import java.util.Random;

public class UtilsForDataPrepare {

    public static String stringRandomGenerate(int length) {
        Random random = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            //используем символы с кодами в диапазоне от 97 до 122 (английские строчные буквы)
            //если появятся ограничения на используемые символы, то можно описать массив с этими символами и выбирать случайные значения из него
            text[i] = (char) (random.nextInt(26) + 97); //26=122-97+1
        }
        return new String(text);
    }

    public static String emailRandom(int length) {
        Random random = new Random();
        int domenLength = random.nextInt(10) + 5;
        String name = stringRandomGenerate(random.nextInt(254 - domenLength) + 1);
        String domen = stringRandomGenerate(domenLength - 4);
        return String.format("%s%s%s%s%s", name, "@", domen, ".", stringRandomGenerate(2));
    }

    public static String substringRandom(String str, int substringLength) {
        Random random = new Random();
        int strLength = str.length();
        int beginSubstr = 0;
        if ((strLength - substringLength) > 0) {
            beginSubstr = random.nextInt(strLength - substringLength);
            return str.substring(beginSubstr, beginSubstr + substringLength - 1);
        }
        else {return str.substring(beginSubstr, strLength);}

    }


}

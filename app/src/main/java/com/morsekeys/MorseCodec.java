package com.morsekeys;

import java.util.*;

public class MorseCodec {
    static final String EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    static final String[] EN_C = {".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--..","-----",".----","..---","...--","....-",".....","-....","--...","---..","----."};
    static final String AR = "ابتثجحخدذرزسشصضطظعغفقكلمنهوي";
    static final String[] AR_C = {".-","-...","-","-.-.",".---","....","---","-..","--..",".-.","---.","...","----","-..-","...-","..-","-.--",".-.-","--.","..-.","--.-","-.-",".-..","--","-.","..-..",".--",".."};

    static String[] codes(String[] std, String key) {
        List<String> l = new ArrayList<>(Arrays.asList(std));
        if (!key.isEmpty()) Collections.shuffle(l, new Random(key.hashCode()));
        return l.toArray(new String[0]);
    }

    public static String encode(String text, boolean ar, String key) {
        String alpha = ar ? AR : EN;
        String[] c = codes(ar ? AR_C : EN_C, key);
        StringBuilder sb = new StringBuilder();
        for (char ch : (ar ? text : text.toUpperCase()).toCharArray()) {
            if (ch == ' ') sb.append("/ ");
            else { int i = alpha.indexOf(ch); if (i >= 0) sb.append(c[i]).append(' '); }
        }
        return sb.toString().trim();
    }

    public static String decode(String morse, boolean ar, String key) {
        String alpha = ar ? AR : EN;
        List<String> c = Arrays.asList(codes(ar ? AR_C : EN_C, key));
        StringBuilder sb = new StringBuilder();
        for (String s : morse.trim().split("\\s+")) {
            if (s.equals("/")) sb.append(' ');
            else { int i = c.indexOf(s); if (i >= 0) sb.append(alpha.charAt(i)); }
        }
        return sb.toString();
    }
}

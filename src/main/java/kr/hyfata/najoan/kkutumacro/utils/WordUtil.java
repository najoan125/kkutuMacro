package kr.hyfata.najoan.kkutumacro.utils;

import kr.hyfata.najoan.kkutumacro.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

public class WordUtil {
    private static final HashSet<String> excludedWords = new HashSet<>();

    public static HashSet<String> getCSVData() throws IOException {
        HashSet<String> result = new HashSet<>();
        InputStream inputStream = WebUtil.class.getClassLoader().getResourceAsStream("kr.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8));

        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line.split(",")[0].replace("\"",""));
        }
        return result;
    }

    public static ArrayList<String> getWords(String start, String start2) {
        ArrayList<String> textList = new ArrayList<>();
        for (String s : Main.getWords()) {
            if (excludedWords.contains(s)) {
                continue;
            }
            if (s.startsWith(start)) {
                textList.add(s);
            } else if (start2 != null && s.startsWith(start2)) {
                textList.add(s);
            }
        }
        Comparator<String> c = (s1, s2) -> Integer.compare(s2.length(), s1.length());
        textList.sort(c);
        return textList;
    }

    public static void addExcludedWord(String word) {
        excludedWords.add(word);
    }

    public static void resetExcludedWords() {
        excludedWords.clear();
    }
}

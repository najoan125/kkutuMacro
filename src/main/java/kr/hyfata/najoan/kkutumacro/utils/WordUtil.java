package kr.hyfata.najoan.kkutumacro.utils;

import kr.hyfata.najoan.kkutumacro.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class WordUtil {
    private static final HashSet<String> excludedWords = new HashSet<>();

    public static ArrayList<String> getCSVData() throws IOException {
        ArrayList<String> result = new ArrayList<>();
        InputStream inputStream = WebUtil.class.getClassLoader().getResourceAsStream("kr.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8));

        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line.split(",")[0].replace("\"",""));
        }
        return result;
    }

    static ArrayList<String> textList = new ArrayList<>(); // All words that match the condition
    static ArrayList<String> missionedList = new ArrayList<>(); // only contains mission word(included at textList)
    static ArrayList<String> result = new ArrayList<>(); // textList + missionedList

    // using at preLoadWords
    static String temp = "";

    // Is preLoadWords successful?
    static boolean success = false;

    public static void preLoadWords(String start, String start2) {
        if (!temp.equals(start)) {
            temp = start;
            textList = new ArrayList<>();

            for (String s : Main.getWords()) {
                if (excludedWords.contains(s)) {
                    continue;
                }
                if (s.startsWith(start) || (start2 != null && s.startsWith(start2))) {
                    textList.add(s);
                }
            }
        }
        success = true;
    }

    public static ArrayList<String> getWords(String mission) {
        if (textList.isEmpty()) {
            return null;
        } else if (!success) {
            return null;
        }
        success = false;

        missionedList = new ArrayList<>();
        result = new ArrayList<>();

        if (mission != null) {
            for (String s : textList) {
                if (s.contains(mission)) {
                    missionedList.add(s);
                }
            }
        }

        if (!missionedList.isEmpty()) {
            result.addAll(missionedList);
            result.addAll(textList);
            return result;
        }
        return textList;
    }

    public static void addExcludedWord(String word) {
        excludedWords.add(word);
    }

    public static void resetExcludedWords() {
        excludedWords.clear();
    }
}

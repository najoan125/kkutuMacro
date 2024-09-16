package kr.hyfata.najoan.kkutumacro.handler;

import kr.hyfata.najoan.kkutumacro.Main;
import kr.hyfata.najoan.kkutumacro.handler.dto.Count;
import kr.hyfata.najoan.kkutumacro.handler.dto.Round;
import kr.hyfata.najoan.kkutumacro.utils.WebUtil;
import kr.hyfata.najoan.kkutumacro.utils.WordUtil;

import java.util.ArrayList;

public class Attack {
    private static int tempCount;
    private static int tempIndex = -1;
    private static String tempRound = "";

    private static boolean passed = false;

    public static void attack() {
        init();
        ArrayList<String> words = WordUtil.getWords(WebUtil.getMissionWord());
        // if failed, init tempWords list
        if (words == null) {
            return;
        }

        // add index of tempWords list
        if (words.size() > tempIndex + 1) {
            tempIndex++;
        }

        // send word OR not found message
        if (!words.isEmpty()) {
            passed = false;
            WebUtil.send(words.get(tempIndex));
        } else {
            if (passed) {
                Main.LOG.warn("The word has not found in dictionary");
                WebUtil.send("ㅠㅠ");
            }
            passed = true;
        }
    }

    private static void reset() {
        WordUtil.resetExcludedWords();
        Count.resetCount();
        passed = false;
    }

    private static void init() {
        if (!tempRound.equals(Round.getCurrentRound())) {
            tempRound = Round.getCurrentRound();
            reset();
        }

        if (tempCount != Count.getCount()) {
            tempIndex = -1;
            tempCount = Count.getCount();
            passed = false;
        } else if (Count.getCount() == 0) { // first turn
            preLoad(); // preLoad words(history is not perfect)
            Count.addCount();
            tempCount = Count.getCount();
            tempIndex = -1;
            passed = false;
        }
    }

    private static void preLoad() {
        String start = WebUtil.getStartWord();
        String start2 = null;
        boolean isRegexMatch = start != null && start.matches("^.|.\\(.\\)$");

        if (isRegexMatch) {
            if (start.contains("(")) {
                String[] parts = start.split("\\(");
                start = parts[0];
                start2 = parts[1].replace(")", "");
            }
            WordUtil.preLoadWords(start, start2);
        }
    }
}

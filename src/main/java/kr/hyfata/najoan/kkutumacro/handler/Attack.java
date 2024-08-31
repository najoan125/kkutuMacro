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
    private static ArrayList<String> tempWords;

    private static String passedWord = "";

    public static void attack(String start, String start2) {
        init(start, start2);
        if (tempWords.size() > tempIndex + 1) {
            tempIndex++;
        }

        if (!tempWords.isEmpty()) {
            passedWord = "";
            WebUtil.send(tempWords.get(tempIndex));
        } else {
            if (!passedWord.equals(start)) {
                Main.LOG.warn("The word has not found in dictionary");
                WebUtil.send("ㅠㅠ");
            }
            passedWord = start;
        }
    }

    private static void reset() {
        WordUtil.resetExcludedWords();
        Count.resetCount();
        passedWord = "";
    }

    private static void init(String start, String start2) {
        if (!tempRound.equals(Round.getCurrentRound())) {
            tempRound = Round.getCurrentRound();
            reset();
        }

        if (tempCount != Count.getCount()) {
            tempWords = WordUtil.getWords(start, start2);
            tempIndex = -1;
            tempCount = Count.getCount();
        } else if (Count.getCount() == 0) {
            tempWords = WordUtil.getWords(start, start2);
            Count.addCount();
            tempCount = Count.getCount();
            tempIndex = -1;
        }
    }
}

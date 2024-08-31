package kr.hyfata.najoan.kkutumacro.handler.dto;

public class Round {
    private static String currentRound;

    public static String getCurrentRound() {
        return currentRound;
    }

    public static void setCurrentRound(String currentRound) {
        Round.currentRound = currentRound;
    }
}

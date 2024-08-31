package kr.hyfata.najoan.kkutumacro.handler;

import kr.hyfata.najoan.kkutumacro.handler.dto.Count;

public class CountHandler {
    private static boolean shouldCount = true;

    public static void turnStart() {
        if (shouldCount) {
            shouldCount = false;
            Count.addCount();
        }
    }

    public static void turnEnd() {
        shouldCount = true;
    }
}

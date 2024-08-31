package kr.hyfata.najoan.kkutumacro.handler.dto;

import java.util.concurrent.atomic.AtomicInteger;

public class Count {
    private static final AtomicInteger count = new AtomicInteger(0);

    public static int getCount() {
        return count.get();
    }

    public static void addCount() {
        count.incrementAndGet();
    }

    public static void resetCount() {
        count.set(0);
    }
}

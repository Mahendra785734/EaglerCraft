package dev.featherish.client.modules;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Counts real mouse-button events in a rolling one-second window.
 *
 * The game input adapter must call recordLeftClick/recordRightClick for each
 * actual click event and call snapshot(nowMillis) with a monotonic clock.
 * This class does not generate input or simulate clicks.
 */
public final class ClickRateCounter {
    private static final long WINDOW_MILLIS = 1000L;

    private final Deque<Long> leftClicks = new ArrayDeque<Long>();
    private final Deque<Long> rightClicks = new ArrayDeque<Long>();

    public synchronized void recordLeftClick(long monotonicMillis) {
        addTimestamp(leftClicks, monotonicMillis);
    }

    public synchronized void recordRightClick(long monotonicMillis) {
        addTimestamp(rightClicks, monotonicMillis);
    }

    public synchronized Counts snapshot(long nowMillis) {
        prune(leftClicks, nowMillis);
        prune(rightClicks, nowMillis);
        return new Counts(leftClicks.size(), rightClicks.size());
    }

    private static void addTimestamp(Deque<Long> timestamps, long timestamp) {
        if (!timestamps.isEmpty() && timestamp < timestamps.peekLast()) {
            // Ignore out-of-order events instead of corrupting the time window.
            return;
        }
        timestamps.addLast(timestamp);
    }

    private static void prune(Deque<Long> timestamps, long nowMillis) {
        long cutoff = nowMillis - WINDOW_MILLIS;
        while (!timestamps.isEmpty() && timestamps.peekFirst() <= cutoff) {
            timestamps.removeFirst();
        }
        while (!timestamps.isEmpty() && timestamps.peekLast() > nowMillis) {
            timestamps.removeLast();
        }
    }

    public static final class Counts {
        public final int left;
        public final int right;

        private Counts(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }
}

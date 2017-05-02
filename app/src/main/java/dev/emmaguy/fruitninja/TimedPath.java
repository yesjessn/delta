package dev.emmaguy.fruitninja;

import android.graphics.Path;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class TimedPath {
    private static final long POINT_DURATION = 100L;

    private final Deque<TimedPoint> points;
    private final Path path;

    public TimedPath() {
        points = new ArrayDeque<>(100);
        path = new Path();
    }

    public TimedPath(long timestamp, float x, float y) {
        this();
        points.add(TimedPoint.create(timestamp, x, y));
        path.moveTo(x, y);
    }

    public synchronized void addPoint(long timestamp, float x, float y) {
        points.add(TimedPoint.create(timestamp, x, y));
        path.lineTo(x, y);
    }

    public synchronized void update(long now) {
        Log.d("FN", String.format("Updating %d points on path @%s (%s - %s)", points.size(), now, getOldestTimestamp(), getLatestTimestamp()));
        boolean pathDirty = false;
        Iterator<TimedPoint> iter = points.iterator();
        if (!iter.hasNext()) {
            return;
        }
        TimedPoint tp = iter.next();
        while (tp.isExpired(now)) {
            Log.d("FN", String.format("Removing expired point @%s (%.2f, %.2f)", tp.timestamp, tp.x, tp.y));
            pathDirty = true;
            tp.destroy();
            iter.remove();
            if (iter.hasNext()) {
                tp = iter.next();
            } else {
                return;
            }
        }

        if (pathDirty) {
            Log.d("FN", String.format("Starting path at @%s (%.2f, %.2f)", tp.timestamp, tp.x, tp.y));
            path.reset();
            path.moveTo(tp.x, tp.y);
            while (iter.hasNext()) {
                tp = iter.next();
                path.lineTo(tp.x, tp.y);
            }
        }
    }

    public synchronized int size() {
        return points.size();
    }

    public synchronized void destroy() {
        TimedPoint tp;
        while ((tp = points.poll()) != null) {
            tp.destroy();
        }
    }

    public Path getPath() {
        return path;
    }

    public long getLatestTimestamp() {
        if (!points.isEmpty()) {
            return points.getLast().timestamp;
        } else {
            return -1;
        }
    }

    public long getOldestTimestamp() {
        if (!points.isEmpty()) {
            return points.getFirst().timestamp;
        } else {
            return -1;
        }
    }

    private static class TimedPoint {
        private static final Deque<TimedPoint> graveyard = new ArrayDeque<>(100);

        public long timestamp;
        public float x, y;

        private TimedPoint(long timestamp, float x, float y) {
            this.timestamp = timestamp;
            this.x = x;
            this.y = y;
            Log.d("TP", "creating new point");
        }

        public boolean isExpired(long now) {
            return timestamp < now - POINT_DURATION;
        }

        public void destroy() {
            synchronized (graveyard) {
                graveyard.push(this);
            }
        }

        public static TimedPoint create(long timestamp, float x, float y) {
            TimedPoint tp;
            synchronized (graveyard) {
                tp = graveyard.poll();
            }
            if (tp == null) {
                return new TimedPoint(timestamp, x, y);
            } else {
                tp.timestamp = timestamp;
                tp.x = x;
                tp.y = y;
                return tp;
            }
        }
    }
}

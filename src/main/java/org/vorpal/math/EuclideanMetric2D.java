/**
 * EuclideanMetric.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.math;

/**
 * Mote: calculates the square of the Euclidean distance.
 * There is no reason to take the sqrt when comparing distances, which is what we will
 * be doing, so if the user wants the actual distance, they can take the sqrt themselves.
 */
final public class EuclideanMetric2D implements Metric2D {
    @Override
    public int distance(int x1, int y1, int x2, int y2) {
        final int dx = x1 - x2;
        final int dy = y1 - y2;
        return dx * dx + dy * dy;
    }
}

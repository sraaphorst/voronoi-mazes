/**
 * EuclideanMetric.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.math;

import java.awt.Point;

/**
 * Mote: calculates the square of the Euclidean distance.
 * There is no reason to take the sqrt when comparing distances, which is what we will
 * be doing, so if the user wants the actual distance, they can take the sqrt themselves.
 */
final public class EuclideanMetric2D implements Metric2D {
    @Override
    public int distance(Point p1, Point p2) {
        final int dx = p1.x - p2.x;
        final int dy = p1.y - p2.y;
        return dx * dx + dy * dy;
    }
}

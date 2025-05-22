/**
 * ChebyshevMetric.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.math;

import java.awt.Point;

final public class ChebyshevMetric2D implements Metric2D {
    @Override
    public int distance(Point p1, Point p2) {
        return Math.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
    }
}

/**
 * ChebyshevMetric.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.math;

final public class ChebyshevMetric2D implements Metric2D {
    @Override
    public int distance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}

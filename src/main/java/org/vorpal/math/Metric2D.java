/**
 * Metric2D.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.math;

@FunctionalInterface
public interface Metric2D {
    int distance(int x1, int y1, int x2, int y2);
}

/**
 * Metric2D.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.math;

import java.awt.Point;

@FunctionalInterface
public interface Metric2D {
    int distance(Point p1, Point p2);
}

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Point2D;

public class Point2DTest {
    private Point2D point;

    public Point2DTest() {
        try {
            point = new Point2D(new double[] {1, 3});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkRot() {
        String testName = "point2D length";
        assertTrue(testName, Math.abs(point.abs() - 3.1623) < 0.001);

        try {
            point = Point2D.rot(point, Math.PI);
            assertTrue(testName, Math.abs(Point2D.add(point, point).abs() - 6.3246) < 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }
}

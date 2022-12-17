import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Point;

public class PointTest {
    
    private Point point;

    public PointTest() {
        try {
            point = new Point(5, new double[] {1, 2, 3, 4, 5});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkLength() {
        String testName = "point length";
        assertTrue(testName, Math.abs(point.abs() - 7.4162) < 0.001);
    }

    @Test
    public void checkAdd() {
        String testName = "point add";
        try {
            assertTrue(testName, Math.abs(Point.add(point, point).abs() - 14.8324) < 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkSub() {
        String testName = "point sub";
        try {
            assertTrue(testName, Math.abs(Point.sub(point, point).abs()) < 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkMulti() {
        String testName = "point multi";
        try {
            assertTrue(testName, Math.abs(Point.mult(point, 3).abs() - 22.2486) < 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }
}

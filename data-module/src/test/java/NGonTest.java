import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.NGon;
import app.Point2D;

public class NGonTest {
    private Point2D[] points;
    private NGon ngon;

    public NGonTest() {
        try {
            points = new Point2D[] {
                new Point2D(1, 1),
                new Point2D(2, 1),
                new Point2D(3, 0),
                new Point2D(4, -2),
                new Point2D(0, 0)
            };
            ngon = new NGon(points);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkLength() {
        String testName = "ngon length";
        try {
            assertTrue(testName, Math.abs(ngon.length() - 10.54) < 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkSquare() {
        String testName = "ngon square";
        try {
            assertTrue(testName, Math.abs(ngon.square() - 5) < 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkCross() {
        String testName = "ngon shift";
        try {
            Point2D[] points1 = new Point2D[] {
                new Point2D(-2, 1),
                new Point2D(-3, 1),
                new Point2D(-4, 0),
                new Point2D(-5, -2),
                new Point2D(-1, 0)
            };
            NGon ngon1 = new NGon(points1);
            // assertTrue(testName, ngon.cross(ngon1));

            double rotAngle = -Math.PI;
            ngon1.rot(rotAngle);
            assertTrue(testName, ngon.cross(ngon1));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }
}

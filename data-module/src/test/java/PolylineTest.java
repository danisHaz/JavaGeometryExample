import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Point2D;
import app.Polyline;

public class PolylineTest {
    
    private Polyline polyline;
    private Point2D[] point;

    public PolylineTest() {
        try {
            point = new Point2D[] {
                new Point2D(1, 3),
                new Point2D(2, 4)
            };
            polyline = new Polyline(point);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkLength() {
        String testName = "Polyline length";
        try {
            assertTrue(testName, Math.abs(polyline.length() - 1.4142) < 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkCross() {
        String testName = "Polyline cross";
        try {
            Point2D[] points = new Point2D[] {
                new Point2D(),
                new Point2D(2, 3),
                new Point2D(1, 4)
            };
            Polyline polyline1 = new Polyline(points);
            assertTrue(testName, polyline1.cross(polyline));

            Point2D shiftVec = new Point2D(0, -2);
            polyline1.shift(shiftVec);
            assertFalse(testName, polyline1.cross(polyline));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }
}

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Circle;
import app.Point2D;

public class CircleTest {
    private Point2D center;
    private double radius = 2;
    private Circle circle;

    public CircleTest() {
        try {
            center = new Point2D(1, 2);
            circle = new Circle(center, radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkSquare() {
        String testName = "circle square";
        try {
            assertTrue(testName, circle.square() == Math.PI * radius * radius);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkLength() {
        String testName = "circle length";
        try {
            assertTrue(testName, circle.length() == Math.PI * 2 * radius);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkShift() {
        String testName = "circle shift";
        try {
            Point2D shiftVec = new Point2D(1, -2);
            circle.shift(shiftVec);
            assertTrue(testName, circle.getP().getX(0) == 2 && circle.getP().getX(1) == 0);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkCross() {
        String testName = "circle cross";
        try {
            Point2D center1 = new Point2D(1, -2);
            double radius1 = 2;
            Circle circle1 = new Circle(center1, radius1);
            assertTrue(testName, circle.cross(circle1));
            
            radius1 = 1;
            circle1.setR(radius1);
            assertFalse(testName, circle.cross(circle1));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }
}

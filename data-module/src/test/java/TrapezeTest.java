import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Point2D;
import app.Trapeze;

public class TrapezeTest {
    
    private Point2D[] points;
    private Trapeze trapeze;

    public TrapezeTest() {
        try {
            points = new Point2D[] {
                new Point2D(),
                new Point2D(4, 6),
                new Point2D(11, 6),
                new Point2D(12, 0)
            };
            trapeze = new Trapeze(points);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkLength() {
        String testName = "trapeze length";
        try {
            assertTrue(testName, Math.abs(trapeze.length() - 32.2939) < 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    } 
}

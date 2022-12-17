import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Point2D;
import app.Segment;

public class SegmentTest {
    
    private Segment segment;
    private Point2D[] points;

    public SegmentTest() {
        try {
            points = new Point2D[] {
                new Point2D(),
                new Point2D(5, 10),
            };
            segment = new Segment(points[0], points[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkLength() {
        String testName = "segment length";
        try {
            assertTrue(testName, Math.abs(segment.length() - 11.1803) < 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkSquare() {
        String testName = "segment square";
        try {
            assertTrue(testName, Math.abs(segment.square()) < 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }
}

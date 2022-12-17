import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Point2D;
import app.TGon;

public class TGonTest {

    private TGon tgon;
    private Point2D[] points;

    public TGonTest() {
        try {
            points = new Point2D[] {
                new Point2D(),
                new Point2D(5, 10),
                new Point2D(10, 0)
            };
            tgon = new TGon(points);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkLength() {
        String testName = "tgon length";
        try {
            assertTrue(testName, Math.abs(tgon.length() - 32.3607) < 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }

    @Test
    public void checkSquare() {
        String testName = "tgon square";
        try {
            assertTrue(testName, Math.abs(tgon.square() - 50.00) < 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(testName, false);
        }
    }
}

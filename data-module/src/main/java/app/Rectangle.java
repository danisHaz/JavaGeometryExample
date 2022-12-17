package app;

import org.bson.Document;

public class Rectangle extends QGon {
	public Rectangle(Point2D[] p) throws Exception {
		super(p);
		type = "Rectangle";
		if (Point2D.mult(Point2D.sub(p[n - 1], p[0]), Point2D.sub(p[0], p[1])) != 0) {
			throw new Exception("Rectangle: Point is not valid\n");
		}
		for (int i = 1; i < n - 1; i++) {
			if (Point2D.mult(Point2D.sub(p[i - 1], p[i]), Point2D.sub(p[i], p[i + 1])) != 0) {
				throw new Exception("Rectangle: Point is not valid\n");
			}
		}
		if (Point2D.mult(Point2D.sub(p[n - 2], p[n - 1]), Point2D.sub(p[n - 1], p[0])) != 0) {
			throw new Exception("Rectangle: Point is not valid\n");
		}
	}

	@Override
	public double square() throws Exception {
		return Point2D.sub(p[0], p[1]).abs() * Point2D.sub(p[1], p[2]).abs();
	}

	@Override
    public Document toBson() {
        Document rectangle = new Document("type", type)
            .append("data", toString());

        return rectangle;
    }

	@Override
	public String toString() {
		return super.toString();
	}
}

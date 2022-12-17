package app;

import org.bson.Document;

public class Segment extends OpenFigure {
	private Point2D start;
	private Point2D finish;
	private String type;

	@Override
	public String getType() { return type; }

	public Segment(Point2D a, Point2D b) throws Exception {
		if (a.getX(0) == b.getX(0) && a.getX(1) == b.getX(1)) {
			throw new Exception("Segment: Start and finish are the same point\n");
		}
		start = new Point2D(a.getX(0), a.getX(1));
		finish = new Point2D(b.getX(0), b.getX(1));
		type = "Segment";
	}

	public Point2D getStart() {
		return start;
	}

	public Point2D getFinish() {
		return finish;
	}

	public void setStart(Point2D p) throws Exception {
		this.start = new Point2D(p.getX(0), p.getX(1));
	}

	public void setFinish(Point2D p) throws Exception {
		this.finish = new Point2D(p.getX(0), p.getX(1));
	}

	@Override
	public double length() throws Exception {
		return (new Point2D(Point.sub(start, finish).getX())).abs();
	}

	@Override
	public Segment shift(Point2D a) throws Exception {
		a.setX(1, a.getX(1));
		start.add(a);
		finish.add(a);
		return this;
	}

	@Override
	public Segment rot(double phi) throws Exception {
		start.rot(phi);
		finish.rot(phi);
		return this;
	}

	@Override
	public Segment symAxis(int i) throws Exception {
		start.symAxis(i);
		finish.symAxis(i);
		return this;
	}

	@Override
	public boolean cross(IShape i) throws Exception {
		if (i instanceof Segment) {
			Segment seg = (Segment)i;
			Point2D a = new Point2D(start.getX(0), start.getX(1));
			Point2D b = new Point2D(finish.getX(0), finish.getX(1));
			Point2D c = new Point2D(seg.getStart().getX(0), seg.getStart().getX(1));
			Point2D d = new Point2D(seg.getFinish().getX(0), seg.getFinish().getX(1));

			Point2D p1 = new Point2D(Point.sub(b, a).getX(0), Point.sub(b, a).getX(1));
			Point2D p2 = new Point2D(Point.sub(d, c).getX(0), Point.sub(d, c).getX(1));

			double x1 = a.getX(0), x2 = b.getX(0), x3 = c.getX(0), x4 = d.getX(0);
			double y1 = a.getX(1), y2 = b.getX(1), y3 = c.getX(1), y4 = d.getX(1);

			if (Math.abs(p1.getX(0) * p2.getX(0) + p1.getX(1) * p2.getX(1) - p1.abs() * p2.abs()) <= 0.001) {
				Point2D additionalPoint = new Point2D(x3 - x1, y3 - y1);
				if ((Math.abs(
						p1.getX(0) * additionalPoint.getX(0)
						+ p1.getX(1) * additionalPoint.getX(1)
						- p1.abs() * additionalPoint.abs()
					) <= 0.001 ||
					Math.abs(
						p1.getX(0) * additionalPoint.getX(0)
						+ p1.getX(1) * additionalPoint.getX(1)
						+ p1.abs() * additionalPoint.abs()
					) <= 0.001)
					&& (x1 <= x3 && x3 <= x2 || x1 <= x4 && x4 <= x2 || x2 <= x3 && x3 <= x1 || x2 <= x4 && x4 <= x1)) {
					return true;
				} else {
					return false;
				}
			}

			double k2Up = (x2 - x1) * y1 + (x3 - x1) * (y2 - y1) - y3 * (x2 - x1);
			double k2Down = (y4 - y3) * (x2 - x1) - (y2 - y1) * (x4 - x3);
			double k2 = k2Up / k2Down;

			Point result = p2.multi(k2);
			result.add(c);

			if (
				(x3 <= result.getX(0) && result.getX(0) <= x4 || x4 <= result.getX(0) && result.getX(0) <= x3)
				&&
				(x1 <= result.getX(0) && result.getX(0) <= x2 || x2 <= result.getX(0) && result.getX(0) <= x1)
			) {
				return true;
			}

			return false;
		} else if (i instanceof Polyline) {
			Point2D[] coords = ((Polyline) i).getP();
			Segment seg;
			for (int j = 1; j < coords.length; j++) {
				seg = new Segment(coords[j - 1], coords[j]);
				if (seg.cross(this))
					return true;
			}
			seg = new Segment(coords[coords.length - 1], coords[0]);
			return seg.cross(this);
		} else if (i instanceof NGon) {
			Point2D[] coords = ((NGon) i).getP();
			Segment seg;

			double[] arr1 = new double[coords.length];
			double[] arr2 = new double[coords.length];

			for (int j = 1; j < coords.length; j++) {
				seg = new Segment(coords[j - 1], coords[j]);
				arr1[j - 1] = (start.getX(0) - coords[j - 1].getX(0)) * (coords[j].getX(1) - coords[j - 1].getX(1))
					- (start.getX(1) - coords[j - 1].getX(1)) * (coords[j].getX(0) - coords[j - 1].getX(0));
				arr2[j - 1] = (finish.getX(0) - coords[j - 1].getX(0)) * (coords[j].getX(1) - coords[j - 1].getX(1))
					- (finish.getX(1) - coords[j - 1].getX(1)) * (coords[j].getX(0) - coords[j - 1].getX(0));
				if (seg.cross(this))
					return true;
			}
			seg = new Segment(coords[coords.length - 1], coords[0]);
			arr1[coords.length - 1] = (start.getX(0) - coords[coords.length - 1].getX(0)) * (coords[0].getX(1) - coords[coords.length - 1].getX(1))
				- (start.getX(1) - coords[coords.length - 1].getX(1)) * (coords[0].getX(0) - coords[coords.length - 1].getX(0));
			arr2[coords.length - 1] = (finish.getX(0) - coords[coords.length - 1].getX(0)) * (coords[0].getX(1) - coords[coords.length - 1].getX(1))
				- (finish.getX(1) - coords[coords.length - 1].getX(1)) * (coords[0].getX(0) - coords[coords.length - 1].getX(0));

			boolean checker = true;

			for (int k = 1; k < coords.length; k++) {
				if (arr1[0] * arr1[k] < 0) {
					checker = false;
					break;


				}

				if (arr2[0] * arr2[k] < 0) {
					checker = false;
					break;
				}
			}

			return (checker | seg.cross(this));
		} else {
			// this is for Circle

			Point2D c = ((Circle) i).getP();
			double r = ((Circle) i).getR();

			if ((new Segment(c, start)).length() <= r || (new Segment(c, finish)).length() <= r) {
				return true;
			}
			return false;
		}
	}

	@Override
    public Document toBson() {
        Document segment = new Document("type", type)
            .append("data", toString());

        return segment;
    }

	@Override
	public String toString() {
		return String.format("Segment(start=%s, finish=%s)", start.toString(), finish.toString());
	}
}

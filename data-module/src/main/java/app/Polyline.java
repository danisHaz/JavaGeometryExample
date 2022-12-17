package app;

import org.bson.Document;

public class Polyline extends OpenFigure {
	private int n;
	private Point2D[] p;
	private String type;

	@Override
	public String getType() { return type; }

	public Polyline(Point2D[] p) throws Exception {
		this.p = new Point2D[p.length];
		for (int i = 0; i < p.length; i++) {
			this.p[i] = new Point2D(p[i].getX(0), p[i].getX(1));
		}
		this.n = p.length;
		type = "Polyline";
	}

	public int getN() {
		return n;
	}

	public Point2D[] getP() {
		return p;
	}

	public Point2D getP(int pos) throws Exception {
		if (pos < 0 || pos >= n)
			throw new Exception("Polyline: Position is not valid\n");

		return p[pos];
	}

	public void setP(Point2D[] arr) throws Exception {
		if (n != arr.length)
			throw new Exception("Polyline: Array is not valid\n");

		p = arr;
	}

	public void setP(Point2D p, int pos) throws Exception {
		if (pos < 0 || pos >= n)
			throw new Exception("Polyline: Position to set is not valid\n");

		this.p[pos] = p;
	}

	@Override
	public double length() throws Exception {
		double res = 0;
		for (int i = 1; i < n; i++) {
			res += (new Segment(p[i - 1], p[i])).length();
		}
		return res;
	}

	@Override
	public Polyline shift(Point2D a) throws Exception {
		for (int i = 0; i < n; i++) {
			p[i].add(a);
		}

		return this;
	}

	@Override
	public Polyline rot(double phi) throws Exception {
		for (int i = 0; i < n; i++) {
			p[i].rot(phi);
		}

		return this;
	}

	@Override
	public Polyline symAxis(int i) throws Exception {
		for (int j = 0; j < n; j++) {
			p[j].symAxis(i);
		}

		return this;
	}

	@Override
	public boolean cross(IShape shape) throws Exception {
		for (int i = 1; i < n; i++) {
			Segment seg = new Segment(p[i - 1], p[i]);
			if (seg.cross(shape))
				return true;
		}

		return false;
	}

	@Override
	public Document toBson() {
		return new Document("type", type)
			.append("data", toString());
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(String.format("Polyline(n=%d, p=(", n));
		String comma = ", ";
		for (int i = 0; i < n; i++) {
			if (i == n - 1)
				comma = "";
			str.append(p[i].toString()).append(comma);
		}
		str.append("))");
		return str.toString();
	}
}

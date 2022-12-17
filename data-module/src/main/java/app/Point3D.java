package app;

public class Point3D extends Point {
	public Point3D() throws Exception {
		super(3);
	}

	public Point3D(double[] x) throws Exception {
		super(3, x);
	}

	public static Point3D cross_prod(Point3D p1, Point3D p2) throws Exception {
		Point3D res = new Point3D();
		res.setX(0, p1.getX(1) * p2.getX(2) - p1.getX(2) * p2.getX(1));
		res.setX(1, p1.getX(0) * p2.getX(2) - p1.getX(2) * p2.getX(0));
		res.setX(2, p1.getX(0) * p2.getX(1) - p1.getX(1) * p2.getX(0));
		return res;
	}

	public Point3D cross_prod(Point3D p2) throws Exception {
		this.setX(0, this.getX(1) * p2.getX(2) - this.getX(2) * p2.getX(1));
		this.setX(1, this.getX(0) * p2.getX(2) - this.getX(2) * p2.getX(0));
		this.setX(2, this.getX(0) * p2.getX(1) - this.getX(1) * p2.getX(0));
		return new Point3D(x);
	}

	public static double mix_prod(Point3D p1, Point3D p2, Point3D p3) throws Exception {
		return p1.getX(0) * p2.getX(1) * p3.getX(2)
			+ p1.getX(1) * p2.getX(2) * p3.getX(0)
			+ p1.getX(2) * p2.getX(0) * p3.getX(1)
			- p1.getX(2) * p2.getX(1) * p3.getX(0)
			- p1.getX(0) * p2.getX(2) * p3.getX(1)
			- p1.getX(1) * p2.getX(0) * p3.getX(2);
	}

	public double mix_prod(Point3D p2, Point3D p3) throws Exception {
		return x[0] * p2.getX(1) * p3.getX(2)
			+ x[1] * p2.getX(2) * p3.getX(0)
			+ x[2] * p2.getX(0) * p3.getX(1)
			- x[2] * p2.getX(1) * p3.getX(0)
			- x[0] * p2.getX(2) * p3.getX(1)
			- x[1] * p2.getX(0) * p3.getX(2);
	}
}

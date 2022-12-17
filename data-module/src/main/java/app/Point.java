package app;

import org.bson.Document;

public class Point {
    protected int dim;
    protected double[] x;
    protected String type = "Point";

    public int getDim() {
        return dim;
    }

    public double[] getX() {
        return x;
    }

    public double getX(int pos) throws Exception {
        if (pos >= dim || pos < 0) {
            throw new Exception("Point: Position is not valid\n");
        }

        return x[pos];
    }

    public void setX(double[] newX) throws Exception {
        if (newX.length != dim) {
            throw new Exception("Point: New coordinates have incorrect dimension\n");
        }

        x = newX;
    }

    public void setX(int pos, double newX) throws Exception {
        if (pos < 0 || pos >= dim) {
            throw new Exception("Point: Position to update is not valid\n");
        }

        x[pos] = newX;
    }

    public Point(int dim) throws Exception {
        if (dim < 0) {
            throw new Exception("Point: Dimension cannot be negative\n");
        }

        this.dim = dim;
        this.x = new double[dim];
        for (int i = 0; i < dim; i++)
            x[i] = 0.0;
    }

    public Point(int dim, double[] arr) throws Exception {
        if (dim < 0) {
            throw new Exception("Point: Dimension cannot be negative\n");
        }

        if (arr.length != dim) {
            throw new Exception("Point: Number of coordinates doesn't equal to dimension\n");
        }

        this.dim = dim;
        this.x = arr;
    }

    public double abs() {
        double rast = 0;
        for (double c : x) {
            rast += c * c;
        }

        return Math.sqrt(rast);
    }

    public static Point add(Point a, Point b) throws Exception {
        if (a.getDim() != b.getDim()) {
            throw new Exception("Point: Cannot add two points\n");
        }

        Point res = new Point(a.getDim());
        for (int i = 0; i < res.getDim(); i++) {
            res.setX(i, a.getX(i) + b.getX(i));
        }

        return res;
    }

    public void add(Point b) throws Exception {
        if (dim != b.getDim()) {
            throw new Exception("Point: Cannot add a point\n");
        }

        for (int i = 0; i < dim; i++) {
            x[i] += b.getX(i);
        }
    }

    public static Point sub(Point a, Point b) throws Exception {
        if (a.getDim() != b.getDim()) {
            throw new Exception("Point: Cannot sub two points\n");
        }

        Point res = new Point(a.getDim());
        for (int i = 0; i < res.getDim(); i++) {
            res.setX(i, a.getX(i) - b.getX(i));
        }

        return res;
    }

    public void sub(Point b) throws Exception {
        if (dim != b.getDim()) {
            throw new Exception("Point: Cannot sub a point\n");
        }

        for (int i = 0; i < dim; i++) {
            x[i] -= b.getX(i);
        }
    }

    public static double mult(Point a, Point b) throws Exception {
        if (a.getDim() != b.getDim()) {
            throw new Exception("Point: Cannot multi two points\n");
        }

        double res = 0.0;
        for (int i = 0; i < a.getDim(); i++) {
            res += a.getX(i) * b.getX(i);
        }

        return res;
    }

    public static Point mult(Point a, double r) throws Exception {
        Point res = new Point(a.getDim());
        for (int i = 0; i < a.getDim(); i++) {
            res.setX(i, a.getX(i) * r);
        }

        return res;
    }

    public double multi(Point a) throws Exception {
        if (a.getDim() != dim) {
            throw new Exception("Point: Cannot multi to a point\n");
        }

        double res = 0.0;
        for (int i = 0; i < dim; i++) {
            res += a.getX(i) * x[i];
        }

        return res;
    }

    public Point multi(double r) {
        for (int i = 0; i < dim; i++) {
            x[i] *= r;
        }

        return this;
    }

    public static Point symAxis(Point a, int pos) throws Exception {
        if (a.getDim() <= pos || pos < 0) {
            throw new Exception("Point: Cannot symAxis point\n");
        }

        Point res = new Point(a.getDim());
        for (int i = 0; i < a.getDim(); i++) {
            if (i == pos) {
                res.setX(i, a.getX(i));
                continue;
            }
            res.setX(i, -a.getX(i));
        }

        return res;
    }

    public Point symAxis(int pos) throws Exception {
        if (pos < 0 || pos >= dim) {
            throw new Exception("Point: Cannot symAxis to a point");
        }

        for (int i = 0; i < dim; i++) {
            if (i == pos) {
                continue;
            }
            x[i] *= (-1);
        }

        return this;
    }

    public Document toBson() {
        return new Document("type", type)
                .append("data", toString());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(String.format("Point(dims=%d, x=(", dim));
        String comma = ", ";
        for (int i = 0; i < x.length; i++) {
            if (i == x.length - 1)
                comma = "";
            str.append(x[i]).append(comma);
        }

        str.append("))");

        return str.toString();
    }

}

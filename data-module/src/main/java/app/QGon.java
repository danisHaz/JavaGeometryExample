package app;

import org.bson.Document;

public class QGon extends NGon {
    public QGon(Point2D[] p) throws Exception {
        super(p);
        type = "QGon";
        if (n != 4)
            throw new Exception("QGon: Array size is not correct\n");
    }

    @Override
    public double square() throws Exception {
        return super.square();
    }

    @Override
    public Document toBson() {
        return new Document("type", type)
            .append("data", toString());
    }

    @Override
    public String toString() {
        return type + super.toString().substring(4);
    }
}

package app;

public interface IMoveable {
    IMoveable shift(Point2D a) throws Exception;
    IMoveable rot(double phi) throws Exception;
    IMoveable symAxis(int i) throws Exception;
}

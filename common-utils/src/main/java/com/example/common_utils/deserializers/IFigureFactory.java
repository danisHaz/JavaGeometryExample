package com.example.common_utils.deserializers;

import java.util.HashMap;

import app.Circle;
import app.IShape;
import app.NGon;
import app.Point2D;
import app.Polyline;
import app.QGon;
import app.Rectangle;
import app.Segment;
import app.TGon;
import app.Trapeze;

public class IFigureFactory {

    public static IShape create(String buildString) {
        IShape shape = null;
        try {
            ConstructorHolder holder = ConstructorHolder.parseFromString(buildString);
            shape = createByName(holder.getName(), holder.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shape;
    }

    private static Point2D createPoint(HashMap<String, String[]> args) throws Exception {
        Point2D point = null;

        if (args.containsKey("dims") && args.containsKey("x")) {
            Integer dims = Integer.parseInt(args.get("dims")[0]);
            String[] coords = args.get("x");

            if (coords.length != dims || dims != 2) {
                throw new Exception("Dims not valid");
            }

            double[] intCoords = new double[dims];
            for (int i = 0; i < coords.length; i++) {
                intCoords[i] = (double)Double.parseDouble(coords[i]);
            }

            point = new Point2D(intCoords);
        }

        return point;
    }

    private interface Creator<T> {
        T create(Point2D[] points) throws Exception;
    }

    private static <T> T createNShape(HashMap<String, String[]> args, Creator<T> creator) throws Exception {
        T ngon = null;

        if (args.containsKey("n") && args.containsKey("p")) {
            Integer n = Integer.parseInt(args.get("n")[0]);
            String[] coords = args.get("p");

            if (coords.length != n) {
                throw new Exception("NGon: n not valid");
            }

            Point2D[] points = new Point2D[n];
            for (int i = 0; i < coords.length; i++) {
                ConstructorHolder holder = ConstructorHolder.parseFromString(coords[i]);
                points[i] = createPoint(holder.getArgs());
            }

            ngon = creator.create(points);
        }

        return ngon;
    }

    private static Circle createCircle(HashMap<String, String[]> args) throws Exception {
        Circle circle = null;

        if (args.containsKey("p") && args.containsKey("r")) {
            Double r = Double.parseDouble(args.get("r")[0]);
            String center = args.get("p")[0];
            Point2D point = null;

            ConstructorHolder holder = ConstructorHolder.parseFromString(center);
            point = createPoint(holder.getArgs());
            circle = new Circle(point, r);
        }

        return circle;
    }

    private static Segment createSegment(HashMap<String, String[]> args) throws Exception {
        Segment segment = null;

        if (args.containsKey("start") && args.containsKey("finish")) {
            String start = args.get("start")[0];
            String finish = args.get("finish")[0];

            Point2D startP = null;
            Point2D finishP = null;
            ConstructorHolder startHolder = ConstructorHolder.parseFromString(start);
            ConstructorHolder finishHolder = ConstructorHolder.parseFromString(finish);

            startP = createPoint(startHolder.getArgs());
            finishP = createPoint(finishHolder.getArgs());

            segment = new Segment(startP, finishP);
        }

        return segment;
    }

    private static IShape createByName(String name, HashMap<String, String[]> args) throws Exception {
        switch(name){
            case "NGon":
                return createNShape(args, (p) -> new NGon(p));
            case "Polyline":
                return createNShape(args, (p) -> new Polyline(p));
            case "QGon":
                return createNShape(args, (p) -> new QGon(p));
            case "Rectangle":
                return createNShape(args, (p) -> new Rectangle(p));
            case "Segment":
                return createSegment(args);
            case "TGon":
                return createNShape(args, (p) -> new TGon(p));
            case "Trapeze":
                return createNShape(args, (p) -> new Trapeze(p));
            case "Circle":
                return createCircle(args);
            default:
                return null;
        }
    };

}

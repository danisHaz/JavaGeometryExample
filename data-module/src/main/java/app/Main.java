package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Main {

    // список фигур
    static ArrayList<IShape> figures = new ArrayList<IShape>();
    // список дополнительных фигур для проверки на пересечение
    static ArrayList<IShape> crossFigures = new ArrayList<IShape>();
    // список названий фигур
    static ArrayList<String> figureNames = new ArrayList<String>();
    // для чтения инпута из консоли
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    // количество фигур
    static int n;
    // список возможных фигур
    static HashSet<String> figureTypes = new HashSet<String>() {{
        add("circle");
        add("ngon");
        add("polyline");
        add("qgon");
        add("rectangle");
        add("segment");
        add("tgon");
        add("trapeze");
    }};
    // список возможных движений
    static HashSet<String> figureMovements = new HashSet<String>() {{
        add("rotate");
        add("shift");
        add("symmetry");
    }};

    // короткий вывод
    static void print(String str) {
        System.out.print(str);
    }

    // короткий вывод для фигуры
    static void print(IShape figure) {
        System.out.println(figure.toString());
    }

    // короткое чтение строки
    static String read() throws IOException {
        return reader.readLine();
    }

    // выходим из программы, если критическая ошибка при чтении
    static void returnOnIOEXception() {
        print("Something went wrong");
        System.exit(1);
    }

    // обработать движение и применить к фигуре
    static IShape readMovementAndApplyToFigure(String movementType, IShape figure) throws Exception {
        print("\n");
        switch (movementType) {
            case "rotate":
                return applyRotateToFigure(figure);
            case "symmetry":
                return applySymmetryToFigure(figure);
            case "shift":
                return applyShiftToFigure(figure);
        }

        throw new Exception("Bad data");
    }

    static IShape applyRotateToFigure(IShape figure) throws Exception {
        print("Enter rotation angle (in radians):\n");

        double phi = Double.parseDouble(read());
        return figure.rot(phi);
    }

    static IShape applySymmetryToFigure(IShape figure) throws Exception {
        print("Enter number of axis of symmetry:\n");

        int axis = Integer.parseInt(read());
        return figure.symAxis(axis);
    }

    static IShape applyShiftToFigure(IShape figure) throws Exception {
        print("Enter vector:\n");

        Point2D point = readPoint();
        return figure.shift(point);
    }

    // обработать фигуру и ввести ее
    static IShape readFigure(String figureType) throws Exception {
        print("\n");
        switch (figureType) {
            case "circle":
                return readCircle();
            case "ngon":
                return readNGon();
            case "polyline":
                return readPolyline();
            case "qgon":
                return readQGon();
            case "rectangle":
                return readRectangle();
            case "segment":
                return readSegment();
            case "tgon":
                return readTGon();                
            case "trapeze":
                return readTrapeze();
        }
        
        throw new IOException("There is no such type");
    }

    // короткая запись ввода точки
    static Point2D readPoint() throws Exception {
        String[] point = read().split(" ");
        if (point.length != 2) {
            throw new IOException("Wrong input");
        }

        return new Point2D(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
    }

    // короткое чтение нескольких точек
    static Point2D[] readManyPoints(int n) throws Exception {
        print(String.format("Enter %d points:\n", n));
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < n; i++) {
            points[i] = readPoint();
        }

        return points;
    }

    static Circle readCircle() throws Exception {
        print("Enter center of circle:\n");
        
        Point2D center = readPoint();

        print("Enter radius:\n");
        double r = Double.parseDouble(read());

        return new Circle(center, r);
    }

    static NGon readNGon() throws Exception {
        print("Enter count of points:\n");
        int n = Integer.parseInt(read());

        return new NGon(readManyPoints(n));
    }

    static Polyline readPolyline() throws Exception {
        print("Enter count of points:\n");
        int n = Integer.parseInt(read());

        return new Polyline(readManyPoints(n));
    }

    static QGon readQGon() throws Exception {
        int n = 4;
        return new QGon(readManyPoints(n));
    }

    static Rectangle readRectangle() throws Exception {
        int n = 4;
        return new Rectangle(readManyPoints(n));
    }

    static Segment readSegment() throws Exception {
        int n = 2;
        Point2D[] points = readManyPoints(n);
        return new Segment(points[0], points[1]);
    }

    static TGon readTGon() throws Exception {
        int n = 3;
        return new TGon(readManyPoints(n));
    }

    static Trapeze readTrapeze() throws Exception {
        int n = 4;
        return new Trapeze(readManyPoints(n));
    }

    // вводим фигуры и обрабатываем запрос (3 и 4 пункты)
    static void readFigures(int n) {
        for (int i = 0; i < n; i++) {
            print("Enter type of possible figure:\n");
            String figureType = "";
            try {
                figureType = read().toLowerCase();
            } catch (IOException e) {
                print(e.getMessage() + "\n");
                i--;
                continue;
            }

            if (!figureTypes.contains(figureType)) {
                print("Input is wrong. Try again:\n");
                i--;
                continue;
            }

            IShape figure;
            try {
                figure = readFigure(figureType);
            } catch (Exception e){
                print(e.getMessage() + "\n");
                i--;
                continue;
            }

            figures.add(figure);
            figureNames.add(figureType);
        }
    }

    // 5 пункт
    static void checkNewFigures(int n) {
        for (int i = 0; i < n; i++) {
            try {
                IShape figure = readFigure(figureNames.get(i));
                if (figure.cross(figures.get(i))) {
                    print("Cross\n");
                } else {
                    print("No Cross\n");
                }

                print("Enter possible movement:\n");
                String movementType = "";
                try {
                    movementType = read().toLowerCase();
                } catch (IOException e) {
                    print(e.getMessage() + "\n");
                    i--;
                    continue;
                }

                if (!figureMovements.contains(movementType)) {
                    print("Input is wrong:\n");
                    i--;
                    continue;
                }

                readMovementAndApplyToFigure(movementType, figure);
                if (figure.cross(figures.get(i))) {
                    print("Cross\n");
                } else {
                    print("No Cross\n");
                }

            } catch (Exception e){
                print(e.getMessage() + "\n");
                i--;
                continue;
            }
        }
    }

    public static void main(String[] args) {
        print("Enter number of figure:\n");
        
        try {
            n = Integer.parseInt(read());
        } catch (IOException e) {
            returnOnIOEXception();
        }

        print("You can input one figure of following:\n");
        for (String figureType: figureTypes) {
            print(figureType + "\n");
        }

        readFigures(n);

        double square = 0.;
        double length = 0.;
        for (IShape figure: figures) {
            try {
                square += figure.square();
            } catch (Exception e) {
                print(e.getMessage() + "\n");
                continue;
            }

            try {
                length += figure.length();
            } catch (Exception e) {
                print(e.getMessage() + "\n");
                continue;
            }
        }

        print(String.format("Square: %.2f", square));
        print(String.format("Length: %.2f", length));
        print(String.format("Average square: %.2f", square / n));

        checkNewFigures(n);
    }
}
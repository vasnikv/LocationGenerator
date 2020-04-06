package my.task;

import java.sql.Timestamp;
import java.util.Random;

public class CoordGenerator {

    static final Double XMIN = 53.1649;
    static final Double XMAX = 53.3219;
    static final Double YMIN = 56.8187;
    static final Double YMAX = 56.8814;

    public CoordGenerator() {

    }

    public Coord getByLastCoord(Coord coord){

        coord.setX(setInRange(XMIN, XMAX, coord.getX()));
        coord.setY(setInRange(YMIN, YMAX, coord.getY()));
        coord.setTime(new Timestamp(System.currentTimeMillis()));
        coord.setPrevious(coord.getId());
        coord.setId(null);
        return coord;
    }

    private double setInRange(Double minVal, Double maxVal, Double prevVal){
        Random r = new Random();
        double val;
        if (prevVal == null  || (prevVal < minVal || prevVal > maxVal)){
            prevVal = r.nextDouble() * (maxVal - minVal) + minVal;
        }
        val = r.nextDouble() * (r.nextBoolean() ? -1.0 : 1.0) / 200.0;
        return ((prevVal + val) < minVal || (prevVal + val) > maxVal) ? prevVal - val : prevVal + val;
    }
}
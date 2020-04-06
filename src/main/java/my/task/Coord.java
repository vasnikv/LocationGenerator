package my.task;

import java.sql.Timestamp;

public class Coord {

    private Integer id;
    private double x;
    private double y;

    private Timestamp time;
    private Integer previous;
    private Integer vehicleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Integer getPrevious() {
        return previous;
    }

    public void setPrevious(Integer previous) {
        this.previous = previous;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }


}
package my.task;

//import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private static final String DB_URL = "jdbc:postgresql://drona.db.elephantsql.com:5432/kirsciwz";//"jdbc:postgresql://127.0.0.1:5432/postgres";

    private static final String USER = "kirsciwz";

    private static final String PASS = "9-l6CRJaATRDvJs7bdrXbu3y2Tt4Zlwv";

    static final String CREATE =
            " drop table vehicles cascade; drop table coords cascade; drop sequence serial cascade;\n " +
            "CREATE SEQUENCE IF NOT EXISTS serial START 1; " +
            "CREATE TABLE IF NOT EXISTS vehicles\n" +
            "(\n" +
            "    id integer NOT NULL DEFAULT nextval('serial'),\n" +
            "    driver_id integer,\n" +
            "    color character varying(20),\n" +
            "    model character varying(50),\n" +
            "    reg_number character varying(20),\n" +
            "    active boolean\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS coords\n" +
            "(\n" +
            "    id integer NOT NULL DEFAULT nextval('serial'),\n" +
            "    x numeric(10,8) NOT NULL,\n" +
            "    y numeric(10,8) NOT NULL,\n" +
            "    datetime timestamp with time zone NOT NULL,\n" +
            "    previous integer,\n" +
            "    vehicle_id integer,\n" +
            "    CONSTRAINT coord_pkey PRIMARY KEY (id)\n" +
            ")\n" ;


    String INITFILL = "INSERT INTO vehicles(driver_id, color, model, reg_number, active) VALUES(11, 'красный', 'RIO', 'в123ке18', true);"
            + "INSERT INTO vehicles(driver_id, color, model, reg_number, active)  VALUES(22, 'белый', 'POLO', 'а234рп18', true);"
            + "INSERT INTO vehicles(driver_id, color, model, reg_number, active) VALUES(33, 'черный', 'LOGAN', 'п345ое18', true);"
            + "INSERT INTO vehicles(driver_id, color, model, reg_number, active) VALUES(44, 'зеленый', 'VESTA', 'к456ув18', false);";

    static final String GETACTIVEVEHICLES = "select id from vehicles where active = true";

    static final String GETLASTCOORDBYVEHICLE = "select id, x, y, previous, vehicle_id from coords where vehicle_id = ? order by datetime desc fetch first 1 rows only";

    static final String INSERTCOORD = "INSERT INTO coords(x, y, datetime, previous, vehicle_id) VALUES(?, ?, ?, ?, ?);";

    Connection connection = null;

    public void connect(){
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }

    }

    public boolean createTables(){
        System.out.println("createTables");
        try {
            System.out.println(CREATE);
            int i = connection.prepareStatement(CREATE).executeUpdate();
            System.out.println("createTables OK");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("createTables ERROR");
            return false;
        }
    }

    public boolean fillTables(){
        try {
            System.out.println(INITFILL);
            int i = connection.prepareStatement(INITFILL).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<Integer> getActiveVehicles() {
        List<Integer> vehicleIds = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(GETACTIVEVEHICLES);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                vehicleIds.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleIds;
    }

    public Coord getLastCoordByVehicle(int vehicleId) {
        Coord coord = new Coord();
        try {
            PreparedStatement stmt = connection.prepareStatement(GETLASTCOORDBYVEHICLE);
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                coord.setId(rs.getInt(1));
                coord.setX(rs.getDouble(2));
                coord.setY(rs.getDouble(3));
                coord.setPrevious(rs.getInt(4));
                coord.setVehicleId(rs.getInt(5));
            } else {
                coord.setVehicleId(vehicleId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coord;
    }

    public boolean insertCoord( Coord coord){
        try {
            System.out.println(String.valueOf(coord.getX()) + " "
                    + String.valueOf(coord.getY()) + " "
                    + String.valueOf(coord.getTime()) + " "
                    + String.valueOf(coord.getPrevious()) + " "
                    + String.valueOf(coord.getVehicleId()));
            PreparedStatement stmt = connection.prepareStatement(INSERTCOORD);
            stmt.setDouble(1, coord.getX());
            stmt.setDouble(2, coord.getY());
            stmt.setTimestamp(3, coord.getTime());
            if (coord.getPrevious() != null) {
                stmt.setInt(4, coord.getPrevious());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, coord.getVehicleId());
            int i = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
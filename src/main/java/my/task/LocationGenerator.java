package my.task;

import java.util.List;

public class LocationGenerator {

    private static Integer TIMEOUT = 10;

    public static void main(String[] args){
        
        Repository rep = new Repository();
        CoordGenerator coordGenerator = new CoordGenerator();
        rep.connect();

        //удаление и создание таблиц с исходными данными
        rep.createTables();
        rep.fillTables();

        //получение списка vehicles
        List<Integer> vehicleIds = rep.getActiveVehicles();
        Integer counter = 0;
        while (true) {
            System.out.println("= "+ String.valueOf(counter++)+" =");
            vehicleIds.forEach(id -> {
                //  найти маршрут
                Coord coord = rep.getLastCoordByVehicle(id);
                //  исходя из последней точки сгенерить точку новую
                coord = coordGenerator.getByLastCoord(coord);
                //  сохранить точку в бд insertCoord
                rep.insertCoord(coord);
            });
            try {
                Thread.sleep(TIMEOUT *1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
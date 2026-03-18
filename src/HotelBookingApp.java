import java.util.*;

class Service{
    private String serviceName;
    private double cost;

    Service(String serviceName,double cost){
        this.serviceName = serviceName;
        this.cost = cost;
    }
    public String getServiceName(){
        return serviceName;
    }
    public double getCost(){
        return cost;
    }
}

class AddOnServiceManager{
    private Map<String, List<Service>> servicesByReservation;

    AddOnServiceManager(){
        servicesByReservation = new HashMap<>();
    }
    public void addService(String reservationId, Service service){
        servicesByReservation.computeIfAbsent(reservationId,k->new ArrayList<>()).add(service);
    }
    public double calculateTotalServiceCost(String reservationId){
        double sum = 0;
        List<Service> service = servicesByReservation.get(reservationId);
        for(Service s : service){
            sum += s.getCost();
        }
        return sum;
    }
}
public class HotelBookingApp{
    public static void main(String[] args){
        System.out.println("Add-On Service Selection");

        AddOnServiceManager reservation = new AddOnServiceManager();
        reservation.addService("Single-1",new Service("Food",1000));
        reservation.addService("Single-1",new Service("Spa",500));

        System.out.println("Reservation ID: Single-1");
        System.out.println("Total Add-On Cost: " +reservation.calculateTotalServiceCost("Single-1"));
    }
}
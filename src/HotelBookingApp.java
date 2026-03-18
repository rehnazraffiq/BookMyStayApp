import java.util.*;

class Reservation{
    private String guestName;
    private String roomType;

    Reservation(String guestName,String roomType){
        this.guestName = guestName;
        this.roomType = roomType;
    }
    public String getGuestName(){
        return guestName;
    }
    public String getRoomType(){
        return roomType;
    }
}
class BookingHistory{
    private List<Reservation> confirmedReservations;

    BookingHistory(){
        confirmedReservations = new ArrayList<>();
    }
    public void addReservation(Reservation reservation){
        confirmedReservations.add(reservation);
    }
    public List<Reservation> getConfirmedReservations(){
        return confirmedReservations;
    }
}
class BookingReportService{
    public void generateReport(BookingHistory history){
        System.out.println("Booking History Report");

        List<Reservation> list = history.getConfirmedReservations();
        for(Reservation r : list){
            System.out.println("Guest: " +r.getGuestName()+ " ,RoomType: " +r.getRoomType());
        }
    }
}
public class HotelBookingApp{
    public static void main(String[] args){
        System.out.println("Booking History and Reporting");
        System.out.println();
        BookingHistory history = new BookingHistory();
        BookingReportService service = new BookingReportService();

        Reservation r1 = new Reservation("Abhi","Single");
        Reservation r2 = new Reservation("Subha","Double");
        Reservation r3 = new Reservation("Vanmathi","Suite");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        service.generateReport(history);
    }
}
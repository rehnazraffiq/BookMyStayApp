import java.util.*;
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single Room", 6);
    }

    public void addRoom(String type) {
        roomAvailability.put(type, roomAvailability.getOrDefault(type, 0) + 1);
    }

    public void showRooms() {
        System.out.println("Updated Single Room Availablitiy: " + roomAvailability.get("Single Room"));
    }
}
class CancellationService{
    private Stack<String> releaseRoomIDs;
    private Map<String, String> reservationRoomTypeMap;

    CancellationService(){
        releaseRoomIDs = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }
    public void registerBooking(String reservationId, String roomType){
        reservationRoomTypeMap.put(reservationId,roomType);
    }
    public void cancelBooking(String reservationId, RoomInventory inventory){
        if(!reservationRoomTypeMap.containsKey(reservationId)){
            System.out.println("Invalid reservation Id");
            return;
        }
        String roomType = reservationRoomTypeMap.remove(reservationId);
        inventory.addRoom(roomType);
        releaseRoomIDs.push(reservationId);
        System.out.println("Booking cancelled successfully.Inventory restored for room type: " +roomType);
    }
    public void showRollBackHistory(){
        System.out.print("Released Room IDs: ");
        while(!releaseRoomIDs.isEmpty()){
            System.out.println(releaseRoomIDs.pop());
        }
    }
}
public class HotelBookingApp{
    public static void main(String[] args){
        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        service.registerBooking("Single-1","Single");

        System.out.println("Booking cancellation");
        service.cancelBooking("Single-1",inventory);

        System.out.println("\nRollBack History (Most Recent First): ");
        service.showRollBackHistory();
        System.out.println();

        inventory.showRooms();
    }
}
import java.util.*;
class BookingRequestQueue {
    private final Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation reservation) {
        queue.offer(reservation);
        notifyAll();
    }

    public synchronized Reservation getRequest() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }
}
class Reservation {
    private final String guestName;
    private final String roomType;

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
class RoomInventory {
    private final Map<String, Integer> availability;
    private final Map<String, Integer> roomCounter;

    public RoomInventory() {
        availability = new HashMap<>();
        roomCounter = new HashMap<>();

        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);

        roomCounter.put("Single", 1);
        roomCounter.put("Double", 1);
        roomCounter.put("Suite", 1);
    }

    public synchronized void allocateRoom(String roomType, String guestName) {
        Integer available = availability.get(roomType);

        if (available != null && available > 0) {
            availability.put(roomType, available - 1);

            int roomNum = roomCounter.get(roomType);
            roomCounter.put(roomType, roomNum + 1);
            String roomId = roomType + "-" + roomNum;

            System.out.println("Booking confirmed for Guest: " + guestName +
                    ", Room ID: " + roomId);
        }
    }

    public void displayInventory() {
        System.out.println("\nRemaining Inventory:");
        System.out.println("Single: " + availability.get("Single"));
        System.out.println("Double: " + availability.get("Double"));
        System.out.println("Suite: " + availability.get("Suite"));
    }
}
class RoomAllocationService {

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        inventory.allocateRoom(reservation.getRoomType(), reservation.getGuestName());
    }
}
class ConcurrentBookingProcessor implements Runnable {
    private final BookingRequestQueue bookingQueue;
    private final RoomInventory inventory;
    private final RoomAllocationService allocationService;

    ConcurrentBookingProcessor(BookingRequestQueue bookingQueue, RoomInventory inventory, RoomAllocationService allocationService){
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }
    @Override
    public void run() {
        while (true){
            Reservation reservation;

            synchronized (bookingQueue){
                try {
                    reservation = bookingQueue.getRequest();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            synchronized (inventory){
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}
public class HotelBookingApp{
    public static void main(String[] args){
        System.out.println("Concurrent Booking Simulation\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );
        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );

        t1.start();
        t2.start();

        try {
            Thread.sleep(2000);
            t1.interrupt();
            t2.interrupt();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        inventory.displayInventory();
        }
    }

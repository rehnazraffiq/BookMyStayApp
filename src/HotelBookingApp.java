import java.io.*;
import java.util.*;
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public int getAvailableCount(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void setAvailableCount(String roomType, int count) {
        if (count >= 0) {
            inventory.put(roomType, count);
        }
    }

    public Map<String, Integer> getAllInventory() {
        return new HashMap<>(inventory);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
class FilePersistenceService {
    public void saveInventory(RoomInventory inventory, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, Integer> entry : inventory.getAllInventory().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }
    public void loadInventory(RoomInventory inventory, String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("System Recovery");
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean dataLoaded = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String roomType = parts[0].trim();
                    try {
                        int count = Integer.parseInt(parts[1].trim());
                        inventory.setAvailableCount(roomType, count);
                        dataLoaded = true;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format for " + roomType + ": " + parts[1]);
                    }
                }
            }

            if (!dataLoaded) {
                System.out.println("System Recovery");
                System.out.println("No valid inventory data found. Starting fresh.");
            }

        } catch (IOException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
            System.out.println("System Recovery");
            System.out.println("No valid inventory data found. Starting fresh.");
        }
    }
}
public class HotelBookingApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistence = new FilePersistenceService();
        String fileName = "inventory.txt";

        persistence.loadInventory(inventory, fileName);

        inventory.displayInventory();

        inventory.setAvailableCount("Single", 4);
        inventory.setAvailableCount("Double", 2);
        inventory.setAvailableCount("Suite", 1);

        persistence.saveInventory(inventory, fileName);

        inventory.displayInventory();
    }
}

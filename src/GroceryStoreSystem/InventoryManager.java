package GroceryStoreSystem;

import java.util.ArrayList;
import java.io.*;

public class InventoryManager {

    private ArrayList<Product> inventory;

    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    // Load inventory
    public void loadFromFile(String filename) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                double price = Double.parseDouble(data[2]);
                int stock = Integer.parseInt(data[3]);

                inventory.add(new Product(id, name, price, stock));
            }

            br.close();

        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    // Save inventory
    public void saveToFile(String filename) {

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename));

            for (Product p : inventory) {
                pw.println(p.getId() + "," +
                        p.getName() + "," +
                        p.getPrice() + "," +
                        p.getStock());
            }

            pw.close();

            System.out.println("Inventory saved.");

        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    // Display all
    public void displayAll() {

        System.out.println("\n===== INVENTORY =====");

        System.out.printf("%-5s %-15s %-10s %-5s\n",
                "ID", "Name", "Price", "Stock");

        for (Product p : inventory) {
            System.out.println(p);
        }
    }

    // Search by ID
    public Product searchById(int id) {

        for (Product p : inventory) {
            if (p.getId() == id) {
                return p;
            }
        }

        return null;
    }

    // Search by name
    public ArrayList<Product> searchByName(String name) {

        ArrayList<Product> result = new ArrayList<>();

        for (Product p : inventory) {

            if (p.getName().toLowerCase()
                    .contains(name.toLowerCase())) {

                result.add(p);
            }
        }

        return result;
    }

    // Add product
    public boolean addProduct(Product p) {

        if (searchById(p.getId()) != null) {
            return false;
        }

        inventory.add(p);
        return true;
    }

    // Remove product
    public boolean removeProduct(int id) {

        Product p = searchById(id);

        if (p != null) {
            inventory.remove(p);
            return true;
        }

        return false;
    }

    // Update stock
    public boolean updateStock(int id, int newStock) {

        Product p = searchById(id);

        if (p != null) {
            p.setStock(newStock);
            return true;
        }

        return false;
    }

    // Availability
    public boolean isAvailable(int id, int requestedQty) {

        Product p = searchById(id);

        return p != null && p.getStock() >= requestedQty;
    }

    public Product getProductById(int id) {
        return searchById(id);
    }
}
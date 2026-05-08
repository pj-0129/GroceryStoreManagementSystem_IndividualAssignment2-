package GroceryStoreSystem;
import java.util.ArrayList;
import java.util.Scanner;

public class GroceryStoreSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        InventoryManager inventory = new InventoryManager();

        CartList cart = new CartList();

        // FIXED HERE
        LinkedListStack<CartAction> undoStack =
                new LinkedListStack<CartAction>();

        inventory.loadFromFile("inventory.txt");

        int choice;

        do {

            System.out.println("\n===== GROCERY STORE =====");

            System.out.println("1. Display Products");
            System.out.println("2. Search Product by ID");
            System.out.println("3. Search Product by Name");
            System.out.println("4. Add Product");
            System.out.println("5. Remove Product");
            System.out.println("6. Update Stock");
            System.out.println("7. Add Item to Cart");
            System.out.println("8. View Cart");
            System.out.println("9. Undo Last Add");
            System.out.println("10. Checkout");
            System.out.println("11. Save & Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    inventory.displayAll();
                    break;

                case 2:

                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();

                    Product p = inventory.searchById(id);

                    if (p != null) {
                        System.out.println(p);
                    } else {
                        System.out.println("Product not found.");
                    }

                    break;

                case 3:

                    sc.nextLine();

                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();

                    ArrayList<Product> result =
                            inventory.searchByName(name);

                    if (result.isEmpty()) {
                        System.out.println("No product found.");
                    } else {

                        for (Product pr : result) {
                            System.out.println(pr);
                        }
                    }

                    break;

                case 4:

                    System.out.print("Enter ID: ");
                    int newId = sc.nextInt();

                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String newName = sc.nextLine();

                    System.out.print("Enter Price: ");

                    if (!sc.hasNextDouble()) {

                        System.out.println(
                                "Invalid input. Price must be a number.");

                        sc.next();
                        break;
                    }

                    double price = sc.nextDouble();

                    System.out.print("Enter Stock: ");
                    int stock = sc.nextInt();

                    Product newProduct =
                            new Product(newId,
                                    newName,
                                    price,
                                    stock);

                    if (inventory.addProduct(newProduct)) {
                        System.out.println("Product added.");
                    } else {
                        System.out.println("Duplicate ID.");
                    }

                    break;

                case 5:

                    System.out.print("Enter Product ID to remove: ");
                    int removeId = sc.nextInt();

                    if (inventory.removeProduct(removeId)) {
                        System.out.println("Product removed.");
                    } else {
                        System.out.println("Product not found.");
                    }

                    break;

                case 6:

                    System.out.print("Enter Product ID: ");
                    int updateId = sc.nextInt();

                    System.out.print("Enter new stock: ");
                    int newStock = sc.nextInt();

                    if (inventory.updateStock(updateId, newStock)) {
                        System.out.println("Stock updated.");
                    } else {
                        System.out.println("Product not found.");
                    }

                    break;

                case 7:

                    System.out.print("Enter Product ID: ");
                    int pid = sc.nextInt();

                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();

                    if (inventory.isAvailable(pid, qty)) {

                        Product product =
                                inventory.getProductById(pid);

                        cart.addItem(product, qty);

                        // temporarily reduce stock
                        product.setStock(
                                product.getStock() - qty);

                        // push into undo stack
                        undoStack.push(
                                new CartAction(product, qty));

                        System.out.println("Item added to cart.");

                    } else {

                        System.out.println(
                                "Insufficient stock or invalid product.");
                    }

                    break;

                case 8:

                    cart.displayCart();
                    break;

                case 9:

                    CartAction action = undoStack.pop();

                    if (action != null) {

                        cart.removeItem(
                                action.product.getId());

                        // restore stock
                        action.product.setStock(
                                action.product.getStock()
                                        + action.quantity);

                        System.out.println("Undo successful.");

                    } else {

                        System.out.println("Nothing to undo.");
                    }

                    break;

                case 10:

                    if (cart.isEmpty()) {

                        System.out.println("Cart is empty.");

                    } else {

                        System.out.println("\n===== BILL =====");

                        cart.displayCart();

                        System.out.printf("TOTAL BILL: RM%.2f\n", cart.calculateTotal());

                        cart.clear();
                        undoStack.clear();

                        System.out.println("Checkout completed.");
                    }

                    break;

                case 11:

                    inventory.saveToFile("inventory.txt");

                    System.out.println("Inventory saved.");
                    System.out.println("Program terminated.");

                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (choice != 11);

        sc.close();
    }
}
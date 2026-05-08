package GroceryStoreSystem;

import java.util.ArrayList;
import java.util.Scanner;

public class GroceryStoreSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        InventoryManager inventory = new InventoryManager();
        CartList cart = new CartList();

        LinkedListStack<CartAction> undoStack = new LinkedListStack<CartAction>();

        inventory.loadFromFile("inventory.txt");

        int choice;

        do {
            System.out.println("\n===== GROCERY STORE =====");
            System.out.println("1.  Display Products");
            System.out.println("2.  Search Product by ID");
            System.out.println("3.  Search Product by Name");
            System.out.println("4.  Add Product");
            System.out.println("5.  Remove Product");
            System.out.println("6.  Update Stock");
            System.out.println("7.  Add Item to Cart");
            System.out.println("8.  Manage Cart");        // covers view/remove/update/clear
            System.out.println("9.  Undo Last Add");
            System.out.println("10. Checkout");
            System.out.println("11. Save & Exit");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
                choice = -1;
                continue;
            }
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    inventory.displayAll();
                    break;

                case 2:
                    System.out.print("Enter ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        sc.next(); break;
                    }
                    int id = sc.nextInt();
                    Product p = inventory.searchById(id);
                    if (p != null) System.out.println(p);
                    else System.out.println("Product not found.");
                    break;

                case 3:
                    sc.nextLine();
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();
                    ArrayList<Product> result = inventory.searchByName(name);
                    if (result.isEmpty()) System.out.println("No product found.");
                    else for (Product pr : result) System.out.println(pr);
                    break;

                case 4:
                    System.out.print("Enter ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        sc.next(); break;
                    }
                    int newId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter Price: ");
                    if (!sc.hasNextDouble()) {
                        System.out.println("Invalid input. Price must be a number.");
                        sc.next(); break;
                    }
                    double price = sc.nextDouble();
                    System.out.print("Enter Stock: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. Stock must be a number.");
                        sc.next(); break;
                    }
                    int stock = sc.nextInt();
                    Product newProduct = new Product(newId, newName, price, stock);
                    if (inventory.addProduct(newProduct)) System.out.println("Product added.");
                    else System.out.println("Duplicate ID.");
                    break;

                case 5:
                    System.out.print("Enter Product ID to remove: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        sc.next(); break;
                    }
                    int removeId = sc.nextInt();
                    if (inventory.removeProduct(removeId)) System.out.println("Product removed.");
                    else System.out.println("Product not found.");
                    break;

                case 6:
                    System.out.print("Enter Product ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        sc.next(); break;
                    }
                    int updateId = sc.nextInt();
                    System.out.print("Enter new stock: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. Stock must be a number.");
                        sc.next(); break;
                    }
                    int newStock = sc.nextInt();
                    if (inventory.updateStock(updateId, newStock)) System.out.println("Stock updated.");
                    else System.out.println("Product not found.");
                    break;

                case 7:
                    System.out.print("Enter Product ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        sc.next(); break;
                    }
                    int pid = sc.nextInt();
                    System.out.print("Enter Quantity: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. Quantity must be a number.");
                        sc.next(); break;
                    }
                    int qty = sc.nextInt();
                    if (qty <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                        break;
                    }
                    if (inventory.isAvailable(pid, qty)) {
                        Product product = inventory.getProductById(pid);
                        cart.addItem(product, qty);
                        product.setStock(product.getStock() - qty);
                        undoStack.push(new CartAction(product, qty));
                        System.out.println("Item added to cart.");
                    } else {
                        System.out.println("Insufficient stock or invalid product.");
                    }
                    break;

                case 8:
                    manageCart(sc, cart, undoStack, inventory);
                    break;

                case 9:
                    CartAction action = undoStack.pop();
                    if (action != null) {
                        CartNode undoNode = cart.findItem(action.product.getId());
                        if (undoNode != null) {
                            if (undoNode.quantity <= action.quantity) {
                                cart.removeItem(action.product.getId());
                            } else {
                                cart.updateQuantity(action.product.getId(),
                                        undoNode.quantity - action.quantity);
                            }
                        }
                        action.product.setStock(action.product.getStock() + action.quantity);
                        System.out.println("Undo successful.");
                    } else {
                        System.out.println("Nothing to undo.");
                    }
                    break;

                case 10:
                    if (cart.isEmpty()) {
                        System.out.println("Cart is empty.");
                        break;
                    }
                    System.out.println("\n===== BILL =====");
                    cart.displayCart();
                    System.out.printf("TOTAL BILL: RM%.2f\n", cart.calculateTotal());
                    cart.clear();
                    undoStack.clear();
                    System.out.println("Checkout completed.");
                    System.out.print("Save inventory to file? (y/n): ");
                    sc.nextLine();
                    String saveChoice = sc.nextLine().trim().toLowerCase();
                    if (saveChoice.equals("y")) {
                        inventory.saveToFile("inventory.txt");
                    }
                    break;

                case 11:
                    inventory.saveToFile("inventory.txt");
                    System.out.println("Program terminated.");
                    break;

                default:
                    System.out.println("Invalid choice. Please choose again.");
            }

        } while (choice != 11);

        sc.close();
    }

    private static void manageCart(Scanner sc, CartList cart,
                                   LinkedListStack<CartAction> undoStack,
                                   InventoryManager inventory) {

        int cartChoice;
        do {
            System.out.println("\n--- MANAGE CART ---");
            System.out.println("1. View Cart");
            System.out.println("2. Remove Item from Cart");
            System.out.println("3. Update Item Quantity");
            System.out.println("4. Clear Cart");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input.");
                sc.next();
                cartChoice = -1;
                continue;
            }
            cartChoice = sc.nextInt();

            switch (cartChoice) {

                case 1:
                    cart.displayCart();
                    break;

                case 2:
                    System.out.print("Enter Product ID to remove from cart: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        sc.next(); break;
                    }
                    int removeId = sc.nextInt();
                    CartNode toRemove = cart.findItem(removeId);
                    if (toRemove != null) {
                        toRemove.product.setStock(toRemove.product.getStock() + toRemove.quantity);
                        cart.removeItem(removeId);
                        System.out.println("Item removed. Stock restored.");
                    } else {
                        System.out.println("Item not found in cart.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Product ID to update: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        sc.next(); break;
                    }
                    int updateId = sc.nextInt();
                    System.out.print("Enter new quantity: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input. Quantity must be a number.");
                        sc.next(); break;
                    }
                    int newQty = sc.nextInt();
                    if (newQty <= 0) {
                        System.out.println("Quantity must be greater than 0. Use option 2 to remove.");
                        break;
                    }
                    CartNode cartNode = cart.findItem(updateId);
                    if (cartNode == null) {
                        System.out.println("Item not found in cart.");
                        break;
                    }
                    int diff = newQty - cartNode.quantity;
                    if (diff > 0 && !inventory.isAvailable(updateId, diff)) {
                        System.out.println("Insufficient stock for the requested quantity.");
                        break;
                    }
                    cartNode.product.setStock(cartNode.product.getStock() - diff);
                    cart.updateQuantity(updateId, newQty);
                    System.out.println("Cart quantity updated.");
                    break;

                case 4:
                    if (cart.isEmpty()) {
                        System.out.println("Cart is already empty.");
                        break;
                    }
                    CartNode curr = cart.getHead();
                    while (curr != null) {
                        curr.product.setStock(curr.product.getStock() + curr.quantity);
                        curr = curr.next;
                    }
                    cart.clear();
                    undoStack.clear();
                    System.out.println("Cart cleared. All stock restored.");
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (cartChoice != 0);
    }
}
package GroceryStoreSystem;

public class CartList {

    private CartNode head;

    // Add item
    public void addItem(Product p, int qty) {

        CartNode existing = findItem(p.getId());

        if (existing != null) {
            existing.quantity += qty;
            return;
        }

        CartNode newNode = new CartNode(p, qty);

        if (head == null) {
            head = newNode;
        } else {

            CartNode temp = head;

            while (temp.next != null) {
                temp = temp.next;
            }

            temp.next = newNode;
        }
    }

    // Find item
    public CartNode findItem(int productId) {

        CartNode temp = head;

        while (temp != null) {

            if (temp.product.getId() == productId) {
                return temp;
            }

            temp = temp.next;
        }

        return null;
    }

    // Remove item
    public boolean removeItem(int productId) {

        if (head == null)
            return false;

        if (head.product.getId() == productId) {
            head = head.next;
            return true;
        }

        CartNode temp = head;

        while (temp.next != null &&
                temp.next.product.getId() != productId) {

            temp = temp.next;
        }

        if (temp.next == null)
            return false;

        temp.next = temp.next.next;

        return true;
    }

    // Update quantity
    public boolean updateQuantity(int productId, int newQty) {

        CartNode node = findItem(productId);

        if (node != null) {
            node.quantity = newQty;
            return true;
        }

        return false;
    }

    // Display cart
    public void displayCart() {

        if (head == null) {
            System.out.println("Cart is empty.");
            return;
        }

        double total = 0;

        System.out.println("\n===== CART =====");

        CartNode temp = head;

        while (temp != null) {

            double subtotal =
                    temp.quantity * temp.product.getPrice();

            total += subtotal;

            System.out.printf(
                    "%s x%d = RM%.2f\n",
                    temp.product.getName(),
                    temp.quantity,
                    subtotal
            );

            temp = temp.next;
        }

        System.out.println("Total: RM" + total);
    }

    // Total
    public double calculateTotal() {

        double total = 0;

        CartNode temp = head;

        while (temp != null) {

            total += temp.quantity *
                    temp.product.getPrice();

            temp = temp.next;
        }

        return total;
    }

    // Clear
    public void clear() {
        head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {

        int count = 0;

        CartNode temp = head;

        while (temp != null) {
            count++;
            temp = temp.next;
        }

        return count;
    }

    public CartNode getHead() {
        return head;
    }
}

package GroceryStoreSystem;

public class LinkedListStack<T> {

    private StackNode<T> top;

    public void push(T data) {

        StackNode<T> newNode = new StackNode<>(data);

        newNode.next = top;
        top = newNode;
    }

    public T pop() {

        if (isEmpty()) {
            return null;
        }

        T data = top.data;
        top = top.next;

        return data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void clear() {
        top = null;
    }
}

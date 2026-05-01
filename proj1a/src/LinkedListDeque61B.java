import java.util.List;
import java.util.ArrayList; // import the ArrayList class

public class LinkedListDeque61B<T> implements Deque61B<T> {

    private class Node {
        T item;
        Node next;
        Node prev;

        Node(T i, Node p, Node n) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private Node sentinel;
    private int size;



    @Override
    public void addFirst(T x) {
        Node fisrt = sentinel.next;
        Node node = new Node(x, sentinel, fisrt);
        sentinel.next = node;
        fisrt.prev = node;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node last = sentinel.prev;
        Node node = new Node(x, last, sentinel);
        last.next = node;
        sentinel.prev = node;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node current = sentinel.next;

        while (current != sentinel) {

            returnList.add(current.item);
            current = current.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        if(sentinel.next == sentinel) {
            return true;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public T get(int index) {
        Node current = sentinel.next;
        int i = 0;
        while (current != sentinel) {
            i++;
            if (i == index) {
                return current.item;
            }
        }

        return null;
    }

    @Override
    public T getRecursive(int index) {
        Node current = sentinel.prev;
        int i = 0;
        while (current != sentinel) {
            i++;
            if (i == index) {
                return current.item;
            }
        }
        return null;
    }

    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }
}

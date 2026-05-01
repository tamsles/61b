import java.util.ArrayList;
import java.util.List;


public class ArrayDeque61B<T> implements Deque61B<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque61B() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];

        int firstIndex = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newItems[i] = items[Math.floorMod(firstIndex + i, items.length)];
        }

        items = newItems;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private int plusOne(int index) {
        return Math.floorMod(index + 1, items.length);
    }

    private int minusOne(int index) {
        return Math.floorMod(index - 1, items.length);
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[nextFirst] = x;
        nextFirst = minusOne(nextFirst);
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[nextLast] = x;
        nextLast = plusOne(nextLast);
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int firstIndex = plusOne(nextFirst);

        for (int i = 0; i < size; i++) {
            returnList.add(items[Math.floorMod(firstIndex + i, items.length)]);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int firstIndex = plusOne(nextFirst);
        T removed = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = firstIndex;
        size--;
        return removed;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIndex = minusOne(nextLast);
        T removedItem = items[lastIndex];
        items[lastIndex] = null;
        nextLast = lastIndex;
        size--;
        return removedItem;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int firstIndex = plusOne(nextFirst);
        int actualIndex = Math.floorMod(firstIndex + index, items.length);
        return items[actualIndex];
    }

    @Override
    public T getRecursive(int index) {
        return null;
    }
}
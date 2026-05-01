package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    private double maxload;

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        buckets = createTable(initialCapacity);
        size = 0;
        maxload = loadFactor;
    }

    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = (Collection<Node>[]) new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }

        return table;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */

    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private int getIndex(K key, int length) {
        return Math.floorMod(key.hashCode(), length);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, buckets.length);
        for(Node node :  buckets[index]) {
            if(node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        buckets[index].add(new Node(key, value));
        size++;

        if((double)size / buckets.length > maxload) {
            resize(buckets.length * 2);
        }
    }

    private void resize(int newCapacity) {
        Collection<Node>[] oldBuckets = buckets;
        buckets = createTable(newCapacity);
        for(Collection<Node> bucket : oldBuckets) {
            for(Node node : bucket) {
                put(node.key, node.value);
            }
        }
    }

    @Override
    public V get(K key) {
        int index = getIndex(key, buckets.length);
        for(Node node : buckets[index]) {
            if(node.key.equals(key)) {
                return node.value;
            }
        }

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = getIndex(key, buckets.length);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public int size(){
        return size;
    }

    @Override
    public void clear() {
        buckets = createTable(buckets.length);
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for(Collection<Node> bucket : buckets) {
            for(Node node : bucket) {
                keys.add(node.key);
            }
        }

        return keys;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
}

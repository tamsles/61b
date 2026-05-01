import java.util.ArrayList;
import java.util.Arrays;

public class UnionFind {
    // TODO: Instance variables
    private int[] list;


    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        list = new int[N];
        for (int i = 0; i < N; i++) {
            list[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        int root = find(v);
        return -list[root];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        // TODO: YOUR CODE HERE
        return list[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if (find(v1) == find(v2)) {
            return true;
        }
        return false;
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // TODO: YOUR CODE HERE
        if (v < 0 || v >= list.length) {
            throw new IllegalArgumentException();
        }

        if(list[v] < 0) {
            return v;
        }

        list[v] = find(list[v]);
        return list[v];
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        // TODO: YOUR CODE HERE
            int r1 =  find(v1);
            int r2 =  find(v2);

            if(r1 == r2) {
                return;
            }

            int s1 = -list[r1];
            int s2 = -list[r2];

            if(s1 <= s2){
                list[r1] = r2;
                list[r2] -= s1;
            }
            else{
                list[r2] = r1;
                list[r1] -= s2;
            }

    }

}

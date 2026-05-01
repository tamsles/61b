import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation{
    // TODO: Add any necessary instance variables.
    private boolean[][] p;
    private int size;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufbottom;
    private int openSites;
    private int topVirtual;
    private int bottomVirtual;

    public Percolation(int N) {
        // TODO: Fill in this constructor.
        if(N <= 0){
            throw new java.lang.IllegalArgumentException();
        }
        p = new boolean[N][N];
        size = N;
        for(int i = 0; i < size; i++){
            for (int j = 0; j < N; j++){
                p[i][j] = false;
            }
        }

        uf = new WeightedQuickUnionUF(N * N + 2);
        ufbottom = new WeightedQuickUnionUF(N * N + 2);
        topVirtual = N * N;
        bottomVirtual = N * N + 1;
        openSites = 0;
    }

    public void open(int row, int col) {
        // TODO: Fill in this method.
        if(row < 0 || row > size - 1 || col < 0 || col > size - 1 ){
            throw new java.lang.IndexOutOfBoundsException();
        }
        if(isOpen(row, col)){
            return;
        }

        p[row][col] = true;
        openSites++;

        int curr = xyto1D(row, col);

        if(row == 0){
            uf.union(topVirtual, curr);
            ufbottom.union(topVirtual, curr);
        }

        if(row == size - 1){
            uf.union(bottomVirtual, curr);
        }

        // 上
        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(curr, xyto1D(row - 1, col));
            ufbottom.union(curr, xyto1D(row - 1, col));
        }

        // 下
        if (row < size - 1 && isOpen(row + 1, col)) {
            uf.union(curr, xyto1D(row + 1, col));
            ufbottom.union(curr, xyto1D(row + 1, col));
        }

        // 左
        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(curr, xyto1D(row, col - 1));
            ufbottom.union(curr, xyto1D(row, col - 1));
        }

        // 右
        if (col < size - 1 && isOpen(row, col + 1)) {
            uf.union(curr, xyto1D(row, col + 1));
            ufbottom.union(curr, xyto1D(row, col + 1));
        }

    }

    public int xyto1D(int row, int col) {
        return row * size + col;
    }

    public boolean isOpen(int row, int col) {
        // TODO: Fill in this method.
        if(row < 0 || row > size - 1 || col < 0 || col > size - 1 ){
            throw new java.lang.IndexOutOfBoundsException();
        }

        return p[row][col];
    }

    public boolean isFull(int row, int col) {
        // TODO: Fill in this method.
        if(row < 0 || row > size - 1 || col < 0 || col > size - 1 ){
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            return false;
        }
        return ufbottom.connected(topVirtual, xyto1D(row, col));
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return openSites;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return uf.connected(topVirtual,  bottomVirtual);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.

}

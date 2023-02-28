import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] arr;

    private int[] father;

    private boolean[] connected;

    private int root;
    private int foot;
    private int n;

    private int cnt;

    private WeightedQuickUnionUF weightedQuickUnionUF1;
    private WeightedQuickUnionUF weightedQuickUnionUF2;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        n = N;
        cnt = 0;
        arr = new boolean[N][N];
        weightedQuickUnionUF1 = new WeightedQuickUnionUF(n * n);
        weightedQuickUnionUF2 = new WeightedQuickUnionUF(n * n);
        root = -1;
        foot = -1;
    }

    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) {
            return;
        }
        cnt++;
        if (row == 0) {
            if (root == -1) {
                root = calculate(row, col);
            } else {
                weightedQuickUnionUF1.union(root, calculate(row, col));
                weightedQuickUnionUF2.union(root, calculate(row, col));
            }
        }
        if (row == n - 1) {
            if (foot == -1) {
                foot = calculate(row, col);
            } else {
                weightedQuickUnionUF1.union(foot, calculate(row, col));
            }
        }
        arr[row][col] = true;
        int tar = calculate(row, col);
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int newX = row + direction[0];
            int newY = col + direction[1];
            if (newX >= 0 && newX < n && newY >= 0 && newY < n && arr[newX][newY]) {
                int cur = calculate(newX, newY);
                if (!weightedQuickUnionUF2.connected(cur, tar)) {
                    weightedQuickUnionUF1.union(tar, cur);
                    weightedQuickUnionUF2.union(tar, cur);
                }
            }
        }
    }

    private int calculate(int p, int q) {
        return p * n + q;
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException();
        }
        return arr[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException();
        }
        if (root == -1) {
            return false;
        }
        return weightedQuickUnionUF2.connected(root, calculate(row, col));
    }

    public int numberOfOpenSites() {
        return cnt;
    }

    public boolean percolates() {
        return foot != -1 && root != -1 && weightedQuickUnionUF1.connected(foot, root);
    }
}

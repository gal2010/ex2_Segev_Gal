import java.util.ArrayList;

/**
 * A {@link Runnable} task that solves the recursive "Subset Sum" decision problem
 * using multithreading.
 * <p>
 * Two instances of this class (named {@code "thread1"} and {@code "thread2"}) share a
 * single {@link SharedData} object. {@code thread1} explores the branch in which the last
 * element of the list is part of the subset, while {@code thread2} explores the branch in
 * which it is not. As soon as one thread finds a solution it raises the shared flag so the
 * other thread can stop early.
 * </p>
 * <p>
 * The input numbers are stored in an {@code ArrayList<Integer>}, while the result vector
 * {@code winArray} remains a primitive {@code boolean[]} whose size is {@code array.size()}.
 * </p>
 */
public class ThreadCheckArray implements Runnable {
    /** Whether this thread found a solution. */
    private boolean flag;
    /** Local result vector; {@code true} at index i means array.get(i) belongs to the subset. */
    private boolean[] winArray;
    /** The object shared between the threads. */
    SharedData sd;
    /** The input numbers, as an {@code ArrayList<Integer>}. */
    ArrayList<Integer> array;
    /** The target sum. */
    int b;

    /**
     * Creates a new task and copies the shared input in a thread-safe manner.
     *
     * @param sd the shared data holding the {@code ArrayList<Integer>} and the target sum
     */
    public ThreadCheckArray(SharedData sd) {
        this.sd = sd;
        synchronized (sd) {
            array = sd.getArray();
            b = sd.getB();
        }
        winArray = new boolean[array.size()];
    }

    /**
     * Recursively checks whether a subset of the first {@code n} elements sums to {@code b}.
     * Returns immediately if another thread has already found a solution.
     *
     * @param n the number of leading elements still under consideration
     * @param b the remaining sum to reach
     */
    void rec(int n, int b) {
        synchronized (sd) {
            if (sd.getFlag()) return;
        }
        if (n == 1) {
            if (b == 0 || b == array.get(n - 1)) {
                flag = true;
                synchronized (sd) { sd.setFlag(true); }
            }
            if (b == array.get(n - 1)) winArray[n - 1] = true;
            return;
        }

        rec(n - 1, b - array.get(n - 1));
        if (flag) winArray[n - 1] = true;
        synchronized (sd) {
            if (sd.getFlag()) return;
        }
        rec(n - 1, b);
    }

    /**
     * Thread entry point. {@code thread1} assumes the last element is in the subset,
     * {@code thread2} assumes it is not. If a solution is found, the result vector is
     * published to the shared data inside a synchronized block.
     */
    public void run() {
        if (array.size() != 1)
            if (Thread.currentThread().getName().equals("thread1"))
                rec(array.size() - 1, b - array.get(array.size() - 1));
            else
                rec(array.size() - 1, b);
        if (array.size() == 1)
            if (b == array.get(0) && !flag) {
                winArray[0] = true;
                flag = true;
                synchronized (sd) { sd.setFlag(true); }
            }
        if (flag) {
            if (Thread.currentThread().getName().equals("thread1"))
                winArray[array.size() - 1] = true;
            synchronized (sd) { sd.setWinArray(winArray); }
        }
    }
}

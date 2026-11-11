import java.util.ArrayList;

/**
 * Holds the data shared between the two {@link ThreadCheckArray} threads that solve the
 * Subset Sum decision problem.
 * <p>
 * The input numbers are stored in an {@code ArrayList<Integer>}, the target sum {@code b}
 * is fixed, and the result vector {@code winArray} remains a primitive {@code boolean[]}.
 * Callers are expected to synchronize on an instance of this class when reading or
 * writing its mutable state.
 * </p>
 */
public class SharedData {
    /** The input numbers, as an {@code ArrayList<Integer>}. */
    private ArrayList<Integer> array;
    /** The result vector; {@code true} at index i means the i-th number belongs to the subset. */
    private boolean[] winArray;
    /** Set to {@code true} as soon as one thread finds a solution. */
    private boolean flag;
    /** The target sum. */
    private final int b;

    /**
     * Creates the shared data.
     *
     * @param array the input numbers, as an {@code ArrayList<Integer>}
     * @param b     the target sum
     */
    public SharedData(ArrayList<Integer> array, int b) {
        this.array = array;
        this.b = b;
    }

    /**
     * Returns the result vector published by the thread that found a solution.
     *
     * @return the result vector, or {@code null} if no solution was published
     */
    public boolean[] getWinArray() { return winArray; }

    /**
     * Publishes the result vector.
     *
     * @param winArray the result vector
     */
    public void setWinArray(boolean[] winArray) { this.winArray = winArray; }

    /**
     * Returns the input numbers.
     *
     * @return the input numbers, as an {@code ArrayList<Integer>}
     */
    public ArrayList<Integer> getArray() { return array; }

    /**
     * Returns the target sum.
     *
     * @return the target sum
     */
    public int getB() { return b; }

    /**
     * Returns whether a solution has been found.
     *
     * @return {@code true} if a thread has found a solution
     */
    public boolean getFlag() { return flag; }

    /**
     * Sets whether a solution has been found.
     *
     * @param flag {@code true} once a solution was found
     */
    public void setFlag(boolean flag) { this.flag = flag; }
}

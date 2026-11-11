# ex2_Segev_Gal – Multithreaded Subset Sum

Lab Exercise 2 – Software Engineering Methods, Braude College of Engineering.
A Java implementation of the **Subset Sum** decision problem, solved recursively by two cooperating threads, used to practice Git/GitHub collaboration, AI-assisted refactoring and merge-conflict resolution.

## 1. The algorithm and thread synchronization

Given a list of integers and a target `b`, the program decides whether some subset sums to `b`, and reports which elements belong to it.

- `ThreadCheckArray.rec(n, b)` recurses over the first `n` elements: it first tries to **include** element `n-1` (`rec(n-1, b - array.get(n-1))`) and, if no solution was found, **excludes** it (`rec(n-1, b)`).
- Two threads run the same task over one shared `SharedData` object and split the search at the last element:
  - `thread1` assumes the last element **is** in the subset.
  - `thread2` assumes it **is not**.
- **Synchronization:** all access to shared state (`flag`, `winArray`, the input list and `b`) happens inside `synchronized (sd)` blocks. When one thread finds a solution it sets the shared `flag`; the other thread checks the flag at every recursion step and stops early.
- The winning thread publishes its result vector with `sd.setWinArray(winArray)`. The driver then prints it as the `C` row (`1` = element is in the subset).

## 2. Refactoring to `ArrayList<Integer>`

The original code stored the input in a primitive `int[] array`. It was changed to `ArrayList<Integer>`:

- `SharedData`: field, constructor parameter and `getArray()` now use `ArrayList<Integer>`.
- `ThreadCheckArray`: `array[i]` became `array.get(i)` and `array.length` became `array.size()`.
- `TestThreadCheckArray`: input is read into an `ArrayList<Integer>`.
- The result vector `winArray` intentionally **remains a primitive `boolean[]`**, sized with `array.size()`.

## 3. Code documentation (Javadoc)

Classes, fields and methods are documented with Javadoc. Generic types are written as `{@code ArrayList<Integer>}` so that the `javadoc` tool does not report malformed HTML. HTML documentation can be generated in IntelliJ with *Tools → Generate JavaDoc…* into the `docs/` folder (excluded from Git by `.gitignore`).

## 4. Authors and Git collaboration roles

| Role | Work |
| :--- | :--- |
| Student A (`gal2010`) | Created the repository, pushed the base project, refactored `SharedData.java` and `ThreadCheckArray.java` on `main`, wrote this README. |
| Student B (`segevmorag`) | Worked on branch `feature/arraylist-refactor-b`: refactored `TestThreadCheckArray.java` and `ThreadCheckArray.java`, merged into `main` and resolved the merge conflict with IntelliJ's 3-Way Merge tool. |

## Running

Compile and run `TestThreadCheckArray`. Verification input:

```text
Enter array size:
4
Enter numbers for array:
1 2 3 7
Enter number:
5
```

Expected output:

```text
Solution for b : 5,n = 4
I:    0    1    2    3
A:    1    2    3    7
C:    0    1    1    0
```

The `C` row marks the values `2` and `3`, which sum to `5`.

## 5. License

This project is distributed under the **Braude College Academic License**. It is intended for instructional use in the Department of Software Engineering at Braude College of Engineering; distribution outside the course is prohibited.

package solveur_de_coups;

import java.util.*;

public class SolveurTrouveSurInternet {
    // classic Rush Hour parameters
    static final int N = 6;
    static final int M = 6;
    static final int GOAL_R = 2;
    static final int GOAL_C = 5;

    // the transcription of the 93 moves, total 24132 configurations problem
    // from http://cs.ulb.ac.be/~fservais/rushhour/index.php?window_size=20&offset=0
    static final String INITIAL =   "333BCC" +
            "B22BCC" +
            "B.XXCC" +
            "22B..." +
            ".BB.22" +
            ".B2222";

    static final String HORZS = "23X";  // horizontal-sliding cars
    static final String VERTS = "BC";   // vertical-sliding cars
    static final String LONGS = "3C";   // length 3 cars
    static final String SHORTS = "2BX"; // length 2 cars
    static final char GOAL_CAR = 'X';
    static final char EMPTY = '.';      // empty space, movable into
    static final char VOID = '@';       // represents everything out of bound

    // breaks a string into lines of length N using regex
    static String prettify(String state) {
        String EVERY_NTH = "(?<=\\G.{N})".replace("N", String.valueOf(N));
        return state.replaceAll(EVERY_NTH, "\n");
    }

    // conventional row major 2D-1D index transformation
    static int rc2i(int r, int c) {
        return r * N + c;
    }

    // checks if an entity is of a given type
    static boolean isType(char entity, String type) {
        return type.indexOf(entity) != -1;
    }

    // finds the length of a car
    static int length(char car) {
        return
                isType(car, LONGS) ? 3 :
                        isType(car, SHORTS) ? 2 :
                                0/0; // a nasty shortcut for throwing IllegalArgumentException
    }

    // in given state, returns the entity at a given coordinate, possibly out of bound
    static char at(String state, int r, int c) {
        return (inBound(r, M) && inBound(c, N)) ? state.charAt(rc2i(r, c)) : VOID;
    }
    static boolean inBound(int v, int max) {
        return (v >= 0) && (v < max);
    }

    // checks if a given state is a goal state
    static boolean isGoal(String state) {
        return at(state, GOAL_R, GOAL_C) == GOAL_CAR;
    }

    // in a given state, starting from given coordinate, toward the given direction,
    // counts how many empty spaces there are (origin inclusive)
    static int countSpaces(String state, int r, int c, int dr, int dc) {
        int k = 0;
        while (at(state, r + k * dr, c + k * dc) == EMPTY) {
            k++;
        }
        return k;
    }

    // the predecessor map, maps currentState => previousState
    static Map<String,String> pred = new HashMap<String,String>();
    // the breadth first search queue
    static Queue<String> queue = new LinkedList<String>();
    // the breadth first search proposal method: if we haven't reached it yet,
    // (i.e. it has no predecessor), we map the given state and add to queue
    static void propose(String next, String prev) {
        if (!pred.containsKey(next)) {
            pred.put(next, prev);
            queue.add(next);
        }
    }

    // the predecessor tracing method, implemented using recursion for brevity;
    // guaranteed no infinite recursion, but may throw StackOverflowError on
    // really long shortest-path trace (which is infeasible in standard Rush Hour)
    static int trace(String current) {
        String prev = pred.get(current);
        int step = (prev == null) ? 0 : trace(prev) + 1;
        System.out.println(step);
        System.out.println(prettify(current));
        return step;
    }

    // in a given state, from a given origin coordinate, attempts to find a car of a given type
    // at a given distance in a given direction; if found, slide it in the opposite direction
    // one spot at a time, exactly n times, proposing those states to the breadth first search
    //
    // e.g.
    //    direction = -->
    //    __n__
    //   /     \
    //   ..o....c
    //      \___/
    //      distance
    //
    static void slide(String current, int r, int c, String type, int distance, int dr, int dc, int n) {
        r += distance * dr;
        c += distance * dc;
        char car = at(current, r, c);
        if (!isType(car, type)) return;
        final int L = length(car);
        StringBuilder sb = new StringBuilder(current);
        for (int i = 0; i < n; i++) {
            r -= dr;
            c -= dc;
            sb.setCharAt(rc2i(r, c), car);
            sb.setCharAt(rc2i(r + L * dr, c + L * dc), EMPTY);
            propose(sb.toString(), current);
            current = sb.toString(); // comment to combo as one step
        }
    }

    // explores a given state; searches for next level states in the breadth first search
    //
    // Let (r,c) be the intersection point of this cross:
    //
    //     @       nU = 3     '@' is not a car, 'B' and 'X' are of the wrong type;
    //     .       nD = 1     only '2' can slide to the right up to 5 spaces
    //   2.....B   nL = 2
    //     X       nR = 4
    //
    // The n? counts how many spaces are there in a given direction, origin inclusive.
    // Cars matching the type will then slide on these "alleys".
    //
    static void explore(String current) {
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                if (at(current, r, c) != EMPTY) continue;
                int nU = countSpaces(current, r, c, -1, 0);
                int nD = countSpaces(current, r, c, +1, 0);
                int nL = countSpaces(current, r, c, 0, -1);
                int nR = countSpaces(current, r, c, 0, +1);
                slide(current, r, c, VERTS, nU, -1, 0, nU + nD - 1);
                slide(current, r, c, VERTS, nD, +1, 0, nU + nD - 1);
                slide(current, r, c, HORZS, nL, 0, -1, nL + nR - 1);
                slide(current, r, c, HORZS, nR, 0, +1, nL + nR - 1);
            }
        }
    }
    public static void main(String[] args) {
        // typical queue-based breadth first search implementation
        propose(INITIAL, null);
        boolean solved = false;
        while (!queue.isEmpty()) {
            String current = queue.remove();
            if (isGoal(current) && !solved) {
                solved = true;
                trace(current);
                //break; // comment to continue exploring entire space
            }
            explore(current);
        }
        System.out.println(pred.size() + " explored");
    }
}
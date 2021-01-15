package wb.analyse1.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Path stores a path of nodes of type N
 *
 * @param <N> type
 */
public class Path<N> {

    private N startNode = null;
    private N goalNode = null;
    private ArrayList<N> path = new ArrayList<>();

    // TODO maybe add a copy constructor

    public Path() {
    }

    public Path(N start) {
        this.startNode = start;
        this.goalNode = start;
        this.path.add(start);
    }

    public Path(ArrayList<N> path) {
        this.startNode = path.get(0);
        this.goalNode = path.get(path.size() - 1);
        this.path = path;
    }

    public Path(N[] path) {
        this.startNode = path[0];
        this.goalNode = path[path.length - 1];
        this.path.addAll(Arrays.asList(path));
    }

    /**
     * Adds new node to the end of path and updates goal node.
     *
     * @param e node to insert
     */
    public void addToPath(N e) {
        if (this.startNode == null) {
            this.startNode = e;
        }
        this.path.add(e);
        this.goalNode = e;
    }

    public N getStart() {
        return this.startNode;
    }

    public N getGoal() {
        return this.goalNode;
    }

    public N get(int i) {
        return this.path.get(i);
    }

    public int length() {
        return this.path.size();
    }

}

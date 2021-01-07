package wb.analyse1.analyse;

import java.util.Arrays;

public class Results {
    double[] closeness;
    double[] betweenness;


    public Results(int[][] adjacencyMatrix){
        closeness = new double[adjacencyMatrix[0].length];
        betweenness = new double[adjacencyMatrix[0].length];
    }

    @Override
    public String toString() {
        return "Results{" +
                "closeness=" + Arrays.toString(closeness) +
                ", betweenness=" + Arrays.toString(betweenness) +
                '}';
    }
}

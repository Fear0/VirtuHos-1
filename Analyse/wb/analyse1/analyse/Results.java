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

    public void normalize_closeness(){
        int n = closeness.length;
        double temp = 0;
        for(int i = 0; i<n; i++){
            if(closeness[i]> temp) temp = closeness[i];
        }
        for(int i = 0; i<n; i++){
            closeness[i] = closeness[i]/temp;
        }
    }
}

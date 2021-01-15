package wb.analyse1.analyse;

import java.util.Arrays;

public class Dijkstra {


    //calculates betweenness and closeness for all Vertices of a given adjacency Matrix
    public static Results calculate(int[][]adjacencyMatrix){
        Results results = new Results(adjacencyMatrix);
        int n = adjacencyMatrix[0].length;
        for(int i = 0; i < n; i++) {
            results = dijkstra(adjacencyMatrix, i, results);
        }
        results.betweenness = normalizeBetweeness(results.betweenness, n);
        results.normalize_closeness();
        return results;
    }

    //Dijkstra's Algorithm for shortest path for a graph using a adjacency matrix
    private static Results dijkstra(int[][] adjacencyMatrix, int source, Results results){
        // Number of Vertices in Graph
        int N = adjacencyMatrix[0].length;

        double[][] invMat = invertMatrix(adjacencyMatrix);

        // holds shortest distance from source to all other Vertices
        double[] shortestDistances = new double[N];

        //calculated[i] is true if vertex i is included un the shortest path tree
        boolean[] calculated = new boolean[N];


        //Initialize
        for(int i = 0; i < N; i++){
            shortestDistances[i]= Integer.MAX_VALUE;
            calculated[i] = false;
        }

        //Array to store PathTree
        int[][] parents = new int[N][N];
        for(int i = 0; i<N; i++) {
            Arrays.fill(parents[i], -2);
        }
        parents[source][0] = -1;

        //Distance to itself is Zero
        shortestDistances[source] = 0;

        //calculate shortest path for all Vertices
        for(int i = 1; i < N; i++){
            int[] closestVertex = new int[N];
            Arrays.fill(closestVertex, -1);
            double minDist = Integer.MAX_VALUE;
            int count = 0;
            for(int j = 0; j < N; j++){
                if(!calculated[j] && shortestDistances[j] <= minDist){
                    if(shortestDistances[j] == minDist) count++;
                    if(shortestDistances[j] < minDist){
                        count = 0;
                        Arrays.fill(closestVertex, -1);
                    }

                    closestVertex[count] = j;
                    minDist = shortestDistances[j];
                }
            }

            /*System.out.print("closest Vertices of" + i + ": ");
            for(int h = 0; h<N; h++) System.out.print(closestVertex[h] + " ");
            System.out.println();*/
            //Update calculated list
            calculated[closestVertex[0]] = true;

            //Update distance values for adjacent vertices
            for(int j = 0; j < N; j++){
                double Distance = invMat[closestVertex[0]][j];
                if(Distance > 0 && (minDist + Distance) < shortestDistances[j]){
                    for(int k = 0; closestVertex[k] != -1; k++){
                        parents[j][k] = closestVertex[k];
                    }
                    shortestDistances[j] = minDist + Distance;

                }
            }
        }

        //printmat(parents);

        return processpath(shortestDistances, source, parents, results);
    }

    // caculates betweenness and closeness centrality can also print shortest paths
    private static Results processpath(double[]shortestDistances, int source, int[][] parents, Results results){
        double n = shortestDistances.length;
        double sumLength = 0;
        for(int i = 0; i < n; i++){
            if(i != source){
                int count = 0;
                sumLength += shortestDistances[i];
                //System.out.print(source + "to" + i + "\t" + String.format("%.3f", shortestDistances[i]) + "\t");
                for(int k = 0; k < n;k++) {
                    count = countpaths(i, parents, source, false, k, count);
                }
                //System.out.println(count);
                for(int k = 0; k < n;k++) {
                    rec_callpath(i, parents, results.betweenness, source, false, k, count);
                }
                //System.out.println();
            }
        }
        results.closeness[source] = 1.0/(sumLength);
        return results;
    }

    //function to determine number of shortest paths
    private static int countpaths(int current, int[][] parents, int source, boolean between, int pathNum, int count){
        if(current != -1 && parents[current][pathNum] != -2){
            if(!between)count++;
            for(int i = 0; parents[current][i] != -2; i++) {
                count = countpaths(parents[current][i], parents, source, true, pathNum, count);
            }
        }
        return count;
    }


    //recursive function to process all parents and calculate betweenness
    private static void rec_callpath(int current, int[][] parents, double[] betweenness,int source, boolean between, int pathNum, int pathcount){
        if(current != -1 && parents[current][pathNum] != -2){
            if(current!= source && between) betweenness[current] =betweenness[current] + 1.0/pathcount;
            for(int i = 0; parents[current][i] != -2; i++) {
                rec_callpath(parents[current][i], parents, betweenness, source, true, pathNum, pathcount);
            }
            //System.out.print(current + " ");
        }
    }

    private static double[] normalizeBetweeness(double[] array, int n){
        for(int i = 0; i < n; i++){
            array[i] = array[i]/2;
            array[i] = array[i]/(((n-1) * (n -2))/2.0);
        }
        return array;
    }

    private static double[][] invertMatrix(int[][] Matrix){
        int n = Matrix[0].length;
        double[][] invMat = new double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(Matrix[i][j] != 0){
                    invMat[i][j] = 1.0/Matrix[i][j];
                }
            }
        }
        return  invMat;
    }


    private static void printmat(int[][] M){
        int n = M[0].length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print("[" + M[i][j] + "] ");
            }
            System.out.println();
        }
    }

    private static void printmat(double[][] M){
        int n = M[0].length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print("[" + M[i][j] + "] ");
            }
            System.out.println();
        }
    }
}


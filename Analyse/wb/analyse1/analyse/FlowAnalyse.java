package wb.analyse1.analyse;

public class FlowAnalyse {


    //calculates FlowDistance for all vertices and return them as a Matrix
    public static double[][] calcFlowDistance(double[][] graph){
        int n = graph.length;
        double[][] result = new double[n][n];
        for(int i = 0; i < n; i++){
            double [] dist = shortestPaths(graph, i);
            for(int j = 0; j < n; j++){
                result[i][j] = dist[j];
            }
        }

        //cleanmat(result);
        cleanAndPrintmat(result);
        return result;
    }


    //calculates shortestPaths from source to all other vertices
    static double[] shortestPaths(double[][] graph, int src)
    {
        int n = graph.length;
        double dist[] = new double[n];

        //calculated[i] is true if vertex i is included in the shortest path tree
        Boolean calculated[] = new Boolean[n];

        // Initialize
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            calculated[i] = false;
        }

        // Distance of source vertex from itself is always 0
        dist[src] = 0;

        //calculate shortest path for all Vertices
        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, calculated);
            calculated[u] = true;

            // Update distance value for adjacent vertices
            for (int v = 0; v < n; v++)
                if (!calculated[v] && graph[u][v] != 0 &&
                        dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v])
                    dist[v] = dist[u] + graph[u][v];
        }

        return dist;
    }


    //returns the vertex with minimum Distance and which is not in the calculated list
    static int minDistance(double dist[], Boolean sptSet[])
    {
        // Initialize
        int n = dist.length;
        double min = Integer.MAX_VALUE;
        int min_vertex = -1;

        for (int i = 0; i < n; i++)
            if (dist[i] <= min && sptSet[i] == false) {
                min = dist[i];
                min_vertex = i;
            }

        return min_vertex;
    }



    // replaces Max values with Zeros for non existent edges and prints to console
    private static double[][] cleanAndPrintmat(double[][] M){
        int n = M[0].length;
        double[][] res = new double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(M[i][j] != Integer.MAX_VALUE) res[i][j] = M[i][j];
                else res[i][j] = 0;
                System.out.print("[" + String.format("%.1f",res[i][j]) + "] ");
            }
            System.out.println();
        }
        return res;
    }

    // replaces Max values with Zeros for non existent edges
    private static double[][] cleanmat(double[][] M){
        int n = M[0].length;
        double[][] res = new double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(M[i][j] != Integer.MAX_VALUE) res[i][j] = M[i][j];
                else res[i][j] = 0;
            }
        }
        return res;
    }
}

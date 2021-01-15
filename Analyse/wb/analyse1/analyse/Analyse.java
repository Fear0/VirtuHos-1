package wb.analyse1.analyse;

import wb.analyse1.parser.Attendee;
import wb.analyse1.parser.Meeting;

import java.util.*;

/**
 * Analyses the BBB network
 */

public class Analyse {

    private int[][] networkMatrix = null;
    private LinkedHashSet<User> users;
    private LinkedHashSet<User> onlineUsers;
    private boolean matrixChanged = false;

    public Analyse() {

    }


    /**
     * creates or updates the network matrix according to the meetings running or run on server
     *
     * @param meetings the matrix doesn't change if there are no new meetings
     */
    public void updateNetworkMatrix(List<Meeting> meetings) {
        if (meetings == null) {
            matrixChanged = false;
            return;
        }
        matrixChanged = true;
        if (networkMatrix == null) {
            LinkedHashSet<User> userSet = new LinkedHashSet<User>();
            List<String> ids = new ArrayList<String>();
            int index = 0;
            for (Meeting meeting : meetings) {


                //THIS IF STATEMENT IS JUST FOR TESTING OUR OWN BBB MEETINGS

                //System.out.println(meeting.getMeetingID());
                if (meeting.getMeetingID().length() < 6) {
                    continue;
                }
                if (!meeting.getMeetingID().substring(0, 6).equalsIgnoreCase("wb-a-1")) {
                    continue;
                }
                // end

                System.out.println(meeting.getMeetingID());
                List<Attendee> attendees = meeting.getAttendees();
                for (Attendee attendee : attendees) {
                    if (!ids.contains(attendee.getId())) {
                        ids.add(attendee.getId());
                        User user = new User(attendee.getId(),attendee.getName(), index);
                        userSet.add(user);
                        index++;
                    }
                }
            }

            //System.out.println(ids);
            this.users = userSet;
            this.onlineUsers = userSet;
            //System.out.println(userSet.size());
            this.networkMatrix = new int[userSet.size()][userSet.size()];
            for (int i = 0; i < userSet.size(); i++) {
                Arrays.fill(networkMatrix[i], 0);
            }
            List<AbstractMap.SimpleEntry<String, String>> communicationPairs = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
            for (Meeting meeting : meetings) {


                //THESE IF STATEMENTS ARE JUST FOR TESTING OUR OWN BBB MEETINGS

                //System.out.println(meeting.getMeetingID());
                if (meeting.getMeetingID().length() < 6) {
                    continue;
                }
                if (!meeting.getMeetingID().substring(0, 6).equalsIgnoreCase("wb-a-1")) {
                    continue;
                }
                // end

                List<Attendee> attendees = meeting.getAttendees();
                for (int i = 0; i < attendees.size(); i++) {
                    for (int j = i + 1; j < attendees.size(); j++)
                        communicationPairs.add(new AbstractMap.SimpleEntry<String, String>(attendees.get(i).getId(), attendees.get(j).getId()));
                }
            }
            for (AbstractMap.SimpleEntry<String, String> pair : communicationPairs) {
                String firstID = pair.getKey();
                String secondID = pair.getValue();
                networkMatrix[ids.indexOf(firstID)][ids.indexOf(secondID)] += 1;
                networkMatrix[ids.indexOf(secondID)][ids.indexOf(firstID)] += 1;
            }
            //System.out.println(communicationPairs.toString());
            System.out.println(Arrays.deepToString(networkMatrix).replaceAll("],", "]," + System.getProperty("line.separator")));
        } else {

            this.onlineUsers = new LinkedHashSet<>();
            /*this.onlineUsers.forEach(x -> System.out.println(x));
            this.users.forEach(x -> System.out.println(x));*/
            List<String> ids = new ArrayList<String>();
            for (User user : this.users) {
                ids.add(user.getId());
            }
            System.out.println(ids);
            int oldUsers = this.users.size();
            int newUsers = 0;
            for (Meeting meeting : meetings) {


                // THIS IF STATEMENT IS JUST FOR TESTING OUR OWN BBB MEETINGS

                //System.out.println(meeting.getMeetingID());
                if (meeting.getMeetingID().length() < 6) {
                    continue;
                }
                if (!meeting.getMeetingID().substring(0, 6).equalsIgnoreCase("wb-a-1")) {
                    continue;
                }
                //


                List<Attendee> attendees = meeting.getAttendees();
                for (Attendee attendee : attendees) {
                    if (!ids.contains(attendee.getId())) {
                        ids.add(attendee.getId());
                        newUsers++;
                        User newUser = new User(attendee.getId(),attendee.getName(), oldUsers - 1 + newUsers);
                        this.users.add(newUser);
                    }
                }
                for (Attendee attendee : attendees){
                    for (User user : this.users){
                        if (user.getId().equals(attendee.getId())){
                            this.onlineUsers.add(user);
                        }
                    }
                }
            }
            System.out.println(newUsers + oldUsers);
            int[][] oldMatrix = this.networkMatrix;
            int[][] newMatrix = new int[oldUsers + newUsers][oldUsers + newUsers];
            if (newUsers > 0) {
                for (int i = 0; i < newMatrix[0].length; i++) {
                    Arrays.fill(newMatrix[i], 0);
                }
                for (int i = 0; i < newMatrix[0].length; i++) {
                    for (int j = 0; j < newMatrix[0].length; j++) {
                        if (i < oldMatrix.length && j < oldMatrix.length) {
                            newMatrix[i][j] = oldMatrix[i][j];
                        }
                    }
                }
            } else {
                newMatrix = oldMatrix;
            }
            List<AbstractMap.SimpleEntry<String, String>> communicationPairs = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
            for (Meeting meeting : meetings) {


                //THIS IF STATEMENT IS JUST FOR TESTING OUR OWN BBB MEETINGS

                //System.out.println(meeting.getMeetingID());
                if (meeting.getMeetingID().length() < 6) {
                    continue;
                }
                if (!meeting.getMeetingID().substring(0, 6).equalsIgnoreCase("wb-a-1")) {
                    continue;
                }
                // end


                List<Attendee> attendees = meeting.getAttendees();
                for (int i = 0; i < attendees.size(); i++) {
                    for (int j = i + 1; j < attendees.size(); j++)
                        communicationPairs.add(new AbstractMap.SimpleEntry<String, String>(attendees.get(i).getId(), attendees.get(j).getId()));
                }
            }
            //System.out.println(communicationPairs.toString());
            for (AbstractMap.SimpleEntry<String, String> pair : communicationPairs) {
                String firstID = pair.getKey();
                String secondID = pair.getValue();
                int firstIndex = getIndexforID(firstID);
                int secondIndex = getIndexforID(secondID);
                if (ids.contains(firstID) && ids.contains(secondID)) {
                    newMatrix[firstIndex][secondIndex] += 1;
                    newMatrix[secondIndex][firstIndex] += 1;
                }
            }
            this.networkMatrix = newMatrix;
            System.out.println(Arrays.deepToString(this.networkMatrix).replaceAll("],", "]," + System.getProperty("line.separator")));
        }
    }

    /**
     *
     * @return the users that are currently on server
     */
    public LinkedHashSet<User> getOnlineUsers(){

            return onlineUsers;

    }


    /**
     * returns the user that has the given position in the network matrix
     *
     * @param index of the searched user
     * @return user with the position index
     */
    public User getUserforIndex(int index) {

        for (User user : this.users) {
            if (user.getPositionMatrix() == index) {
                return user;
            }
        }
        return null;
    }

    /**
     * gets the position in the matrix for a user id
     *
     * @param ID of the user
     * @return the position in the network matrix
     */
    public int getIndexforID(String ID) {
        for (User user : this.users) {
            if (user.getId().equals(ID)) {
                return user.getPositionMatrix();
            }
        }
        return -1;
    }

    /**
     * @return a list of all users in the matrix
     */
    public LinkedHashSet<User> getUsers() {
        return this.users;
    }

    public void setUsers(LinkedHashSet<User> users) {
        this.users = users;
    }


    public boolean isMatrixChanged() {
        return matrixChanged;
    }

    public void setNetworkMatrix(int[][] matrix) {
        this.networkMatrix = matrix;
    }

    public int[][] getNetworkMatrix() {
        return networkMatrix;
    }

    /**
     * Determines all connected components of the graph presented as adjacency matrix. If the graph is connected
     * then the algorithm returns one component that contains all vertices in the graph
     *
     * @param matrix presents the undirected graph
     * @return list of integer lists. Every list contains the vertices of each connected component
     */
    public static List<List<Integer>> getConnectedComponents(int[][] matrix) {

        /* We use breadth-first-search traversal to go through all vertices of the graph. The idea is
        quite popular. We visit a vertex x and see if there is at least one path that leads to another vertex
        starting from x. If so then both vertices are located in the same connected component.
        If there are other connected components after finding previous ones, then there will be at least one
        unvisited vertex.
         */

        /* we save all connected components in a list, where each element contains the vertices (indexes) of each
        connected component.*/
        List<List<Integer>> connectedComponents = new ArrayList<>();
        boolean[] visited = new boolean[matrix.length];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                List<Integer> connectedComponent = new ArrayList<>();
                connectedComponent.add(i);

                // breadth-first-search traversal -> FIRST-IN-FIRST-OUT -> Queue
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                visited[i] = true;
                while (!queue.isEmpty()) {

                    // the vertex node exists in the actual connected component
                    int node = queue.remove();
                    List<Integer> adjacentNodes = new ArrayList<>();
                    /* we go through the network matrix to determine if the are paths leading to other vertices. If so
                    we add it to the list of adjacent nodes*/
                    for (int j = 0; j < matrix[node].length; j++) {
                        if (matrix[node][j] != 0) {
                            adjacentNodes.add(j);
                        }
                    }
                    /* We are looking for the largest connected components that's why we need to follow the connectivity
                    until no connection is found. At the same time we add the connected vertices to the actual connected
                    component*/
                    for (Integer adjacent : adjacentNodes) {
                        if (!visited[adjacent]) {
                            visited[adjacent] = true;
                            connectedComponent.add(adjacent);
                            queue.add(adjacent);
                        }
                    }
                }
                connectedComponents.add(connectedComponent);

            }
        }
        return connectedComponents;
    }

    /**
     * returns the results of the betweenness and the closeness analysis and updates user attributes
     */

    public void betweennessAndCloseness() {

        int[][] networkMatrix = getNetworkMatrix();
        List<List<Integer>> connectedComponents = getConnectedComponents(networkMatrix);
        // if the graph is connected, then there will be only one connected component
        for (List<Integer> component : connectedComponents) {
            if (component.size() == 1){
               int solo = component.get(0);
               User user = getUserforIndex(solo);
               user.setCloseness(0.0);
               user.setBetweenness(0.0);
               continue;
            }
            int[][] matrix = new int[component.size()][component.size()];
            for (int i = 0; i < component.size(); i++) {
                for (int j = i; j < component.size(); j++) {
                    matrix[i][j] = networkMatrix[component.get(i)][component.get(j)];
                    matrix[j][i] = networkMatrix[component.get(i)][component.get(j)];
                }
            }
            Results results = Dijkstra.calculate(matrix);
            System.out.println(results);
            int i = 0;
            for (Integer index : component) {
                User user = getUserforIndex(index);
                user.setBetweenness(results.betweenness[i]);
                user.setCloseness(results.closeness[i]);
                i++;
            }
        }
    }


    /**
     * normalizes a vector of doubles
     *
     * @param vector the vector to normalize
     * @return a vector with each value is divided by the norm of the original vector
     */
    public double[] normalizeVector(double[] vector) {
        double norm = 0.0;
        double[] normalized = new double[vector.length];
        for (double v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = (double) (vector[i] / norm);
        }
        return normalized;
    }

    /**
     * Calculates Eigenvector centrality of the network and updates users.
     *
     * @param numSimulations Error rate ist almost 0 after 10 iterations
     */
    public void eigenvectorCentrality(int numSimulations) {

        int size = this.users.size();
        double[] b_k = new double[size];
        double[] b_k1 = new double[size];
        Arrays.fill(b_k, new Random().nextDouble());

        for (int i = 0; i < numSimulations; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    b_k1[j] += this.networkMatrix[j][k] * b_k[k];
                }
            }
            b_k = normalizeVector(b_k1);

        }

        //int position = 0;
        for (User user : this.users) {
            int position = user.getPositionMatrix();
            user.setEigenvector(Math.round(b_k[position] * 1000.0) / 1000.0);
        }
    }


    public static AbstractMap.SimpleEntry<Integer, List<List<Integer>>> calculateCliques(int[][] networkMatrix){

        /* To generate 3-cliques from 2-cliques we take each combination pair of 2-cliques and take intersection of the pair,
         if the intersection is an edge and it is present in the graph then the union of the pair is a clique of size 3.
         By doing intersection of the pair we find the missing edge so that the 2-clique can be extended to 3-clique,
         and if the edge is present in the graph then we extend the 2-clique pair into 3-clique and store it.
         In similar way we generate k+1-clique from k-clique.
         */

        /* stores all cliques of all sizes. The key is the size of the cliques. The value
         holds list of indexeslists.*/

            if (networkMatrix == null) {
                return null;
            }
            List<AbstractMap.SimpleEntry<Integer, List<List<Integer>>>> allCliques = new ArrayList<>();
            // stores the cliques of the actual size. Start with Cliques of 2 vertices (like edges)
            List<List<Integer>> cliques = new ArrayList<>();
            // stores the edges of the network graph
            List<List<Integer>> edges = new ArrayList<>();
            for (int i = 0; i < networkMatrix.length; i++) {
                for (int j = i + 1; j < networkMatrix.length; j++) {
                    if (networkMatrix[i][j] != 0 && i != j) {
                        cliques.add(Arrays.asList(i, j));
                        edges.add(Arrays.asList(i, j));
                    }
                }
            }
            int k = 2;
            while (!cliques.isEmpty()) {

                // we add all the cliques found of the previous size (k - 1) (start with 2)
                allCliques.add(new AbstractMap.SimpleEntry<>(k, cliques));
                // stores the cliques of the actual size K
                Set<List<Integer>> cliques1 = new LinkedHashSet<>();
                List<AbstractMap.SimpleEntry<List<Integer>, List<Integer>>> combinations = new ArrayList<>();

                // get all pairs possible of the found cliques (because k+1-cliques are constructed from connections between k-cliques)
                for (List<Integer> cliks1 : cliques) {
                    for (List<Integer> cliks2 : cliques) {
                        //List<AbstractMap.SimpleEntry<List<Integer>,List<Integer>>> pairsofLists = new ArrayList<>();
                        AbstractMap.SimpleEntry<List<Integer>, List<Integer>> pair = new AbstractMap.SimpleEntry<>(cliks1, cliks2);
                        if (!cliks1.equals(cliks2) && !combinations.contains(pair) &&
                                !combinations.contains(new AbstractMap.SimpleEntry<>(cliks2, cliks1))) {
                            combinations.add(pair);
                        }
                    }
                }
                //find the vertices that dont exist in both cliques
                for (AbstractMap.SimpleEntry<List<Integer>, List<Integer>> pair : combinations) {
                    List<Integer> differences = new ArrayList<>();
                    for (Integer integer : pair.getKey()) {
                        if (!pair.getValue().contains(integer)) {
                            differences.add(integer);
                        }
                    }
                    for (Integer integer : pair.getValue()) {
                        if (!pair.getKey().contains(integer)) {
                            differences.add(integer);
                        }
                    }

                /* check if there were 2 vertices found and that their edge exist in the network graph. If so then develop the clique
                with that edge*/

                    Collections.sort(differences);
                    //System.out.println(pair);
                    //System.out.println(differences);
                    Set<Integer> set = new LinkedHashSet<>(pair.getKey());
                    set.addAll(differences);
                    List<Integer> combined = new ArrayList<>(set);
                    //System.out.println(combined);
                    if (differences.size() == 2 && edges.contains(differences)) {
                        cliques1.add(combined);
                    }
                }

                // remove duplicate cliques. For example the clique {1,2,5} is the same as {2,1,5} or {5,1,2}...
                for (List<Integer> list : cliques1) {
                    Collections.sort(list);
                }
                Set<List<Integer>> noDup = new LinkedHashSet<>(cliques1);
                cliques = new ArrayList<>(noDup);

                // increment k to look for bigger cliques
                k++;
            }

        /* all found cliques of all sizes are now stored in allCliques. We access only the last element as it contains the
        cliques with the largest size. Then we store the users (vertice) that make those cliques (through their position in matrix)*/

        //Set<List<User>> usersInMaxCliques = new LinkedHashSet<>();
        AbstractMap.SimpleEntry<Integer, List<List<Integer>>> maxClique = allCliques.get(allCliques.size() - 1);
        return maxClique;
    }

    /**
     * finds the largest cliques in the network graph(matrix)
     *
     * @return the list of the users that make every largest clique. The size of the clique is the size of the list of users
     */

    public Set<List<User>> cliqueAnalysis() {

        Set<List<User>> usersInMaxCliques = new LinkedHashSet<>();
        AbstractMap.SimpleEntry<Integer, List<List<Integer>>> maxClique = calculateCliques(this.networkMatrix);
        System.out.printf("Largest Cliques have size %d. Each clique has following users:  \n", maxClique.getValue().get(0).size());
        int j = 1;
        for (List<Integer> indexes : maxClique.getValue()) {
            System.out.print("Clique"+ j+ " consists of indices: ");
            for (Integer index : indexes){

                System.out.print(index+ ", ");
            }
            System.out.println();
            j++;

        }
        for (User user : this.users){
            user.setCliqueIDs("");
        }
        int i = 1;
        for (List<Integer> clique : maxClique.getValue()) {
            List<User> userInClique = new ArrayList<>();
            for (Integer index : clique) {
                User user = getUserforIndex(index);
                user.setCliqueIDs(user.getCliqueIDs() + i + ",");
                userInClique.add(user);
            }
            i++;
            usersInMaxCliques.add(userInClique);
        }
        for (User user : this.users) {
            String[] values = user.getCliqueIDs().split(",");
            List<String> valuesAsList = new ArrayList<>(Arrays.asList(values));
            Set<String> noDup = new LinkedHashSet<>(valuesAsList);
            String CliqueIDs = "";
            for (String str : noDup) {
                CliqueIDs += str + ",";
            }
            user.setCliqueIDs(CliqueIDs);

        }

        // console output
        System.out.println();
        System.out.printf("Largest Cliques have size %d. Each clique has following users:  \n", maxClique.getValue().get(0).size());
         j = 0;
        for (List<User> users : usersInMaxCliques) {
            System.out.printf("Clique %d with users: ", j + 1);
            for (User user : users) {
                System.out.printf("%s,%s,index: %d ", user.getName(),user.getCliqueIDs(),user.getPositionMatrix());
            }
            System.out.println();
            j++;
        }
        System.out.println();
        //System.out.println("Result:\n" + allCliques);
        //return a set the contains the users of each maximal clique found
        return usersInMaxCliques;
    }

    /**
     * Calculates degree centrality of the network matrix and updates users.
     *
     * @param weighted if false only number of adjacent edges is counted.
     */
    public void degreeCentrality(boolean weighted) {

        int size = this.users.size();

        for (User u : this.users) {
            int row = u.getPositionMatrix();
            int degree = 0;

            if (weighted) {
                for (int col = 0; col < size; col++) {
                    degree += this.networkMatrix[row][col];
                }
            } else {
                for (int col = 0; col < size; col++) {
                    if (this.networkMatrix[row][col] != 0) {
                        ++degree;
                    }
                }
            }
            u.setDegree(degree);
        }
        /*System.out.println(Arrays.deepToString(this.users.toArray())
                .replaceAll(",", System.getProperty("line.separator")));*/
    }

    /**
     * calculates density of whole network
     */
    public void networkDensity() {

        int numEdges = 0;
        int numVertices = this.users.size();

        for (int row = 0; row < numVertices - 1; row++) {
            for (int col = row + 1; col < numVertices; col++) {
                if (this.networkMatrix[row][col] != 0) {
                    ++numEdges;
                }
            }
        }

        double density = 2 * numEdges / (double) (numVertices * (numVertices - 1));
        System.out.println("Edges: " + numEdges);
        System.out.println("Network density: " + density);
        // TODO insert value in network class
    }

    public void printUsers() {
        if (this.users != null) {
            System.out.println(Arrays.deepToString(this.users.toArray())
                    .replaceAll(",", System.getProperty("line.separator")));
        }
    }

    public void printMatrix() {
        if (this.networkMatrix != null) {
            System.out.println(Arrays.deepToString(this.networkMatrix).replaceAll("],", "]," + System.getProperty("line.separator")));
        }
    }

}


import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.min;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import static javafx.scene.input.KeyCode.N;

public class MaxFlowProblem {
    
    
     int c;
     int n;
     int s;
     int t;
     int w ;

    private int[] nodeId;    // nodeId[i] = parent of i
    private int count;   // number of components

    public MaxFlowProblem(int nodes) {
        nodeId = new int[nodes];
        count = nodes;

        for (int i = 0; i < nodes; i++) {
            nodeId[i] = i;
        }
    }

    
    
    
    public static class Graph {
        
        int vertices;
        int graph[][];
        
        //graph consists of vertex index, vetex value
        public Graph(int vertex, int[][] graph) {
            this.vertices = vertex;
            this.graph = graph;
        }
        
        
            public void printGraph() {
            System.out.println("Graph: (Adjacency Matrix)");
                for (int i = 0; i < vertices; i++) {
                    for (int j = 0; j <vertices ; j++) {
                        System.out.print(graph[i][j]+ " ");
            }
            System.out.println();
        }
    }
        
        
        
            public int findMaxFlow(int source, int sink) {
                
            //residual graph
            // residual flow - o ile mozna zwiekszyc przeplyw
            // tak zeby nie przekroczyl jej przepustkowosci
            // do sieci (grafu naleza te krawedzie przez ktore przeplyw
            //mozna zwiekszyc)
            
            //updated graph
            int[][] residualGraph = new int[vertices][vertices];

            //initialize residual graph same as original graph
            // same number of verices 
            
            for (int i = 0; i <vertices ; i++) {
                for (int j = 0; j <vertices ; j++) {
                    residualGraph[i][j] = graph[i][j];
               
                }
            }
            
            
             //initialize parent [] to store the path Source to destination
            // path is created by conneciting parent with child node and so on
           //so that the source and sink are connected
            int [] parent = new int[vertices];

            int max_flow = 0; //initialize the max flow
            
            ///bfs
            while(isPathExist_BFS(residualGraph, source, sink, parent)){
                //if here means still path exist from source to destination

                //parent [] will have the path from source to destination
                //find the capacity which can be passed though the path (in parent[])

                int flow_capacity = Integer.MAX_VALUE;

                int t = sink;
                while(t!=source){
                    
                    int s = parent[t];
                    flow_capacity = Math.min(flow_capacity, residualGraph[s][t]);
                    t = s;
                 
                }

                //update the residual graph
                //reduce the capacity on forwaerd edge by flow_capacity
                //add the capacity on back edge by flow_capacity
                t = sink;
                while(t!=source){
                    int s = parent[t];
                    residualGraph[s][t]-=flow_capacity;
                    residualGraph[t][s]+=flow_capacity;
                    t = s;
                }

                //add flow_capacity to max value
                max_flow+=flow_capacity;
            }
            return max_flow;
        }

        public boolean isPathExist_BFS(int [][] residualGraph, int src, int dest, int [] parent){
            boolean pathFound = false;

            //create visited array [] to
            //keep track of visited vertices
            boolean [] visited = new boolean[vertices];

            //Create a queue for Breadth-Firts Search
            Queue<Integer> queue = new LinkedList<>();

            //insert the source vertex, mark it visited
            //beacause it alway starts from that 
            //add it ti the queue
            queue.add(src);
            //parent is always the node that is behind 
            parent[src] = -1;
            visited[src] = true;
            
            
            //while queque 
            while(queue.isEmpty()==false){
                int u = queue.poll();

                //visit all the adjacent vertices
                for (int v = 0; v <vertices ; v++) {
                    //if vertex is not already visited and u-v edge weight >0
                    if(visited[v]==false && residualGraph[u][v]>0) {
                        queue.add(v);
                        parent[v] = u;
                        visited[v] = true;
                    }
                }
            }
            //check if dest is reached during BFS
            pathFound = visited[dest];
            return pathFound;
        }   
    }
    
    
    
    
    
    //create class Edge
    private static class Edge {
        public int from, to;
        public Edge residual;
        public long flow;
        public final long capacity;
        
        
        public Edge(int from, int to, long capacity) {
          this.from = from;
          this.to = to;
          this.capacity = capacity;
        }
        
        //checking if is residual
        public boolean isResidual() {
            return capacity == 0;
        }
        
        
        //capacity left
        public long remainingCapacity() {
            return capacity - flow;
        }
        
        public void augment(long bottleNeck) {
            flow += bottleNeck;
            residual.flow -= bottleNeck;
        }
        
        
        public String toString(int s, int t) {
            String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
            String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
            
            return String.format(
          "Edge %s -> %s | flow = %3d | capacity = %3d | is residual: %s",
          u, v, flow, capacity, isResidual());
            
    }
}
    
    
    
    
        private abstract static class linkFlow {
        
        // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
            static final long INF = Long.MAX_VALUE / 2;
            
            
        // Inputs: n = number of nodes, s = source, t = sink
            final int n, s, t;
            
            
        // 'visited' and 'visitedToken' are variables used in graph sub-routines to
        // track whether a node has been visited or not. In particular, node 'i' was
        // recently visited if visited[i] == visitedToken is true. This is handy
        // because to mark all nodes as unvisited simply increment the visitedToken.
    
            private int visitedToken = 1;
            private int[] visited;
            
            
            // Indicates whether the network flow algorithm has ran. The solver only
            // needs to run once because it always yields the same result.
            protected boolean solved;

            // The maximum flow. Calculated by calling the {@link #solve} method.
            protected long maxFlow;

            // The adjacency list representing the flow graph.
            protected List<Edge>[] graph;
            
            
            public linkFlow(int n, int s, int t) {
                this.n = n;
                this.s = s;
                this.t = t;
                initializeEmptyFlowGraph();
                visited = new int[n];          
            }
            
             // Constructs an empty graph with n nodes including s and t.
            private void initializeEmptyFlowGraph() {
                graph = new List[n];
                for (int i = 0; i < n; i++) graph[i] = new ArrayList<MaxFlowProblem.Edge>();
            }

            public void addEdge(int from, int to, long capacity) {
                
            if (capacity <= 0) throw new IllegalArgumentException("Forward edge capacity <= 0");
                Edge e1 = new Edge(from, to, capacity);
                Edge e2 = new Edge(to, from, 0);
                e1.residual = e2;
                e2.residual = e1;
                graph[from].add(e1);
                graph[to].add(e2);
            }
            
            
            public void deleteEdge(int from, int to, long capacity){
                
                if (capacity <= 0) throw new IllegalArgumentException("Forward edge capacity <= 0");
                Edge e1 = new Edge(from, to, capacity);
                Edge e2 = new Edge(to, from, 0);
                e1.residual = e2;
                e2.residual = e1;
                graph[from].remove(e1);
                graph[to].remove(e2);
            }
            
            
            public List<MaxFlowProblem.Edge>[] getGraph() {
                execute();
                return graph;
            }

            // Returns the maximum flow from the source to the sink.
            public long getMaxFlow() {
                execute();
                return maxFlow;
            }

            // Marks node 'i' as visited.
            public void visit(int i) {
                visited[i] = visitedToken;
            }

            // Returns true/false depending on whether node 'i' has been visited or not.
            public boolean visited(int i) {
                return visited[i] == visitedToken;
            }

            // Resets all nodes as unvisited. This is especially useful to do
            // between iterations finding augmenting paths, O(1)
            public void markAllNodesAsUnvisited() {
                visitedToken++;
            }

            // Wrapper method that ensures we only call solve() once
            private void execute() {
                if (solved) return;
                    solved = true;
                    solve();
            }

            // Method to implement which solves the network flow problem.
            public abstract void solve();

        private void getGraph(int c) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

        private static class Algo extends linkFlow {
        
            public Algo(int n, int s, int t) {
                super(n, s, t);
            }
            
            @Override
            public void solve() {
                long flow;
                    do {
                        markAllNodesAsUnvisited();
                        flow = bfs();
                        maxFlow += flow;
                    } while (flow != 0);           
            }
            
            private long bfs() {
                  Queue<Integer> q = new ArrayDeque<>(n);
                  visit(s);
                  q.offer(s);

                  // Perform BFS from source to sink
                  MaxFlowProblem.Edge[] prev = new MaxFlowProblem.Edge[n];
                        while (!q.isEmpty()) {
                            int node = q.poll();
                            if (node == t) break;

                            for (MaxFlowProblem.Edge edge : graph[node]) {
                                long cap = edge.remainingCapacity();
                                if (cap > 0 && !visited(edge.to)) {
                                    visit(edge.to);
                                    prev[edge.to] = edge;
                                    q.offer(edge.to);
                                }
                            }
                        }

                        // Sink not reachable!
                        if (prev[t] == null) return 0;

                        // Find augmented path and bottle neck
                        long bottleNeck = Long.MAX_VALUE;
                        for (MaxFlowProblem.Edge edge = prev[t]; edge != null; edge = prev[edge.from])
                            bottleNeck = min(bottleNeck, edge.remainingCapacity());

                        // Retrace augmented path and update flow values.
                        for (MaxFlowProblem.Edge edge = prev[t]; edge != null; edge = prev[edge.from]) edge.augment(bottleNeck);

                        // Return bottleneck flow
                
                        return bottleNeck;
                    
                        
                        }
}
            
        
        
    
    
    
    
    
    public int count() {
        return count;
    }
    

    
    public boolean exit(){
           
        boolean exit = true;
        System.out.println("The end of the program.");
        return exit;
    }
    
    
    public boolean showMenu(){
        
        boolean exit = false;
        
        System.out.println("Menu");
        System.out.println("1.Search and modify link/node");
        System.out.println("2.Add link/node");
        System.out.println("3.Delete link/node");
        System.out.println("4.Find max flow");
        System.out.println("5.Exit");
     
        
        System.out.print("Your choice: ");
        Scanner s = new Scanner(System.in);
        int choiceShowMenu = s.nextInt(); // users input - selected option
        

        switch (choiceShowMenu){
            case 1:
                search();
                showMenu();
                break;
            case 2:
                add();
                showMenu();
                break;
            case 3:
                delete();
                showMenu();
                break;
            case 4:
                findMaxFlow();
                showMenu();
                break;
            case 5:
                exit();
                break;
            default:
                System.out.println("Wrong input. Try again");
                showMenu();    
                break;
        }
        return exit;
    }

    //search node/link
    public void search() {

        System.out.println("Do you want to search for: ");
        System.out.println("1. Node");
        System.out.println("2. Link");
        
        System.out.print("Your choice: ");
        Scanner b = new Scanner(System.in);
        int choiceSearch = b.nextInt();
        
        
        switch (choiceSearch){
            case 1:
                System.out.println("Choose a node");
                System.out.print("Your choice: ");
                Scanner x = new Scanner(System.in);
                int choiceSearchNode = x.nextInt(); 
                
                System.out.println("Node: " + choiceSearchNode);
                System.out.println("1. Delete");
                System.out.println("2. Exit");
                
                
                
                break;
                 
                  
                  
            case 2:
                System.out.println("Choose a link");
                System.out.print("Your choice: ");
                Scanner y = new Scanner(System.in);
                int choiceSearchLink = y.nextInt();
                
                
                
                
                
                System.out.println("Link: " + choiceSearchLink);
                System.out.println("1.Show and calculate capacity");
                System.out.println("2.Change capacity");
                System.out.println("3.Calculate flow network");
                
                Scanner z = new Scanner(System.in);
                int choiceSearchLinkMenu = z.nextInt();
                
                switch(choiceSearchLinkMenu){
                    
                    case 1:
                        calculateCap();
                         break;
                    case 2:
                        changeCap();
                         break;
                    case 3:
                        calculateFlow();
                        break;
                    default:
                        
                        System.out.println("Wrong input.");
                
                        System.out.println("1.Try again");
                        System.out.println("2.Go back");
                        System.out.println("3.Exit");
                        
                        Scanner a = new Scanner(System.in);
                        int choiceSearchMenuWrong = a.nextInt();
                
                        switch(choiceSearchMenuWrong){
                        case 1:
                            search();
                             break;
                        case 2:
                            showMenu();
                             break;
                        case 3:
                            exit();
                             break;
                    } 
                        
                        
                        
                }
              
                break;
             
                
            default:
                System.out.println("Wrong input.");
                
                System.out.println("1.Try again");
                System.out.println("2.Go back");
                System.out.println("3.Exit");
                
                Scanner c = new Scanner(System.in);
                int choiceSearchWrong = c.nextInt();
                
                switch(choiceSearchWrong){
                    case 1:
                        search();
                         break;
                    case 2:
                        showMenu();
                         break;
                    case 3:
                        exit();
                         break;
                } 
                
        }
        
    }

    //delete node/link
    public void delete() {
        
        
        //irrelevant to adding 
        //methods that enters the structure you are using
        
        // 1. search
        // 2. visit all nodes - no node is left behind
        // 3. delete
        
        
        
        
        
        

        System.out.println("Do you want to delete a: ");
        System.out.println("1. Node");
        System.out.println("2. Link");
        
        System.out.print("Your choice: ");
        Scanner d = new Scanner(System.in);
        int choiceDelete = d.nextInt(); // users input - selected option
        
     
        
        switch (choiceDelete){
            case 1:
                System.out.println("Choose a node: ");
                Scanner e = new Scanner(System.in);
                int deleteNode = e.nextInt();
                
                System.out.println("Deleting " + deleteNode + " node from the graph...");
                 break;
            case 2:
                System.out.println("Choose a link: ");
                Scanner f = new Scanner(System.in);
                int deleteLink = f.nextInt();
                 
                 System.out.println("Deleting " + deleteLink + " link from the graph...");
                  break;
  
            default:
                System.out.println("Wrong input.");
                System.out.println("1.Try again");
                System.out.println("2.Go back");
                System.out.println("3.Exit");
                
                 Scanner g = new Scanner(System.in);
                 int deleteNodeWrong = g.nextInt();
                 
                 switch(deleteNodeWrong){
                     
                     case 1:
                         delete();
                          break;
                     case 2:
                         showMenu();
                          break;
                     case 3:
                         exit();
                          break;
                 }
                  break;
    }
  
    }

    //add node/link
    public void add() {

        System.out.println("Do you want to add a: ");
        System.out.println("1. Node");
        System.out.println("2. Link");
        
             
        System.out.print("Your choice: ");
        Scanner h = new Scanner(System.in);
        int choiceAdd = h.nextInt(); // users input - selected option
  
        
        switch (choiceAdd){
            case 1:
                System.out.println("Add a node");
                break;
                
            case 2:
                System.out.println("Add a link");
                System.out.println("From node : ");
                Scanner ads = new Scanner(System.in);
                int choiceAddSource = ads.nextInt();

                System.out.println("To : ");
                Scanner adt = new Scanner(System.in);
                int choiceAddSink = adt.nextInt();
                
                System.out.println("Capacity : ");
                Scanner cap = new Scanner(System.in);
                long choiceAddCap = cap.nextLong();
              
                break;
                
            default:
                System.out.println("Wrong input.");
                System.out.println("1.Try again");
                System.out.println("2.Go back");
                System.out.println("3.Exit");
                
                Scanner i = new Scanner(System.in);
                int choiceAddWrong = i.nextInt();
                
                switch (choiceAddWrong){
                    case 1:
                        add();
                        break;
                    case 2:
                        showMenu();
                        break;
                    case 3:
                        exit();
                        break;
                }
                 break;
        }

    }

    //calculate capacity
    public void calculateCap() {
        System.out.println("Calculating capacity of the network...");

    }

    //calculate flow network
    public void calculateFlow() {
        System.out.println("Calculating flow of the network...");
    }

    //find max flow
    public void findMaxFlow() {

        System.out.println("Finding max flow...");
        demoPath();

    }

    //demonstrate path of maximum flow 
    public void demoPath() {

        System.out.println("The path of the maximum flow");
        //DFS
        //Stack

    }


    //re-run the agorithm
    //change capacity of a link
    public void changeCap() {

        System.out.println("Changing capacity of the link");

    }

    //re-run the algorithm
    //change the dataset
    public void changeData() {

        System.out.println("New dataset");
        chooseData();

    }

    //measure time - stop watch
    public void measureTime() {
        //to start
        Stopwatch timer = new Stopwatch();
        //to print
        StdOut.println("Elapsed time = " + timer.elapsedTime() + " seconds");

    }
    
    
    public boolean chooseData(){
        
             
        boolean exit = false; 
        
        
        
        System.out.println("Choose a data set. Press number from the menu: ");
        System.out.println(" ");
        System.out.println("1. 6 nodes ");
        System.out.println("2. 12 nodes");
        System.out.println("3. 24 nodes");
        System.out.println("4. 48 nodes");
        System.out.println(" ");

        System.out.print("Your choice: ");
        Scanner m = new Scanner(System.in);
        int choice = m.nextInt(); // users input - selected option

        
            
            switch(choice){

                case 1:

                System.out.println("You have chosen small datset.");
                Scanner input = null;
                try {
                    input = new Scanner(new File("1dataset.txt"));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // END of IDE version

                // int N = StdIn.readInt();  <- ENABLE if reading from console or terminal
                int nodes = input.nextInt(); // ONLY for IDE version

                System.out.println("Number of nodes is: " + nodes);
                MaxFlowProblem smallData = new MaxFlowProblem(nodes);
                
                //showMenu();
                
                  c = 0;
                  n = 7;
                  s = n - 2;
                  t = n - 1;
                  w = 0;

                linkFlow solver = new Algo(n, s, t);
                Stopwatch timer = new Stopwatch();
                // while (!StdIn.isEmpty()) {   <- ENABLE ONLY if reading from console or terminal
                while (input.hasNextInt()) { // ONLY for IDE version, remove otherwise
                    
              
                // int p = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
                 s = input.nextInt(); // ONLY for IDE version, remove otherwise
                // int q = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
    		 t = input.nextInt(); // ONLY for IDE version, remove otherwise
                 w = input.nextInt();
                 solver.addEdge(s, t, w);
                 c++;
                 
            
            
                StdOut.println("Edge " + c + ". from: " + s + " to: " + t + " weight: " + w );
                }

                
                int graph[][] = 
                { {0, 10, 8, 0, 0, 0},
                {0, 0, 5, 5, 0, 0},
                {0, 4, 0, 0, 10, 0},
                {0, 0, 9, 0, 10, 3},
                {0, 0, 0, 6, 0, 14},
                {0, 0, 0, 0, 0, 0}
                };
                
                //graph representation
                
                System.out.println("-------------------------------------------");
                Graph g = new Graph(nodes, graph);
                int max_flow = g.findMaxFlow(s,t);
                System.out.printf("Maximum Flow is: %d\n", max_flow-2);
                StdOut.println("Elapsed time = " + timer.elapsedTime() + " seconds");
                System.out.println("-------------------------------------------");
                
              
                g.printGraph();
                
                


        System.out.println("-------------------------------------------");
        System.out.println("Menu");
        System.out.println("1.Search and modify link/node");
        System.out.println("2.Add link/node");
        System.out.println("3.Delete link/node");
        System.out.println("4.Find max flow");
        System.out.println("5.Exit");
     
        
        System.out.print("Your choice: ");
        Scanner r = new Scanner(System.in);
        int choiceShowMenu = r.nextInt(); // users input - selected option
        
        switch (choiceShowMenu){
            case 1:
                
                System.out.println("Do you want to search for: ");
                System.out.println("1. Link");
        
                System.out.print("Your choice: ");
                Scanner b = new Scanner(System.in);
                int choiceSearch = b.nextInt();
        
        
                switch (choiceSearch){
    
                  
                case 1:
                System.out.println("Choose a link");
                System.out.print("From: ");
                Scanner y = new Scanner(System.in);
                int choiceSearchLinkF = y.nextInt();
                
                System.out.print("To: ");
                Scanner u = new Scanner(System.in);
                int choiceSearchLinkT = u.nextInt();
                
                System.out.print("With capacity: ");
                Scanner i = new Scanner(System.in);
                int choiceSearchLinkCap = i.nextInt();
                

                System.out.println("Link from: " + choiceSearchLinkF + " to:" + choiceSearchLinkT + " with capacity: " + choiceSearchLinkCap);
                
                max_flow = g.findMaxFlow(s,t);
                System.out.printf("Maximum Flow is: %d\n", max_flow-2);
                
                
                System.out.println("Press 1 to: ");
                System.out.println("1.Change capacity");

                
                Scanner z = new Scanner(System.in);
                int choiceSearchLinkMenu = z.nextInt();
                
                switch(choiceSearchLinkMenu){
                    
                
                case 1:
                       
                Scanner d = new Scanner(System.in);
                solver.deleteEdge(choiceSearchLinkF, choiceSearchLinkT, choiceSearchLinkCap);
                System.out.println("Enter new capacity: ");
                Scanner q = new Scanner(System.in);
                int choiceSearchLinkMenuChange = q.nextInt();
                solver.addEdge(choiceSearchLinkF, choiceSearchLinkT,choiceSearchLinkMenuChange);
                System.out.println("Link from: " + choiceSearchLinkF + " to: " + choiceSearchLinkT + " with capacity: " + choiceSearchLinkMenu);
                
                
                
                max_flow = g.findMaxFlow(s,t);
                System.out.printf("Maximum Flow is: %d\n", max_flow-2);
                break;
                    
                    default:
                        System.out.println("Wrong input.");
                
                        System.out.println("1.Try again");
                        System.out.println("2.Go back");
                        System.out.println("3.Exit");
                        
                        Scanner a = new Scanner(System.in);
                        int choiceSearchMenuWrong = a.nextInt();
                
                        switch(choiceSearchMenuWrong){
                        case 1:
                            search();
                             break;
                        case 2:
                            showMenu();
                             break;
                        case 3:
                            exit();
                             break;
                    } 
                        
                        
                        
                }
              
                break;
             
                
            default:
                System.out.println("Wrong input.");
                
                System.out.println("1.Try again");
                System.out.println("2.Go back");
                System.out.println("3.Exit");
                
                Scanner c = new Scanner(System.in);
                int choiceSearchWrong = c.nextInt();
                
                switch(choiceSearchWrong){
                    case 1:
                        search();
                         break;
                    case 2:
                        showMenu();
                         break;
                    case 3:
                        exit();
                         break;
                } 
                
        }
               
                
                
                
                showMenu();
                break;
            case 2:
                
                
                
        System.out.println("Do you want to add a: ");
        System.out.println("1. Node");
        System.out.println("2. Link");
        
             
        System.out.print("Your choice: ");
        Scanner h = new Scanner(System.in);
        int choiceAdd = h.nextInt(); // users input - selected option
  
        
        switch (choiceAdd){
            case 1:
                
                n = n-1;
                n++;
                System.out.println("Number of nodes: " + n);
                System.out.println("----------------------");
                showMenu();
                break;
                
            case 2:
                
                 System.out.println("Add a link ");
                 Scanner o = new Scanner(System.in);
                 
                 System.out.println("From: ");
                 s = o.nextInt(); // users input - selected option
                // s = input.nextInt(); // ONLY for IDE version, remove otherwise
                // int q = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
                 System.out.println("To: ");
    		 t = o.nextInt(); // ONLY for IDE version, remove otherwise
                 System.out.println("Capacity: ");
                 w = o.nextInt();
                 solver.addEdge(s, t, w);
                 c++;

                StdOut.println("Edge " + c + ". from: " + s + " to: " + t + " weight: " + w );
                
                max_flow = g.findMaxFlow(s,t);
                System.out.printf("Maximum Flow is: %d\n", max_flow-2);
                
                showMenu();
                break;
                
            default:
                System.out.println("Wrong input.");
                System.out.println("1.Try again");
                System.out.println("2.Go back");
                System.out.println("3.Exit");
                
                Scanner i = new Scanner(System.in);
                int choiceAddWrong = i.nextInt();
                
                switch (choiceAddWrong){
                    case 1:
                        add();
                        break;
                    case 2:
                        showMenu();
                        break;
                    case 3:
                        exit();
                        break;
                }
                 break;
        }


              
            case 3:
                //delete();
                System.out.println("Delete");
                Scanner d = new Scanner(System.in);
                 
                System.out.println("From: ");
                s = d.nextInt(); // users input - selected option
                // s = input.nextInt(); // ONLY for IDE version, remove otherwise
                // int q = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
                System.out.println("To: ");
    		t = d.nextInt(); // ONLY for IDE version, remove otherwise
                System.out.println("Capacity: ");
                w = d.nextInt();
                solver.deleteEdge(s, t, w);
                StdOut.println("Deleting edge from: " + s + " to: " + t + " weight: " + w );
                
                max_flow = g.findMaxFlow(s,t);
                System.out.printf("Maximum Flow is: %d\n", max_flow-2);
     
                
                
                break;
            case 4:
                max_flow = g.findMaxFlow(s,t);
                System.out.printf("Maximum Flow is: %d\n", max_flow-2);
                break;
            case 5:
                exit();
                break;
            default:
                System.out.println("Wrong input. Try again");
                showMenu();    
                break;
        }
        return exit;
                
        
                case 2: 

                System.out.println("You have chosen medium datset.");
                
                Scanner input1 = null;
                try {
                    input1 = new Scanner(new File("2dataset.txt"));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // END of IDE version

                // int N = StdIn.readInt();  <- ENABLE if reading from console or terminal
                nodes = input1.nextInt(); // ONLY for IDE version

                System.out.println("Number of nodes is: " + nodes);
                MaxFlowProblem mediumData1 = new MaxFlowProblem(nodes);
                
                  c = 0;
                  n = 13;
                  s = n - 2;
                  t = n - 1;
                  

                linkFlow solver1 = new Algo(n, s, t);
                Stopwatch timer1 = new Stopwatch();
    	
                // while (!StdIn.isEmpty()) {   <- ENABLE ONLY if reading from console or terminal
                while (input1.hasNextInt()) { // ONLY for IDE version, remove otherwise
                // int p = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
                 s = input1.nextInt(); // ONLY for IDE version, remove otherwise
                // int q = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
    		 t = input1.nextInt(); // ONLY for IDE version, remove otherwise
                 w = input1.nextInt();
                 solver1.addEdge(s, t, w);
                 c++;
                
                StdOut.println("Edge " + c + ". from: " + s + " to: " + t + " weight: " + w );
                }
                System.out.println(" ");
                StdOut.println("Elapsed time = " + timer1.elapsedTime() + " seconds");
                System.out.println("-------------------------------------------");
                showMenu();
                break;


            

                case 3: 

                System.out.println("You have chosen large datset.");
                
                
                Scanner input2 = null;
                try {
                    input2 = new Scanner(new File("3dataset.txt"));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // END of IDE version

                // int N = StdIn.readInt();  <- ENABLE if reading from console or terminal
                nodes = input2.nextInt(); // ONLY for IDE version

                System.out.println("Number of nodes is: " + nodes);
                MaxFlowProblem largeData = new MaxFlowProblem(nodes);
                
                 
                  c = 0;
                  n = 25;
                  s = n - 2;
                  t = n - 1;
                  

                linkFlow solver2 = new Algo(n, s, t);
                Stopwatch timer2 = new Stopwatch();
    	
                // while (!StdIn.isEmpty()) {   <- ENABLE ONLY if reading from console or terminal
                while (input2.hasNextInt()) { // ONLY for IDE version, remove otherwise
                // int p = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
                 s = input2.nextInt(); // ONLY for IDE version, remove otherwise
                // int q = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
    		 t = input2.nextInt(); // ONLY for IDE version, remove otherwise
                 w = input2.nextInt();
                 solver2.addEdge(s, t, w);
                 c++;
                
                StdOut.println("Edge " + c + ". from: " + s + " to: " + t + " weight: " + w );
                }
                System.out.println(" ");
                StdOut.println("Elapsed time = " + timer2.elapsedTime() + " seconds");
                System.out.println("-------------------------------------------");
                showMenu();
                break;
                
                case 4:
                    
                System.out.println("You have chosen the largest datset.");
                
                
                Scanner input3 = null;
                try {
                    input3 = new Scanner(new File("3dataset.txt"));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // END of IDE version

                // int N = StdIn.readInt();  <- ENABLE if reading from console or terminal
                nodes = input3.nextInt(); // ONLY for IDE version

                System.out.println("Number of nodes is: " + nodes);
                MaxFlowProblem largestData = new MaxFlowProblem(nodes);
                
                
                c = 0;
                n = 49;
                s = n - 2;
                t = n - 1;
                  

                linkFlow solver3 = new Algo(n, s, t);
                Stopwatch timer3 = new Stopwatch();
    	
                // while (!StdIn.isEmpty()) {   <- ENABLE ONLY if reading from console or terminal
                while (input3.hasNextInt()) { // ONLY for IDE version, remove otherwise
                // int p = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
                 s = input3.nextInt(); // ONLY for IDE version, remove otherwise
                // int q = StdIn.readInt(); <- ENABLE ONLY if reading from console or terminal
    		 t = input3.nextInt(); // ONLY for IDE version, remove otherwise
                 w = input3.nextInt();
                 solver3.addEdge(s, t, w);
                 c++;
                
                StdOut.println("Edge " + c + ". from: " + s + " to: " + t + " weight: " + w );
                }
                System.out.println(" ");
                StdOut.println("Elapsed time = " + timer3.elapsedTime() + " seconds");
                System.out.println("-------------------------------------------");
                break;

                default:
                    
                    
                    System.out.print("Wrong input. ");
                    System.out.println("1. Try again");
                    System.out.println("2. Exit");
                    
                    Scanner o = new Scanner(System.in);
                    int choiceDataWrong = o.nextInt();
                    
                    switch(choiceDataWrong){
                        case 1:
                            chooseData();
                             break;
                        case 2:
                            exit();
                            break;
                    }        
    }
    return exit;
}

    public static void main(String[] args) {
        
        //6 - is just default value
        MaxFlowProblem m = new MaxFlowProblem(6);
        m.chooseData();

    }

    }





package maxflowproblem1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;


public class MaxFlowProblem1 {
    
    
    //list for all edges in the graph
    LinkedList<Edge> list = new LinkedList();
    private int numEdge;  
    
    public MaxFlowProblem1(int listLength){
        this.numEdge = listLength;
        list = new LinkedList<Edge>();
    }
    
    public MaxFlowProblem1(){
        this.list =list;
    }
    
    
    public void chooseDataset(){
        
      System.out.println("-----------------------------------------");
      System.out.println("Choose one of the following datasets: ");
      System.out.println(" ");
      System.out.println("1. small dataset");
      System.out.println("2. medium dataset");
      System.out.println("3. large dataset");
      System.out.println("4. largest dataset");
      System.out.println(" ");
      System.out.println("-----------------------------------------"); 
    }
    
    
    
    public void printGraphRep(int rows,int columns){
       System.out.println("Rows : " + rows + ", Columns : " + columns);
    }
    
    
    public void generateGraph(int rows, int columns,int V){
        
         int graph[][] = new int[rows][columns];
         int capacity;
        
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 1; j < columns; j++) {
                capacity = (int) (Math.random() * 5 + 1);
                if (j==i){
                    graph[i][j] = 0;  
                    Edge e = new Edge(i, j, 0);
                    list.add(e);
                    System.out.println(e);
                    numEdge++;
                }
                else{
                   
                    graph[i][j] = capacity;
                    //System.out.println("Edge from: " + i + " to: " + j + " with capacity: " + capacity);
                    //String str = ("Edge from: " + i + " to: " + j + " with capacity: " + capacity).toString();
                    //System.out.println(str);
                    Edge e = new Edge(i, j, capacity);
                    numEdge++;
                    list.add(e);
                    System.out.println(e);
                }
                     
            }
            System.out.println("-----------------------------------");
        }
        //System.out.println(Arrays.deepToString(graph));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(((graph[i][j] < 10) ? "0" : "") + (graph[i][j]) + " ");
            }
            System.out.println();
        }  
        
            //residual graph
            int[][] residualGraph = new int[V][V];

            //initialize residual graph same as original graph
            for (int i = 0; i <V ; i++) {
                for (int j = 0; j <V ; j++) {
                    residualGraph[i][j] = graph[i][j];
                }
            }

            //initialize parent [] to store the path Source to destination
            int [] parent = new int[V];
            int max_flow = 0; //initialize the max flow
            while(isPathExist_BFS(residualGraph, 0, V-1, parent, V)){
                //if here means still path exist from source to destination

                //parent [] will have the path from source to destination
                //find the capacity which can be passed though the path (in parent[])

                int flow_capacity = Integer.MAX_VALUE;

                int t = V-1;
                while(t!=0){
                    int s = parent[t];
                    flow_capacity = Math.min(flow_capacity, residualGraph[s][t]);
                    t = s;
                    
                }

                //update the residual graph
                //reduce the capacity on fwd edge by flow_capacity
                //add the capacity on back edge by flow_capacity
                t = V-1;
                while(t!=0){
                    int s = parent[t];
                    residualGraph[s][t]-=flow_capacity;
                    residualGraph[t][s]+=flow_capacity;
                    t = s;
                }
                

                //add flow_capacity to max value
                max_flow+=flow_capacity;
            }
            System.out.println("-----------------------------------");
            System.out.println("Maximum flow: " + max_flow);
            System.out.println("-----------------------------------");
            
            
            
        //DFS
        int h = graph.length;
        if (h == 0)
            return;
        int l = graph[0].length;
        boolean[][] visited = new boolean[h][l];
        Stack<String> stack = new Stack<>();

        stack.push(0 + "," + 0);

        System.out.println("Depth-First Traversal: ");
        while (stack.empty() == false) {

            String x = stack.pop();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);

            if(row<0 || col<0 || row>=h || col>=l || visited[row][col])
                continue;

            visited[row][col]=true;
            System.out.print(graph[row][col] + " ");
            stack.push(row + "," + (col-1)); //go left
            stack.push(row + "," + (col+1)); //go right
            stack.push((row-1) + "," + col); //go up
            stack.push((row+1) + "," + col); //go down
            
            
        }
        }

        public boolean isPathExist_BFS(int [][] residualGraph, int src, int dest, int [] parent,int V){
            boolean pathFound = false;

            //create visited array [] to
            //keep track of visited vertices
            boolean [] visited = new boolean[V];

            //Create a queue for BFS
            Queue<Integer> queue = new LinkedList<>();
            //insert the source vertex, mark it visited
            queue.add(src);
            parent[src] = -1;
            visited[src] = true;
            

            
            
            
            while(queue.isEmpty()==false){
                int u = queue.poll();

                //visit all the adjacent vertices
                for (int v = 0; v <V ; v++) {
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
        
        
        public void recalculateFlow(int rows,int columns, int V){
            
         int graph[][] = new int[rows][columns];
         int capacity;
        
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 1; j < columns; j++) {
                capacity = list.get(i).getCapacity();
               
                if (j==i)
                    graph[i][j] = 0;
                else
                   
                    graph[i][j] = capacity;
                    //System.out.println("Edge from: " + i + " to: " + j + " with capacity: " + capacity);
                    //String str = ("Edge from: " + i + " to: " + j + " with capacity: " + capacity).toString();
                    //System.out.println(str);
                    Edge e = new Edge(i, j, capacity);
                    list.add(e);
                    System.out.println(e);
                     
            }
            System.out.println("-----------------------------------");
        }
        //System.out.println(Arrays.deepToString(graph));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(((graph[i][j] < 10) ? "0" : "") + (graph[i][j]) + " ");
            }
            System.out.println();
        }  
        
            //residual graph
            int[][] residualGraph = new int[V][V];

            //initialize residual graph same as original graph
            for (int i = 0; i <V ; i++) {
                for (int j = 0; j <V ; j++) {
                    residualGraph[i][j] = graph[i][j];
                }
            }

            //initialize parent [] to store the path Source to destination
            int [] parent = new int[V];
            int max_flow = 0; //initialize the max flow
            while(isPathExist_BFS(residualGraph, 0, V-1, parent, V)){
                //if here means still path exist from source to destination

                //parent [] will have the path from source to destination
                //find the capacity which can be passed though the path (in parent[])

                int flow_capacity = Integer.MAX_VALUE;

                int t = V-1;
                while(t!=0){
                    int s = parent[t];
                    flow_capacity = Math.min(flow_capacity, residualGraph[s][t]);
                    t = s;
                    
                }

                //update the residual graph
                //reduce the capacity on fwd edge by flow_capacity
                //add the capacity on back edge by flow_capacity
                t = V-1;
                while(t!=0){
                    int s = parent[t];
                    residualGraph[s][t]-=flow_capacity;
                    residualGraph[t][s]+=flow_capacity;
                    t = s;
                }
                

                //add flow_capacity to max value
                max_flow+=flow_capacity;
            }
            System.out.println("-----------------------------------");
            System.out.println("Maximum flow: " + max_flow);
            System.out.println("-----------------------------------");
            
            
            
        //DFS
        int h = graph.length;
        if (h == 0)
            return;
        int l = graph[0].length;
        boolean[][] visited = new boolean[h][l];
        Stack<String> stack = new Stack<>();

        stack.push(0 + "," + 0);

        System.out.println("Depth-First Traversal: ");
        while (stack.empty() == false) {

            String x = stack.pop();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);

            if(row<0 || col<0 || row>=h || col>=l || visited[row][col])
                continue;

            visited[row][col]=true;
            System.out.print(graph[row][col] + " ");
            stack.push(row + "," + (col-1)); //go left
            stack.push(row + "," + (col+1)); //go right
            stack.push((row-1) + "," + col); //go up
            stack.push((row+1) + "," + col); //go down
            
            
        }

            
        }
        
        
        public void showMenu(){
            
            
            System.out.println("---------------------------------------------");
            System.out.println("Modify graph: ");
            System.out.println("1. Add edge");
            System.out.println("2. Remove edge");
            System.out.println("3. Modify edge");
            System.out.println("---------------------------------------------");
            
            Scanner chooseMenu = new Scanner(System.in);
            int choiceMenu = chooseMenu.nextInt(); // users input - selected option
            
            if (choiceMenu == 1){
                System.out.println("Adding edge");
                System.out.println("From: ");
                Scanner addEF = new Scanner(System.in);
                int from = addEF.nextInt();
                
                System.out.println("To: ");
                Scanner addET = new Scanner(System.in);
                int to = addET.nextInt();
                
                System.out.println("With capacity: ");
                Scanner addEC = new Scanner(System.in);
                int capacity = addEC.nextInt();
                
                addEdge(from,to,capacity);
                
            }
            
            if (choiceMenu == 2){
                System.out.println("Removing edge");
                System.out.println("From: ");
                Scanner remEF = new Scanner(System.in);
                int from = remEF.nextInt();
                
                Scanner remET = new Scanner(System.in);
                System.out.println("To: ");
                int to = remET.nextInt();
                
                Scanner remEC = new Scanner(System.in);
                System.out.println("With capacity: ");
                int capacity = remEC.nextInt();
                
                deleteEdge(from,to,capacity);
                
            }
            
            if (choiceMenu == 3){
                System.out.println("Modifing edge");
                System.out.println("From: ");
                Scanner remEF = new Scanner(System.in);
                int from = remEF.nextInt();
                
                Scanner remET = new Scanner(System.in);
                System.out.println("To: ");
                int to = remET.nextInt();
                
                Scanner remEC = new Scanner(System.in);
                System.out.println("With capacity: ");
                int capacity = remEC.nextInt();
                
                deleteEdge(from,to,capacity);
                System.out.println("Choose different capacity: ");
                Scanner modEC = new Scanner(System.in);
                capacity = modEC.nextInt();
                
                addEdge(from,to,capacity); 
            }
        }
        
        
        public void printList(){
            System.out.println(list);
        }
        
        
        
        public void addEdge(int from, int to, int capacity){
            //String str = ("Edge from: " + from + " to: " + to + " with capacity: " + capacity).toString();
            Edge e = new Edge(from, to, capacity);
            list.add(e);
            System.out.println("Added to the list");
            System.out.println(e);
            
        }
        
        public void deleteEdge(int from, int to, int capacity){
            //String str = ("Edge from: " + from + " to: " + to + " with capacity: " + capacity).toString();
            Edge e = new Edge(from, to, capacity);
            list.remove(e);
            System.out.println("Removed from the list");
            System.out.println(e);
            
            
        }
 
        
    public static void main(String[] args) {
        

    //choosing dataset
    MaxFlowProblem1 mi = new MaxFlowProblem1();
    mi.chooseDataset();
    Scanner chooseData = new Scanner(System.in);
    int choice = chooseData.nextInt(); // users input - selected option
    
    
    
    //initialising the size of matrix
    int dataset;
    int V;
    int rows;
    int columns;
    
    
    
    //actions depending on each operation
    if (choice == 1){
        System.out.println("You have chosen the smallest dataset");
        dataset = 6;
        V = dataset;
        rows = dataset;
        columns = dataset;
        mi.printGraphRep(rows,columns);
        mi.generateGraph(rows,columns,V);
        System.out.println(" ");
        mi.showMenu();
        mi.recalculateFlow(rows,columns,V);


        
    }
    
    if (choice == 2){
        System.out.println("You have chosen the medium dataset");
        dataset = 12;
        V = dataset;
        rows = dataset;
        columns = dataset;
        mi.printGraphRep(rows,columns);
        mi.generateGraph(rows,columns,V);
        System.out.println(" ");
        mi.showMenu();
        System.out.println("Recalculating the flow");
        mi.recalculateFlow(rows,columns,V);

        
    }
    
    if (choice == 3){
        System.out.println("You have chosen the large dataset");
        dataset = 24;
        V = dataset;
        rows = dataset;
        columns = dataset;
        mi.printGraphRep(rows,columns);
        mi.generateGraph(rows,columns,V);
        //StdOut.println("Elapsed time = " + timer.elapsedTime() + " seconds");
        System.out.println(" ");
        mi.showMenu();
        System.out.println("Recalculating the flow");
        mi.recalculateFlow(rows,columns,V);
        
        
    }
    
    if (choice == 4){
        System.out.println("You have chosen the largest dataset");
        dataset = 48;
        V = dataset;
        rows = dataset;
        columns = dataset;
        mi.printGraphRep(rows,columns);
        mi.generateGraph(rows,columns,V);
        System.out.println(" ");
        mi.showMenu();
        System.out.println("Recalculating the flow");
        mi.recalculateFlow(rows,columns,V);

    }
}
    
}


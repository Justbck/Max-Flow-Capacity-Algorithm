//autor: Justyna Bucko
//student ID: w1692858

package maxflowproblem1;

//initialize
public class Edge {
    
    int from;
    int to;
    int capacity;

    Edge(int from, int to, int capacity) {
      this.from =from;
      this.to = to;
      this.capacity =capacity;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Edge" + " from: " + from + ", to: " + to + ", capacity: " + capacity ;
    }

    int get(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
}


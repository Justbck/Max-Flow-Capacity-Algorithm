# Max-Flow-Capacity-Algorithm
Java code for finding Max Flow Capacity in a directed graph with the usage of depth first search.


In optimization theory, maximum flow problems involve finding a feasible flow through a flow network that obtains the maximum possible flow rate.
The maximum flow problem can be seen as a special case of more complex network flow problems, such as the circulation problem.

Given a graph which represents a flow network where every edge has a capacity. 
Also given two vertices source ‘s’ and sink ‘t’ in the graph, find the maximum possible flow from s to t with following constraints: <br>
a) Flow on an edge doesn’t exceed the given capacity of the edge.<br>
b) Incoming flow is equal to outgoing flow for every vertex except s and t.
<br><br>
For example, consider the following graph. <br>

<img width="197" alt="ford_fulkerson11" src="https://user-images.githubusercontent.com/62395299/117550243-2a0b3700-b037-11eb-9bc9-2b2789167e09.png">

<img width="228" alt="ford_fulkerson2" src="https://user-images.githubusercontent.com/62395299/117550242-28da0a00-b037-11eb-95de-c8066475a4c0.png">


<br>

The algorithmic strategy to solve this approach was to use algorithm match technique. In this case I have done research through common data structures. Max flow problem has been represented in other graph examples. I have developed my solution to this problem given by Edmund Karp’s algorithm, which is an implementation of Ford-Fulkerson methods. Both methods consist on Breadth-First Search Strategy.




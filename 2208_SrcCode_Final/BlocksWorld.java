import java.io.PrintStream;
import java.util.*;

/**
 * AUTHOR: Roberto J Gregoratti - rjg1g12@ecs.soton.ac.uk , University of Southampton ECS
 * PROJECT: COMP2208 Search Methods assignment (BlocksWorld coursework)
 * VERSION: v1.0
 * DATE: Jan 2014
 * CLASS: BlocksWorld (class containing all search methods and representing a world with a block setup)
 */

/*
This class represents an instance of the assignment's problem, the blocksworld. It has a specific set-up
of tiles (provided by the main method through various tile-placing strategies), start and finish nodes,
a PrintStream it receives by the main class when created to print the results to a CSv file for analysis
purposes, and methods to perform the different kinds of heuristic and uninformed searches.
The uninformed searches implemented are: BFS, DFS, ID(DF)S.
The heuristic search I chose to implement is: A*.
 */

public class BlocksWorld{

    PrintStream out;        //obtained from main class, to print data to a file for analysis

    Node finish;                    //end node
    Node current;                   //start state
    int totalIDSMoves;          //variable recording the total number of moves performed by IDS during all iterations

    /*
    The constructor takes int parameters to specify the position of the tiles on the grid for both the start and goal states,
    the size of the grid (worldSize) and the printstream coming from the main classes. The start and finish nodes are
    initialised with those parameters.
     */
    public BlocksWorld(int xa, int ya, int xb, int yb, int xc, int yc, int xAg, int yAg, int finXA, int finYA, int finXB,
                       int finYB, int finXC, int finYC, int finXAg, int finYAg, int worldSize, PrintStream out) throws Exception {
        finish = new Node(new State(finXA,finYA,finXB,finYB,finXC,finYC,finXAg,finYAg, worldSize));
        current = new Node(new State(xa,ya,xb,yb,xc,yc,xAg,yAg, worldSize));
        this.out = out;
    }

    /*
    Method to perform breadth-first search (BFS), adapted from pseudocodes found on Wikipedia, lecture slides and
    AIAMA2e (Russell-Norvig)
     */
    public Node breadthFirst(Node start, Node finish) throws Exception{
        long start_time = System.currentTimeMillis();           //real-time registered (at the beginning)
        Queue queue = new LinkedList();          //DS containing nodes to analyse
        HashSet set = new HashSet();            //DS containing nodes visited (to avoid repetitions)
        queue.add(start);           //start node added to both queue and set (at start)
        set.add(start);

        while(!queue.isEmpty()){                //while there are still nodes left to analyse
            Node node = (Node) queue.poll();        //get first node to analyse from the queue

            //when element in queue matches, terminate search and print results/get total search time
            if(node.getState().compareTo(finish.getState())){
                double time_end = (System.currentTimeMillis() - start_time);
                printResults("BFS",start,node,time_end,set);
                return node;
            }

            ArrayList<Node> possibleMoves = node.checkMoves();      //get possible moves of the agent from state being analysed

            //if neighbour not visited yet, add to both queue and set
            for(Node n : possibleMoves){
                if (!set.contains(n)){
                    set.add(n);
                    queue.add(n);
                }
            }
        }

        System.out.println("Error occurred while running BFS! Search failed!");     //nothing found - null returned
        return null;
    }


    /*
    Method to perform depth-first search (DFS), adapted from pseudocodes found on Wikipedia, lecture slides and
    AIAMA2e (Russell-Norvig). Works similarly to BFS one above, minor modifications, uses stack instead of queue DS
     */
    public Node depthFirst(Node start, Node finish) throws Exception{
        long start_time = System.currentTimeMillis();

        Stack stack = new Stack();              //use stack DS to store nodes to visit, not queue (like BFS)
        HashSet set = new HashSet();
        stack.push(start);
        set.add(start);

        while(!stack.isEmpty()){
            Node node = (Node) stack.pop();     //pop element to analyse from stack

            //search completed when what's popped from the stack matches goal, search completed.
            if(node.getState().compareTo(finish.getState())){
                double time_end = (System.currentTimeMillis() - start_time);
                printResults("DFS",start,node,time_end,set);
                return node;
            }

            ArrayList<Node> possibleMoves = node.checkMoves();

            for(Node n : possibleMoves){
                if (!set.contains(n)){
                    set.add(n);
                    stack.push(n);
                }
            }
        }

        System.out.println("Error occurred while running DFS! Search failed!");     //nothing found - null returned
        return null;
    }

    /*/
    Two methods to perform Iterative Deepening Search (IDS). The first method starts from depth 1, creates null
    node, calls the IDS-performing method and stores the returned Node in n. While still null, continues looping
    to find IDS solution. Depth is increased at every iteration.
    IDS code in performIDS() is adapted from pseudocode found on Russell-Norvig, websites and lecture slides
    NOTE: the 'end' and 'time' variables passed to the iterativeDeepening() method are just for result-printing
    purposes due to the fact that the search is split between two methods!
     */
    public Node iterativeDeepening(Node start, Node finish,Node end,double time) throws Exception{

        long start_time = System.currentTimeMillis();

        int depth = 1;      //initial depth
        Node n = null;      //initially, null node created, will be equal to what the DFS returns at 'depth' depth

        if(end == null){
            while(n == null){
                n = performIDS(start, finish, depth,start_time);        //perform DFS with max depth 'depth'
                depth++;            //increase depth, after having set n to be what the IDDFS returned
            }
        }
        else{
            //when match found, print to output the results
            out.println("IDS" + "," + start.getState().gridDimension + "," + end.getDepth() + "," + totalIDSMoves + "," + time);
        }

        return n;
    }

    public Node performIDS(Node start, Node finish, int depth, long time) throws Exception{     //DFS with limited depth (iterative deepening)

        Stack stack = new Stack();      //uses stack DS like DFS
        Map<Node,Integer> map = new HashMap<Node, Integer>();       //uses Map DS to store visited nodes and depths
        stack.push(start);
        map.put(start, start.getDepth());       //start node entered in Map with initial depth

        while(!stack.isEmpty()){
            Node node = (Node) stack.pop();

            if(node.getState().compareTo(finish.getState())){
                double end_time = (System.currentTimeMillis() - time);
                iterativeDeepening(start,finish,node,end_time);
                return node;
            }

            //if the node is at smaller depth than current target maximum depth for the search, check if it's in map, etc.
            if(depth > node.getDepth()){
                ArrayList<Node> possibleMoves = node.checkMoves();
                for(Node n : possibleMoves){
                    if (!map.containsKey(n) || map.get(n) >= n.getDepth()){
                        map.put(n, n.getDepth());
                        stack.push(n);
                    }
                }
            }
        }

        totalIDSMoves += map.size();        //add the number of visited nodes in this iteration to the global number
        return null;
    }

    /*
    Method to perform an A* heuristic search, adapted from pseudocode in Russell-Norvig, on Wikipedia and online.
     */
    public Node heuristic(Node start, Node finish) throws Exception{
        long start_time = System.currentTimeMillis();

        Queue queue = new PriorityQueue();
        HashSet set = new HashSet();
        start.getCostEstimate(finish);      //create cost estimate (saved within the start node's parameters) for path to finish node
        queue.add(start);

        while(!queue.isEmpty()){
            Node current = (Node) queue.poll();

            if(current.getState().compareTo(finish.getState())){
                double time_end = (System.currentTimeMillis() - start_time);
                printResults("A*",start,current,time_end,set);
                return current;
            }

            set.add(current);
            ArrayList<Node> possibleMoves = current.checkMoves();

            for(Node n : possibleMoves){
                if (!set.contains(n) && !queue.contains(n)){
                    n.getCostEstimate(finish);                      //create and store cost estimate from neighbour to finish node (state)
                    queue.add(n);
                }
            }
        }
        return null;
    }

    /*
    Method used to test that the methods worked. Printed to output console various parameters, like start and end configurations of the
    world board, depth of solution, moves and time taken to reach it, path cost. Commented out due to the newer one printing to csv files.

    public void printResults(String search, Node start, Node current, double srcTime, Set set){         //print (full) result detail
        System.out.println(search + " SEARCH COMPLETE:\n\nAt start state " + start.getState().toString() +".\nAt final state " + current.getState().toString()
            + "\nDepth reached: " + current.getDepth() + " (path cost " + current.getCost() + ")");
        System.out.println("Solution path: " + Arrays.toString(current.displaySolution()));
        System.out.println("Search completed in " + set.size() + " moves, time taken " + srcTime + " ms\n\n");
    }
    */

    /*
    Method used to test the solution path printing.

    public void printResults(String search, Node start, Node current, double srcTime, Set set){
        System.out.println(search + " COMPLETE = Solution path: " + Arrays.toString(current.displaySolution()));
    }
    */

    /*
    Method to print the searches' results. Takes and prints in CSV format some data about search results on various search spaces,
    prints it to the file the output stream is linked to.
     */
    public void printResults(String search, Node start, Node current, double srcTime, Set set) throws Exception{
        out.println(search + "," + current.getState().gridDimension + "," + current.getDepth() + "," + set.size() + "," + srcTime);
    }

}

import java.util.ArrayList;

/**
 * AUTHOR: Roberto J Gregoratti - rjg1g12@ecs.soton.ac.uk , University of Southampton ECS
 * PROJECT: COMP2208 Search Methods assignment (BlocksWorld coursework)
 * VERSION: v1.0
 * DATE: Jan 2014
 * CLASS: Node (basic project infrastructure class to represent a node in the blocks world)
 */

/*
    This is another one of the basic 'infrastructure' classes of the project, representing a Node in the search
    tree for the blocksworld puzzle. Each Node has a State (board configuration), heuristic estimated cost to the
    end node for the heuristic search, a parent node and other characteristics
 */

public class Node implements Comparable {

    private Node parent;       //basic node characteristics
    private State state;
    private int pathCost;
    private int depth;
    private String direction;
    private int heuristic;      //holds the estimated heuristic cost of a node (to the finish node)

    public Node(Node parent, State state, String direction){      //non-root constructor
        this.parent = parent;
        this.state = state;
        if(parent != null){
            this.pathCost = parent.pathCost + 2;
            this.depth = parent.depth + 1;
        }
        this.direction = direction;
    }

    public Node(State state){           //root constructor
        this.state = state;
        this.depth = 0;
        this.pathCost = 0;
    }

    public Node getParent(){
        return parent;
    }

    public State getState(){
        return state;
    }

    public int getDepth(){
        return depth;
    }

    public int getCost(){
        return pathCost;
    }

    /*
    Method to create and return an ArrayList of Nodes representing the possible moves from this instance of node.
    The x/y coordinates are checked against the array limits (for the world boundaries), then if the move is
    feasible a new temporary node is created, in which the agent moves in that specific direction (swapping positions
    with the tile that was in its place earlier). The new (neighbour) node is added to the arraylist of possible
    moves, which will be retrieved by the search methods when calling this method.
     */
    public ArrayList<Node> checkMoves(){

        ArrayList<Node> moves = new ArrayList<Node>();

        if(state.agent.getYPos() > 0){
            State state1 = new State(this.state.a.getXPos(),this.state.a.getYPos(),this.state.b.getXPos(),this.state.b.getYPos(),
                    this.state.c.getXPos(),this.state.c.getYPos(),this.state.agent.getXPos(),this.state.agent.getYPos(), this.state.gridDimension);
            Node temp = new Node(this,state1, "U");
            temp.state.moveAgent(Move.UP);
            moves.add(temp);
        }
        if(state.agent.getYPos() < state.gridDimension - 1){
            State state1 = new State(this.state.a.getXPos(),this.state.a.getYPos(),this.state.b.getXPos(),this.state.b.getYPos(),
                    this.state.c.getXPos(),this.state.c.getYPos(),this.state.agent.getXPos(),this.state.agent.getYPos(),this.state.gridDimension);
            Node temp = new Node(this,state1, "D");
            temp.state.moveAgent(Move.DOWN);
            moves.add(temp);
        }
        if(state.agent.getXPos() > 0){
            State state1 = new State(this.state.a.getXPos(),this.state.a.getYPos(),this.state.b.getXPos(),this.state.b.getYPos(),
                    this.state.c.getXPos(),this.state.c.getYPos(),this.state.agent.getXPos(),this.state.agent.getYPos(),this.state.gridDimension);
            Node temp = new Node(this,state1, "L");
            temp.state.moveAgent(Move.LEFT);
            moves.add(temp);
        }
        if(state.agent.getXPos() < state.gridDimension - 1){
            State state1 = new State(this.state.a.getXPos(),this.state.a.getYPos(),this.state.b.getXPos(),this.state.b.getYPos(),
                    this.state.c.getXPos(),this.state.c.getYPos(),this.state.agent.getXPos(),this.state.agent.getYPos(),this.state.gridDimension);
            Node temp = new Node(this,state1, "R");
            temp.state.moveAgent(Move.RIGHT);
            moves.add(temp);
        }

        return moves;
    }

    @Override
    public boolean equals(Object o) {       //generated automatically
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (state != null ? !state.equals(node.state) : node.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {                 //generated automatically
        int result = (state != null ? state.hashCode() : 0);
        return result;
    }

    /*
    Method to display the path taken from the start node to the solution (in terms of directions taken): an ArrayList
    of Strings for the directions is created, and while the parent of the node considered (this instance) is not null,
    the direction taken to get to it from its parent is added to the list, and so on iteratively.
    A String[] is created by converting the AL and returned by the method.
     */
    public String[] displaySolution(){
        Node node = this;
        ArrayList<String> moves = new ArrayList<String>();

        while(node.parent != null){
            moves.add(0,node.direction);        //while parent isn't null, add direction from parent to this node
            node = node.parent;
        }

        String[] result = (String[]) moves.toArray(new String[moves.size()]);
        return result;
    }

    /*
    Method to estimate heuristic cost from current node to end node, by summing path distance for each tile from current
    (this) node to end node and further summing the depth of the current node. The agent cost has been commented out to
    make the heuristics accurate and consistent, otherwise data obtained was deemed inconsistent.
     */
    public void getCostEstimate(Node target){            //returns heuristic estimate

        int aCostX = Math.abs(target.getState().a.getXPos() - getState().a.getXPos());
        int aCostY = Math.abs(target.getState().a.getYPos() - getState().a.getYPos());
        int bCostX = Math.abs(target.getState().b.getXPos() - getState().b.getXPos());
        int bCostY = Math.abs(target.getState().b.getYPos() - getState().b.getYPos());
        int cCostX = Math.abs(target.getState().c.getXPos() - getState().c.getXPos());
        int cCostY = Math.abs(target.getState().c.getYPos() - getState().c.getYPos());
        //int agentCostX = Math.abs(target.getState().agent.getXPos() - getState().agent.getXPos());
        //int agentCostY = Math.abs(target.getState().agent.getYPos() - getState().agent.getYPos());

        this.heuristic = aCostX + aCostY + bCostX + bCostY + cCostX + cCostY + getDepth();
    }

    /*
    compareTo method overriding the one in the Object class, returns an int depending on the comparison result
     */
    public int compareTo(Object node){
        if(heuristic < ((Node) node).heuristic) return -1;
        else if(heuristic == ((Node) node).heuristic) return 0;
        else return 1;
    }

}

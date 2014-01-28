/**
 * AUTHOR: Roberto J Gregoratti - rjg1g12@ecs.soton.ac.uk , University of Southampton ECS
 * PROJECT: COMP2208 Search Methods assignment (BlocksWorld coursework)
 * VERSION: v1.0
 * DATE: Jan 2014
 * CLASS: State (basic project infrastructure class to represent a state (blocks' configuration) in the blocks world)
 */

/*
This class represents a state in the blocksworld (ie, a specific configuration of tiles in the board). It has methods
to make the agent perform a move and other variables used by other classes.
 */

public class State {

    private Block[][] blocks;               //grid, array of arrays of Blocks [ie rows and columns for a 2D coord space]
    public int gridDimension;               //block array (grid) dimension (ie, if 4, then it will be 4x4)
    private int maxArrDim;                  //max array actual dimension (will be set to grid dimension -1)
    public Block agent, a, b, c;            //blocks for tiles A,B,C and agent.


    public State(int xa, int ya, int xb, int yb, int xc, int yc, int xAg, int yAg, int worldSize) throws IllegalArgumentException{

        this.gridDimension = worldSize;     //set the max grid dimension from the parameter passed
        this.maxArrDim = worldSize - 1;

        /*
        check for illegality of arguments: if x/y positions smaller than 0 or bigger than max array (grid) positions,
        check if any tiles (including the agent) have been given same positions. Throw an Exception in any case.
         */

        if(xa > maxArrDim || xb > maxArrDim || xc > maxArrDim || xAg > maxArrDim || ya > maxArrDim || yb > maxArrDim || yc > maxArrDim || yAg > maxArrDim ||
                xa < 0 || xb < 0 || xc < 0 || xAg < 0 || ya < 0 || yb < 0 || yc < 0 || yAg < 0){

            throw new IllegalArgumentException("Error: one of the parameters for the tiles is invalid. The minimum x/y position" +
                    "for a tile is 0 and the maximum is " + maxArrDim);

        }
        else if((xa == xb && ya == yb) || (xb == xc && yb == yc) || (xa == xc && ya == yc)){

            throw new IllegalArgumentException("Error: different positions have to be specified for the tiles. Please ensure that" +
                    "the x/y parameters provided differ!");

        }
        else if((xAg == xa && yAg == ya) || (xAg == xb && yAg == yb) || (xAg == xc && yAg == yc)){

            throw new IllegalArgumentException("Error: the agent's position has to be different from the tiles' ones. Please ensure" +
                    "that the x/y parameters provided for the agent differ!");

        }
        else{       //if all parameters correct, initialise and insert empty tiles and A/B/C/agent tiles in array
            blocks = new Block[gridDimension][gridDimension];
            agent = new Block("X",xAg,yAg);         //create the four blocks, agent to be identified by the letter "X"
            a = new Block("A",xa,ya);
            b = new Block("B",xb,yb);
            c = new Block("C",xc,yc);

            for(int i=0;i<gridDimension;i++){
                for(int j=0;j<gridDimension;j++){
                    setPosition(i,j,new Block("Empty",i,j));        //set all tiles to be empty tiles
                }
            }

            setPosition(xa,ya,a);           //set the positions of the tiles
            setPosition(xb,yb,b);
            setPosition(xc,yc,c);
            setPosition(xAg,yAg,agent);
        }

    }

    /*
        Method to set a block of the array to be at the passed coordinates and with the passed block
     */
    public void setPosition(int x, int y, Block block){
        blocks[x][y] = block;
    }

    public String toString(){           //override Object toString method, this one will return cells' positions as a String

        return "The blocks are in the following positions: A - (" + a.getXPos() + "," + a.getYPos() + ")" +
                ", B - (" + b.getXPos() + "," + b.getYPos() + ")" +
                ", C - (" + c.getXPos() + "," + c.getYPos() + ")" +
                ", Agent (X) - (" + agent.getXPos() + "," + agent.getYPos() + ")";

    }

    /*
        Method to compare the current state to the goal state (positions of A,B,C,Agent being fixed) by comparing the
        Strings representing those tiles' positions
     */
    public boolean compareTo(State state){
        return (this.a.getPosition().equals(state.a.getPosition())
                && this.b.getPosition().equals(state.b.getPosition())
                && this.c.getPosition().equals(state.c.getPosition())
                && this.agent.getPosition().equals(state.agent.getPosition()));
    }

    /*
    Method to compare the current state to the goal state (where the agent can end anywhere, with only A/B/C being fixed),
    works similarly to compareTo(State state).
     */
    public boolean equalToGoal(State goal){
        return (this.a.getPosition().equals(goal.a.getPosition())
                && this.b.getPosition().equals(goal.b.getPosition())
                && this.c.getPosition().equals(goal.c.getPosition()));
    }


    /*
    Method to make the agent move. It analyses the passed move, then utilises a switch block to determine the action to
    take. For each move, if it is permitted (ie, the agent is within the boundaries where that move can be achieved) then
    a temporary block is created with the position of the tile the agent has to shift with. Both the agent and the other
    tile are shifted and their positions are set. True is returned if the move is possible, false otherwise.
     */
    public boolean moveAgent(Move move) throws IllegalArgumentException {

        switch(move){
            case UP:
                if(agent.getYPos() == 0) return false;
                else{
                    int newYPos = agent.getYPos() - 1;
                    Block old = blocks[agent.getXPos()][newYPos];
                    blocks[agent.getXPos()][agent.getYPos()] = old;
                    old.setYPos(agent.getYPos());
                    blocks[agent.getXPos()][newYPos] = agent;
                    agent.setYPos(newYPos);

                    return true;
                }
            case DOWN:
                if(agent.getYPos() == maxArrDim) return false;
                else{
                    int newYPos = agent.getYPos() + 1;
                    Block old = blocks[agent.getXPos()][newYPos];
                    blocks[agent.getXPos()][agent.getYPos()] = old;
                    old.setYPos(agent.getYPos());
                    blocks[agent.getXPos()][newYPos] = agent;
                    agent.setYPos(newYPos);

                    return true;
                }
            case LEFT:
                if(agent.getXPos() == 0) return false;
                else{
                    int newXPos = agent.getXPos() - 1;
                    Block old = blocks[newXPos][agent.getYPos()];
                    blocks[agent.getXPos()][agent.getYPos()] = old;
                    old.setXPos(agent.getXPos());
                    blocks[newXPos][agent.getYPos()] = agent;
                    agent.setXPos(newXPos);

                    return true;
                }
            case RIGHT:
                if(agent.getXPos() == maxArrDim) return false;
                else{
                    int newXPos = agent.getXPos() + 1;
                    Block old = blocks[newXPos][agent.getYPos()];
                    blocks[agent.getXPos()][agent.getYPos()] = old;
                    old.setXPos(agent.getXPos());
                    blocks[newXPos][agent.getYPos()] = agent;
                    agent.setXPos(newXPos);

                    return true;
                }
        }
        return false;

    }

    /*
        Method used to print the board's state at any time (used for testing purposes only)

    public void performTests(){
        for(Block[] blockArr : blocks){
            for(Block block : blockArr){
                if(block != null){
                    System.out.println("Now in position " + block.getXPos() + "," + block.getYPos() + " we have " + block.getName());
                }
                else{
                    System.out.println("NULL BLOCK");
                }
            }
        }
    }
     */
    @Override
    public boolean equals(Object o) {       //generated automatically
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (gridDimension != state.gridDimension) return false;
        if (maxArrDim != state.maxArrDim) return false;
        if (a != null ? !a.equals(state.a) : state.a != null) return false;
        if (agent != null ? !agent.equals(state.agent) : state.agent != null) return false;
        if (b != null ? !b.equals(state.b) : state.b != null) return false;
        if (c != null ? !c.equals(state.c) : state.c != null) return false;

        return true;
    }

    @Override
    public int hashCode() {                 //generated automatically
        int result = gridDimension;
        result = 31 * result + maxArrDim;
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        result = 31 * result + (a != null ? a.hashCode() : 0);
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        return result;
    }
}

/**
 * AUTHOR: Roberto J Gregoratti - rjg1g12@ecs.soton.ac.uk , University of Southampton ECS
 * PROJECT: COMP2208 Search Methods assignment (BlocksWorld coursework)
 * VERSION: v1.0
 * DATE: Jan 2014
 * CLASS: Block (basic project infrastructure class to represent a block in the blocks world)
 */

/*
This class represents a block in a world (grid) problem. Each block has an identifier (string for the name),
and x and y coordinates representing its position in the grid (a Block[][] , see State and Node classes).
The methods contained in here are standard getters and setters, plus a method to return the position as
a string containing the coordinates
 */

public class Block {

    private String name;                //tile identifier: either A,B,C,X (agent) or Empty (any other tile)
    private int yPos;                //x and y coordinates for position in the grid (at given state)
    private int xPos;

    public Block(String name, int xPos, int yPos){      //create new block with passed parameters
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public String getName(){
        return this.name;
    }

    public int getYPos(){
        return this.yPos;
    }

    public int getXPos(){
        return this.xPos;
    }

    public void setXPos(int newXPos){
        this.xPos = newXPos;
    }       //methods to set new positions

    public void setYPos(int newYPos){
        this.yPos = newYPos;
    }

    public String getPosition(){            //retrieves position returned as coordinate string (eg (1,2) )
        return "(" + this.xPos + "," + this.getYPos() + ")";
    }

    //equals() and hashCode() created automatically
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (xPos != block.xPos) return false;
        if (yPos != block.yPos) return false;
        if (!name.equals(block.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + yPos;
        result = 31 * result + xPos;
        return result;
    }
}
import java.io.File;
import java.io.PrintStream;

/**
 * AUTHOR: Roberto J Gregoratti - rjg1g12@ecs.soton.ac.uk , University of Southampton ECS
 * PROJECT: COMP2208 Search Methods assignment (BlocksWorld coursework)
 * VERSION: v1.0
 * DATE: Jan 2014
 * CLASS: TestHarness (class to run tests on the infrastructure, create and run searches in worlds of different sizes)
 */

/*
This class is a test harness written to firstly test that the 'infrastructure' for the blocksworld worked, then to create
and initialise different worlds to do their searches. There are some booleans to store the feasibility of the various
searches which will be toggled to change to false when those searches run out of memory space, and methods to create
different worlds (automated) and initialise them to perform their searches.
 */
public class TestHarness {

    static boolean dfsOK = true;        //booleans representing search feasibility
    static boolean bfsOK = true;
    static boolean idsOK = true;
    static boolean heurOK = true;

    public static void main(String[] args) throws Exception{
        try{                                            //test to see if states created properly (error checking done previously)
            //State state = new State(1,1,1,2,1,3,2,2);
            //System.out.println(state.toString());       //print blocks' positions to see if they comply

            //State state2 = new State(1,1,1,2,1,3,2,1);
            //State state3 = new State(1,1,1,2,1,3,2,2);

            /*
            Preliminary tests

            System.out.println(state.compareTo(state2));          //comparison and goal achievement checking
            System.out.println(state.compareTo(state3));
            System.out.println(state.equalToGoal(state2));

            state.moveAgent(Move.UP);       //brings test agent to 2,1
            System.out.println("\n================\n");

            state.moveAgent(Move.DOWN);     //brings test agent to 2,2
            System.out.println("\n================\n");

            state.moveAgent(Move.DOWN);     //brings test agent to 2,3
            System.out.println("\n================\n");

            state.moveAgent(Move.UP);       //brings test agent to 2,2
            System.out.println("\n================\n");

            state.moveAgent(Move.LEFT);       //brings test agent to 1,2, B to 2.2
            System.out.println("\n================\n");

            state.moveAgent(Move.RIGHT);       //brings test agent to 2,2, B to 1,2
            System.out.println("\n================\n");

            */

             /*
            Move check testing

            System.out.println("Testing moves:");
            for(Node node : world1.current.checkMoves()){
               System.out.println(node.getState());
            }

            */

            PrintStream out = new PrintStream(new File("SearchResultsAgent.csv"));      //stream to print results to file

            for(int i=4;i<=20;i++){             //create worlds (grids) to perform tests/searches on, sizes 4x4 to 20x20
                createSizedWorld(i,out);
            }

        }
        catch(Exception e){     //catch any exceptions that may arise
            e.printStackTrace();
        }

    }

/*
    This method creates a world of the size provided. In my interpretation of the coursework specs and implementation
    of the blocksworld, the tiles have fixed positions (and are therefore created automatically with the size:
    in the start state the tiles will be in the bottom row, first 3 tiles from the left, with the agent being in the
    bottom right corner. The end state will always be with the agent in the bottom right corner and the stacked tiles
    in the bottom three tiles of the second column.
 */
    public static void createSizedWorld(int size,PrintStream out) throws Exception{
        //Create the world (grid) of the specified tiles, with the tiles' positions fixed for start and end
        int minusOne = size - 1;        //helper variables for board state creation
        int minusTwo = size - 2;
        int minusThree = size - 3;
        BlocksWorld temp = new BlocksWorld(0,minusOne,1,minusOne,2,minusOne, minusOne, minusOne,1,minusThree,1,minusTwo,
                1,minusOne,minusOne,minusOne,size,out);
        //created using set parameters (read method documentation, above)

        try{
            performSearches(temp,size);     //try to call the search-performing method, catch any exceptions arising
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /*
    Method to perform the various searches on the given world and output the results. For every search, check if
    it's still feasible (ie, if boolean hasn't been toggled to false at the earlier iterations), then if it is
    perform the relative search, catch OOM Exception and return message otherwise, toggling the feasibility boolean
    to be set to false (not feasible anymore)
     */
    public static void performSearches(BlocksWorld world, int size) throws Exception{

        System.out.println("\n\nBOARD OF SIZE " + size + "x" + size + "NOW RUNNING...");

        if(heurOK){
            try{
                world.heuristic(world.current, world.finish);
            }
            catch(OutOfMemoryError oome){
                System.out.println("Heuristic search failed for size " + size + ", out of heap space");
                heurOK = false;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if(bfsOK){
            try{
                world.breadthFirst(world.current, world.finish);
            }
            catch(OutOfMemoryError oome){
                System.out.println("Breadth-first search failed for size " + size + ", out of heap space");
                bfsOK = false;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if(dfsOK){
            try{
                world.depthFirst(world.current, world.finish);
            }
            catch(OutOfMemoryError oome){
                System.out.println("Depth-first search failed for size " + size + ", out of heap space");
                dfsOK = false;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if(idsOK){
            try{
                world.iterativeDeepening(world.current, world.finish, null, 0);
            }
            catch(OutOfMemoryError oome){
                System.out.println("Iterative deepening search failed for size " + size + ", out of heap space");
                idsOK = false;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}

import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 * 
 * Extended by:
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.00; //was 0.02
    // The probability that a rabbit will be created in any given position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.00; //was 0.08

    // The probability that a cheetah will be created in any given position.
    private static final double CHEETAH_CREATION_PROBABILITY = 0.02; // 0.02
    // The probability that a lion will be created in any given position.
    private static final double LION_CREATION_PROBABILITY = 0.00; //0.05
    // The probability that a hyena will be created in any given position.
    private static final double HYENA_CREATION_PROBABILITY = 0.00; //0.06
    // The probability that a zebra will be created in any given position.
    private static final double ZEBRA_CREATION_PROBABILITY = 0.15; //0.15
    // The probability that a wildebeest will be created in any given position.
    private static final double WILDEBEEST_CREATION_PROBABILITY = 0.17; //0.17
    // The probability that a gazelle will be created in any given position.
    private static final double GAZELLE_CREATION_PROBABILITY = 0.21; //0.21
    

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;
    // The time of day (either "Day" or "Night")
    private String time;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
        // new code
        time = "Day";
    }
    
    /**
     * Return the time of day
     * @return time
     */
    public String getTime(){
        return time;
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        field = new Field(depth, width);
        view = new SimulatorView(depth, width);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }
    
    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        reportStats();
        for(int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        
        //new code
        if(step%2==0){
            time = "Day";
        }
        else{
            time = "Night";
        }
        
        
        // Use a separate Field to store the starting state of
        // the next step.
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Animal> animals = field.getAnimals();
        for (Animal anAnimal : animals) {
            anAnimal.act(field, nextFieldState, time);
        }
        
        // Replace the old state with the new one.
        field = nextFieldState;

        reportStats();
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        populate();
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, location);
                    field.placeAnimal(fox, location);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, location);
                    field.placeAnimal(rabbit, location);
                }
                
                else if(rand.nextDouble() <= CHEETAH_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Cheetah cheetah = new Cheetah(true, location);
                    field.placeAnimal(cheetah, location);
                }
                else if(rand.nextDouble() <= LION_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, location);
                    field.placeAnimal(lion, location);
                }
                else if(rand.nextDouble() <= HYENA_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Hyena hyena = new Hyena(true, location);
                    field.placeAnimal(hyena, location);
                }
                else if(rand.nextDouble() <= ZEBRA_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Zebra zebra = new Zebra(true, location);
                    field.placeAnimal(zebra, location);
                }
                else if(rand.nextDouble() <= WILDEBEEST_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Wildebeest wildebeest = new Wildebeest(true, location);
                    field.placeAnimal(wildebeest, location);
                }
                else if(rand.nextDouble() <= GAZELLE_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Gazelle gazelle = new Gazelle(true, location);
                    field.placeAnimal(gazelle, location);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        //System.out.print("Step: " + step + " ");
        field.fieldStats(time);
    }
    
    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }
}

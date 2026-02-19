import java.util.List;
import java.util.Random;

/**
 * A simple model of a Wildebeest.
 * Wildebeests age, move, breed, eat and die.
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Wildebeest extends Prey
{
    // TO WORK ON:
    // BREEDING WITH MALE AND FEMALE
    // IMPLEMENT EATING PLANTS
    // EVERY ACT() DECREASE FOOD LEVEL
    //IMPLEMENT BEHAVIOUR WHEN DAY NIGHT CYCLE

    // Characteristics shared by all Wildebeests (Class variables).
    // The age at which a Wildebeest can start to breed.
    private static final int BREEDING_AGE = 2;
    // the age to which a Wildebeest can live.
    private static final int MAX_AGE = 2500000; //25
    // the likelihood of a Wildebeest breeding.
    private static final double BREEDING_PROBABILITY = 0.00; //0.80
    // the maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // the max food level of a Wildebeest
    private static final int MAX_FOOD_LEVEL = 7;  // might change this to be more similar to fox implementation

  
    /**
     * Create a new Wildebeest. A Wildebeest may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Wildebeest will have a random age.
     * @param location The location within the field.
     */
    public Wildebeest(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    @Override
    public String toString() {
        return "Wildebeest{" +
        "age=" + age +
        ", gender=" + gender +
        ", alive=" + isAlive() +
        ", location=" + getLocation() + 
        ", foodLevel=" + foodLevel +
        '}';
    }

    /**
     * Returns the maximum age of the wildebeest
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns breeding age of the wildebeest
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns breeding age of the wildebeest
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum litter size of the wildebeest
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    @Override
    protected Prey createYoung(Location loc)
    {
        return new Wildebeest(false,loc);
    }

}

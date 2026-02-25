import java.util.List;
import java.util.Random;

/**
 * A simple model of a Zebra.
 * Zebras age, move, breed, eat and die.
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Zebra extends Prey
{
    // Characteristics shared by all zebras (Class variables).
    // The age at which a zebra can start to breed.
    private static final int BREEDING_AGE = 10;
    // the age to which a zebra can live.
    private static final int MAX_AGE = 200; 
    // the likelihood of a zebra breeding.
    private static final double BREEDING_PROBABILITY = 0.12; 
    // the maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // the max food level of a zebra
    private static final int MAX_FOOD_VALUE = 30; 
    

    /**
     * Create a new Zebra. A zebra may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the zebra will have a random age.
     * @param location The location within the field.
     */
    public Zebra(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(MAX_FOOD_VALUE) + 1;
            randomHealthyOrNot();
        }
        else{
            age = 0;
            foodLevel = MAX_FOOD_VALUE;
            setHealthy();
        }
        
    }


    @Override
    public String toString() {
        return "Zebra{" +
        "age=" + age +
        ", gender=" + gender +
        ", alive=" + isAlive() +
        ", location=" + getLocation() + 
        ", foodLevel=" + foodLevel +
        '}';
    }

    /**
     * @return the maximum age of the zebra
     */
    @Override
    public int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * @return breeding age of the zebra
     */
    @Override
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * @return breeding probability of the zebra
     */
    @Override
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * @return the maximum litter size of the zebra
     */
    @Override
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    @Override
    protected Prey createYoung(Location loc)
    {
        return new Zebra(false,loc);
    }
    
    /**
     * @return max food value of the zebra
     */
    @Override
    public int getMaxFoodValue()
    {
        return MAX_FOOD_VALUE;
    }
    

}

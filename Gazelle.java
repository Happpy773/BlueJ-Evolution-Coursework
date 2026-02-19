import java.util.List;
import java.util.Random;

/**
 * A simple model of a Gazelle.
 * Gazelles age, move, breed, eat and die.
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Gazelle extends Prey
{
    // TO WORK ON:
    // 
    // IMPLEMENT EATING PLANTS
    // EVERY ACT() DECREASE FOOD LEVEL

    // Characteristics shared by all Gazelles (Class variables).
    // The age at which a Gazelle can start to breed.
    private static final int BREEDING_AGE = 1;
    // the age to which a Gazelle can live.
    private static final int MAX_AGE = 1500000; //15
    // the likelihood of a Gazelle breeding.
    private static final double BREEDING_PROBABILITY = 0.00; //0.65
    // the maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // the max food level of a Gazelle
    private static final int MAX_FOOD_LEVEL = 6;  // might change this to be more similar to fox implementation


    /**
     * Create a new Gazelle. A Gazelle may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Gazelle will have a random age.
     * @param location The location within the field.
     */
    public Gazelle(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    @Override
    public String toString() {
        return "Gazelle{" +
        "age=" + age +
        ", gender=" + gender +
        ", alive=" + isAlive() +
        ", location=" + getLocation() + 
        ", foodLevel=" + foodLevel +
        '}';
    }

    /**
     * Returns the maximum age
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns breeding age of the gazelle
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns breeding age of the gazelle
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum litter size of the gazelle
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    @Override
    protected Prey createYoung(Location loc)
    {
        return new Gazelle(false,loc);
    }
    
    }



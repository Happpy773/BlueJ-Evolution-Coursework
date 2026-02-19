import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Cheetah
 * Cheetahs age, move, eat prey, and die
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Cheetah extends Predator
{
    // Characteristics shared by all Cheetahs (class variables).
    // The age at which a Cheetah can start to breed.
    private static final int BREEDING_AGE = 15;
    // the age to which a Cheetah can live.
    private static final int MAX_AGE = 1500000; //150
    // the likelihood of a Cheetah breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single gazelle.
    private static final int GAZELLE_FOOD_VALUE = 9;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // maximum food level of a Cheetah
    private static final int MAX_FOOD_VALUE = 70;

    /**
     * Create a Cheetah. A Cheetah can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Cheetah will have random age and hunger level
     * @param location The location within the field.
     */
    public Cheetah(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
            foodLevel = MAX_FOOD_VALUE;
        }
        foodLevel = rand.nextInt(MAX_FOOD_VALUE);
    }

    /**
     * This is what the Cheetah does most of the time: it hunts for 
     * zebras and wildebeests. In the process, it might breed, die of hunger, or 
     * die of old age.
     * @param currentField The field currently occupied
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, String time){
        incrementAge();
        incrementHunger();
        if(isAlive()){
            if(time.equals("Day")){
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
                if(! freeLocations.isEmpty()) {
                    if(this.getGender().equals("Female")){
                        //check if in adjacent fields there are other cheetahs
                        Animal animal = checkAnimalAdjacentLocation(currentField, nextFieldState);
                        if(animal != null){
                            boolean ableToBreed = checkCompatibleGender(this, animal);
                            if(ableToBreed){
                                giveBirth(nextFieldState, freeLocations);
                            }
                        }
                    }

                }
                // Move towards a source of food if found.
                Location nextLocation = findFood(currentField);
                if(nextLocation == null && ! freeLocations.isEmpty()){
                    // No food found - try to move to a free location
                    nextLocation = freeLocations.remove(0);
                }
                // See if it was possible to move.
                if(nextLocation != null) {
                    setLocation(nextLocation);
                    nextFieldState.placeAnimal(this, nextLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
            else{
                Location nextLocation = getLocation();
                nextFieldState.placeAnimal(this, nextLocation);
            }

        }
    }

    @Override
    public String toString() {
        return "Cheetah{" +
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
     * Look for gazelles adjacent to the current location
     * Only the first live prey is eaten.
     * @param field The field is currently occupied
     * @return Where the food was found, or null if it wasn't
     */
    private Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Animal animal = field.getAnimalAt(loc);
            if(animal instanceof Gazelle gazelle) {
                if(gazelle.isAlive()){
                    gazelle.setDead();
                    foodLevel = GAZELLE_FOOD_VALUE;
                    // do i need to increment the food value here??
                    foodLocation = loc;
                }
            }

        }
        return foodLocation;
    }
    
    /**
     * Returns breeding age of the Hyena
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns breeding age of the Hyena
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum litter size of the Hyena
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    @Override
    protected Predator createYoung(Location loc)
    {
        return new Cheetah(false,loc);
    }
}

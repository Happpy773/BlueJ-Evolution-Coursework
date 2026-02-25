import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A simple model of a Hyena
 * Hyenas age, move, eat prey, and die
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Hyena extends Predator
{
    // Characteristics shared by all Hyenas (class variables).
    // The age at which a Hyena can start to breed.
    private static final int BREEDING_AGE = 15;
    // the age to which a Hyena can live.
    private static final int MAX_AGE = 150; //150
    // the likelihood of a Hyena breeding.
    private static final double BREEDING_PROBABILITY = 0.09;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single zebra.
    private static final int ZEBRA_FOOD_VALUE = 30;
    // the food value of a single wildebeest
    private static final int WILDEBEEST_FOOD_VALUE = 25;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // maximum food level of a Hyena
    private static final int MAX_FOOD_VALUE = 60;

    /**
     * Create a Hyena. A Hyena can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Hyena will have random age and hunger level
     * @param location The location within the field.
     */
    public Hyena(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            randomHealthyOrNot();
        }
        else {
            age = 0;
            foodLevel = MAX_FOOD_VALUE;
            setHealthy();
        }
        foodLevel = rand.nextInt(MAX_FOOD_VALUE);
    }

    /**
     * This is what the Hyena does most of the time: it hunts for 
     * zebras and wildebeests. In the process, it might breed, die of hunger, or 
     * die of old age.
     * @param currentField The field currently occupied
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, String time, String weather){
        incrementAge();
        incrementHunger(weather);
        if(isAlive()){
            //check that if has disease, then there is a random chance it dies
            if(this.getHealth() == Health.DISEASE){
                String aliveOrNot = randomChanceItDies();
                if(aliveOrNot.equals("Dead")){
                    setDead();
                    return;
                }
            }

            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
            if(! freeLocations.isEmpty()) {
                if(this.getGender().equals("Female")){
                    //check if in adjacent fields there are other hyenas
                    Animal animal = checkAnimalAdjacentLocation(currentField, nextFieldState);
                    if(animal != null){
                        boolean ableToBreed = checkCompatibleGender(this, animal);
                        if(ableToBreed){
                            giveBirth(nextFieldState, freeLocations, weather);
                        }
                    }
                }

            }

            // check for animal in adjacent list have disease
            ArrayList<Animal> animals = checkAnimalAdjacentLocationList(currentField, nextFieldState);
            if(animals.size() == 0){
                // do nothing 
            }
            else{
                for(Animal animal: animals){
                    if(animal.getHealth() == Health.DISEASE){
                        chanceOfDisease();
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
    }

    /**
     * This will check the animal's adjacent locations and check whether there is another one
     * of its species. It will return a list of the animals of its same type.
     * @return The list of animals found or null if no animal found.
     */
    protected ArrayList checkAnimalAdjacentLocationList(Field currentField, Field nextFieldState){
        List<Location> adjacentLocations = nextFieldState.getAdjacentLocations(getLocation());
        ArrayList<Animal> animals = new ArrayList<>();
        for(Location location: adjacentLocations){
            Animal animal = currentField.getAnimalAt(location);
            if(animal == null){
                //do nothing
            }
            else if(animal.getClass().equals(this.getClass())){
                animals.add(animal);
            }
        }
        return animals;
    }

    @Override
    public String toString() {
        return "Hyena{" +
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
     * Look for prey (Zebras/Wildebeests) adjacent to the current location
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
            if(animal instanceof Zebra zebra) {
                if(zebra.isAlive()){
                    zebra.setDead();
                    foodLevel = foodLevel + ZEBRA_FOOD_VALUE; 
                    if(foodLevel > MAX_FOOD_VALUE){
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    foodLocation = loc;
                }
            }
            if(animal instanceof Wildebeest wildebeest){
                if(wildebeest.isAlive()){
                    wildebeest.setDead();
                    foodLevel = foodLevel + WILDEBEEST_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE){
                        foodLevel = MAX_FOOD_VALUE;
                    }
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

    /**
     * Returns the maximum food value of the Hyena
     */
    public int getMaxFoodValue()
    {
        return MAX_FOOD_VALUE;
    }

    /**
     * returns the food value of the given animal to be consumed. 
     * The food value is the amount of hunger that will be replenished when this animal is consumed
     */
    protected int getFoodValue(Animal animal)
    {
        if (animal instanceof Zebra)
        {
            return ZEBRA_FOOD_VALUE;
        }
        else if (animal instanceof Wildebeest)
        {
            return WILDEBEEST_FOOD_VALUE;
        }
        return 0;
    }

    @Override
    protected Predator createYoung(Location loc)
    {
        return new Hyena(false,loc);
    }

    /**
     * Check if the hyena can eat a given animal. Hyena can only eat Zebra and Wildebeest
     */
    protected boolean canEat(Animal animal)
    {
        return animal instanceof Zebra || animal instanceof Wildebeest;
    }
}


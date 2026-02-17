import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Hyena
 * Hyenas age, move, eat prey, and die
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Hyena extends Animal
{
    // Characteristics shared by all Hyenas (class variables).
    // The age at which a Hyena can start to breed.
    private static final int BREEDING_AGE = 15;
    // the age to which a Hyena can live.
    private static final int MAX_AGE = 1500000; //150
    // the likelihood of a Hyena breeding.
    private static final double BREEDING_PROBABILITY = 0.06;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single zebra.
    private static final int ZEBRA_FOOD_VALUE = 9;
    // the food value of a single wildebeest
    private static final int WILDEBEEST_FOOD_VALUE = 6;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // maximum food level of a Hyena
    private static final int MAX_FOOD_VALUE = 60;

    // Individual characterstics (instance fields).

    // The Hyena's age.
    private int age;
    // The Hyena's food level, which is increased by eating prey.
    private int foodLevel;
    // the Hyena's gender.
    private String gender;

    /**
     * Create a Hyena. A Hyena can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Hyena will have random age and hunger level
     * @param location The location within the field.
     */
    public Hyena(boolean randomAge, Location location)
    {
        super(location);
        chooseGender();
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
     * @return gender of the hyena
     */
    public String getGender(){
        return gender;
    }

    /**
     * Chooses the gender of the Hyena randomly
     * @return gender of the Hyena
     */
    private String chooseGender(){
        Random zeroOrOne = new Random();
        int zeroOrOneValue = zeroOrOne.nextInt(2); // generate a value between 0 and 1
        if(zeroOrOneValue == 0){
            gender = "Female";
        }
        else{
            gender = "Male";
        }
        return gender;
    }

    /**
     * This will check the animal's adjacent locations and check whether there is another one 
     * of its species. If there is, it will return the first one it finds.
     * @return The animal found or null if no animal found
     */
    private Animal checkAnimalAdjacentLocation(Field currentField, Field nextFieldState){
        List<Location> adjacentLocations = nextFieldState.getAdjacentLocations(getLocation()); //check whether this is nextFieldState or currentField
        for(Location location: adjacentLocations){
            Animal animal = currentField.getAnimalAt(location);
            if(animal == null){
                // do nothing
            }
            else if(animal instanceof Hyena hyena){
                return animal;
            }
        }
        //if reaches here it means that no corresponding animal was found
        return null;
    }

    /**
     * Checks whether or not one of the animals is male and the other female 
     * otherwise, they cannot give birth
     * @return true if yes, false if no
     */
    private boolean checkCompatibleGender(Animal animal1, Animal animal2){
        if(animal1.getGender() == "Male" && animal2.getGender() == "Female"){
            return true;
        }
        else if(animal1.getGender() == "Female" && animal2.getGender() == "Male"){
            return true;
        }
        else{
            return false;
        }
    }   

    /**
     * This is what the Hyena does most of the time: it hunts for 
     * zebras and wildebeests. In the process, it might breed, die of hunger, or 
     * die of old age.
     * @param currentField The field currently occupied
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, String time){
        incrementAge();
        incrementHunger();
        if(isAlive()){
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
            if(! freeLocations.isEmpty()) {
                if(this.getGender().equals("Female")){
                    //check if in adjacent fields there are other hyenas
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
     * Increase the age. This could result in the Hyena's death.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this Hyena more hungry. This could result in the Hyena's death
     */
    private void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
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
     * Check whether this Hyena is to give birth at this step
     * new births will be made into free adjacent locations
     * @param freeLOcations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        // New Hyenas are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births>0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Hyena young = new Hyena(false, loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * A Hyena can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}


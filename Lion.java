import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a lion
 * Lions age, move, eat prey, and die
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Lion extends Animal
{
    // Characteristics shared by all lions (class variables).
    // The age at which a lion can start to breed.
    private static final int BREEDING_AGE = 15;
    // the age to which a lion can live.
    private static final int MAX_AGE = 1500000; //150
    // the likelihood of a lion breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single zebra.
    private static final int ZEBRA_FOOD_VALUE = 9;
    // the food value of a single wildebeest
    private static final int WILDEBEEST_FOOD_VALUE = 6;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // maximum food level of a lion
    private static final int MAX_FOOD_VALUE = 70;

    // Individual characterstics (instance fields).

    // The lion's age.
    private int age;
    // The lion's food level, which is increased by eating prey.
    private int foodLevel;
    // the lion's gender.
    private String gender;

    /**
     * Create a lion. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the lion will have random age and hunger level
     * @param location The location within the field.
     */
    public Lion(boolean randomAge, Location location)
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
     * @return gender of the lion
     */
    public String getGender(){
        return gender;
    }

    /**
     * Chooses the gender of the lion randomly
     * @return gender of the lion
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
            else if(animal instanceof Lion lion){
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
        if(animal1.getGender().equals(animal2.getGender())){
            return false;
        }
        else{
            return true;
        }
    }    

    /**
     * This is what the lion does most of the time: it hunts for 
     * zebras and wildebeests. In the process, it might breed, die of hunger, or 
     * die of old age.
     * @param currentField The field currently occupied
     * @param nextFieldState The updated field.
     */
    public void act2(Field currentField, Field nextFieldState, String time){
        incrementAge();
        incrementHunger();
        if(isAlive()){
            if(time.equals("Day")){
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
                if(! freeLocations.isEmpty()) {
                    if(this.getGender().equals("Female")){
                        //check if in adjacent fields there are other lions
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
    
    /**
     * This is what the lion does most of the time: it hunts for 
     * zebras and wildebeests. In the process, it might breed, die of hunger, or 
     * die of old age.
     * @param currentField The field currently occupied
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, String time){
        incrementAge();
        incrementHunger(); 
        if(isAlive()){
            if(time.equals("Night")){
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
                if(! freeLocations.isEmpty()) {
                    if(this.getGender().equals("Female")){
                        //check if in adjacent fields there are other lions
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
        return "Lion{" +
        "age=" + age +
        ", gender=" + gender + 
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        ", foodLevel=" + foodLevel +
        '}';
    }

    /**
     * Increase the age. This could result in the lion's death.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this lion more hungry. This could result in the lion's death
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
     * Check whether this lion is to give birth at this step
     * new births will be made into free adjacent locations
     * @param freeLOcations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        // New lions are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births>0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Lion young = new Lion(false, loc);
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
     * A lion can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}

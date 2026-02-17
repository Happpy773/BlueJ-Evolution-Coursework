import java.util.List;
import java.util.Random;

/**
 * A simple model of a Zebra.
 * Zebras age, move, breed, eat and die.
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Zebra extends Animal
{
    // TO WORK ON:
    // BREEDING WITH MALE AND FEMALE
    // IMPLEMENT EATING PLANTS
    // EVERY ACT() DECREASE FOOD LEVEL
    //IMPLEMENT BEHAVIOUR WHEN DAY NIGHT CYCLE

    // Characteristics shared by all zebras (Class variables).
    // The age at which a zebra can start to breed.
    private static final int BREEDING_AGE = 3;
    // the age to which a zebra can live.
    private static final int MAX_AGE = 4000000; //40
    // the likelihood of a zebra breeding.
    private static final double BREEDING_PROBABILITY = 0.00; //0.80
    // the maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // a shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // the max food level of a zebra
    private static final int MAX_FOOD_LEVEL = 10;  // might change this to be more similar to fox implementation

    // Individual characteristics (instance fields).

    // the zebra's age
    private int age;
    // the zebra's food level, which is increased by eating grass
    private int foodLevel;
    // the zebra's gender
    private String gender;

    /**
     * Create a new Zebra. A zebra may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the zebra will have a random age.
     * @param location The location within the field.
     */
    public Zebra(boolean randomAge, Location location)
    {
        super(location);
        chooseGender();
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    /**
     * @return gender of the zebra
     */
    public String getGender(){
        return gender;
    }

    /**
     * Chooses the gender of the zebra randomly
     * @return gender of the zebra
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
            else if(animal instanceof Zebra zebra){
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
     * this is what the zebra does most of the time - it runs around. Sometimes it will breed or die of old age.
     * It will eat if it moves to a location adjacent to grass unless food level is at the max
     * It will die if its food level reaches 0
     * @param currentField The field occupied
     * @param nextfieldState The updated field
     */
    public void act(Field currentField, Field nextFieldState, String time)
    {
        incrementAge();
        if(isAlive()){
            if(time.equals("Day")){
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());

                // if there is a grass in an adjacent location, eat it (if not at max food level)
                //if(foodLevel < MAX_FOOD_LEVEL &&                  ADD THIS METHOD LATER!!!!

                if(!freeLocations.isEmpty()){
                    if(this.getGender().equals("Female")){
                        //check if in adjacent fields there are other zebras
                        Animal animal = checkAnimalAdjacentLocation(currentField, nextFieldState);
                        if(animal != null){
                            boolean ableToBreed = checkCompatibleGender(this, animal);
                            if(ableToBreed){
                                giveBirth(nextFieldState, freeLocations);
                            }
                        }
                    }

                }
                // Try to move into a free location.
                if(! freeLocations.isEmpty()){
                    Location nextLocation = freeLocations.get(0);
                    setLocation(nextLocation);
                    nextFieldState.placeAnimal(this, nextLocation);
                }
                else{
                    // Overcrowding.
                    setDead();
                }
            }
            else{
                // we want nextLocation to be the same location the animal is already in 
                //is there a way to extract the location from the currentField?
                // there is a getLocation() method but idk where it is from
                Location nextLocation = getLocation();
                nextFieldState.placeAnimal(this, nextLocation);
            }

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
     * Increase the age.
     * This could result in the zebra's death.
     */
    private void incrementAge(){
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this zebra more hungry, this could result in the zebra's death.
     */
    private void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Check whether or not this zebra is to give birth at this step
     * New births will be made into free adjacent locations
     * @param freeLocations the locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        // New zebras are born into adjacent locations
        // Get a list of adjacent free locations
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Zebra young = new Zebra(false, loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed
     * @return The number of births (may be zero)
     */
    private int breed(){
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
     * A zebra can breed if it has reached the breeding age.
     * @return true if the zebra can breed, false otherwise
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}

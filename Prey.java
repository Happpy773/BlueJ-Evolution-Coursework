import java.util.Random;
import java.util.List;

/**
 * Write a description of class Prey here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Prey extends Animal
{
    // the prey's age
    protected int age;
    // the prey's food level, which is increased by eating grass
    protected int foodLevel;
    // the prey's gender
    protected String gender;
    // a shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    

    /**
     * Constructor for objects of class Prey
     */
    public Prey(boolean randomAge, Location location)
    {
        super(location);
        chooseGender();
        age = 0;
    }

    
    /**
     * this is what the prey does most of the time - it runs around. Sometimes it will breed or die of old age.
     * It will eat if it moves to a location adjacent to grass unless food level is at the max
     * It will die if its food level reaches 0
     * @param currentField The field occupied
     * @param nextfieldState The updated field
     */
    public void act(Field currentField, Field nextFieldState, String time)
    {
        incrementAge();
        incrementHunger();
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
    
    /**
     * Chooses the gender of the prey randomly
     * @return gender of the prey
     */
    protected String chooseGender(){
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
     * @return gender of the prey
     */
    public String getGender(){
        return gender;
    }
    
    /**
     * Make this zebra more hungry, this could result in the zebra's death.
     */
    protected void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * abstract method to call the child class getMaxAge() method
     */
    public abstract int getMaxAge();
    
    /**
     * abstract method to call the child class getBreedingAge() method
     */
    public abstract int getBreedingAge();
    
    /**
     * abstract method to call the child class getBreedingProbability() method
     */
    public abstract double getBreedingProbability();
    
    /**
     * abstract method to call the child class getMaxLitterSize() method
     */
    public abstract int getMaxLitterSize();
    
    /**
     * abstract method to call the child class createYoung() method
     */
    protected abstract Prey createYoung(Location loc);
    
    
    /**
     * Increase the age.
     * This could result in the prey's death.
     */
    protected void incrementAge(){
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * A zebra can breed if it has reached the breeding age.
     * @return true if the zebra can breed, false otherwise
     */
    private boolean canBreed() {
        return age >= getBreedingAge();
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed
     * @return The number of births (may be zero)
     */
    private int breed(){
        int births;
        
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }
    
    /**
     * Check whether or not this prey is to give birth at this step
     * New births will be made into free adjacent locations
     * @param freeLocations the locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        // New zebras are born into adjacent locations
        // Get a list of adjacent free locations
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Prey young = createYoung(loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }
    
    /**
     * This will check the animal's adjacent locations and check whether there is another one 
     * of its species. If there is, it will return the first one it finds.
     * @return The animal found or null if no animal found
     */
    protected Animal checkAnimalAdjacentLocation(Field currentField, Field nextFieldState){
        List<Location> adjacentLocations = nextFieldState.getAdjacentLocations(getLocation()); //check whether this is nextFieldState or currentField
        for(Location location: adjacentLocations){
            Animal animal = currentField.getAnimalAt(location);
            if(animal == null){
                // do nothing
            }
            else if(animal.getClass().equals(this.getClass())){
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
    protected boolean checkCompatibleGender(Animal animal1, Animal animal2){
        if(animal1.getGender().equals(animal2.getGender())){
            return false;
        }
        else{
            return true;
        }
    }   
}

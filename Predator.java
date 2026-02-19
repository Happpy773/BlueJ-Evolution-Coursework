import java.util.Random;
import java.util.List;

/**
 * Write a description of class Predator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Predator extends Animal
{
     // the predators's age
    protected int age;
    // the predators's food level, which is increased by eating grass
    protected int foodLevel;
    // the predaors's gender
    protected String gender;
    // a shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    
    /**
     * Constructor for objects of class Predator
     */
    public Predator(boolean randomAge, Location location)
    {
        super(location);
        chooseGender();
        age = 0;
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
    protected abstract Predator createYoung(Location loc);
    
    /**
     * Chooses the gender of the predator randomly
     * @return gender of the lion
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
     * @return gender of the predator
     */
    public String getGender(){
        return gender;
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
     * Increase the age. This could result in the lion's death.
     */
    protected void incrementAge() {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Make this predator more hungry. This could result in the predator's death
     */
    protected void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
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
     * Check whether or not this predator is to give birth at this step
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
                Predator young = createYoung(loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }
    
    /**
     * A predator can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= getBreedingAge();
    }
}
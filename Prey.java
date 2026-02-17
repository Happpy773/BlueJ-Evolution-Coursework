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
}

import java.util.Random;
import java.util.List;

/**
 * Common elements of foxes and rabbits.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 * 
 * Extended by:
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    // the animals's age
    protected int age;
    // the animals's food level, which is increased by eating grass
    protected int foodLevel;
    // the animal's gender
    protected String gender;
    // a shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(Location location)
    {
        this.alive = true;
        this.location = location;
    }
    
    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState, String time, String weather);
    
    /**
     * @return gender
     */
    abstract public String getGender();
    
    /**
     * @return max age
     */
    abstract public int getMaxAge();
    
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
    protected abstract Animal createYoung(Location loc);
    
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     */
    protected void setDead()
    {
        //System.out.println(this.getClass().getSimpleName() + " died at age" + age + " with food" + foodLevel);
        alive = false;
        location = null;
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Set the animal's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }
    
    /**
     * Increase the age.
     * This could result in the animals's death.
     */
    protected void incrementAge(){
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Make this animal more hungry. In a heatwave, hunger decreases twice as fast. This could result in the animal's death
     */
    protected void incrementHunger(String weather) {
        int hungerDrain = 1; // Base rate (sunny)
        if(weather.equals("Heatwave"))
        {
            hungerDrain = 2; // hunger doubles
        }
        foodLevel -= hungerDrain;
        
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * A predator can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= getBreedingAge();
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed. If it is a thunderstorm, the chances of breeding are halved
     * @return The number of births (may be zero)
     */
    protected int breed(String weather){
        int births;
        double prob = getBreedingProbability();
        
        if(weather.equals("Thunderstorm"))
        {
            prob = prob * 0.5;
        }
        
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }
    
    /**
     * Check whether or not this animal is to give birth at this step
     * New births will be made into free adjacent locations
     * @param freeLocations the locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations, String weather) {
        // New animals are born into adjacent locations
        // Get a list of adjacent free locations
        int births = breed(weather);
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Animal young = createYoung(loc);
                nextFieldState.placeAnimal(young, loc);
            }
        }
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
}

import java.util.Random;
import java.util.List;
import java.util.Iterator;

/**
 * Comprises of the methods and behaviours that all predators share
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public abstract class Predator extends Animal
{
    
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
    protected abstract Animal createYoung(Location loc);
    
    /**
     * abstract method to call the getFoodValue() method
     */
    protected abstract int getFoodValue(Animal animal);
    
    /**
     * abstract method to call the getMaxFoodValue() method
     */
    protected abstract int getMaxFoodValue();
    
    /**
     * abstract method to call the canEat() method
     */
    protected abstract boolean canEat(Animal animal);
    
    /**
     * @return gender of the predator
     */
    public String getGender(){
        return gender;
    }
    
    /**
     * This will check the animal's adjacent locations and check whether there is another one 
     * of its species. If there is, it will return the first one it finds.
     * @param currentField the current field state
     * @param nextFieldState the state of the next field
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
     * Look for prey adjacent to the current location
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
            if(animal != null && canEat(animal)) {
                if(animal.isAlive()){
                    animal.setDead();
                    foodLevel = Math.min(foodLevel + getFoodValue(animal), getMaxFoodValue());
                    foodLocation = loc;
                }
            }

        }
        return foodLocation;
    }
}
import java.util.List;
import java.util.Random;

/**
 * A simple model of a Gazelle.
 * Gazelles age, move, breed, eat and die.
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Gazelle extends Prey
{
    // TO WORK ON:
    // 
    // IMPLEMENT EATING PLANTS
    // EVERY ACT() DECREASE FOOD LEVEL

    // Characteristics shared by all Gazelles (Class variables).
    // The age at which a Gazelle can start to breed.
    private static final int BREEDING_AGE = 1;
    // the age to which a Gazelle can live.
    private static final int MAX_AGE = 1500000; //15
    // the likelihood of a Gazelle breeding.
    private static final double BREEDING_PROBABILITY = 0.00; //0.65
    // the maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // the max food level of a Gazelle
    private static final int MAX_FOOD_LEVEL = 6;  // might change this to be more similar to fox implementation


    /**
     * Create a new Gazelle. A Gazelle may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Gazelle will have a random age.
     * @param location The location within the field.
     */
    public Gazelle(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
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
            else if(animal instanceof Gazelle gazelle){
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
     * this is what the Gazelle does most of the time - it runs around. Sometimes it will breed or die of old age.
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
                        //check if in adjacent fields there are other gazelles
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
                Location nextLocation = getLocation();
                nextFieldState.placeAnimal(this, nextLocation);
            }

        }
    }

    @Override
    public String toString() {
        return "Gazelle{" +
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
     * Returns breeding age of the gazelle
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns breeding age of the gazelle
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum litter size of the gazelle
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    @Override
    protected Prey createYoung(Location loc)
    {
        return new Gazelle(false,loc);
    }
    
    }



import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class Prey here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Prey extends Animal
{

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
    public void act(Field currentField, Field nextFieldState, String time, String weather)
    {
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
            
            
            if(time.equals("Day")){
                findGrass(currentField);
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());


                if(!freeLocations.isEmpty()){
                    if(this.getGender().equals("Female")){
                        //check if in adjacent fields there are other zebras
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
    
    private void findGrass(Field field)
    {
        Location loc = getLocation();
        int grassValue = field.getGrassAt(loc);
        if(grassValue >= 3)
        {
            this.foodLevel += grassValue;
            field.consumeGrass(loc);
            if(this.foodLevel > getMaxFoodValue())
            {
              this.foodLevel = getMaxFoodValue();
            }
        }
    }
    
    /**
     * @return gender of the prey
     */
    public String getGender(){
        return gender;
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
     * abstract method to call the child class getMaxFoodValue() method
     */
    public abstract int getMaxFoodValue();
    
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
}

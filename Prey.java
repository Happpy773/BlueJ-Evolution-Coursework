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
    // the zebra's age
    private int age;
    // the zebra's food level, which is increased by eating grass
    private int foodLevel;
    // the zebra's gender
    private String gender;
    

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
     * Make this zebra more hungry, this could result in the zebra's death.
     */
    private void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
}

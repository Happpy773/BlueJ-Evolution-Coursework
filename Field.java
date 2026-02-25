import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal/object.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 * 
 * Extended by 
 * @author Diego Abete and Harrison Buck
 * @version 1.0
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The dimensions of the field.
    private final int depth, width;
    // Animals mapped by location.
    private final Map<Location, Animal> field = new HashMap<>();
    // The animals.
    private final List<Animal> animals = new ArrayList<>();
    // A map of the grass 
    private static int[][] grassMap;
    // The maximum level grass can grow to    
    private final int MAX_GRASS_LEVEL = 5;
    
    

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
        if(grassMap == null)
        {
            grassMap = new int[depth][width];
        }
    }

    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     * @param anAnimal The animal to be placed.
     * @param location Where to place the animal.
     */
    public void placeAnimal(Animal anAnimal, Location location)
    {
        assert location != null;
        Object other = field.get(location);
        if(other != null) {
            animals.remove(other);
        }
        field.put(location, anAnimal);
        animals.add(anAnimal);
    }
    
    /**
     * Return the animal at the given location, if any.
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Animal getAnimalAt(Location location)
    {
        return field.get(location);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {
            Animal anAnimal = field.get(next);
            if(anAnimal == null) {
                free.add(next);
            }
            else if(!anAnimal.isAlive()) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Print out the number of different animals in the field.
     * @param time The time of day that it currently is
     * @param weather The weather that it currently is
     */
    public void fieldStats(String time, String weather)
    {
        int numFoxes = 0, numRabbits = 0;
        int numZebras = 0, numWildebeests = 0, numGazelles = 0;
        int numCheetahs = 0, numLions = 0, numHyenas = 0;
        for(Animal anAnimal : field.values()) {
            
            if(anAnimal instanceof Zebra zebra) {
                if(zebra.isAlive()) {
                    numZebras++;
                }
            }
            else if(anAnimal instanceof Wildebeest wildebeest) {
                if(wildebeest.isAlive()) {
                    numWildebeests++;
                }
            }
            else if(anAnimal instanceof Gazelle gazelle) {
                if(gazelle.isAlive()) {
                    numGazelles++;
                }
            }
            else if(anAnimal instanceof Cheetah cheetah) {
                if(cheetah.isAlive()) {
                    numCheetahs++;
                }
            }
            else if(anAnimal instanceof Lion lion) {
                if(lion.isAlive()) {
                    numLions++;
                }
            }
            else if(anAnimal instanceof Hyena hyena) {
                if(hyena.isAlive()) {
                    numHyenas++;
                }
            }
        }
        System.out.println("Rabbits: " + numRabbits +
                           " Foxes: " + numFoxes +
                           " Zebras: " + numZebras +
                           " Wildebeests: " + numWildebeests +
                           " Gazelles: " + numGazelles +
                           " Cheetahs: " + numCheetahs +
                           " Lions: " + numLions +
                           " Hyenas: " + numHyenas);
                           
        System.out.println("Time: " + time);
        System.out.println("Weather: " + weather);
    }

    /**
     * Empty the field.
     */
    public void clear()
    {
        field.clear();
    }
    
    /**
     * Keeps the simulation constantly running no matter what
     * @return true 
     */
    public boolean isViable(){
        return true;
    }
    
    /**
     * Get the list of animals.
     * @return animals A list of the animals
     */
    public List<Animal> getAnimals()
    {
        return animals;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
    
    /**
     * Grass grows faster in the rain, not at all in a heatwave, and normally any other time.
     * Grass can grow to a set maximum level 
     * @param weather The weather that it currently is
     */
    public void growGrass(String weather)
    {
        int growthAmount;
        if(weather.equals("Rainy"))
        {
            growthAmount = 2; // grass grows twice as fast in the rain
        }
        else if(weather.equals("Heatwave"))
        {
            growthAmount = 0; //grass doesnt grow in a heatwave
        }
        else
        {
            growthAmount = 1;
        }
        
        
        for(int row = 0; row < depth; row++)
        {
            for(int col = 0; col < width; col++)
            {
                if(grassMap[row][col] < MAX_GRASS_LEVEL)
                {
                    grassMap[row][col] += growthAmount;
                    if(grassMap[row][col] > MAX_GRASS_LEVEL)
                    {
                        grassMap[row][col] = MAX_GRASS_LEVEL;
                    }
                }
            }
        }
    }
    
    /**
     * @return the value at the grass map location
     */
    public int getGrassAt(Location location)
    {
        return grassMap[location.row()][location.col()];
    }
    
    /**
     * Reset the grass value at that location to 0 after eating
     */
    public void consumeGrass(Location location)
    {
        grassMap[location.row()][location.col()] = 0; 
    }
    
    /**
     * Prints out a map of the grass values in the terminal
     */
    public void printGrassMap()
    {
        System.out.println("---Current grass map state---");
        for(int row = 0; row < depth; row++)
        {
            for(int col = 0; col < width; col++)
            {
                System.out.print(grassMap[row][col] + " " );
            }
            System.out.println();
        }
    }
}

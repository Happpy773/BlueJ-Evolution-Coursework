
/**
 * Enumeration class Health - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum Health
{
    HEALTHY, DISEASE;
    
    @Override
    public String toString() {
        switch(this) {
            case HEALTHY: return "Healthy";
            case DISEASE: return "Disease";
            default: return "Healthy";
        }
    }
}
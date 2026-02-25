
/**
 * Enumeration class Health - has two states, HEALTHY or DISEASE
 *
 * @author Diego Abete and Harrison Buck
 * @version 1.0
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
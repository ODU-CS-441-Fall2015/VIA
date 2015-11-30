package rainbowbeard.viaglass.data;

import java.util.List;

/**
 * Struct for holding a wikipedia response
 *
 * @author mchaney 
 */
public class WikiResponse {
    public String queryParam;
    public List<String> names;
    public List<String> descriptions;
    public List<String> links;
}

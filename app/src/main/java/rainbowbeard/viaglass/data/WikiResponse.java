package rainbowbeard.viaglass.data;

import java.util.Collections;
import java.util.List;

/**
 * Struct for holding a wikipedia response
 *
 * @author mchaney 
 */
public class WikiResponse {
    public final String name;
    public final List<String> names;
    public final List<String> descriptions;
    public final List<String> links;

    public WikiResponse(String name, List<String> names, List<String> descriptions, List<String> links) {
        this.name = name;
        this.names = Collections.unmodifiableList(names);
        this.descriptions = Collections.unmodifiableList(descriptions);
        this.links = Collections.unmodifiableList(links);
    }
}

package rainbowbeard.viaglass.datastore;

import java.util.Collections;
import java.util.List;

/**
 * Created by mchaney on 11/22/15.
 */
public class ImageResponse {
    private final String url;
    private final String name;
    private List<String> links;

    public ImageResponse(final String url, final String name, final List<String> links) {
        this.url = url;
        this.name = name;
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public List<String> getLinks() {
        return Collections.unmodifiableList(links);
    }
}

package rainbowbeard.viaglass.datastore;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton holder for image response objects
 *
 * Created by mchaney on 11/22/15.
 */
public class ImageResponseStore {
    private static ImageResponseStore instance;
    private ConcurrentHashMap<String, ImageResponse> responseMap;

    private ImageResponseStore() {
        responseMap = new ConcurrentHashMap<String, ImageResponse>();
    }

    public static ImageResponseStore getInstance() {
        if(null == instance) {
            instance = new ImageResponseStore();
        }
        return instance;
    }

    public ImageResponse get(final String url) {
        return responseMap.get(url);
    }

    public void put(final String url, final ImageResponse response) {
        responseMap.put(url, response);
    }
}

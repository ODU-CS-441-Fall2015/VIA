package rainbowbeard.viaglass.data;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton holder for wiki response objects
 *
 * Created by mchaney on 11/22/15.
 */
public class ResponseStore {
    private static ResponseStore instance;
    private ConcurrentHashMap<String, WikiResponse> responseMap;

    private ResponseStore() {
        responseMap = new ConcurrentHashMap<String, WikiResponse>();
    }

    /**
     * Get the instance of our ResponseStore
     */
    public static ResponseStore getInstance() {
        if(null == instance) {
            instance = new ResponseStore();
        }
        return instance;
    }

    /**
     * Get the {@link WikiResponse} to a queryParam
     * @param queryParam
     * @return a WikiResponse if the queryParam has been used in a WikiSearchTask, or null if
     * the queryParam hasn't been encountered this session.
     */
    public WikiResponse get(final String queryParam) {
        return responseMap.get(queryParam);
    }

    /**
     * Add a new {@link WikiResponse} to the store
     * @param queryParam the search term
     * @param response the {@link WikiResponse} object corresponding to a wikipedia search of the
     *                 queryParam
     */
    public void put(final String queryParam, final WikiResponse response) {
        responseMap.put(queryParam, response);
    }
}

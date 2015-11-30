package rainbowbeard.viaglass.tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import rainbowbeard.viaglass.data.ResponseStore;
import rainbowbeard.viaglass.data.WikiResponse;

/**
 * Performs a wikipedia search of the query param.
 *
 * @author mchaney
 */
public class WikiSearchTask extends SearchTask {
    public static final String WIKI_SEARCH_RESPONSE = "WIKI_SEARCH_RESPONSE";

    private static final String
            URL_HEAD = "https://en.wikipedia.org/w/api.php?action=opensearch&search=",
            URL_TAIL = "";

    public WikiSearchTask(final Context context, final String queryParam) {
        super(context, queryParam, URL_HEAD, URL_TAIL);
    }

    @Override
    public void parseResponse(final Document document) {
        final String json = document.select("body").text();

        // store json in ResponseStore and broadcast result to rest of app
        final WikiResponse wikiResponse = new WikiResponse();
        try {
            final JSONArray items = new JSONArray(json);
            final JSONArray names = items.getJSONArray(1);
            final JSONArray descriptions = items.getJSONArray(2);
            final JSONArray links = items.getJSONArray(3);
            wikiResponse.queryParam = queryParam;
            wikiResponse.names = getJSONList(names);
            wikiResponse.descriptions = getJSONList(descriptions);
            wikiResponse.links = getJSONList(links);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // add our WikiResponse to the ResponseStore
        ResponseStore.getInstance().put(queryParam, wikiResponse);

        // broadcast our new WikiResponse to the rest of the app
        final Intent resultIntent = new Intent(WIKI_SEARCH_RESPONSE);
        resultIntent.putExtra(WIKI_SEARCH_RESPONSE, queryParam);
        LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent);
    }

    private static List<String> getJSONList(final JSONArray array) {
        final List<String> results = new ArrayList<String>();
        try {
            for(int i=0; i<array.length(); i++) {
                results.add(array.getString(i).toString());
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return results;
    }
}

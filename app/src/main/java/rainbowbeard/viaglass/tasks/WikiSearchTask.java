package rainbowbeard.viaglass.tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rainbowbeard.viaglass.data.ResponseStore;
import rainbowbeard.viaglass.data.WikiResponse;

/**
 * Performs a search of the query param.
 *
 * @author mchaney
 */
public class WikiSearchTask extends SearchTask {
    public static final String WIKI_SEARCH_RESPONSE = "WIKI_SEARCH_RESPONSE";

    private static final String
            URL_HEAD = "https://www.google.com/search?q=",
            URL_TAIL = "";

    public WikiSearchTask(final Context context, final String queryParam) {
        super(context, queryParam, URL_HEAD, URL_TAIL);
    }

    @Override
    public void parseResponse(final Document document) {
        final WikiResponse wikiResponse = new WikiResponse();
        wikiResponse.queryParam = queryParam;
        wikiResponse.names = Arrays.asList(new String [] { queryParam });
        final Elements nameElement = document.select(".kno-rdesc");
        final String description = nameElement.text();
        wikiResponse.descriptions = Arrays.asList(new String[] { description });

        // add our WikiResponse to the ResponseStore
        ResponseStore.getInstance().put(queryParam, wikiResponse);

        // broadcast our new WikiResponse to the rest of the app
        final Intent resultIntent = new Intent(WIKI_SEARCH_RESPONSE);
        resultIntent.putExtra(WIKI_SEARCH_RESPONSE, queryParam);
        LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent);
    }
}

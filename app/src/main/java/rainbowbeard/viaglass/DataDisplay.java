package rainbowbeard.viaglass;

/*
 Data display: (class + activity)
                    	Receive data
                    	Display data
                    	End activity
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import rainbowbeard.viaglass.data.ResponseStore;
import rainbowbeard.viaglass.data.WikiResponse;
import rainbowbeard.viaglass.tasks.WikiSearchTask;

public class DataDisplay extends AppCompatActivity {
    private static final String TAG = DataDisplay.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        // create our search response event broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "received " + WikiSearchTask.WIKI_SEARCH_RESPONSE + " broadcast");

                // extract the queryParam from the response event and use it to get the WikiResponse
                final String response = intent.getStringExtra(WikiSearchTask.WIKI_SEARCH_RESPONSE);
                if (null != response && !"".equals(response)) {
                    final WikiResponse wikiResponse = ResponseStore.getInstance().get(response);
                    if(null != wikiResponse) {
                        if(null != wikiResponse.names && wikiResponse.names.size() > 0) {
                            final TextView titleText = (TextView) findViewById(R.id.title);
                            titleText.setText(wikiResponse.names.get(0));
                        }
                        if(null != wikiResponse.descriptions && wikiResponse.descriptions.size() > 0) {
                            final TextView summaryText = (TextView) findViewById(R.id.summary);
                            summaryText.setText(wikiResponse.descriptions.get(0));
                        }
                    }
                } else {
                    Toast.makeText(DataDisplay.this, "Image Search failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(WikiSearchTask.WIKI_SEARCH_RESPONSE));
    }

    public void onButtonClick(View view) {
        finish();
    }
}

package rainbowbeard.viaglass;

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

public class ImageSearchActivity extends AppCompatActivity {
    private static final String TAG = ImageSearchActivity.class.getCanonicalName();
    public static final String SEARCH_RESPONSE_EVENT = "IMAGE_SEARCH_RESPONSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "created activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagesearch);

        // create our search response event broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "received " + SEARCH_RESPONSE_EVENT + " broadcast");

                // extract the text from the response event and display it
                final String response = intent.getStringExtra(SEARCH_RESPONSE_EVENT);
                if(null != response) {
                    final TextView resultView = (TextView) findViewById(R.id.result_view);
                    resultView.setText(response);
                }
            }
        }, new IntentFilter(SEARCH_RESPONSE_EVENT));
        Log.d(TAG, "registered broadcast receiver for " + SEARCH_RESPONSE_EVENT);
    }

    public void onRetrieveClick(final View v) {
        Log.d(TAG, "retrieve clicked");
        final String imageFile = "uploads/upload-1.jpg";
        new ImageSearchTask(this, imageFile).start();
        final Toast toast = Toast.makeText(this, "Searching for image: " + imageFile, Toast.LENGTH_SHORT);
        toast.show();
    }
}

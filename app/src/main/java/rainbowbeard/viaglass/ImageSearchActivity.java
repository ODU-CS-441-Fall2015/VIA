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

import rainbowbeard.viaglass.tasks.ImageSearchTask;

public class ImageSearchActivity extends AppCompatActivity {
    private static final String TAG = ImageSearchActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "created activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagesearch);

        // create our search response event broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "received " + ImageSearchTask.IMG_SEARCH_RESPONSE + " broadcast");

                // extract the text from the response event and display it
                final String response = intent.getStringExtra(ImageSearchTask.IMG_SEARCH_RESPONSE);
                if(null != response) {
                    final TextView resultView = (TextView) findViewById(R.id.result_view);
                    resultView.setText(response);
                }
            }
        }, new IntentFilter(ImageSearchTask.IMG_SEARCH_RESPONSE));
        Log.d(TAG, "registered broadcast receiver for " + ImageSearchTask.IMG_SEARCH_RESPONSE);
    }

    public void onRetrieveClick(final View v) {
        Log.d(TAG, "retrieve clicked");

        // TODO: use response from FileServer once we sort out the network blockage
        // hardcode image filename for now
        final String imageFile = "uploads/upload-1.jpg";
        new ImageSearchTask(this, imageFile).start();
        final Toast toast = Toast.makeText(this, "Searching for image: " + imageFile, Toast.LENGTH_SHORT);
        toast.show();
    }
}

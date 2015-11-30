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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rainbowbeard.viaglass.data.ImageResponse;
import rainbowbeard.viaglass.data.ResponseStore;
import rainbowbeard.viaglass.data.Upload;
import rainbowbeard.viaglass.data.WikiResponse;
import rainbowbeard.viaglass.tasks.ImageSearchTask;
import rainbowbeard.viaglass.tasks.UploadService;
import rainbowbeard.viaglass.tasks.WikiSearchTask;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ImageSearchActivity extends AppCompatActivity {
    private static final String TAG = ImageSearchActivity.class.getCanonicalName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "created activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagesearch);

        // create our search response event broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "received " + WikiSearchTask.WIKI_SEARCH_RESPONSE + " broadcast");

                // extract the queryParam from the response event and use it to get the WikiResponse
                final String response = intent.getStringExtra(WikiSearchTask.WIKI_SEARCH_RESPONSE);
                if (null != response) {
                    final WikiResponse wikiResponse = ResponseStore.getInstance().get(response);
                    final TextView resultView = (TextView) findViewById(R.id.result_view);
                    // set the text in the TextView to the first item in the WikiResponse names list
                    resultView.setText(wikiResponse.names.get(0));
                }
            }
        }, new IntentFilter(WikiSearchTask.WIKI_SEARCH_RESPONSE));
        Log.d(TAG, "registered broadcast receiver for " + WikiSearchTask.WIKI_SEARCH_RESPONSE);
    }

    public void onImageClick(final View v) {
        Log.d(TAG, "retrieve clicked");

        // create new UploadService with our picture file stored in the Upload object
        final Upload upload = new Upload();
        upload.title = "lincoln.jpg";
        upload.description = "some dude";
        upload.image = resToFile(R.raw.lincoln, upload.title);

        new UploadService(this).Execute(upload, new Callback<ImageResponse>() {
            @Override
            public void success(ImageResponse imageResponse, Response response) {
                Toast.makeText(ImageSearchActivity.this, "Performing Image Search", Toast.LENGTH_SHORT).show();
                // successful upload, start reverse image search
                new ImageSearchTask(ImageSearchActivity.this, imageResponse.data.link).start();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ImageSearchActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Converts a resourceId + filename pair to a {@link File} object
     * @param resourceID
     * @param filename
     * @return
     */
    private File resToFile(final int resourceID, final String filename) {
        final File file = getApplicationContext().getFileStreamPath(filename);
        if(file.exists()) {
            return file;
        }

        InputStream is;
        FileOutputStream fos;
        try {
            is = getResources().openRawResource(resourceID);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            fos = openFileOutput(filename, MODE_PRIVATE);
            fos.write(buffer);
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}

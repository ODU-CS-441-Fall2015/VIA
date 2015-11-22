package rainbowbeard.viaglass;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ImageSearchActivity extends AppCompatActivity {
    private static final String TAG = ImageSearchActivity.class.getCanonicalName();
    private static final String
            URIHEAD = "https://images.google.com/searchbyimage?image_url=http%3A%2F%2Fwww.cs.odu.edu%2F%7Emchaney%2Fuploads%2F",
            URITAIL = "&image_content=&filename=&hl=en";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagesearch);
    }

    public void onRetrieveClick(View v) {
        new TestRetriever(encodeURI("upload-1")).execute();
    }

    private static String encodeURI(final String filename) {
        return URIHEAD + filename + URITAIL;
    }

    private class TestRetriever extends AsyncTask<Void, Void, Void> {
        private final String url;
        String response;

        public TestRetriever(final String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ImageSearchActivity.this);
            mProgressDialog.setTitle("Android Basic JSoup Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).followRedirects(true).get();
                Elements elements = document.select("._gUb");
                elements.text();
//                response =
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast toast = Toast.makeText(ImageSearchActivity.this, response, Toast.LENGTH_LONG);
            toast.show();
            mProgressDialog.dismiss();
        }
    }
}

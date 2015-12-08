package rainbowbeard.viaglass;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import rainbowbeard.viaglass.data.ImageResponse;
import rainbowbeard.viaglass.data.Upload;
import rainbowbeard.viaglass.tasks.ImageSearchTask;
import rainbowbeard.viaglass.tasks.NotificationHelper;
import rainbowbeard.viaglass.tasks.UploadService;
import rainbowbeard.viaglass.tasks.WikiSearchTask;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private Uri imageUri;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationHelper = new NotificationHelper(this);
    }

    public void onCameraClick(View v) {
        final Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), "picture.bmp");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1); //1 for rear-facing camera
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == Activity.RESULT_OK) {
            File photo = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES), "picture.bmp");
            final Uri selectedImage = Uri.fromFile(photo);
            getContentResolver().notifyChange(selectedImage,null);

            final ImageView imageView = (ImageView)findViewById(R.id.image_camera);
            final ContentResolver cr =getContentResolver();
            Bitmap bitmap;

            try{
                bitmap = MediaStore.Images.Media.getBitmap(cr,selectedImage);
                imageView.setImageBitmap(bitmap);
                imageView.setId(R.id.target_picture);
            }catch(Exception e){
                Log.e("Image retrieve fail:", e.toString());
            }
        }
    }

    public void onRetrieverClick(View view) {
        final Upload upload = new Upload();
        upload.image = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), "picture.bmp");
        notificationHelper.createUploadingNotification();
        new UploadService(this).Execute(upload, new Callback<ImageResponse>() {
            @Override
            public void success(ImageResponse imageResponse, Response response) {
                notificationHelper.createUploadedNotification(imageResponse);
                Toast.makeText(MainActivity.this, "Performing Image Search", Toast.LENGTH_SHORT).show();
                // successful upload, start reverse image search
                new ImageSearchTask(MainActivity.this, imageResponse.data.link).start();
            }

            @Override
            public void failure(RetrofitError error) {
                notificationHelper.createFailedUploadNotification();
            }
        });
        Log.d(TAG, "registered broadcast receiver for " + WikiSearchTask.WIKI_SEARCH_RESPONSE);
        final Intent intent = new Intent(this, DataDisplay.class);
        startActivity(intent);
    }
}

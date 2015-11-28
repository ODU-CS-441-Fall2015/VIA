package rainbowbeard.viaglass.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.ref.WeakReference;

import rainbowbeard.viaglass.Constants;
import rainbowbeard.viaglass.data.ImageResponse;
import rainbowbeard.viaglass.data.ImgurAPI;
import rainbowbeard.viaglass.data.Upload;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.mime.TypedFile;

public class UploadService {
    public final static String TAG = UploadService.class.getSimpleName();

    private WeakReference<Context> mContext;

    public UploadService(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    /**
     * performs this task's operations and sends results to the callback param
     * @param upload the object containing the upload data
     * @param callback success or failure callback object
     */
    public void Execute(final Upload upload, final Callback<ImageResponse> callback) {
        Log.d(TAG, "executing UploadService");

        // we must have a valid callback to call when the operation fails or completes
        if(null == callback) {
            System.err.println("Must pass in non-null callback");
            return;
        }

        // must have internet connectivity to perform this task
        if (!isConnected(mContext.get())) {
            callback.failure(null);
            return;
        }

        // build our REST adapter and send the post to our imgur api endpoint
        new RestAdapter.Builder()
                .setEndpoint(ImgurAPI.server)
                .build()
                .create(ImgurAPI.class)
                .postImage(
                    Constants.getClientAuth(),
                    upload.title,
                    upload.description,
                    upload.albumId,
                    null,
                    new TypedFile("image/*", upload.image),
                    callback);
    }

    private static boolean isConnected(Context mContext) {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
        return false;
    }
}

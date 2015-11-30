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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import rainbowbeard.viaglass.data.ResponseStore;
import rainbowbeard.viaglass.data.WikiResponse;

public class DataDisplay extends AppCompatActivity {
    private String title, summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);
        Intent i = getIntent();
        final String response = i.getStringExtra("response key");
        if(null != response){
            final WikiResponse wr = ResponseStore.getInstance().get(response);
            TextView titleText = (TextView)findViewById(R.id.title);
            titleText.setText(wr.names.get(0));
            TextView summaryText = (TextView)findViewById(R.id.summary);
            summaryText.setText(wr.descriptions.get(0));
        }

    }

    public void onButtonClick(View view){
        finish();
    }
}

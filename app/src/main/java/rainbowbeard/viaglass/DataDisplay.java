package rainbowbeard.viaglass;

/*
 Data display: (class + activity)
                    	Receive data
                    	Display data
                    	End activity
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DataDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);
    }
}

package door.nfc.sakailab.com.nfcdooropen.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import door.nfc.sakailab.com.nfcdooropen.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = null;
                switch (id) {
                    case R.id.nfc_button:
                        intent = new Intent(MainActivity.this, NfcReadActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.serial_button:
                        intent = new Intent(MainActivity.this, SerialConnectActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        Button nfcButton = (Button) findViewById(R.id.nfc_button);
        Button serialButton = (Button) findViewById(R.id.serial_button);

        nfcButton.setOnClickListener(listener);
        serialButton.setOnClickListener(listener);
    }
}

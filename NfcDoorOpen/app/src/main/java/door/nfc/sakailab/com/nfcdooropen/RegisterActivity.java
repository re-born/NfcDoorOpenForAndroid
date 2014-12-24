package door.nfc.sakailab.com.nfcdooropen;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class RegisterActivity extends Activity {

    private String mIdm = null;
    private Button mRegButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    private void initRegViews() {
        mRegButton = (Button) findViewById(R.id.register_button);
        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPostUserData();
            }
        });
    }

    private void requestPostUserData() {

    }
}

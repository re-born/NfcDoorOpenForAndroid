package door.nfc.sakailab.com.nfcdooropen;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by taisho6339 on 2014/12/23.
 */
public class NfcReadActivity extends Activity {

    private TextView mIdLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcread_activity_layout);

        mIdLabel = (TextView) findViewById(R.id.nfc_id_label);

        Intent intent = getIntent();
        String action = intent.getAction();

        // NFCかどうかActionの判定
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Log.d("DEBUG_TAG", "NFC DISCOVERD:" + action);
            // IDmを表示させる
            String idm = getIdm(getIntent());
            if (idm != null) {
                mIdLabel.setText(idm);
            }
        }
    }


    private String getIdm(Intent intent) {
        String idm = null;
        StringBuffer idmByte = new StringBuffer();
        byte[] rawIdm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        if (rawIdm != null) {
            for (int i = 0; i < rawIdm.length; i++) {
                idmByte.append(String.format("%1$02x", rawIdm[i] & 0xff));
            }
            idm = idmByte.toString();
        }
        return idm;
    }
}

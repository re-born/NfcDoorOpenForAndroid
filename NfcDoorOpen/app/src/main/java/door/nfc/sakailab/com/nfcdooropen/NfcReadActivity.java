package door.nfc.sakailab.com.nfcdooropen;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by taisho6339 on 2014/12/23.
 */
public class NfcReadActivity extends Activity implements RequestTask.RequestCallBack<JSONObject> {


    private TextView mIdLabel;
    private Button mRegButton;

    //    private static final String REQUEST_ID_PARAM_NAME = "name";
    private static final String ID_REQUEST_API = Config.SERVER_URL + "/api/v0/user";
    private static final String REQUEST_ID_PARAM_IDM = "idm";

    protected String mIdm = null;

    @Override
    public void doCallBack(JSONObject result) {
        Log.d(Config.DEBUG_TAG, result.toString());
    }

    @Override
    public void onFailed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcread_activity_layout);

        mIdLabel = (TextView) findViewById(R.id.nfc_id_label);
        mRegButton = (Button) findViewById(R.id.register_button);

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
                requestIdm();
            }
        }
    }

    private void requestIdm() {
        try {
            JSONObject vals = new JSONObject();
            vals.put(REQUEST_ID_PARAM_IDM, "5678cc8c9scd9cd8scd");
            RequestTask task = new RequestTask(this, ID_REQUEST_API, vals, this, RequestTask.REQUEST_MODE_GET);
            task.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected String getIdm(Intent intent) {
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

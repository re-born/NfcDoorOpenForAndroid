package door.nfc.sakailab.com.nfcdooropen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import door.nfc.sakailab.com.nfcdooropen.config.Config;
import door.nfc.sakailab.com.nfcdooropen.mqtt.PushService;
import door.nfc.sakailab.com.nfcdooropen.util.RequestTask;

/**
 * Created by taisho6339 on 2014/12/23.
 */
public class NfcReadActivity extends Activity implements RequestTask.RequestCallBack<JSONObject> {

    private TextView mIdLabel;

    private static final String ID_REQUEST_API = Config.SERVER_URL + "/api/v0/user";
    private static final String REQUEST_ID_PARAM_IDM = "idm";
    private String mIdm = "";

    @Override
    public void doCallBack(JSONObject result) {
        Log.d(Config.DEBUG_TAG, result.toString());
        PushService.actionPublish(this);
//        PushService.actionStop(this);
    }

    @Override
    public void onFailed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcread_activity_layout);

        PushService.actionStart(this, "nfc/sakailab");

        mIdLabel = (TextView) findViewById(R.id.nfc_id_label);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Log.d("DEBUG_TAG", "NFC DISCOVERD:" + action);
            // IDmを表示させる
            requestIdm();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushService.actionStop(this);
    }


    private void requestIdm() {
        String idm = getIdm(getIntent());
        if (idm == null) {
            return;
        }
        Log.d(Config.DEBUG_TAG, idm);
        mIdm = idm;
        mIdLabel.setText(idm);
        try {
            JSONObject vals = new JSONObject();
//            vals.put(REQUEST_ID_PARAM_IDM, "5678cc8c9scd9cd8scd");
            vals.put(REQUEST_ID_PARAM_IDM, mIdm);
            RequestTask task = new RequestTask(this, ID_REQUEST_API, vals, this, RequestTask.REQUEST_MODE_GET);
            task.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        requestIdm();
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

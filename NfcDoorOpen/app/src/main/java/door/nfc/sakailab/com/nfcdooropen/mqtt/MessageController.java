package door.nfc.sakailab.com.nfcdooropen.mqtt;

import android.content.Context;

import door.nfc.sakailab.com.nfcdooropen.ui.SerialConnectActivity;

/**
 * Created by taisho6339 on 2014/08/28.
 */
public class MessageController {

    public static void onCallBack(String data, Context context) {
        if (data.equals("NFC_OK")) {
            SerialConnectActivity.sendCommand(context);
        }
    }

}

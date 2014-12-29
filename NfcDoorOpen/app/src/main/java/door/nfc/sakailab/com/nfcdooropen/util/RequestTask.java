package door.nfc.sakailab.com.nfcdooropen.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import door.nfc.sakailab.com.nfcdooropen.R;

/**
 * Created by taisho6339 on 2014/06/25.
 */
public class RequestTask extends AsyncTask<Void, JSONObject, JSONObject> {

    public static final int REQUEST_MODE_GET = 0;
    public static final int REQUEST_MODE_POST = 1;

    private ProgressDialog mDialog;
    private Context mContext;
    private RequestCallBack mCallBack;
    private int mMode;
    private String mApi;
    private JSONObject mValues;

    public RequestTask(Context context, String reqApi, JSONObject values,
                       RequestCallBack callback, int mode) {
        mContext = context;
        mApi = reqApi;
        mValues = values;
        mCallBack = callback;
        mMode = mode;
    }

    private JSONObject request(int mode) throws IOException, JSONException {
        JSONObject res = null;

        switch (mode) {
            case REQUEST_MODE_GET:
                res = RequestUtil.requestGet(mApi, mValues);
                break;
            case REQUEST_MODE_POST:
                res = RequestUtil.requestPost(mApi, mValues);
                break;
        }
        return res;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mDialog != null)
            mDialog.dismiss();
    }

    @Override
    public void onPreExecute() {
        mDialog = new ProgressDialog(mContext);
        mDialog.setTitle(R.string.server_request_title);
        mDialog.setMessage(mContext.getString(R.string.server_request_message));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public JSONObject doInBackground(Void... params) {
        try {
            return request(mMode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(JSONObject result) {
        mDialog.dismiss();
        if (result == null || mCallBack == null) {
            mCallBack.onFailed();
            return;
        }
        mCallBack.doCallBack(result);
    }

    /**
     * Created by taisho6339 on 2014/06/25.
     */
    public interface RequestCallBack<E> {
        public void doCallBack(E result);

        public void onFailed();

    }

}

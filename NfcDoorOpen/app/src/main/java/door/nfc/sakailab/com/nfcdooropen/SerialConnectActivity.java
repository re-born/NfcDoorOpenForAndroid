package door.nfc.sakailab.com.nfcdooropen;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import door.nfc.sakailab.com.nfcdooropen.mqtt.PushService;

public class SerialConnectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_connect);
        PushService.actionStart(this, "nfc/sakailab");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushService.actionStop(this);
    }

    public static void sendCommand(Context context) {
        pushToArduino("o", context);
    }

    private static void pushToArduino(String command, Context context) {

        UsbManager sUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbSerialDriver sUsb = UsbSerialProber.acquire(sUsbManager);

        if (sUsb != null) {
            try {
                sUsb.open();
                sUsb.setBaudRate(9600);
                sUsb.write(command.getBytes("UTF-8"), 1);
                Thread.sleep(1500);
                sUsb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

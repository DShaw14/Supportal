package oaklabs.supportal;

import android.graphics.drawable.Drawable;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import javax.security.auth.callback.Callback;

public class OnError implements Callback {

    public boolean handleMessage(Message msg) {
        Log.e("onError","ERROR");
        return false;
    }

}

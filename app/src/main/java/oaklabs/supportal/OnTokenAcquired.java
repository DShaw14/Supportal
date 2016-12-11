package oaklabs.supportal;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by David Shaw on 12/11/2016.
 */
public class OnTokenAcquired implements AccountManagerCallback<Bundle> {

    @Override
    public void run(AccountManagerFuture<Bundle> result){
        // Get the result of the operation from the AccountManagerFuture.
        try {
            Bundle bundle = result.getResult();
            // The token is a named value in the bundle. The name of the value
            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

        }catch(android.accounts.OperationCanceledException ex){
            ex.printStackTrace();
        }catch(java.io.IOException ex){
            ex.printStackTrace();
        }catch(android.accounts.AuthenticatorException ex){
            ex.printStackTrace();
        }
    }
}

package oaklabs.supportal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends Activity {

    EditText username;
    EditText emailAddress;
    EditText password;
    Button createAccount;
    RequestQueue createQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = (EditText) findViewById(R.id.desiredUsernameEnter);
        password = (EditText) findViewById(R.id.passwordEnter);
        emailAddress = (EditText) findViewById(R.id.createEmailEdit);
        createAccount = (Button) findViewById(R.id.createAcntBtn);
        createQueue = Volley.newRequestQueue(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View v) {
                                                 if (username.getText().toString().trim().length() != 0 &&
                                                         password.getText().toString().trim().length() != 0 &&
                                                         emailAddress.getText().toString().trim().length() != 0) {
                                                     Map<String, String> params = new HashMap<String, String>();

                                                     StringRequest req = new StringRequest(Request.Method.POST,
                                                             "http://hurst.pythonanywhere.com/supportal/rest-auth/registration",
                                                             reqSuccessListener(),
                                                             reqErrorListener()) {

                                                         protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                                                             Map<String, String> params = new HashMap<String, String>();
                                                             params.put("username", username.getText().toString());
                                                             params.put("password1", password.getText().toString());
                                                             params.put("password2", password.getText().toString());
                                                             params.put("email", emailAddress.getText().toString());
                                                             return params;
                                                         }

                                                         ;
                                                     };
                                                     // add the request object to the queue to be executed
                                                     createQueue.add(req);
                                                 }
                                             }

                                             private Response.ErrorListener reqErrorListener() {
                                                 return new Response.ErrorListener() {
                                                     @Override
                                                     public void onErrorResponse(VolleyError error) {
                                                         Log.e("CREATE FAIL", error.getMessage());
                                                     }
                                                 };
                                             }

                                             private Response.Listener<String> reqSuccessListener() {
                                                 return new Response.Listener<String>() {
                                                     @Override
                                                     public void onResponse(String response) {
                                                         Log.e("CREATE SUCCESS", response);
                                                     }
                                                 };
                                             }
                                         }
        );
    }
}




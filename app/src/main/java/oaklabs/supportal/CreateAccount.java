package oaklabs.supportal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
public class CreateAccount extends Activity {

    EditText username;
    EditText emailAddress;
    EditText password;
    EditText confirm;
    Button createAccount;
    RequestQueue accountQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = (EditText) findViewById(R.id.desiredUsernameEnter);
        password = (EditText) findViewById(R.id.passwordEnter);
        emailAddress = (EditText) findViewById(R.id.createEmailEdit);
        createAccount = (Button) findViewById(R.id.createAcntBtn);
        confirm = (EditText) findViewById(R.id.confirmPassEnter);
        accountQueue = Volley.newRequestQueue(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (username.getText().toString().trim().length() != 0 && password.getText().toString().trim().length() != 0 &&
                        emailAddress.getText().toString().trim().length() != 0 && confirm.getText().toString().trim().length() != 0 && confirm.getText().toString().equals(password.getText().toString())) {
                    JSONObject jsObj = new JSONObject();

                    try {
                        jsObj.put("username", username.getText().toString());
                        jsObj.put("email", emailAddress.getText().toString());
                        jsObj.put("password1", password.getText().toString());
                        jsObj.put("password2", confirm.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, "http://hurst.pythonanywhere.com/supportal/rest-auth/registration/", jsObj, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    displayMessage("Account created");
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    String json = null;

                                    NetworkResponse response = error.networkResponse;
                                    if(response != null && response.data != null){
                                        switch(response.statusCode){
                                            case 400:
                                                json = new String(response.data);
                                                if(json.toLowerCase().contains("email")){
                                                    displayMessage("Email is already in use");
                                                }
                                                if(json.toLowerCase().contains("password1") || json.toLowerCase().contains("password2")){
                                                    displayMessage("Password is too short, must be 8 characters long");
                                                }
                                                if(json.toLowerCase().contains("username")){
                                                    displayMessage("Username is already in use");
                                                }
                                        }
                                    }
                                }
                            });
                    // add the request object to the queue to be executed
                    accountQueue.add(jsObjRequest);
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "There are errors in the form, fill out empty fields and check passwords.";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString){
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }
}
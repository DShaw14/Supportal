package oaklabs.supportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.*;

public class UserLogin extends Activity {

    EditText username;
    EditText password;
    Button loginBtn;
    Button forgotPassBtn;
    Button createAccountBtn;
    private String forgotPassText;
    RequestQueue loginQueue;
    RequestQueue passwordQueue;
    TextView logoText;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        username = (EditText)findViewById(R.id.usernameLine);
        password = (EditText)findViewById(R.id.passwordLine);
        loginBtn = (Button)findViewById(R.id.loginButton);
        forgotPassBtn = (Button)findViewById(R.id.forgotPassword);
        createAccountBtn = (Button)findViewById(R.id.createAccount);
        logoText = (TextView)findViewById(R.id.supportalTxtView);
    }

    public void buttonOnClick(View v){
        switch(v.getId()){
            case R.id.loginButton:
                if(username.getText().toString().trim().length() != 0 && password.getText().toString().trim().length() != 0){
                    final Intent main = new Intent(this, MainPage.class);
                    loginQueue = Volley.newRequestQueue(this);
                    JSONObject jsObj = new JSONObject();
                    final String user = username.getText().toString();
                    final String pass = password.getText().toString();

                    try {
                        jsObj.put("username", username.getText().toString());
                        jsObj.put("password", password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, "http://hurst.pythonanywhere.com/supportal/rest-auth/login/", jsObj, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        token = response.getString("key");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    main.putExtra("USERNAME", user);
                                    main.putExtra("TOKEN", token);
                                    main.putExtra("PASS", pass);
                                    startActivity(main);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    int loginError = error.networkResponse.statusCode;
                                    if(loginError == 400) {
                                        makeDialog("Invalid login credentials");
                                    }
                                    error.printStackTrace();
                                }
                            });
                    // add the request object to the queue to be executed
                    loginQueue.add(jsObjRequest);
                }
                else if(username.getText().toString().trim().length() == 0 && password.getText().toString().trim().length() != 0){
                    makeDialog("Please enter a username");
                }
                else if(password.getText().toString().trim().length() == 0 && username.getText().toString().trim().length() != 0){
                    makeDialog("Please enter a password");
                }
                else{
                    makeDialog("Please enter a username and password");
                }
                break;
            case R.id.forgotPassword:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Forgot Password?");
                builder.setMessage("Enter your username to be emailed recovery procedures");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

                builder.setPositiveButton("Send Recovery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        forgotPassText = input.getText().toString();
                        tryPasswordReset(forgotPassText);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.createAccount:
                Intent create = new Intent(this, CreateAccount.class);
                startActivity(create);
                break;
        }
    }

    public void tryPasswordReset(String email){
        passwordQueue = Volley.newRequestQueue(this);
        JSONObject jsObj = new JSONObject();
        try {
            jsObj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "http://hurst.pythonanywhere.com/supportal/rest-auth/password/reset/", jsObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Context context = getApplicationContext();
                        CharSequence text = "An email will be sent to " + forgotPassText + " with password recovery procedures." + response.toString();
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        // add the request object to the queue to be executed
        passwordQueue.add(jsObjRequest);
    }

    public void makeDialog(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}



package oaklabs.supportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.net.*;
import java.io.*;
import java.util.*;
import com.loopj.android.http.*;
import org.json.*;

public class UserLogin extends Activity {

    EditText username;
    EditText password;
    Button loginBtn;
    Button forgotPassBtn;
    Button createAccountBtn;
    private String forgotPassText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        username = (EditText)findViewById(R.id.usernameLine);
        password = (EditText)findViewById(R.id.passwordLine);
        loginBtn = (Button)findViewById(R.id.loginButton);
        forgotPassBtn = (Button)findViewById(R.id.forgotPassword);
        createAccountBtn = (Button)findViewById(R.id.createAccount);
    }

    public void buttonOnClick(View v){
        switch(v.getId()){
            case R.id.loginButton:
                if(username.getText().toString().trim().length() != 0 && password.getText().toString().trim().length() != 0){
                    URL url = new URL("https://127.0.0.1:8000/supportal/rest-auth/login");
                    Map<String,Object> params = new LinkedHashMap<>();
                    params.put("username", "testuser");
                    params.put("password", "supportal2016");
                    
                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String,Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);

                    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                    for (int c; (c = in.read()) >= 0;)
                        System.out.print((char)c);
                    //Intent intent = new Intent(this, MainPage.class);
                    //startActivity(intent);
                }
                else if(username.getText().toString().trim().length() == 0 && password.getText().toString().trim().length() != 0){
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter a username";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(password.getText().toString().trim().length() == 0 && username.getText().toString().trim().length() != 0){
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter a password";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter a username and password";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
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
                        //Do something with this string via the Django API most likely
                        Context context = getApplicationContext();
                        CharSequence text = "An email will be sent to " + forgotPassText + " with password recovery procedures.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
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
}

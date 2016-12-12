package oaklabs.supportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.net.*;
import java.io.*;
import java.util.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.*;
import org.json.*;

import retrofit2.Call;
import retrofit2.Callback;

import static java.lang.System.in;

public class UserLogin extends Activity {

    EditText username;
    EditText password;
    Button loginBtn;
    Button forgotPassBtn;
    Button createAccountBtn;
    private String forgotPassText;
    RequestQueue loginQueue;

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
                    /*User loginUser = new User();
                    loginUser.logUsername = username.getText().toString();
                    loginUser.logPassword = password.getText().toString();

                    ApiInterface api = APIClient.getClient().create(ApiInterface.class);

                    Call<User> call = api.loginUser(loginUser);

                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                            String token = response.body();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });*/


                    loginQueue = Volley.newRequestQueue(this);
                    Map<String, String> params = new HashMap<String, String>();

                    StringRequest req = new StringRequest(Request.Method.POST,
                            "http://hurst.pythonanywhere.com/supportal/rest-auth/login",
                            reqSuccessListener(),
                            reqErrorListener()) {

                        protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Username", "shaw14");
                            params.put("Password", "supportal2016");
                            return params;
                        };
                    };
                    // add the request object to the queue to be executed
                    loginQueue.add(req);

                    Intent main = new Intent(this, MainPage.class);
                    startActivity(main);

                    /*HttpURLConnection urlConnection=null;
                    try {
                        //THIS PART IS OKAY
                        URL url = new URL("http://hurst.pythonanywhere.com/supportal/rest-auth/login");
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setDoOutput(true);
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type", "application/json");

                        Intent main = new Intent(this, MainPage.class);
                        startActivity(main);

                        //Create JSONObject here
                        //Problem Part
                        JSONObject json = new JSONObject();
                        json.put("username", "testuser");
                        json.put("password", "supportal2016");
                        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                        out.write(json.toString());
                        out.flush();
                        out.close();

                        StringBuilder sb = new StringBuilder();
                        int HttpResult = urlConnection.getResponseCode();
                        if (HttpResult == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            br.close();
                            System.out.println("" + sb.toString());
                        } else {
                            System.out.println(urlConnection.getResponseMessage());
                        }
                    }catch(MalformedURLException ex){
                        ex.printStackTrace();
                    }catch(IOException ex) {
                        ex.printStackTrace();
                    //}catch(JSONException ex){
                    //    ex.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally{
                        if(urlConnection!=null)
                            urlConnection.disconnect();
                    }*/
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

    private Response.ErrorListener reqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOGIN FAIL", error.getMessage());
            }
        };
    }

    private Response.Listener<String> reqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LOGIN SUCCESS", response);
            }
        };
    }
}

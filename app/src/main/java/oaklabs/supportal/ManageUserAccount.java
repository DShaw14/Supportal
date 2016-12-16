package oaklabs.supportal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManageUserAccount extends Activity {

    TextView usernameDisplay;
    TextView firstName;
    TextView lastName;
    TextView emailDisplay;
    EditText firstNameEnter;
    EditText lastNameEnter;
    EditText emailNew;
    Button setNewInfo;
    RequestQueue getUserQueue;
    String result;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_account);
        final String token = getIntent().getStringExtra("TOKEN");
        final String user = getIntent().getStringExtra("USERNAME");
        final String pass = getIntent().getStringExtra("PASS");
        firstNameEnter = (EditText)findViewById(R.id.firstNameEdit);
        lastNameEnter = (EditText)findViewById(R.id.lastNameEdit);
        emailNew = (EditText)findViewById(R.id.newEmailEdit);
        usernameDisplay = (TextView)findViewById(R.id.userAccountName);
        firstName = (TextView)findViewById(R.id.firstNameDisp);
        lastName = (TextView)findViewById(R.id.lastNameDisp);
        emailDisplay = (TextView)findViewById(R.id.emailDisp);
        setNewInfo = (Button)findViewById(R.id.changeInfoBtn);
        getUserQueue = Volley.newRequestQueue(this);
        final JSONObject jsObj = new JSONObject();
        usernameDisplay.setText(getIntent().getStringExtra("USERNAME"));

        setNewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jsObj.put("first_name", firstNameEnter.getText().toString());
                    jsObj.put("last_name", lastNameEnter.getText().toString());
                    jsObj.put("email", emailNew.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.PATCH, "http://hurst.pythonanywhere.com/supportal/rest-auth/user/", jsObj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Context context = getApplicationContext();
                                CharSequence text = "Account info updated!";
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap <String, String> headers = new HashMap <String, String> ();
                        // add headers <key,value>
                        String credentials = user +":"+ pass;
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(),
                                Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };
                getUserQueue.add(jsObjRequest);
            }
        });

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "http://hurst.pythonanywhere.com/supportal/rest-auth/user/", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            firstName.append(" " + response.getString("first_name"));
                            lastName.append(" " + response.getString("last_name"));
                            emailDisplay.append(" " + response.getString("email"));
                            firstNameEnter.setText(response.getString("first_name"));
                            lastNameEnter.setText(response.getString("last_name"));
                            emailNew.setText(response.getString("email"));
                            emailDisplay.setWidth(1080);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap <String, String> headers = new HashMap <String, String> ();
                // add headers <key,value>
                String credentials = user +":"+ pass;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        getUserQueue.add(jsObjRequest);
    }
}

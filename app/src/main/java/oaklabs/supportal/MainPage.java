package oaklabs.supportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class MainPage extends Activity implements View.OnClickListener {

    Button submitPageBtn;
    Button managePageBtn;
    String token;
    String user;
    String pass;
    RequestQueue logoutQueue;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.manage_account:
                manageAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        submitPageBtn = (Button)findViewById(R.id.submitIssue);
        managePageBtn = (Button)findViewById(R.id.manageIssues);
        token = getIntent().getStringExtra("TOKEN");
        user = getIntent().getStringExtra("USERNAME");
        pass = getIntent().getStringExtra("PASS");
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.submitIssue:
                Intent submit = new Intent(this, SubmitIssue.class);
                submit.putExtra("TOKEN", token);
                submit.putExtra("USERNAME", user);
                submit.putExtra("PASS", pass);
                startActivity(submit);
                break;
            case R.id.manageIssues:
                Intent manage = new Intent(this, ManageIssues.class);
                manage.putExtra("TOKEN", token);
                manage.putExtra("USERNAME", user);
                manage.putExtra("PASS", pass);
                startActivity(manage);
                break;
        }
    }

    public void logout(){
        logoutQueue = Volley.newRequestQueue(this);
        JSONObject jsObj = new JSONObject();
        final Intent logout = new Intent(this, UserLogin.class);

        try {
            jsObj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "http://hurst.pythonanywhere.com/supportal/rest-auth/logout/", jsObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logout);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        //add the request object to the queue to be executed
        logoutQueue.add(jsObjRequest);
    }

    public void manageAccount(){
        Intent manage = new Intent(this, ManageUserAccount.class);
        manage.putExtra("TOKEN", token);
        manage.putExtra("USERNAME", user);
        manage.putExtra("PASS", pass);
        startActivity(manage);
    }
}

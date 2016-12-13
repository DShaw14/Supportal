package oaklabs.supportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageIssues extends Activity {

    TextView display;
    RequestQueue issueQueue;
    String url ="https://api.bitbucket.org/1.0/repositories/shawdl/supportal2016test/issues";

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
        setContentView(R.layout.activity_manage_issues);

        display = (TextView)findViewById(R.id.issueManageDescription);
        issueQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonIssues = response.getJSONArray("issues");

                    for(int i=0; i < jsonIssues.length(); i++){
                        JSONObject issue = jsonIssues.getJSONObject(i);

                        if(!issue.getString("status").equals("closed")) {
                            String title = issue.getString("title");
                            String description = issue.getString("content");
                            display.append("Title: " + title + "\n" + "Description: " + description + "\n" + "\n");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE", "ERROR");
            }
        });
        issueQueue.add(jsonRequest);
    }

    public void logout(){
        Intent logout = new Intent(this, UserLogin.class);
        startActivity(logout);
    }

    public void manageAccount(){
        Intent manage = new Intent(this, ManageUserAccount.class);
        startActivity(manage);
    }
}

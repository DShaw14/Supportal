package oaklabs.supportal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
}

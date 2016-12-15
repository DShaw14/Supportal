package oaklabs.supportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ManageIssues extends Activity {

    TextView display;
    RequestQueue issueQueue, deleteQueue, logoutQueue;
    String url ="https://api.bitbucket.org/1.0/repositories/shawdl/supportal2016test/issues";
    ArrayList<String> issueListArr;
    ArrayAdapter<String> adapter;
    ListView issuesList;
    String token;
    String user;
    String pass;

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
        token = getIntent().getStringExtra("TOKEN");
        user = getIntent().getStringExtra("USERNAME");
        pass = getIntent().getStringExtra("PASS");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_issues);

        display = (TextView) findViewById(R.id.issueManageDescription);
        issuesList = (ListView) findViewById(R.id.issueList);
        issueListArr = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.issue_layout, R.id.txt, issueListArr);
        issuesList.setAdapter(adapter);

        issuesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) (issuesList.getItemAtPosition(position));
                String selected = selectedFromList.substring(0, selectedFromList.indexOf(' '));
                deleteRequest(selected);
            }
        });
    }

    public void onStart(){
        super.onStart();
        issueQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray issueArray = response.getJSONArray("issues");
                    for(int i=0; i < issueArray.length(); i++){
                        JSONObject issue = issueArray.getJSONObject(i);
                        if(!issue.getString("status").equals("closed")) {
                            String id = issue.getString("local_id");
                            String title = issue.getString("title");
                            String description = issue.getString("content");
                            String issuefinal = id + " " + title + "\n" + description;
                            issueListArr.add(issuefinal);
                        }
                        adapter.notifyDataSetChanged();
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

    public void deleteRequest(final String id){
        deleteQueue = Volley.newRequestQueue(this);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete Issue?")
                .setMessage("Are you sure you want to remove this issue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject jsObj = new JSONObject();

                        try {
                            jsObj.put("id", id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.POST, "http://hurst.pythonanywhere.com/supportal/api/delete/", jsObj, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Context context = getApplicationContext();
                                        CharSequence text = "Issue deleted!";
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
                        deleteQueue.add(jsObjRequest);
                    }

                })
                .setNegativeButton("No", null)
                .show();
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

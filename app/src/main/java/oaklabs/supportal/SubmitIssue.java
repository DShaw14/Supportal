package oaklabs.supportal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.os.StrictMode;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SubmitIssue extends Activity implements View.OnClickListener {

    EditText titleEdit;
    EditText descriptionEdit;
    Button submitBtn;
    CheckBox priority;
    String slackParams;
    RequestQueue issueQueue, logoutQueue;
    Spinner kind;
    String issuePriority;
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
        setContentView(R.layout.activity_submit_issue);
        titleEdit = (EditText)findViewById(R.id.issueTitleEdit);
        descriptionEdit = (EditText)findViewById(R.id.descriptionEdit);
        priority = (CheckBox)findViewById(R.id.priorityChkBox);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        kind = (Spinner)findViewById(R.id.kindSpinner);
        submitBtn.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kinds_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kind.setAdapter(adapter);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void onClick(View v){
        if(titleEdit.getText().toString().trim().length() != 0 && descriptionEdit.getText().toString().trim().length() != 0) {
            String issueTitle = titleEdit.getText().toString();
            String issueDesc = descriptionEdit.getText().toString();
            String kindSelected = kind.getSelectedItem().toString();
            issuePriority = "minor";
            if(priority.isChecked()){
                issuePriority = "major";
            }

            issueQueue = Volley.newRequestQueue(this);
            JSONObject jsObj = new JSONObject();

                    try {
                        jsObj.put("title", issueTitle);
                        jsObj.put("description", issueDesc);
                        jsObj.put("kind", kindSelected);
                        jsObj.put("priority", issuePriority);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://hurst.pythonanywhere.com/supportal/api/create/", jsObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Context context = getApplicationContext();
                            CharSequence text = "Issue posted";
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
            issueQueue.add(jsObjRequest);

            if(priority.isChecked()) {
                SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
                api.call(new SlackMessage("#random", "Supportal", "High Priority issue created:\n" + "*" + issueTitle + "*" + "\n" + ">" + issueDesc));
            }
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





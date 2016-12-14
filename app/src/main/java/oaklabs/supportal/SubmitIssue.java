package oaklabs.supportal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.os.StrictMode;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SubmitIssue extends Activity implements View.OnClickListener {

    EditText titleEdit;
    EditText descriptionEdit;
    Button submitBtn;
    CheckBox priority;
    String slackParams;
    RequestQueue issueQueue;
    Spinner kind;
    String selectedKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_issue);
        titleEdit = (EditText)findViewById(R.id.issueTitleEdit);
        descriptionEdit = (EditText)findViewById(R.id.descriptionEdit);
        priority = (CheckBox)findViewById(R.id.priorityChkBox);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        kind = (Spinner)findViewById(R.id.kindSpinner);
        submitBtn.setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void onClick(View v){
        if(titleEdit.getText().toString().trim().length() != 0 && descriptionEdit.getText().toString().trim().length() != 0) {
            String issueTitle = titleEdit.getText().toString();
            String issueDesc = descriptionEdit.getText().toString();
            String kindSelected = kind.getSelectedItem().toString();

            issueQueue = Volley.newRequestQueue(this);
            JSONObject jsObj = new JSONObject();

                    try {
                        jsObj.put("title", issueTitle);
                        jsObj.put("description", issueDesc);
                        jsObj.put("kind", kindSelected);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://hurst.pythonanywhere.com/supportal/api/", jsObj, new Response.Listener<JSONObject>() {
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
                api.call(new SlackMessage("#random", "supportal", "just created a high priority:\n" + "*" + issueTitle + "*" + "\n" + ">" + issueDesc));
            }
        }
    }

    /*public class Networking extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params){

            return null;
        }

    }*/
/*
        private static String slackAPIKey = "api key";
        private static SlackSession slackSession;
        private static SubmitIssue simpleSlackAPITest;

        public SubmitIssue(String[] args) {
            System.out.println("Constructing SimpleSlackAPITest");
            simpleSlackAPITest = this;
            run(args);
        }

        public static void main(String[] args) {
            System.out.println("Hello world, SimpleSlackAPITest here.");
            new SubmitIssue(args);
        }

        public static void run(String[] args) {
            System.out.println("SimpleSlackAPITest > run > Slack Init");
        /* SLACK INIT
            slackSession = SlackSessionFactory.createWebSocketSlackSession(slackAPIKey);
            try {
                slackSession.connect();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }


            SlackEvents slackevents = simpleSlackAPITest.new SlackEvents();
            slackSession.addMessagePostedListener(slackevents);

            while (true) {
                System.out.println("loop");
                simpleSlackAPITest.sendMessageToAChannel(slackSession);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public class SlackEvents implements SlackMessagePostedListener {

            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session) {
                // Ignore self.
                if (session.sessionPersona().getId().equals(event.getSender().getId())) {
                    System.out.println("Ignoring self.");
                    return;
                } else {
                    System.out.println("Not Self");
                    System.out.println(session.sessionPersona().getId());
                    System.out.println(event.getSender().getId());
                }

                SlackChannel channelOnWhichMessageWasPosted = event.getChannel();
                String messageContent = event.getMessageContent();
                SlackUser messageSender = event.getSender();

                System.out.println(channelOnWhichMessageWasPosted.getName() + ":" + messageSender.getUserName() + ":"
                        + messageContent);
            }
        }

        public void sendMessageToAChannel(SlackSession session) {

            // get a channel
            SlackChannel channel = session.findChannelByName("test1");

            session.sendMessage(channel, "test message");
            session.sendMessage(channel, "test message with name", null,
                    SlackChatConfiguration.getConfiguration().withName("Zach"));
        }
        */

    public void logout(){
        Intent logout = new Intent(this, UserLogin.class);
        startActivity(logout);
    }

    public void manageAccount(){
        Intent manage = new Intent(this, ManageUserAccount.class);
        startActivity(manage);
    }
}





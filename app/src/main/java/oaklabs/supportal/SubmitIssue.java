package oaklabs.supportal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.os.StrictMode;
import android.os.AsyncTask;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SubmitIssue extends Activity implements View.OnClickListener {

    EditText titleEdit;
    EditText descriptionEdit;
    Button submitBtn;
    CheckBox priority;
    String slackParams;
    RequestQueue issueQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_issue);
        titleEdit = (EditText)findViewById(R.id.issueTitleEdit);
        descriptionEdit = (EditText)findViewById(R.id.descriptionEdit);
        priority = (CheckBox)findViewById(R.id.priorityChkBox);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /*SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
        api.call(new SlackMessage("#random", "Zach", "Test Message"));*/
       /* HttpURLConnection connection;
        try {
            URL slackUrl = new URL("https://hooks.slack.com/services/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");
            connection = (HttpURLConnection) slackUrl.openConnection();
            connection.connect();

            OutputStream stream = connection.getOutputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void onClick(View v){
        if(titleEdit.getText().toString().trim().length() != 0 && descriptionEdit.getText().toString().trim().length() != 0) {
            String issueTitle = titleEdit.getText().toString();
            String issueDesc = descriptionEdit.getText().toString();

            /*AccountManager am = AccountManager.get(this);
            Bundle options = new Bundle();

            am.getAuthToken(
                    "shawdl",                       // Account retrieved using getAccountsByType()
                    "issue",                        // Auth scope
                    options,                        // Authenticator-specific options
                    this,                           // Your activity
                    new OnTokenAcquired(),          // Callback called when a token is successfully acquired
                    new Handler(new OnError()));    // Callback called if an error occurs

            URL url = null;
            try {
                url = new URL("https://api.bitbucket.org/1.0/repositories/shawdl/supportal2016test/issues");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("client_id", "djnug2AYYSwzudDYdj");
                conn.addRequestProperty("client_secret", "K42BdNv8WXAD5erXt8ZK9SEycH39mQ5u");
                conn.setRequestProperty("Authorization", "OAuth " + am);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");

                JSONObject issue = new JSONObject();

                issue.put("title","Android Example");
                issue.put("content", "This is the Android example");

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(issue.toString());
                wr.flush();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            issueQueue = Volley.newRequestQueue(this);

            final String URL = "https://api.bitbucket.org/1.0/repositories/shawdl/supportal2016test/issues";
            // Post params to be sent to the server
            HashMap<String, String> params = new HashMap<String, String>();
            String creds = String.format("shawdl:supportal2016","USERNAME","PASSWORD");
            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
            params.put("Authorization", auth);
            params.put("title", "Android test");
            params.put("content", "test");

            JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            issueQueue.add(req);

            if(priority.isChecked()) {
                SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
                api.call(new SlackMessage("#random", "supportal", "just created a high priority:\n" + "*" + issueTitle + "*" + "\n" + ">" + issueDesc));
            }
        }
    }

    public String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL("https://api.bitbucket.org/1.0/repositories/shawdl/supportal2016test/issues");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
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
}





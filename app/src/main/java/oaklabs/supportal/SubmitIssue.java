package oaklabs.supportal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.os.StrictMode;
import android.os.AsyncTask;
import com.loopj.android.http.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

            

            if(priority.isChecked()) {
                SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
                api.call(new SlackMessage("#random", "supportal", "just created a high priority:\n" + "*" + issueTitle + "*" + "\n" + ">" + issueDesc));
            }
        }
        /*

        String urlString = "https://api.bitbucket.org/2.0/repositories/zmenken/testing";
        URL bb = null;

        // handle Exception
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.out.println("The URL is not valid.");
            System.out.println(e.getMessage());
        }

        // print
        if (url != null) {
            System.out.println(url.toString());
        }


        bb = new URL(urlString);
        URLConnection bbcon = bb.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(bbcon.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null){
            System.out.println(inputLine);
        }
        in.close();
        */
        /*


        String repolocation = "https://api.bitbucket.org/2.0/repositories/zmenken/testing";
        URL bb = null;

        bb = new URL(repolocation);
        URLConnection bbcon = bb.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        bbcon.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null){
            System.out.println(inputLine);
        }
        in.close();
        */
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





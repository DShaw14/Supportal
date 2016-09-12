package oaklabs.supportal;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;


public class SubmitIssue extends Activity {

    EditText nameEdit;
    EditText titleEdit;
    EditText descriptionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_issue);

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

    /*public class Networking extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params){

            return null;
        }

    }*/

        private static String slackAPIKey = "xoxp-63069436370-63053733136-77700959316-30a9396b86";
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
        /* SLACK INIT */
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
}

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
}

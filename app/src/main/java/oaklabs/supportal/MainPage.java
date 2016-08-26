package oaklabs.supportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainPage extends Activity implements View.OnClickListener {

    Button submitPageBtn;
    Button managePageBtn;

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
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.submitIssue:
                Intent submit = new Intent(this, SubmitIssue.class);
                startActivity(submit);
                break;
            case R.id.manageIssues:
                Intent manage = new Intent(this, ManageIssues.class);
                startActivity(manage);
                break;
        }
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

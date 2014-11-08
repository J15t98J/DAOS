package uk.herts.sch.damealiceowens.studio.daos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import uk.sch.herts.damealiceowens.studio.daos.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // GUI FUNCTIONS

    public boolean validate(String uname, String pword) {
        // return DB query (if it doesn't exist, create it!
        return true; // test purposes
    }


    public void login(View view) {
        String username = ((EditText)findViewById(R.id.uname)).getText().toString();
        String password = ((EditText)findViewById(R.id.pword)).getText().toString();

        if(validate(username, password)) {
            Intent intent = new Intent(this, HomeActivity.class);

        }
    }
}

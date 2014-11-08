package uk.herts.sch.damealiceowens.studio.daos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.sql.ResultSet;
import java.sql.SQLException;

import uk.herts.sch.damealiceowens.studio.daos.db.DatabaseConnection;
import uk.sch.herts.damealiceowens.studio.daos.R;

public class LoginActivity extends Activity {
    protected String pathToDB = "dev.db";

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
        DatabaseConnection db = new DatabaseConnection(pathToDB);
        ResultSet results = db.executeQuery("SELECT * FROM STUDENTS, STAFF WHERE USERNAME='" + uname + "'");
        try {
            if(results.first()) { // If there is a user
                return results.getString("PASSWORD") == pword;
            } else {
                //TODO: implement sign-up if user does not exist (require creation pword?)
                //db.executeUpdate("INSERT INTO ")
                //return true
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        // return DB query (if it doesn't exist, create it!
        return true; // test purposes
    }


    public void login(View view) {
        String username = ((EditText)findViewById(R.id.uname)).getText().toString();
        String password = ((EditText)findViewById(R.id.pword)).getText().toString();

        //TODO: what if we can't connect?
        if(validate(username, password)) {
            Intent intent = new Intent(this, HomeActivity.class);

        } else {
            //TODO: errors
        }
    }
}

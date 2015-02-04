package uk.co.appsbystudio.damealiceowens.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import uk.co.appsbystudio.damealiceowens.R;

public class Login extends Fragment {

    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public boolean validate(String uname, String pword) {
        /*
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
        //*/
        return true; // test purposes
    }


    public void login(View view) {
        String username = ((EditText) getActivity().findViewById(R.id.uname)).getText().toString();
        String password = ((EditText) getActivity().findViewById(R.id.pword)).getText().toString();

        //TODO: what if we can't connect?
        if(validate(username, password)) {
            Intent intent = new Intent(getActivity(), Home.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            //TODO: errors
        }
    }
}

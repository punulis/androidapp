package com.example.mokytojas.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Register2Activity extends AppCompatActivity {

    private static final String INSERT_URL2 = "https://punulis.000webhostapp.com/mobile/users.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        setTitle(R.string.registerTitle);

        final EditText username = findViewById(R.id.textUsername);
        final EditText password = findViewById(R.id.textPassword);
        final EditText email = findViewById(R.id.textEmail);
        Button registerBtn = findViewById(R.id.btnRegister);

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String userLogin = username.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                User person = new User(userLogin, userPassword);
                person.setEmailForRegistration(userEmail);

                username.setError(null);
                email.setError(null);
                password.setError(null);

                if (Validation.isValidUsername(person.getUsernameForRegistration()) && Validation.isValidPassword(person.getPasswordForRegistration()) && Validation.isValidEmail(person.getEmailForRegistration())) {
                    insertToDB(person);
                    Toast.makeText(Register2Activity.this, person.getUsernameForRegistration() + "\n" + person.getEmailForRegistration() + "\n" +  person.getPasswordForRegistration(), Toast.LENGTH_SHORT).show();
                    Intent goToLoginActivity = new Intent(Register2Activity.this, LoginActivity.class);
                    startActivity(goToLoginActivity);
                } else{
                    //Toast.makeText(LoginActivity.this, "Error. Wrong username or password.", Toast.LENGTH_SHORT).show();
                    username.setError(getResources().getString(R.string.registerError));
                    username.requestFocus();
                }


            }
        });


    }

    private void insertToDB(User person) {
        class NewEntry extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            DB users = new DB();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register2Activity.this,
                        getResources().getString(R.string.entryDatabaseInfo),
                        null, true, true);
            }

            @Override
            protected String doInBackground(String... strings) {
                // Pirmas string yra raktas, antras - reikšmė.
                HashMap<String, String> userData = new HashMap<String, String>();
                userData.put("name", strings[0]);
                userData.put("email", strings[1]);
                userData.put("password", strings[2]);
                userData.put("action", "insert");

                String result = users.sendPostRequest(INSERT_URL2, userData);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Register2Activity.this, s, Toast.LENGTH_SHORT).show();
                Intent goToLoginActivity = new Intent(Register2Activity.this, LoginActivity.class);
                startActivity(goToLoginActivity);
            }
        }

        NewEntry newEntry = new NewEntry();
        newEntry.execute(person.getUsernameForRegistration(),
                person.getEmailForRegistration(),
                person.getPasswordForRegistration());
    }
}




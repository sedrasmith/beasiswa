package o.astra1st;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class  LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    //private FirebaseDatabase database;
    //private DatabaseReference mreference;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //database = FirebaseDatabase.getInstance();
        //mreference = database.getReference("users");
        //enable offline mode
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);



        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();





                auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "wrong username or password", Toast.LENGTH_LONG).show();
                                } else {
                                    String[] separated_username = username.split("@");

                                    myRef = database.getReference("user");
                                    myRef.child(separated_username[0]).child("login").setValue(true);


                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                    intent.putExtra("id", separated_username[0]);
                                    LoginActivity.this.startActivity(intent);
                                    finish();

                                }
                            }
                        });



            }

        });
    }
}

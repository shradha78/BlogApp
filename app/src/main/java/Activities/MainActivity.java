package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.theblog.vlogapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private EditText email,password;
    private Button loginButton,createAccntButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        setUpUI();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser != null){

                    Toast.makeText(MainActivity.this, "Signed In",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,PostListActivity.class));
                    finish();

                }else{
                    Toast.makeText(MainActivity.this, "Not Signed In",Toast.LENGTH_LONG).show();
                }

            }
        };
        loginButton.setOnClickListener(this);
        createAccntButton.setOnClickListener(this);
    }
    // This method coz On create method is slows down a bit
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    private void setUpUI(){
        email= (EditText) findViewById(R.id.EmailId);
        password= (EditText) findViewById(R.id.PasswordId);
        loginButton = (Button) findViewById(R.id.LoginButtonId);
        createAccntButton = (Button) findViewById(R.id.CreateAccountButtonId);
    }

    @Override
    public void onClick(View v) {
        Log.d("On CLICK" , " This MEthod *********");
        switch(v.getId()){
            case (R.id.LoginButtonId) :
                if(!email.getText().toString().equals("") && !(password.getText().toString().equals(""))){
                    String emailText = email.getText().toString();
                    String pwdText = password.getText().toString();
                    login(emailText,pwdText);

                }else{
                    email.setError("Both Fields have to be field");
                    password.setError("Both Fields have to be field");
                }

                break;
            case(R.id.CreateAccountButtonId) :
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                finish();
                break;
            default: Log.d("No Button" ,"*******");
        }
    }

    private void login(String emailText, String pwdText) {
        Log.e("Login Method called " , emailText + " : " + pwdText);
        mAuth.signInWithEmailAndPassword(emailText, pwdText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Authentication ",
                                    Toast.LENGTH_SHORT).show();
                            mUser = mAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this,PostListActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Errorrrrr$$$$$ ", e.getLocalizedMessage());
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_signOut){
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
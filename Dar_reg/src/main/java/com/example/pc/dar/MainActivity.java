package com.example.pc.dar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.util.Patterns;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText edName,edEmail,edPassword,edDepartment;
    Button btnReg;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = (EditText) findViewById(R.id.editName);
        edEmail = (EditText) findViewById(R.id.editEmail);
        edPassword = (EditText) findViewById(R.id.editPassword);
        edDepartment = (EditText) findViewById(R.id.editDepartment);
        btnReg = (Button) findViewById(R.id.buttonRegister);
        mAuth = FirebaseAuth.getInstance();
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edName.getText().toString().isEmpty()){
                    edName.setError("Name required");
                    edName.requestFocus();
                    return;
                }
                if(edEmail.getText().toString().isEmpty()){
                    edEmail.setError("Email required");
                    edEmail.requestFocus();
                    return;
                }
                if(edPassword.getText().toString().isEmpty()){
                    edPassword.setError("Password required");
                    edPassword.requestFocus();
                    return;
                }
                if(edDepartment.getText().toString().isEmpty()){
                    edDepartment.setError("Department required");
                    edDepartment.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString()).matches()){
                    edEmail.setError("Enter valid email");
                    edEmail.requestFocus();
                    return;
                }
                if(edPassword.getText().toString().length()<6){
                    edPassword.setError("Minimum length of password required is 6");
                    edPassword.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(edEmail.getText().toString(),edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this,LoginPage.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                FirebaseUser user = mAuth.getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.btnReg).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })

            }
        });
    }
}

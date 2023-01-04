package com.example.converter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private EditText user_name, pass_word;
    private String m_Text = "";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_name=findViewById(R.id.email);
        pass_word=findViewById(R.id.password);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_sign = findViewById(R.id.btn_signup);
        mAuth=FirebaseAuth.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= user_name.getText().toString().trim();
                String password=pass_word.getText().toString().trim();
                if(email.isEmpty())
                {
                    user_name.setError("Email is empty");
                    user_name.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    user_name.setError("Enter the valid email");
                    user_name.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    pass_word.setError("Password is empty");
                    pass_word.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    pass_word.setError("Length of password should be more than 6");
                    pass_word.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        m_Text = user_name.getText().toString();
                        int lim=m_Text.indexOf("@");
                        String s=m_Text.substring(0,lim);
                        Intent i = new Intent(MainActivity.this,Decrypt.class);
                        i.putExtra("YourValueKey", s);
                       startActivity(i);
                        Toast.makeText(MainActivity.this,
                                "Login Successful",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,
                                "Please Check Your login Credentials",
                                Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });


        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });


    }

}
package com.example.converter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt extends AppCompatActivity {

    private EditText editText;
    private ListView listView;

    private DatabaseReference databaseReference;

    private String stringMessage;
    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

            //editText = findViewById(R.id.edittext);
            listView = findViewById(R.id.listView);
        Intent intent = getIntent();
        String user = intent.getExtras().getString("YourValueKey");

            try{
                databaseReference = FirebaseDatabase.getInstance().getReference(user);

                try {
                    cipher = Cipher.getInstance("AES");
                    decipher = Cipher.getInstance("AES");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }

                secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        stringMessage = dataSnapshot.getValue().toString();
                        stringMessage = stringMessage.substring(1,stringMessage.length()-1);

                        String[] stringMessageArray = stringMessage.split(", ");
                        Arrays.sort(stringMessageArray);
                        String[] stringFinal = new String[stringMessageArray.length*2];
                        ArrayList<Message> arrayList = new ArrayList<>();


                        try {
                            for (int i = 0; i<stringMessageArray.length; i++) {
                                String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                                stringFinal[2 * i] = "Message: "+AESDecryptionMethod(stringKeyValue[1])+" Time: "+(String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", Long.parseLong(stringKeyValue[0]));
                                stringFinal[2 * i+1] = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", Long.parseLong(stringKeyValue[0]));

                                arrayList.add(new Message(R.drawable.user1,"Message: "+AESDecryptionMethod(stringKeyValue[1]),
                                        " Date: "+(String) android.text.format.DateFormat.format("dd-MM-yyyy", Long.parseLong(stringKeyValue[0]))+"\n"+" Time: "
                                                +(String) android.text.format.DateFormat.format("hh:mm a", Long.parseLong(stringKeyValue[0]))));
                                //listView.setAdapter(new String(Decrypt.this, android.R.layout.simple_list_item_1, stringFinal[2 * i]));

                            }

                          MessageAdapter messageAdapter = new MessageAdapter(Decrypt.this,R.layout.list_row,arrayList);
                            listView.setAdapter(messageAdapter);
                           // listView.setAdapter(new ArrayAdapter<String>(Decrypt.this, android.R.layout.simple_list_item_1, stringFinal));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

//        public void sendButton(View view){
//
//            Date date = new Date();
//            databaseReference.child(Long.toString(date.getTime())).setValue(AESEncryptionMethod(editText.getText().toString()));
//            editText.setText("");
//
//        }

//        private String AESEncryptionMethod(String string){
//
//            byte[] stringByte = string.getBytes();
//            byte[] encryptedByte = new byte[stringByte.length];
//
//            try {
//                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//                encryptedByte = cipher.doFinal(stringByte);
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            }
//
//            String returnString = null;
//
//            try {
//                returnString = new String(encryptedByte, "ISO-8859-1");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            return returnString;
//        }

        private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
            byte[] EncryptedByte = string.getBytes("ISO-8859-1");
            String decryptedString = string;

            byte[] decryption;

            try {
                decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
                decryption = decipher.doFinal(EncryptedByte);
                decryptedString = new String(decryption);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            return decryptedString;
        }
    }
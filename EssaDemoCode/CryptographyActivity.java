package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Rule06Activity extends AppCompatActivity {
    //Rec. 01. Declaration and Initialization - Do not declare more than one variable per declaration.
    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule06);

        // Generate Key
        try {
            //Rec. 01. Declaration and Initialization - Minimize the scope of variables.
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(AES_KEY_SIZE);
            secretKey = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] IV = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        Button buttonEncrypt = findViewById(R.id.buttonEncrypt);
        TextView tvECB = findViewById(R.id.textViewECB);
        TextView tvGCM = findViewById(R.id.textViewGCM);

        buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText plainText = (EditText) findViewById(R.id.editTextPlaintext);
                    //Rule 00. Input Validation and Data Sanitization - String.replace is used to ensure no line endings are present in the user input.
                    //Rec. 00. Input Validation and Data Sanitization - Understand how escape characters are interpreted when strings are loaded.
                    byte[] encryptECB = encryptECB(filterString(plainText.getText().toString().replace("\\n", "")).getBytes(), secretKey);
                    //An initialization vector is an arbitrary number used in combination with a secret key as a means to encrypt data.
                    byte[] encryptGCM = encryptGCM(filterString(plainText.getText().toString().replace("\\n", "")).getBytes(), secretKey, IV);
                    String encryptTextECB = new String(encryptECB, "UTF-8");
                    String encryptTextGCM = new String(encryptGCM, "UTF-8");
                    tvECB.setText(encryptTextECB);
                    tvGCM.setText(encryptTextGCM);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // encryptGCM() and encryptECB() would violate Rec22 MET50-J, MET51-J with the below shape:
    // (Ambiguous or confusing overloading, and not using overload to differentiate execution)
    /*public static byte[] encrypt(byte[] plaintext, SecretKey key) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(plainText);
    }
      public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] IV) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        return cipher.doFinal(plaintext);
      }
     */
    public static byte[] encryptGCM(byte[] plaintext, SecretKey key, byte[] IV) throws Exception
    {
        // Get Cipher Instance - Rule 06. Cryptography - NOT using default AES encryption (AES/ECB)
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform and Return Encryption
        return cipher.doFinal(plaintext);
    }

    public static byte[] encryptECB(byte[] plainText, SecretKey key) throws Exception
    {
        // Rule 06. Cryptography - Using default AES encryption (AES/ECB)
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // Perform and Return Encryption
        return cipher.doFinal(plainText);
    }

    public static String filterString(String str) {
        String tag = Normalizer.normalize(str, Normalizer.Form.NFKC);

        // Rule 00. Input Validation and Data Sanitization - Perform any string modifications before validation.
        // Replaces all HTML tags in user input with an empty tag to avoid malicious tags (i.e. <script>)
        tag = tag.replaceAll("<[^>]*>", "<>");

        // Validate input
        Pattern pattern = Pattern.compile("<>");
        Matcher matcher = pattern.matcher(tag);
        if (matcher.find()) {
            throw new IllegalArgumentException("Invalid input");
        }
        return tag;
    }
}

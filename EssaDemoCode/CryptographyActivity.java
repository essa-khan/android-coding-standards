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
    /*
    Android (Java applied) Rec. 08. Declaration and Initialization - Do not declare more than one variable per declaration.
    It is recommended to not declare multiple variables of different data types or a mix of initialized and uninitialized 
    in a single declaration. 
    
    The general practice is to declare each variable on its own line with a comment explaining 
    its role in the code. Failing to do so may cause confusion for the developer and others.
    */
    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule06);

        // Generate Key
        /*
          Android (Java applied) Rec. 08. Declaration and Initialization - Minimize the scope of variables.
          The scope of a variable is the part of the program where the variable is accessible. Variables 
          can be defined as having one of three types of scope: class (any variable declared within a class 
          would be accessible by all methods in that class), method (any variable declared within a method), 
          and block (any variable declared in loops would not be accessible after the loop concludes). 
            
          The practice of scope minimization is to detect variables that may have a larger scope than it is 
          actually required by the code to execute.
        */
        try {
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

        
        /*
        Android (Java applied) Rule 19. Input Validation and Data Sanitization - Do not log unsanitized user input. String.replace
        is used to ensure no line endings are present in the user input. User input should be suitably encoded before it is logged. 
        Doing so will restrict a malicious user from causing confusion in other ways in the logs.
        */
        
        /*
        Android (Java applied) Rec. 19. Input Validation and Data Sanitization - Understand how escape characters are interpreted 
        when strings are loaded. The incorrect use of escape characters in string inputs may potentially result in misinterpretation 
        and incorrect data output. Use double backslash! (\\n, \\b, \\t, \\r)
        */
        buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText plainText = (EditText) findViewById(R.id.editTextPlaintext);
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
    /*
    Android Rule 06. Cryptography - Do not use the Android cryptographic security provider encryption default for AES (ECB).
    
    Use AES/GCM/NoPadding. CBC mode eliminates ECB’s issue by carrying information from the encryption or decryption of 
    one block to the next with the use of an initialization vector (IV).
    */
    public static byte[] encryptGCM(byte[] plaintext, SecretKey key, byte[] IV) throws Exception
    {
        // Get Cipher Instance
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
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // Perform and Return Encryption
        return cipher.doFinal(plainText);
    }

    /*
    // Android (Java applied) Recommendation 22 MET56-J Do not use Object.equals() to compare cryptographic keys
    // NON-COMPLIANT SOLUTION
    private static boolean compareKeys(SecretKey key1, SecretKey key2) {
        if (key1.equals(key2)) {
            return true;
        }
        return false;
    }

    // COMPLIANT SOLUTION
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean compareKeys(SecretKey key1, SecretKey key2) {
        if (key1.equals(key2)) {
            return true;
        }
        String encodedKey1 = Base64.getEncoder().encodeToString(key1.getEncoded());
        String encodedKey2 = Base64.getEncoder().encodeToString(key2.getEncoded());

        // Convert the keys to a string first checks whether the secret keys are equivalent
        if (encodedKey1.equals(encodedKey2)) {
            return true;
        }
        return false;
    }
    */
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

package com.example.it355project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Button saveButton, loadButton; //App's buttons
    EditText fileInput, userName; //App's edit text fields
    TextView fileOutput; // App's text field to display text
    private static final String FILE_NAME = "example.txt"; //File for saving demo strings
    private static final String SERIALIZE_FILE = "serialize.txt"; //File for saving serialized objects
    ArrayList<Input> textList = new ArrayList<>(); //Array list to hold input objects for demoing

    //Setting all the app's ui components to the defined variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);
        fileInput = findViewById(R.id.fileInput);
        fileOutput = findViewById(R.id.fileOutput);
        userName = findViewById(R.id.userName);
    }
    /*
    * Save function for taking the text from the user name edit text and saving it to the example.txt
    * using a fileOutputStream
     */
    public void save (View view) {
        String text = fileInput.getText().toString(); // Getting the input from the edit box

        if (text != "") { // if there was an input create an input object
            Input input = new Input(); // creating a new object
            input.text = text; //setting the object's text
            input.origin = input.USER_INPUT; // setting the object origin
            textList.add(input); // add the Object to the array list
        }
        // The following code creates a File Output Stream and writes the text to the file
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE); // opening file privately
            fileOutputStream.write(text.getBytes(StandardCharsets.UTF_8)); // writing the text

            fileInput.getText().clear();
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show(); // generate a toast notification for the user
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close(); // close the output screen
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // function to save the text from the username input and is very similar to save
    public void saveUser (View view) {
        String text = userName.getText().toString(); // Getting the username from the edit box

        if (text != "") { // if there was an input create an input object
            Input input = new Input(); // creating a new object
            input.text = text; //setting the object's text
            input.origin = input.USER_NAME; // setting the object origin
            textList.add(input); // add the Object to the array list
        }
        // The following code creates a File Output Stream and writes the text to the file
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE); // opening file privately
            fileOutputStream.write(text.getBytes(StandardCharsets.UTF_8)); // writing the text

            userName.getText().clear();
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close(); // close the output screen
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // This function loads the text from the file and displays it in the TextView box
    public void load(View view) {
        FileInputStream fileInputStream = null; // creating an inputStream
        try {
            fileInputStream = openFileInput(FILE_NAME); // Opening the file
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream); // Creating an input stream
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // Creating a buffered reader for text
            StringBuilder stringBuilder = new StringBuilder(); // Creating a string builder
            String text; // Variable to hold data that is being read

            while((text = bufferedReader.readLine()) != null) { // loop to read the text
                stringBuilder.append(text).append("\n");
            }
            fileOutput.setText(stringBuilder.toString()); // Setting the text to the text view field
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close(); // Closing the input stream
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Function to display logging and serialization
    public void printList(View view){
        Iterator<Input> itr = textList.iterator(); // Iterator for parsing array list
        String output =""; // Output string to build upon to output to user
        FileOutputStream fileOutputStream = null; // Stream for file output
        FileInputStream fileInputStream = null; // Stream for file input

        try {
            fileOutputStream = openFileOutput(SERIALIZE_FILE, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =  new ObjectOutputStream(fileOutputStream);
            fileInputStream = openFileInput(SERIALIZE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            while (itr.hasNext()) { // Iterating through all the Input objects to output their data
                Input element = itr.next();
                if (element.origin == Input.USER_NAME) { // If the origin is username, hide it to follow rules of logging
                    Log.i("LIST_OUTPUT", "UserName");
                    output = output + " " + "User Name";
                } else {
                    Log.i("LIST_OUTPUT", element.text);
                    output = output + " " + element.text;
                }
                objectOutputStream.writeObject(element); // Output object for serialization demo
                try {
                    Input newInput = (Input)ois.readObject(); // Read serialized object for demo
                    Log.i("OBJ_OUTPUT", newInput.origin); // Log out the origin
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close(); // close the file output stream
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileInputStream != null) {
                try {
                    fileInputStream.close(); // close the file input stream
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        fileOutput.setText(output); // Set the output text to the TextView
    }

}

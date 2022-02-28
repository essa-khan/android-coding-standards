package com.example.it355project;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

// Dummy class used to show the implementations of the code and the rules it follows
public class Input extends ContentProvider implements Serializable {
    String text; // Text usually assigned from user, either will be a username or just text
    String origin; // Shows from which input field the text came from
    static final String USER_NAME = "user"; // Use if text came from user name
    static final String USER_INPUT = "input"; // Use if generic input

    // The following methods would need to be implemented to use a ContentProvider
    // For this project the ContentProvider does not need to be implemented but the rule is shown
    // in the xml file.
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

package com.example.entrancetask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebHelper {
    public static String contactsURL = "https://randomuser.me/api/?results=30";

    public interface ImageDownloadListener {
        void onLoadingComplete(Bitmap bitmap);

        void onLoadingFailed();
    }

    public interface ContactsDownloadListener {
        void onLoadingComplete(List<Contact> contacts);

        void onLoadingFailed();
    }

    public static void getBitmapFromURL(
            String src,
            ImageDownloadListener listener
    )
    {
        new ImageDownloadTask(listener).execute(src);
    }

    public static void loadContacts(ContactsDownloadListener listener) {
        new ContactsDownloadTask(listener).execute(contactsURL);
    }

    private static class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        private ImageDownloadListener listener;

        ImageDownloadTask(ImageDownloadListener listener) {
            this.listener = listener;
            if (!MainActivity.isOnline()) cancel(true);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            if (strings.length == 0) cancel(true);
            try {
                java.net.URL url = new java.net.URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            listener.onLoadingFailed();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            listener.onLoadingComplete(bitmap);
        }
    }


    private static class ContactsDownloadTask extends AsyncTask<String, Void, List<Contact>> {

        private ContactsDownloadListener listener;

        ContactsDownloadTask(ContactsDownloadListener listener) {
            this.listener = listener;
            if (!MainActivity.isOnline()) cancel(true);
        }

        @Override
        protected List<Contact> doInBackground(String... strings) {
            if (strings.length == 0) cancel(true);

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                BufferedReader streamReader
                        = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                JSONObject obj = new JSONObject(responseStrBuilder.toString());
                if (obj.has("error")) {
                    cancel(true);
                    return null;
                }
                JSONArray results = obj.getJSONArray("results");
                List<Contact> contacts = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject conInfo = results.getJSONObject(i);
                    Contact con = new Contact();
                    con.setIconURL(conInfo.getJSONObject("picture").getString("medium"));
                    con.setFirstName(conInfo.getJSONObject("name").getString("first"));
                    con.setLastName(conInfo.getJSONObject("name").getString("last"));
                    con.setGender(conInfo.getString("gender"));
                    contacts.add(con);
                }
                return contacts;
            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            listener.onLoadingComplete(contacts);
        }

        @Override
        protected void onCancelled() {
            listener.onLoadingFailed();
        }
    }
}

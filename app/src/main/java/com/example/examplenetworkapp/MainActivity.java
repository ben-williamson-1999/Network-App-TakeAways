package com.example.examplenetworkapp;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textView = null;

    private ArrayList<String> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
    }

    public void onClick(View v){
        new GetDataClass().execute("http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_postcode&postcode=WA15+7UR");
    }

    private class GetDataClass extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                URLConnection tc = url.openConnection();
                InputStreamReader isr = new InputStreamReader(tc.getInputStream());
                BufferedReader in = new BufferedReader(isr);

                String line;
                while((line = in.readLine()) != null){
                    JSONArray ja = new JSONArray(line);
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo = (JSONObject) ja.get(i);
                        listItems.add(jo.getString("BusinessName"));
                        listItems.add(jo.getString("RatingValue"));
                    }
                }
                textView = findViewById(R.id.dump);
                StringBuilder string = new StringBuilder();
                for(String s: listItems){
                    string.append(s);
                }
                //textView.setText(string.toString() + "\n");

            } catch (Exception e){
                e.printStackTrace();
            }

            return listItems.toString();
        }

        @Override
        protected void onPostExecute(String string) {
            textView.findViewById(R.id.dump);
            textView.setText(string);
        }
    }
}

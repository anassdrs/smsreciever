package com.example.asd.smsreceiver;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Main extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private TextView smsText;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        RequestTask task = new RequestTask();
        task.execute();
    }

    public class RequestTask extends AsyncTask<Void, Void, Void> {
        private String message;
        private String key;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EditText keyText = (EditText) findViewById(R.id.keyText);
            this.key = keyText.getText().toString();
        }


        @Override
        protected Void doInBackground(Void... params) {
            String sUrl = "http://vapnik.ru:8000/receive/?key=";
            sUrl = sUrl + this.key;
            try {
                URL url = new URL(sUrl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String inputLine;
                String all = "";
                while ((inputLine = in.readLine()) != null)
                    all += inputLine;
                this.message = all;

            } catch (Exception e) {
                this.message = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView out = (TextView) findViewById(R.id.textView2);
            out.setText(this.message);
        }
    }
}


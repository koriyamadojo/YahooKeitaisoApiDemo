package com.kirishikistudios.android.yahookeitaisodemo3;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void analyze(final String sentence) {
        final Handler handler=new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String BR = System.getProperty("line.separator");
                URL url;
                try {
                    String appId = "YOUR_APP_ID_HERE";
                    url = new URL("http://jlp.yahooapis.jp/MAService/V1/parse?appid=" + appId + "&sentence=" + sentence);
                    URLConnection connection = url.openConnection();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                    final StringBuilder sb = new StringBuilder();

                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(in);
                    int type = parser.getEventType();
                    while(type != XmlPullParser.END_DOCUMENT) {
                        if(type == XmlPullParser.START_TAG) {
                            String name = parser.getName();
                            type = parser.next();
                            if(type == XmlPullParser.TEXT) {
                                sb.append(name + ":" + parser.getText() + BR);
                            }
                        }
                        type = parser.next();
                    }
                    Log.d("debug", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = (TextView) findViewById(R.id.textView2);
                            textView.setText(sb.toString());

                        }
                    });

                } catch (MalformedURLException | UnsupportedEncodingException | XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
        EditText edit = (EditText)findViewById(R.id.editText);
        SpannableStringBuilder sp = (SpannableStringBuilder)edit.getText();
        analyze(sp.toString());
    }
}

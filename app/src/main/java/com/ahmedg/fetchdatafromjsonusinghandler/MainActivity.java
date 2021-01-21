package com.ahmedg.fetchdatafromjsonusinghandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String URL = "https://jsonplaceholder.typicode.com/posts";
    Button btnNext, btnPrevious;
    TextView txtBody, txtID, txtUserID, txtTitle;
    Handler handler;
    HttpHandler httpHandler;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        txtBody = findViewById(R.id.txtBody);
        txtID = findViewById(R.id.txtID);
        txtUserID = findViewById(R.id.txtUserID);
        txtTitle = findViewById(R.id.txtTitle);
        httpHandler = new HttpHandler();
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle data=msg.getData();
                String id=data.getString("id");
                String userId=data.getString("userId");
                String title=data.getString("title");
                String body=data.getString("body");
                txtID.setText(id);
                txtUserID.setText(userId);
                txtTitle.setText(title);
                txtBody.setText(body);
            }
        };
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                if (index <= 0) {
                    index = 0;
                    Toast.makeText(MainActivity.this, "That is The First One", Toast.LENGTH_SHORT).show();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> json = getData(index);
                            Bundle bundle=new Bundle();
                            bundle.putString("id",json.get("id"));
                            bundle.putString("userId",json.get("userId"));
                            bundle.putString("title",json.get("title"));
                            bundle.putString("body",json.get("body"));
                            Message message = Message.obtain();
                            message.setData(bundle);
                            handler.sendMessage(message);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> json = getData(index);
                            Bundle bundle=new Bundle();
                            bundle.putString("id",json.get("id"));
                            bundle.putString("userId",json.get("userId"));
                            bundle.putString("title",json.get("title"));
                            bundle.putString("body",json.get("body"));
                            Message message = Message.obtain();
                            message.setData(bundle);
                            handler.sendMessage(message);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    Map<String, String> getData(int index) throws IOException, JSONException {
        String jsonString = httpHandler.makeServiceCallJson(URL);
        Map<String, String> allData = new HashMap<String, String>();

        if (jsonString != null) {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject c = jsonArray.getJSONObject(index);
            allData.put("id", c.getString("id"));
            allData.put("userId", c.getString("userId"));
            allData.put("title", c.getString("title"));
            allData.put("body", c.getString("body"));
        }
        return allData;
    }
}
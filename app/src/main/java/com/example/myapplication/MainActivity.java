package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String userVariableKey = "USER_VARIABLE";
    public static User CurrentUser;
    private final List<MaskElement> listQuote = new ArrayList<>();
    private final List<Mask> listFeeling = new ArrayList<>();
    private AdapterElement pAdapter;
    private Adapter dataRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvGreeting = findViewById(R.id.AvatarName);

        ImageView menu = findViewById(R.id.menu);
        menu.setOnClickListener(this);

        ImageView avatar = findViewById(R.id.avatar);
        avatar.setOnClickListener(this);
        avatar.setImageBitmap(CurrentUser.getAvatarBitmap());
        ImageView profile = findViewById(R.id.profile);
        profile.setOnClickListener(this);
        tvGreeting.setText("С возвращением, " + CurrentUser.getNickName() + "!");


        ListView ivProducts = findViewById(R.id.ListQuestion);
        pAdapter = new AdapterElement(MainActivity.this, listQuote);
        ivProducts.setAdapter(pAdapter);
        new GetQuotes().execute();

        RecyclerView rvFeeling = findViewById(R.id.FillingView);
        rvFeeling.setHasFixedSize(true);
        rvFeeling.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        dataRVAdapter = new Adapter(listFeeling, MainActivity.this);
        rvFeeling.setAdapter(dataRVAdapter);
        new GetFeeling().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putAll(outState);
        super.onSaveInstanceState(outState);
    }

    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CurrentUser = (User) savedInstanceState.get(userVariableKey);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                startActivity(new Intent(MainActivity.this, Menu.class));
                break;
            case R.id.avatar:
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, Profile.class));
                break;
        }
    }


    private class GetQuotes extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/quotes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                listQuote.clear();
                pAdapter.notifyDataSetInvalidated();

                JSONObject object = new JSONObject(s);
                JSONArray tempArray = object.getJSONArray("data");

                for (int i = 0; i < tempArray.length(); i++) {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    MaskElement tempProduct = new MaskElement(
                            productJson.getInt("id"),
                            productJson.getString("title"),
                            productJson.getString("image"),
                            productJson.getString("description")
                    );
                    listQuote.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception exception) {
                Toast.makeText(MainActivity.this, "Ошибка при отображении", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetFeeling extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/feelings");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                listFeeling.clear();
                dataRVAdapter.notifyDataSetChanged();

                JSONObject object = new JSONObject(s);
                JSONArray tempArray = object.getJSONArray("data");

                for (int i = 0; i < tempArray.length(); i++) {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    Mask tempProduct = new Mask(
                            productJson.getInt("id"),
                            productJson.getString("title"),
                            productJson.getString("image"),
                            productJson.getInt("position")
                    );
                    listFeeling.add(tempProduct);
                    dataRVAdapter.notifyDataSetChanged();
                }
            } catch (Exception exception) {
                Toast.makeText(MainActivity.this, "Ошибка при отображении", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
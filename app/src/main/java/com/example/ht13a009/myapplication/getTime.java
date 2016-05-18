package com.example.ht13a009.myapplication;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class getTime extends AsyncTask<URL, Void, String> {

    ArrayAdapter<String> mAdapter;

    public getTime(ArrayAdapter<String> adapter ) {
        super();
        mAdapter = adapter;
    }

    @Override
    protected String doInBackground(URL... url) {
        String result = "";
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url[0].openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            //int resp = conn.getResponseCode();
            // respを使っていろいろ
            result = readIt(conn.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    public String readIt(InputStream stream) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        try {
            stream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String resp){

        ArrayList<String> arr = new ArrayList<>();

        try {

            JSONObject json = new JSONObject(resp);

            JSONArray datas = json.getJSONArray("Dia");

            for (int i = 0; i < datas.length(); i++) {
                JSONObject data = datas.getJSONObject(i);

                arr.add(data.getString("departureTime") + " " + data.getString("note"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.clear();
        mAdapter.addAll(arr);
    }
}
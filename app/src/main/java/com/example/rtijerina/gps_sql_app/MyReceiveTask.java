package com.example.rtijerina.gps_sql_app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by RTijerina on 12/28/2016.
 */

public class MyReceiveTask extends AsyncTask<Void, Void, String> {

    private Context con;
    String gps_locations_lat, gps_locations_long;
    private TextView textView;

    public MyReceiveTask(Context context, TextView tv){
        con = context;
        textView = tv;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        InputStream is = null;

        URL url ;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://www.jordontijerina.com/files/GPS_Receive.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            is = new BufferedInputStream(urlConnection.getInputStream());
            Log.i("URL Connection", "is: " + is.available());
        } catch(MalformedURLException me) {
            me.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        }
        finally {
           // urlConnection.disconnect();
        }

        //convert response to string
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));

            StringBuilder sb = new StringBuilder();
            Log.i("Reader_1", "is ready: " + reader.ready());
            String line = "";
            Log.i("Reader_2", "is ready: " + reader.ready());
            while ((line = reader.readLine()) != null) {
                Log.i("Reader_3", "is ready: " + reader.ready());
                sb.append(line + "\n");
                Log.i("From Reader", "is: " + line);
            }

            Log.i("While loop", "is ready: " + reader.ready());
            is.close();
            result = sb.toString();

        } catch (Exception e) {
            Log.e("BufferedReader Part",result + "caused failure");
            // Toast.makeText(con, "Input reading fail", Toast.LENGTH_SHORT).show();
        }

        String return_String = "";
        //parse json data
        try {

            JSONArray jArray = new JSONArray(result);

            Log.i("JSONArray", "is: " + jArray.toString());

            for (int i = 0; i < jArray.length() - 1; i++) {

                JSONObject json_data = jArray.getJSONObject(i);

                String date = json_data.getString("date");

                String stime1 = String.valueOf(json_data.getDouble("latitude"));

                String  stime2 = String.valueOf(json_data.getDouble("longitude"));

                return_String+= date + " : " + stime1 + " : " + stime2 + "\n";
                //Toast.makeText(con, stime1 + "  :  " + stime2, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e( "JSON Error: " , e.getMessage() );
            e.printStackTrace();
            //Toast.makeText(con, "JsonArray fail", Toast.LENGTH_SHORT).show();
        }
        //return result;
        return return_String;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        textView.setText(str);
    }
}

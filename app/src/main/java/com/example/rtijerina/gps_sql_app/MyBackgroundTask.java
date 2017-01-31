package com.example.rtijerina.gps_sql_app;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by RTijerina on 12/25/2016.
 */

public class MyBackgroundTask extends AsyncTask<String, Void, String> {

    private Context mbt_context;
    String gps_locations_lat, gps_locations_long;


    public MyBackgroundTask(Context cont) {
        mbt_context = cont;
    }

    @Override
    protected String doInBackground(String... params) {

        String conn = "http://www.jordontijerina.com/files/conn.php";
        String register = "http://www.jordontijerina.com/files/register.php";
        String GPS_INSERT = "http://www.jordontijerina.com/files/GPS_Insert.php";
        String GPS_RETRIEVE = "http://www.jordontijerina.com/files/GPS_Retrieve.php";

        String purpose = params[0];
        gps_locations_lat = params[1];
        gps_locations_long = params[2];

        if(purpose.equals("register")){
            try{
                URL url = new URL(GPS_INSERT);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter(os,"UTF-8"));

                String data = URLEncoder.encode("latitude","UTF-8") + "=" + URLEncoder.encode(gps_locations_lat,"UTF-8") + "&"
                           +  URLEncoder.encode("longitude","UTF-8") + "=" + URLEncoder.encode(gps_locations_long, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                os.close();

                InputStream is = httpURLConnection.getInputStream();
                is.close();

                return "Success!!!";
            }
            catch( MalformedURLException me ){

            }
            catch( IOException e){

            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... voids) {
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(mbt_context, "Success: " + this.gps_locations_lat + ", " + this.gps_locations_long, Toast.LENGTH_SHORT).show();
    }
}

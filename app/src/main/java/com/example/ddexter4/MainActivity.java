package com.example.ddexter4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      editText=findViewById(R.id.editText);
      textView2=findViewById(R.id.textView2);
    }

    public void getweather(View view)
    {
        Log.i("info","Hello");
        DownloadTask task=new DownloadTask();
        String London="44418";
        String SanFrans="2487956";
        task.execute("https://www.metaweather.com/api/location/" + editText.getText().toString()+ "/");
    }

    public class DownloadTask extends AsyncTask<String, Void , String> {
        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            Log.i("info","Hello");
            HttpURLConnection urlConnection=null;
            try {
                Log.i("info","Hello");
                url =new URL(urls[0]);

                String London="44418";
                String SanFrans="2487956";
                Log.i("info",url.toString());
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                Log.i("info",result);
                while (data!=-1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                Log.i("info",result);

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("info","Hello");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if(s!=null){
                // Do you work here on success
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String weatherinfo=jsonObject.getString("consolidated_weather");
                    JSONArray arr=new JSONArray(weatherinfo);
                    String message = "";
                    for(int i=0;i<arr.length();i++)
                    {
                        JSONObject jsonPart=arr.getJSONObject(i);
                        String main=jsonPart.getString("weather_state_name");
                        String location_type=jsonPart.getString("max_temp");
                        if(!main.equals("")&&!location_type.equals(""))
                        {
                            message+="Day "+ i +"\n" + "Cloud Condition-" + main +"\n"+ "Max_Temp-"+location_type+ "\n";
                        }
                    }
                    if(!message.equals(""))
                    {
                        textView2.setText(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                // null response or Exception occur
                textView2.setText("");
            }

        }
    }
}
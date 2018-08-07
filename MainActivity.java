package com.smove.smovebot;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static String url="https://challenge.smove.sg/locations";
    TextToSpeech t1;
    EditText query;
    ArrayList latitude_ontrip = new ArrayList();
    ArrayList longitude_ontrip = new ArrayList();
    ArrayList latitude_notontrip = new ArrayList();
    ArrayList longitude_notontrip = new ArrayList();
    private ProgressDialog pDialog;
    Button findanswer;
    Button mapping;
    Button total;
    int count=0;int trip_logic;
    int count_ontrip=0;int count_notontrip=0;int logic_change;
    int totalcount_ontrip=0;int totalcount_notontrip=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         query=(EditText)findViewById(R.id.query);
         total=(Button) findViewById(R.id.total);
        mapping=(Button) findViewById(R.id.mapping);
         findanswer=(Button)findViewById(R.id.findanswer);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    //t1.setPitch(.7f);
                    //t1.setSpeechRate(.6f);
                    t1.speak("Welcome to smovebot", TextToSpeech.QUEUE_FLUSH, null);       //initialization of android tts
                }
            }
        });


total.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        new total_status().execute();
    }
});


findanswer.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        if(query.getText().toString().equals(""))
        {

            Toast.makeText(MainActivity.this, "Please type in query", Toast.LENGTH_LONG).show();
        }
        else
        {new carcount_status().execute();}

    }
});

mapping.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        new getmap().execute();
    }
});





    }









    private class carcount_status extends AsyncTask<Void, Void, Void> {

        double distance;
        float[] result = new float[1];
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());  //to convert coordinates to location address and vice versa


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
// Making a request to url and getting response


            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
// Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

// looping through All Details
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);


                        String latitude = c.getString("latitude");
                        String longitude = c.getString("longitude");
                        String trip_status=c.getString("is_on_trip");




                        try {
                            List<Address> list2 = geocoder.getFromLocationName(query.getText().toString(), 1);
                           Address add2 = list2.get(0);


                            Location.distanceBetween(Double.parseDouble(latitude),Double.parseDouble(longitude),
                                    add2.getLatitude(), add2.getLongitude(), result);


                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

              distance=result[0];

                   if (distance<5000)            //finding points that are within 5km radius
                   {count=count+1;

                   if (trip_status.equals("true"))
                       {count_ontrip=count_ontrip+1;}

                       else
                       {count_notontrip=count_notontrip+1;}

                   }


                    }

                    t1.speak("Number of cars around the area is "+count+",Number of cars on trip is "+count_ontrip+",Number of cars at rest is "+count_notontrip, TextToSpeech.QUEUE_FLUSH, null);

                    count=0;count_ontrip=0;count_notontrip=0;

                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;


        }







    }

    private class total_status extends AsyncTask<Void, Void, Void> {





        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
// Making a request to url and getting response


            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
// Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

// looping through All Details
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);


                        //String latitude = c.getString("latitude");
                        //String longitude = c.getString("longitude");
                        String trip_status=c.getString("is_on_trip");




                        if (trip_status.equals("true"))
                            {totalcount_ontrip=totalcount_ontrip+1;}

                            else
                            {totalcount_notontrip=totalcount_notontrip+1;}






                    }

                    t1.speak("Total number of cars on trip "+totalcount_ontrip+",Total number of cars at rest "+totalcount_notontrip, TextToSpeech.QUEUE_FLUSH, null);

                    totalcount_ontrip=0;totalcount_notontrip=0;

                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;


        }







    }



    private class getmap extends AsyncTask<Void, Void, Void>  {





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
// Making a request to url and getting response


            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
// Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

// looping through All Details
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject c = data.getJSONObject(i);


                        String latitude = c.getString("latitude");
                        String longitude = c.getString("longitude");
                        String trip_status=c.getString("is_on_trip");




                        if (trip_status.equals("true"))
                        {latitude_ontrip.add(latitude);
                        longitude_ontrip.add(longitude);}


                        else
                        {latitude_notontrip.add(latitude);
                            longitude_notontrip.add(longitude);}






                    }



                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;


        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
// Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Intent intent=new Intent(MainActivity.this,maps.class);
           Bundle bundle1=new Bundle();

          bundle1.putSerializable("key1",latitude_ontrip);
            bundle1.putSerializable("key2",longitude_ontrip);
            bundle1.putSerializable("key3",latitude_notontrip);
            bundle1.putSerializable("key4",longitude_notontrip);                  //serializable and bundle used to pass Arraylist to another Activity,map Activity

          intent.putExtras(bundle1);

          startActivity(intent);

            latitude_ontrip= new ArrayList();
            longitude_ontrip=new ArrayList();
            latitude_notontrip= new ArrayList();
            longitude_notontrip=new ArrayList();






        }




    }






}
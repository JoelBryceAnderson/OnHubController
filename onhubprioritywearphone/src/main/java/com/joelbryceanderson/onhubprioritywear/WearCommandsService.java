package com.joelbryceanderson.onhubprioritywear;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JAnderson on 5/16/16.
 */
public class WearCommandsService extends WearableListenerService {

    private GoogleApiClient mGoogleApiClient;
    private String API_KEY = "YOUR_API_KEY_GOES_HERE";
    private String triggerPhrase = "https://maker.ifttt.com/trigger/";
    private String postTriggerPhrase = "/with/key/";

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                    }
                })
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().endsWith("on")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String eventName = new String(messageEvent.getData());
            String fullCommand = triggerPhrase + eventName + postTriggerPhrase + API_KEY;
            System.out.println(fullCommand);
            StringRequest postRequest = new StringRequest(Request.Method.POST, fullCommand,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("name", "Alif");
                    params.put("domain", "http://itsalif.info");

                    return params;
                }
            };
            queue.add(postRequest);
        }
    }
}

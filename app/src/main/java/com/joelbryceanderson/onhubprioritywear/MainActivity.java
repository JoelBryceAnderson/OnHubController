package com.joelbryceanderson.onhubprioritywear;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements
        MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks {

    private RecyclerView wearRecycler;
    private List<ListItem> list;
    private Node peerNode;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup Layouts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect Google API client to send commands to phone
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //Recycler view setup
                wearRecycler = (RecyclerView) stub.findViewById(R.id.main_recycler);
                wearRecycler.setHasFixedSize(true);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                wearRecycler.setLayoutManager(manager);

                //Add header and footer, as well as any and all devices, to list
                list = new ArrayList<>();
                list.add(new ListItem("header", null, "header"));

                list.add(new ListItem("Nexus 6P", getDrawable(R.drawable.ic_phone),
                        "priority_nexus"));
                list.add(new ListItem("Macbook", getDrawable(R.drawable.ic_laptop),
                        "priority_macbook"));
                list.add(new ListItem("Nexus Player",
                        getDrawable(R.drawable.ic_television), "priority_nexus_player"));
                list.add(new ListItem("Xbox One",
                        getDrawable(R.drawable.ic_videogame), "priority_xbox"));

                list.add(new ListItem("footer", null, "footer"));

                ListAdapter adapter =
                        new ListAdapter(list, MainActivity.this);
                wearRecycler.setAdapter(adapter);

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mGoogleApiClient, this).setResultCallback(resultCallback);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
    }

    public void turnPriorityOn(int position) {
        String togglePath = "/priority/item/" + "on";

        PendingResult<MessageApi.SendMessageResult> result = Wearable.MessageApi.sendMessage(
                mGoogleApiClient,
                peerNode.getId(),
                togglePath,
                list.get(position).getCommand().getBytes()
        );
    }

    private ResultCallback<Status> resultCallback =  new ResultCallback<Status>() {
        @Override
        public void onResult(Status status) {
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    sendStartMessage();
                    return null;
                }
            }.execute();
        }
    };

    private void sendStartMessage(){

        NodeApi.GetConnectedNodesResult rawNodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (final Node node : rawNodes.getNodes()) {
            PendingResult<MessageApi.SendMessageResult> result = Wearable.MessageApi.sendMessage(
                    mGoogleApiClient,
                    node.getId(),
                    "/start",
                    null
            );

            result.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                @Override
                public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                    peerNode = node;
                }
            });
        }
    }
}

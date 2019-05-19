package chatbot.ali.com.chatbotproje;

import android.annotation.SuppressLint;
import android.app.Person;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MesajlasmaEkrani extends AppCompatActivity {

    private String token, url;
    private Room room;
    private AppCompatImageButton gonder;
    private EditText mesaj;
    private android.support.v7.widget.SearchView arama;

    private String userAdi;

    MessagesRecyclerViewAdapter messagesRecyclerViewAdapter;
    RecyclerView messagesRecyclerView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesajlasma_ekrani);

        Intent i = getIntent();
        token = i.getStringExtra("token");
        url = i.getStringExtra("url");

        gonder = (AppCompatImageButton) findViewById(R.id.gonder);
        mesaj = (EditText) findViewById(R.id.mesaj);
        arama = (android.support.v7.widget.SearchView) findViewById(R.id.arama);

        arama.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                sohbetAra(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soru = mesaj.getText().toString();
                room.addMessage(new Message(soru,  userAdi, false));

                mesaj.setText("");
//                listViewAdapter.notifyDataSetChanged();
                messagesRecyclerViewAdapter.notifyDataSetChanged();
//                messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(room.getMessages(), getBaseContext());
//                messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);

                mesajGonder(soru);
            }
        });

        room = new Room();
        mesajlariGetir();

        messagesRecyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
        messagesRecyclerView.setHasFixedSize(true);

        LinearLayoutManager messagesLayoutManager = new LinearLayoutManager(this);
        messagesRecyclerView.setLayoutManager(messagesLayoutManager);

        messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(room.getMessages(), getApplicationContext());
        messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);


    }


    private void mesajGonder(String soru) {
        String girisUrl = this.url + "/api/sohbetEt";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setResponseTimeout(10000);

        RequestParams postParams = new RequestParams();
        postParams.put("token", this.token);
        postParams.put("soru", soru);

        client.post(girisUrl, postParams, new JsonHttpResponseHandler() {


            public void  onSuccess(int statusCode, Header[] headers, JSONObject json) {
                String message = null;
                try {
                    message = json.getString("message");

                    if ( message.equals("tokenGecersiz")){
                        Toast.makeText(MesajlasmaEkrani.this, "Oturum geçersiz.", Toast.LENGTH_LONG).show();
                    }
                    else if ( message.equals("basarili")) {
                        String cevap = json.getString("cevap");
                        room.addMessage(new Message(cevap,  "chatbot", true));
//                        listViewAdapter.notifyDataSetChanged();
                        messagesRecyclerViewAdapter.notifyDataSetChanged();
//                        messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(room.getMessages(), getApplicationContext());
//                        messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject e) {
                t.printStackTrace();
            }
        });
    }

    private void mesajlariGetir() {
        String girisUrl = this.url + "/api/mesajlariGetir";
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams postParams = new RequestParams();
        postParams.put("token", this.token);

        client.post(girisUrl, postParams, new JsonHttpResponseHandler() {


            public void  onSuccess(int statusCode, Header[] headers, JSONObject json) {
                String message = null;
                try {
                    message = json.getString("message");

                    if ( message.equals("tokenGecersiz")){
                        Toast.makeText(MesajlasmaEkrani.this, "Oturum geçersiz.", Toast.LENGTH_LONG).show();
                    }
                    else if ( message.equals("basarili")) {
                        userAdi = json.getString("userAdi");
                        for (int i = 0; i < json.length()-2; i++) {
                            String adi = json.getJSONObject(String.valueOf(i)).getString("adi");
                            String mess = json.getJSONObject(String.valueOf(i)).getString("mesaj");
                            Boolean isChatbot = json.getJSONObject(String.valueOf(i)).getBoolean("isChatbot");

                            room.addMessage(new Message(mess,  adi, isChatbot));

                        }
//                        listViewAdapter.notifyDataSetChanged();
                        messagesRecyclerViewAdapter.notifyDataSetChanged();
//                        messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(room.getMessages(), getApplicationContext());
//                        messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject e) {
                t.printStackTrace();
            }
        });
    }

    private void sohbetAra(String ara) {
        String girisUrl = this.url + "/api/sohbetAra";
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams postParams = new RequestParams();
        postParams.put("token", this.token);
        postParams.put("ara", ara);

        client.post(girisUrl, postParams, new JsonHttpResponseHandler() {


            public void  onSuccess(int statusCode, Header[] headers, JSONObject json) {
                String message = null;
                try {
                    message = json.getString("message");

                    if ( message.equals("basarisiz")){
                        Toast.makeText(MesajlasmaEkrani.this, "Bulunamadı.", Toast.LENGTH_LONG).show();

                        room = new Room();
                        messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(room.getMessages(), getApplicationContext());
                        messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);
                    }
                    else if ( message.equals("basarili")) {
                        room = new Room();
                        for (int i = 0; i < json.length()-1; i++) {
                            String adi = json.getJSONObject(String.valueOf(i)).getString("adi");
                            String mess = json.getJSONObject(String.valueOf(i)).getString("mesaj");
                            Boolean isChatbot = json.getJSONObject(String.valueOf(i)).getBoolean("isChatbot");
                            room.addMessage(new Message(mess,  adi, isChatbot));
                        }
                        messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(room.getMessages(), getApplicationContext());
                        messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);
//                        listViewAdapter.notifyDataSetChanged();
                        messagesRecyclerViewAdapter.notifyDataSetChanged();
//                        messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(room.getMessages(), getApplicationContext());
//                        messagesRecyclerView.setAdapter(messagesRecyclerViewAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject e) {
                t.printStackTrace();
            }
        });
    }


}

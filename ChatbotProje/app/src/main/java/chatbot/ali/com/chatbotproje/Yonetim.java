package chatbot.ali.com.chatbotproje;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class Yonetim extends AppCompatActivity {

    public String token, url;

    ArrayList<HashMap<String,String>> kullanicilar;
    ListView listView;
    KullaniciListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yonetim);
        initialize();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        kullanicilariGetir();

    }

    private void initialize() {
        Intent i = getIntent();
        token = i.getStringExtra("token");
        url = i.getStringExtra("url");

        Button ekle = findViewById(R.id.kullanıcıEkle);
        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Yonetim.this, KullnaniciEkle.class);
                i.putExtra("token", token);
                i.putExtra("url", url);
                startActivity(i);
            }
        });

    }

    private void kullanicilariGetir() {
        kullanicilar = new ArrayList<HashMap<String,String>>();

        String girisUrl = this.url + "/api/kullanicilariGetir";
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams postParams = new RequestParams();
        postParams.put("token", this.token);

        client.post(girisUrl, postParams, new JsonHttpResponseHandler() {


            public void  onSuccess(int statusCode, Header[] headers, JSONObject json) {
                String message = null;
                try {
                    message = json.getString("message");
                    if ( message.equals("tokenGecersiz")){
                        Toast.makeText(Yonetim.this, "Oturum geçersiz.", Toast.LENGTH_LONG).show();
                    }
                    else if ( message.equals("basarili")) {
                        HashMap<String, String> item;
                        for (int i = 0; i < json.length()-1; i++) {
                            item = new HashMap<String, String>();
                            item.put( "adi", json.getJSONObject(String.valueOf(i)).getString("adi") );
                            item.put( "soyadi", json.getJSONObject(String.valueOf(i)).getString("soyadi") );
                            item.put( "kullaniciAdi", json.getJSONObject(String.valueOf(i)).getString("kullaniciAdi") );
                            if (json.getJSONObject(String.valueOf(i)).getBoolean("yoneticiMi"))
                                item.put( "yoneticiMi", "Evet" );
                            else
                                item.put( "yoneticiMi", "Hayır" );
                            kullanicilar.add( item );
                        }
                        listView = (ListView) findViewById(R.id.message_list_view);
                        SimpleAdapter sa = new SimpleAdapter(Yonetim.this, kullanicilar,
                                R.layout.kullanici_view,
                                new String[]{"kullaniciAdi", "adi", "soyadi", "yoneticiMi"},
                                new int[]{R.id.kullaniciAdi, R.id.adi, R.id.soyadi, R.id.yoneticiMi});
                        listView.setAdapter(sa);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                                uyariGoster("Silme İşlemi", item.get("kullaniciAdi"));
                            }
                        });

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

    private void kullaniciSil(String kullaniciAdi) {
        String girisUrl = "http://192.168.12.1:8080/api/kullaniciSil";
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams postParams = new RequestParams();
        postParams.put("token", this.token);
        postParams.put("kullaniciAdi", kullaniciAdi);

        client.post(girisUrl, postParams, new JsonHttpResponseHandler() {


            public void  onSuccess(int statusCode, Header[] headers, JSONObject json) {
                String message = null;
                try {
                    message = json.getString("message");

                    if ( message.equals("tokenGecersiz")){
                        Toast.makeText(Yonetim.this, "Oturum geçersiz.", Toast.LENGTH_LONG).show();
                    }
                    else if ( message.equals("basarili")) {
                        Toast.makeText(Yonetim.this, "Kullanıcı silindi.", Toast.LENGTH_LONG).show();
                        kullanicilariGetir();
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

    private void uyariGoster (String baslik, final String kullaniciAdi){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Yonetim.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Yonetim.this);
        }
        builder.setTitle(baslik);
        builder.setMessage(kullaniciAdi + " adlı kayıt silinsin mi?");
        builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                kullaniciSil(kullaniciAdi);
            }
        }); builder.setNegativeButton("Silme", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();

    }


}

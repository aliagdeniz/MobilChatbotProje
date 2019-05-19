package chatbot.ali.com.chatbotproje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class KullnaniciEkle extends AppCompatActivity {

    public String token, url;
    public EditText adi, soyadi, kullaniciAdi, parola;
    Button ekle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullnanici_ekle);
        initialize();
    }

    private void initialize() {
        Intent i = getIntent();
        token = i.getStringExtra("token");
        url = i.getStringExtra("url");

        adi = findViewById(R.id.adi);
        soyadi = findViewById(R.id.soyadi);
        kullaniciAdi = findViewById(R.id.kullaniciAdi);
        parola = findViewById(R.id.parola);
        ekle = findViewById(R.id.ekle);

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( adi.getText().toString().equals("") ||
                        soyadi.getText().toString().equals("") ||
                        kullaniciAdi.getText().toString().equals("") ||
                        parola.getText().toString().equals("") ){
                    Toast.makeText(KullnaniciEkle.this, "Girilmeyen alanlar bulunmaktadır.", Toast.LENGTH_LONG).show();
                } else {
                    kullaniciEkle(token, adi.getText().toString(), soyadi.getText().toString(), kullaniciAdi.getText().toString(), parola.getText().toString() );
                }
            }
        });
    }
    private void kullaniciEkle(String token, String adi, String soyadi, String kullaniciAdi, String parola ) {
        String girisUrl = "http://192.168.12.1:8080/api/kullaniciEkle";
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams postParams = new RequestParams();
        postParams.put("token", token);
        postParams.put("kullaniciAdi", kullaniciAdi);
        postParams.put("adi", adi);
        postParams.put("soyadi", soyadi);
        postParams.put("parola", parola);

        client.post(girisUrl, postParams, new JsonHttpResponseHandler() {


            public void  onSuccess(int statusCode, Header[] headers, JSONObject json) {
                String message = null;
                try {
                    message = json.getString("message");

                    if ( message.equals("tokenGecersiz")){
                        Toast.makeText(KullnaniciEkle.this, "Oturum geçersiz.", Toast.LENGTH_LONG).show();
                    }
                    else if ( message.equals("basarili")) {
                        Toast.makeText(KullnaniciEkle.this, "Kullanıcı eklendi.", Toast.LENGTH_LONG).show();
                        edittextSifirla();
                    }
                    else if ( message.equals("kullaniciVar")) {
                        Toast.makeText(KullnaniciEkle.this, "Farklı bir kullanıcı adında ile ekleyin.", Toast.LENGTH_LONG).show();
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

    private void edittextSifirla() {
        this.adi.setText("");
        this.soyadi.setText("");
        this.parola.setText("");
        this.kullaniciAdi.setText("");
    }
}

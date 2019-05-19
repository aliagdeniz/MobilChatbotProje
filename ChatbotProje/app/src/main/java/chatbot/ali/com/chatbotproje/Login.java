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

public class Login extends AppCompatActivity {

    EditText kullanici, parola;
    Button giris;
    String url = "http://192.168.12.1:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewleriOlustur();
        viewOlaylariEkle();


    }

    private void viewOlaylariEkle() {
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap(kullanici.getText().toString(),
                        parola.getText().toString());
            }
        });
    }

    private void viewleriOlustur() {
        kullanici = findViewById(R.id.kullanici);
        parola = findViewById(R.id.parola);
        giris = findViewById(R.id.giris);
        kullanici.setText("123456");
        parola.setText("654321");
//        kullanici.setText("123");
//        parola.setText("321");
    }

    public void girisYap(String kadi, String parola) {
        String girisUrl = this.url + "/api/login";
        AsyncHttpClient client = new AsyncHttpClient();
//            client.setResponseTimeout(timeOutVal);

        RequestParams postParams = new RequestParams();
        postParams.put("kullaniciAdi", kadi);
        postParams.put("parola", parola);

        client.post(girisUrl, postParams, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                String message = null;
                try {
                    message = json.getString("message");

                    if ( message.equals("kullaniciYok")){
                        Toast.makeText(Login.this, "Kullanıcı bulunamadı.", Toast.LENGTH_LONG).show();
                    }
                    else if ( message.equals("parolaYanlis")) {
                        Toast.makeText(Login.this, "Parola Yanlış.", Toast.LENGTH_LONG).show();
                    }
                    else if ( message.equals("basarili")) {
                        String token = json.getString("token");
                        String yoneticiMi = json.getString("yoneticiMi");

                        if (yoneticiMi.equals("True")){
                            Intent i = new Intent(Login.this, Yonetim.class);
                            i.putExtra("token", token);
                            i.putExtra("url", url);
                            startActivity(i);
                        }else {
                            Intent i = new Intent(Login.this, MesajlasmaEkrani.class);
                            i.putExtra("token", token);
                            i.putExtra("url", url);
                            startActivity(i);
                        }
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

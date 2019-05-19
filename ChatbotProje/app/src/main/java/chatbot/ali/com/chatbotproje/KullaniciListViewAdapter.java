package chatbot.ali.com.chatbotproje;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class KullaniciListViewAdapter extends ArrayAdapter<Kullanici> {

    private final LayoutInflater inflater;
    private final Context context;
    private ViewHolder holder;
    private final ArrayList<Kullanici> kullanicilar;

    public KullaniciListViewAdapter(Context context, ArrayList<Kullanici> kullanicilar) {
        super(context,0, kullanicilar);
        this.context = context;
        this.kullanicilar = kullanicilar;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return kullanicilar.size();
    }

    @Override
    public Kullanici getItem(int position) {
        return kullanicilar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return kullanicilar.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.kullanici_view, null);

            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.getRootView();
            holder.kullaniciAdi = (TextView) convertView.findViewById(R.id.kullaniciAdi);
            holder.adi = (TextView) convertView.findViewById(R.id.adi);
            holder.soyadi = (TextView) convertView.findViewById(R.id.soyadi);
            holder.yoneticiMi = (TextView) convertView.findViewById(R.id.yoneticiMi);

            convertView.setTag(holder);

        }
        else{
            //Get viewholder we already created
            holder = (ViewHolder)convertView.getTag();
        }

        final Kullanici message = kullanicilar.get(position);
        if(message != null){
            holder.kullaniciAdi.setText(message.kullaniciAdi);
            holder.adi.setText(message.kullaniciAdi);
            holder.soyadi.setText(message.kullaniciAdi);
            holder.yoneticiMi.setText(message.yoneticiMi);

//            holder.linearLayout.setOnClickListener(Yonetim);

//            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    uyariGoster("Silme İşlemi", message.kullaniciAdi, v.getContext());
//                }
//
//                private void uyariGoster (String baslik, final String kullaniciAdi, Context context){
//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(context);
//                    }
//                    builder.setTitle(baslik);
//                    builder.setMessage(kullaniciAdi + " adlı kayıt silinsin mi?");
//                    builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            kullaniciSil(kullaniciAdi);
//                        }
//                    }); builder.setNegativeButton("Silme", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//                    builder.setIcon(android.R.drawable.ic_dialog_alert);
//                    builder.show();
//
//                }
//            });
        }
        return convertView;
    }




    //View Holder Pattern for better performance
    private static class ViewHolder {
        public LinearLayout linearLayout;
        public TextView kullaniciAdi;
        public TextView adi;
        public TextView soyadi;
        public TextView yoneticiMi;
    }
}

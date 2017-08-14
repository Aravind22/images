package com.imgsearch.app.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText ed1;
    private ImageView imgv;
    private ImageButton btn;
    private Button bt1,bt2;
    private ArrayList<String> resu;
    private ArrayList<String> title;
    private String link;
    private int i =0;
    final Context mcon = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1 = (EditText)findViewById(R.id.editText);
        btn = (ImageButton)findViewById(R.id.imageButton);
        bt1 = (Button)findViewById(R.id.button);
        bt2 = (Button)findViewById(R.id.button2);
        imgv = (ImageView)findViewById(R.id.imageView);
        resu = new ArrayList<>();
        title = new ArrayList<>();


    }

    public void search(View view){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

      runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resu.clear();
                String key = "AIzaSyAZ79CAXIvj9A_Na6hBJRXV6n324Gd3gLk";
                String cx = "011551326954752579110:be-l5di4zbu";
                String qry = ed1.getText().toString();
                try{
                    URL url = new URL(
                            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx="+cx+"&q="+ qry + "&searchType=image&fileType=jpg&imgSize=small&alt=json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String output;
                    while ((output = br.readLine()) != null) {

                        if(output.contains("\"link\": \"")){
                            link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                            resu.add(link+"\n\n");

                        }

                    }

                    conn.disconnect();
                    try{
                        URL url1 = new URL(resu.get(i));
                        Bitmap bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                        imgv.setImageBitmap(bmp);

                    }catch (Exception e){

                    }
                }catch(Exception e){
                    if(e.toString().contains("associated")){
                        Toast.makeText(mcon, "No Internet Connection Found!", Toast.LENGTH_SHORT).show();
                    }
                    else{Toast.makeText(mcon, ""+e.toString(), Toast.LENGTH_SHORT).show();}

                }
            }
        });

    }
    public void next(View view){
        i++;
       if(i>9){
           i=0;
       }
       else{
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   try{
                       URL url1 = new URL(resu.get(i));

                       Bitmap bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                       imgv.setImageBitmap(bmp);
                   }catch (Exception e){
                       Toast.makeText(mcon, "*Error*"+"\n"+"Try Again", Toast.LENGTH_SHORT).show();
                   }

               }
           });

       }





    }
    public void back(View view){

        i--;
        if(i<0){
            Toast.makeText(mcon, "End of Page!", Toast.LENGTH_SHORT).show();
        }
        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url1 = new URL(resu.get(i));

                        Bitmap bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                        imgv.setImageBitmap(bmp);
                    }catch (Exception e){
                        Toast.makeText(mcon, "*Error*", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }




    }
}

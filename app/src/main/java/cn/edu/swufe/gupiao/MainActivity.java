package cn.edu.swufe.gupiao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    public class MainActivity extends AppCompatActivity {
    private final String TAG = "Rate";
    Handler handler;
    //private ArrayAdapter<HashMap<String,String,String>;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
        public void onClick(View btn) {
            if(btn.getId()==R.id.hotsong){
                Intent list= new Intent(this,hotsong.class);
                startActivity(list);
            }else if(btn.getId()==R.id.netsong){
                Intent list= new Intent(this,netsong.class);
                startActivity(list);
            }else if(btn.getId()==R.id.oldsong){
                Intent list= new Intent(this,oldsong.class);
                startActivity(list);
            }else{
                Intent list= new Intent(this,musicplayer.class);
                startActivity(list);
            }
        }
        @Override

        public boolean onCreateOptionsMenu(Menu menu) {
            //getMenuInflater()获得MenuIflater对象，inflate()方法给当前活动创建菜单
            getMenuInflater().inflate(R.menu.musicmenu,menu);
            //true 代表允许创建的菜单显示出来
            return  true;
        }

        public boolean onOptionsItemSelected(MenuItem item){

            switch (item.getItemId()) {

                case R.id.hot:
                    Intent list= new Intent(this,hotsong.class);
                    startActivity(list);
                    break;

                case R.id.net:
                    Intent list1= new Intent(this,netsong.class);
                    startActivity(list1);
                    break;

                case R.id.old:
                    Intent list2= new Intent(this,oldsong.class);
                    startActivity(list2);
                    break;

                case R.id.exit:
                    Intent list3= new Intent(this,denglu.class);
                    startActivity(list3);
                    break;

                case R.id.my:
                    Intent list4= new Intent(this,musicplayer.class);
                    startActivity(list4);
                    break;

                default:

            }

            return true;

        }
    }


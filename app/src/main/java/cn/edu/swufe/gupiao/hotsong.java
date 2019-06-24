package cn.edu.swufe.gupiao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
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

public class hotsong extends ListActivity implements Runnable,AdapterView.OnItemClickListener{
    private final String TAG = "Rate";
    Handler handler;
    List<String> listsong = new ArrayList<String>();
    List<String> listurl = new ArrayList<String>();
    private SimpleAdapter listItemAdapter;


    //private ArrayAdapter<HashMap<String,String,String>;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread t = new Thread(this);
        t.start();
        //dataList = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("arrayList");
        getListView().setOnItemClickListener(this);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
//                   ArrayList<HashMap<String, String>> retList = (ArrayList<HashMap<String, String>>) msg.obj;
//                    MyAdapter myAdapter = new MyAdapter(netsong.this, R.layout.activity_rate_list, retList);
//                    ListView listView = (ListView) findViewById(R.id.list_test1);
//                    listView.setAdapter(myAdapter);
//                    for (int i=0;i<retList.size();i++){
//                    Log.i("handler", "reset list..." + myAdapter.getItem(i));}
//                    myAdapter.notifyDataSetChanged();
                    ArrayList<HashMap<String, String>> searchResults = (ArrayList<HashMap<String, String>>) msg.obj;
                    for (int i = 0; i < searchResults.size(); i++) {
                        HashMap<String, String> map1 = searchResults.get(i);
                        String url = map1.get("url").toString();
                        String song = map1.get("item").toString();
                        listsong.add(song);
                        listurl.add(url);
                    }
                    ListAdapter adapter = new ArrayAdapter<String>(hotsong.this,android.R.layout.simple_list_item_1,listsong);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };


    }

    public void run() {
        /*ArrayList<HashMap<String, String>> searchResults = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> searchResults2 = new ArrayList<HashMap<String, String>>();
        ArrayList<SearchResult> searchResults1 = new ArrayList<SearchResult>();
        Bundle bundle = new Bundle();
        HashMap<String, String> map = new HashMap<String, String>();*/
        Document doc = null;
        ArrayList<Map<String, Object>> searchResults2 = new ArrayList<Map<String, Object>>();
        ArrayList<Map<String, Object>> searchResults3 = new ArrayList<Map<String, Object>>();
        ArrayList<HashMap<String, String>> searchResults = new ArrayList<HashMap<String, String>>();
        ArrayList<netsong.SearchResult> searchResults1 = new ArrayList<netsong.SearchResult>();
        List<String> list = new ArrayList<String>();

        ArrayList<Map<String, Object>> listurl = new ArrayList<Map<String, Object>>();
        try {
            doc = Jsoup.connect("http://music.taihe.com/top/dayhot").get();
            //doc = Jsoup.parse(html);
            Log.i(TAG, "run:" + doc.title());
            Elements songItem = doc.select("span.song-title");
            Elements artists = doc.select("span.author_list");
            for (Element item : artists) {
                Element a = item.selectFirst("span.author_list").getElementsByTag("a").first();

                Element singer = item.selectFirst("span.author_list");
                String artist = singer.attr("title");

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("ItemDetail", artist);
                searchResults2.add(map);
            }
            for (Element item : songItem) {
                Element a = item.selectFirst("span.song-title").getElementsByTag("a").first();
                String url = a.attr("href");
                String name = a.text();

                SearchResult searchResult1 = new SearchResult();
                SearchResult searchResult2 = new SearchResult();
                Map<String, Object> map = new HashMap<String, Object>();
                Map<String, Object> map1 = new HashMap<String, Object>();
                map.put("ItemTitle", name);
                map1.put("url",url);
                searchResult2.setUrl(url);
                //searchResults1.add(searchResult2);
                searchResults3.add(map);
                listurl.add(map1);
            }
            for (int i = 0; i < searchResults2.size(); i++) {
                ;
                Map<String, Object> map = searchResults2.get(i);
                Map<String, Object> map1 = listurl.get(i);
                Map<String, Object> map2 = searchResults3.get(i);
                String url = map1.get("url").toString();
                String song = map2.get("ItemTitle").toString();
                String singer = map.get("ItemDetail").toString();

                HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("item",singer+"   "+song);
                map3.put("url",url);
                //map1.put("ItemTitle", song);
                //map1.put("ItemDetail", singer);

                searchResults.add(map3);
                list.add(singer+"   "+song);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = searchResults;
        handler.sendMessage(msg);
        Log.i("handler", "reset list...");
    }


    public class SearchResult implements Serializable {
        private static final long serialVersionUID = 0X00000001l;
        private String musicName;
        private String url;
        private String artist;
        private String album;

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getMusicName() {
            return musicName;
        }

        public void setMusicName(String musicName) {
            this.musicName = musicName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        String url =  listurl.get(position);
        //打开新的页面
        Uri uri = Uri.parse("http://music.taihe.com"+url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater()获得MenuIflater对象，inflate()方法给当前活动创建菜单
        getMenuInflater().inflate(R.menu.musicnet,menu);
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

            case R.id.my:
                Intent list4= new Intent(this,musicplayer.class);
                startActivity(list4);
                break;

            case R.id.exit:
                Intent list3= new Intent(this,denglu.class);
                startActivity(list3);
                break;

            default:

        }

        return true;

    }

}
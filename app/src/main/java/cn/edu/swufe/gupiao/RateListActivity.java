package cn.edu.swufe.gupiao;

import android.app.ListActivity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    private String[] list_data = {"one","tow","three","four"};
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        Thread t = new Thread(this);
        t.start();;

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 5){
                    List<String> retList = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,retList);
                    setListAdapter(adapter);
                    Log.i("handler","reset list...");
                }
                super.handleMessage(msg);
            }
        };
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




    @Override
    public void run() {
        Log.i("thread","run.....");
        List<String> rateList = new ArrayList<String>();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://music.taihe.com/top/new/?pst=shouyeTop").get();
            //doc = Jsoup.parse(html);
            Elements songTitles = doc.select("span.song-title");
            Elements artists = doc.select("span.author_list");
            ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>();

            for (int i = 0; i < songTitles.size(); i++) {
                SearchResult searchResult = new SearchResult();
                Elements urls = songTitles.get(i).getElementsByTag("a");
                searchResult.setUrl(urls.get(0).attr("href"));
                searchResult.setMusicName(urls.get(0).text());

                Elements artistElements = artists.get(i).getElementsByTag("a");
                searchResult.setArtist(artistElements.get(0).text());
                searchResult.setAlbum("最新推荐");
                searchResults.add(searchResult);
            }
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        Message msg1 = handler.obtainMessage();
        msg1.what = 4;
        msg1.obj  = bundle;
        handler.sendMessage(msg1);
    }
}

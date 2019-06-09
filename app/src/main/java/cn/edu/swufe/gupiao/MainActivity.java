package cn.edu.swufe.gupiao;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements Runnable{
    private final String TAG = "Rate";
    Handler handler;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread(this);
        t.start();;


    }

    public void run() {

        Bundle bundle = new Bundle();
        getresult(bundle);
        Message msg1 = handler.obtainMessage();
        msg1.what = 4;
        msg1.obj  = bundle;
        handler.sendMessage(msg1);
    }

    private void getresult(Bundle bundle) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://music.taihe.com/top/new/?pst=shouyeTop").get();
            //doc = Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
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


        } catch (IOException e) {
            e.printStackTrace();
        }
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
}

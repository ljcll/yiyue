package cn.edu.swufe.gupiao;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.os.Handler;
import android.widget.TextView;

import java.io.File;

public class musicplayer extends AppCompatActivity implements Runnable{
    int flag = 1;//设置一个标志，供点击“开始/暂停”按钮使用
    private Button btnStart, btnStop, btnNext, btnLast;
    private TextView txtInfo;
    private ListView listView;
    private SeekBar seekBar;
    private MusicService musicService = new MusicService();
    private Handler handler;// 处理改变进度条事件
    int UPDATE = 0x101;
    private boolean autoChange, manulChange;// 判断是进度条是自动改变还是手动改变
    private boolean isPause;// 判断是从暂停中恢复还是重新播放



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        try {  setListViewAdapter();//添加文件名字
            } catch (Exception e) {
            Log.i("TAG", "读取信息失败");
        }

        btnStart = (Button) findViewById(R.id.btn_star);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /**                     * 引入flag作为标志，当flag为1 的时候，此时player内没有东西，所以执行musicService.play()函数
                     *  * 进行第一次播放，然后flag自增二不再进行第一次播放
                     *  * 当再次点击“开始/暂停”按钮次数即大于1 将执行暂停或继续播放goplay()函数
                     *  */
                    if (flag == 1) {
                        musicService.play();
                        flag++;
                    } else {
                        if (!musicService.player.isPlaying()) {
                            musicService.goPlay();
                        } else if (musicService.player.isPlaying()) {
                            musicService.pause();
                        }
                    }
                } catch (Exception e) {
                    Log.i("LAT", "开始异常！");
                }
            }
        });

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    musicService.stop();
                    flag = 1;//当点击停止按钮时，flag置为1
                    seekBar.setProgress(0);
                    txtInfo.setText("播放已经停止");
                } catch (Exception e) {
                    Log.i("LAT", "停止异常！");
                }
            }
        });

        btnLast = (Button) findViewById(R.id.btn_last);
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    musicService.last();
                } catch (Exception e) {
                    Log.i("LAT", "上一曲异常！");
                }
            }
        });

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    musicService.next();
                } catch (Exception e) {
                    Log.i("LAT", "下一曲异常！");
                }
            }
        });
        seekBar = (SeekBar) findViewById(R.id.sb);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {//用于监听SeekBar进度值的改变//
                }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}//用于监听SeekBar开始拖动             }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//用于监听SeekBar停止拖动  SeekBar停止拖动后的事件
                int progress = seekBar.getProgress();
                Log.i("TAG:", "" + progress + "");
                int musicMax = musicService.player.getDuration(); //得到该首歌曲最长秒数
                int seekBarMax = seekBar.getMax();
                musicService.player.seekTo(musicMax * progress / seekBarMax);//跳到该曲该秒
                autoChange = true;
                manulChange = false;
            }
        });
        Thread t = new Thread(this);// 自动改变进度条的线程        //实例化一个handler对象
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);//更新UI
                    int mMax = musicService.player.getDuration();//最大秒数
                    if (msg.what == UPDATE) {
                        try {
                            seekBar.setProgress(msg.arg1);
                            txtInfo.setText(setPlayInfo(msg.arg2 / 1000, mMax / 1000));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        seekBar.setProgress(0);
                        txtInfo.setText("播放已经停止");
                    }
                }
            };
            t.start();


    }

    private String setPlayInfo (int position, int max){
        String info = "正在播放:  " + musicService.songName + "\t\t";int pMinutes = 0; while (position >= 60) {
            pMinutes++;
            position -= 60;
        }
        String now = (pMinutes < 10 ? "0" + pMinutes : pMinutes) + ":"+ (position < 10 ? "0" + position : position);
        int mMinutes = 0;
        while (max >= 60) {
            mMinutes++;
            max -= 60;        }
        String all = (mMinutes < 10 ? "0" + mMinutes : mMinutes) + ":"+ (max < 10 ? "0" + max : max);
        return info + now + " / " + all;
    }

    private void setListViewAdapter() {
        String[] str = new String[musicService.musicList.size()];
        int i = 0;
        for (String path : musicService.musicList) {
            File file = new File(path);
            str[i++] = file.getName();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,str);
        listView = (ListView) findViewById(R.id.lv1);
        listView.setAdapter(adapter);    }

    @Override
    public void run() {
        int position, mMax, sMax;
        while (!Thread.currentThread().isInterrupted()) {
            if (musicService.player != null && musicService.player.isPlaying()) {
                position = musicService.getCurrentProgress();//得到当前歌曲播放进度(秒)
                mMax = musicService.player.getDuration();//最大秒数
                sMax = seekBar.getMax();//seekBar最大值，算百分比
                Message m = handler.obtainMessage();//获取一个Message
                 m.arg1 = position * sMax / mMax;//seekBar进度条的百分比
                 m.arg2 = position;
                 m.what = UPDATE;
                 handler.sendMessage(m);
                 //handler.sendEmptyMessage(UPDATE);
                 try {
                     Thread.sleep(1000);// 每间隔1秒发送一次更新消息
                         } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
            }
        }
    }

}

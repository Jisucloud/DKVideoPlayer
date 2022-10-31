package xyz.doikki.dkplayer.activity.extend;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import xyz.doikki.dkplayer.R;
import xyz.doikki.dkplayer.activity.BaseActivity;
import xyz.doikki.dkplayer.util.DataUtil;
import xyz.doikki.dkplayer.widget.component.MyDanmakuView;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videoplayer.DKVideoView;

/**
 * 弹幕播放
 * Created by Doikki on 17-6-11.
 */

public class DanmakuActivity extends BaseActivity<DKVideoView> {

    private MyDanmakuView mMyDanmakuView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_danmaku_player;
    }

    @Override
    protected int getTitleResId() {
        return R.string.str_danmu;
    }

    @Override
    protected void initView() {
        super.initView();
        mVideoView = findViewById(R.id.player);
        StandardVideoController controller = new StandardVideoController(this);
        controller.addPreferredComponents(getString(R.string.str_danmu), false);
        mMyDanmakuView = new MyDanmakuView(this);
        controller.addControlComponent(mMyDanmakuView);
        mVideoView.setVideoController(controller);
        mVideoView.setDataSource(DataUtil.SAMPLE_URL);
        mVideoView.start();

        mVideoView.addOnStateChangeListener(new DKVideoView.OnStateChangeListener() {
            @Override
            public void onPlayerStateChanged(int playState) {
                if (playState == DKVideoView.STATE_PREPARED) {
                    simulateDanmu();
                } else if (playState == DKVideoView.STATE_PLAYBACK_COMPLETED) {
                    mMyDanmakuView.removeAllDanmakus(true);
                    mHandler.removeCallbacksAndMessages(null);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void showDanMu(View view) {
        mMyDanmakuView.show();
    }

    public void hideDanMu(View view) {
        mMyDanmakuView.hide();
    }

    public void addDanmakuWithDrawable(View view) {
        mMyDanmakuView.addDanmakuWithDrawable();
    }

    public void addDanmaku(View view) {
        mMyDanmakuView.addDanmaku("这是一条文字弹幕~", true);
    }


    private Handler mHandler = new Handler();

    /**
     * 模拟弹幕
     */
    private void simulateDanmu() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("Danmu","1 "+System.currentTimeMillis());
                mMyDanmakuView.addDanmaku("破防了", false);
                mHandler.postDelayed(this, 100);
            }
        });
    }
}

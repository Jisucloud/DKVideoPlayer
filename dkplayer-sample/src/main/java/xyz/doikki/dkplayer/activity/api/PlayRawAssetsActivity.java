package xyz.doikki.dkplayer.activity.api;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.view.View;

import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

import java.io.IOException;

import xyz.doikki.dkplayer.R;
import xyz.doikki.dkplayer.activity.BaseActivity;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videoplayer.DKVideoView;
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory;

/**
 * 播放raw/assets视频
 */

public class PlayRawAssetsActivity extends BaseActivity<DKVideoView> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_play_raw_assets;
    }

    @Override
    protected int getTitleResId() {
        return R.string.str_raw_or_assets;
    }

    @Override
    protected void initView() {
        super.initView();
        mVideoView = findViewById(R.id.player);
        StandardVideoController controller = new StandardVideoController(this);
        controller.addPreferredComponents(getString(R.string.str_raw_or_assets), false);
        mVideoView.setVideoController(controller);
    }

    public void onButtonClick(View view) {
        mVideoView.release();
        Object playerFactory = mVideoView.getRenderFactory();

        switch (view.getId()) {
            case R.id.btn_raw:
                if (playerFactory instanceof ExoMediaPlayerFactory) { //ExoPlayer
                    DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.movie));
                    RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(this);
                    try {
                        rawResourceDataSource.open(dataSpec);
                    } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                        e.printStackTrace();
                    }
                    String url = rawResourceDataSource.getUri().toString();
                    mVideoView.setDataSource(url);
                } else { //MediaPlayer,IjkPlayer
                    String url = "android.resource://" + getPackageName() + "/" + R.raw.movie;
                    mVideoView.setDataSource(url);
                }
                break;
            case R.id.btn_assets:
                if (playerFactory instanceof ExoMediaPlayerFactory) { //ExoPlayer
                    mVideoView.setDataSource("file:///android_asset/" + "test.mp4");
                } else { //MediaPlayer,IjkPlayer
                    AssetManager am = getResources().getAssets();
                    AssetFileDescriptor afd = null;
                    try {
                        afd = am.openFd("test.mp4");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mVideoView.setDataSource(afd);
                }
                break;
        }

        mVideoView.start();
    }
}

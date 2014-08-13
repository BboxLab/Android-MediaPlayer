package fr.bouyguestelecom.bboxapi.android.tv.players;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * This is an abstraction of the {@link android.media.MediaPlayer MediaPlayer}.
 * Instantiate a {@link Player Player} with it to be able to play local or internet link.
 * @author Pierre-Etienne Cherière PCHERIER@bouyguestelecom.fr
 */
public abstract class ClassicPlayer extends AbstractPlayer {

    private Context mContext;
    private VideoView videoView = null;
    private RelativeLayout relativeLayout = null;

    /**
     * The default constructor take the {@link android.content.Context Context} of the application and
     * the {@link android.widget.RelativeLayout RelativeLayout} in which the player will be contained.
     * You also need to Override the {@link #onEnd()} method to be notified when the current media end.
     * @param mContext
     * @param relativeLayout
     */
    public ClassicPlayer(Context mContext, RelativeLayout relativeLayout) {
        this.provider = Provider.OTT;
        this.mContext = mContext;
        this.relativeLayout = relativeLayout;
    }

    @Override
    public void play(String media) {
        deletePlayer();
        videoView = new VideoView(mContext) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                setMeasuredDimension(playerWidth, playerHeight);
            }
        };
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                deletePlayer();
                onEnd();
            }
        });
        relativeLayout.addView(videoView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        videoView.setVideoURI(Uri.parse(media));
        videoView.start();
        move(playerX, playerY);
        resize(playerWidth, playerHeight);
        videoView.clearFocus();
    }

    @Override
    public void resume() {
        if (videoView != null) {
            videoView.start();
        }
    }

    @Override
    public void pause() {
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    public void stop() {
        if (videoView != null) {
            deletePlayer();
        }
    }

    @Override
    public void seek(int mSec) {
        if (videoView != null) {
            videoView.seekTo(mSec);
        }
    }

    @Override
    public void move(int x, int y) {
        playerX = x;
        playerY = y;
        if (videoView != null) {
            videoView.setX(x);
            videoView.setY(y);
        }
    }

    @Override
    public void resize(int width, int height) {

        playerWidth = width;
        playerHeight = height;

        if (videoView != null) {
            videoView.getHolder().setFixedSize(width, height);
            videoView.forceLayout();
            videoView.invalidate();
        }
    }

    private void deletePlayer() {
        if (videoView != null) {
            videoView.stopPlayback();
            relativeLayout.removeView(videoView);
            videoView = null;
        }
    }

    /**
     * This method is called when the player has done playing the media.
     */
    public abstract void onEnd();

    @Override
    public int getCurrentTime() {
        if (videoView != null) {
            return videoView.getCurrentPosition()/1000;
        }
        return -1;
    }

    @Override
    public int getTotalTime() {
        if (videoView != null) {
            return videoView.getDuration()/1000;
        }
        return -1;
    }

    @Override
    public boolean isPlaying() {
        if (videoView != null) {
            return videoView.isPlaying();
        }
        return false;
    }

    @Override
    public void bringToFront() {
        if (videoView != null) {
            videoView.bringToFront();
        }
    }
}

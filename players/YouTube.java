package fr.bouyguestelecom.bboxapi.android.tv.players;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is an abstraction of the {@link com.google.android.youtube.player.YouTubePlayer YouTubePlayer}.
 * Instantiate a {@link Player Player} with it to be able to play YouTube link.
 * The activity which will manage the YouTube player must extend {@link com.google.android.youtube.player.YouTubeBaseActivity YouTubeBaseActivity}.
 * @author Pierre-Etienne Cherière PCHERIER@bouyguestelecom.fr
 */

public abstract class YouTube extends AbstractPlayer {

    private String apiKey;

    private Activity activity;

    private YouTubePlayerView youTubePlayerView     = null;
    private YouTubePlayer youTubePlayer             = null;

    private String videoId = "";

    /**
     * Default constructor for a YouTube player. The activity which will manage the YouTube player
     * must extend {@link com.google.android.youtube.player.YouTubeBaseActivity YouTubeBaseActivity}.
     * You must provide the {@link android.app.Activity Activity} which will manage the player,
     * the {@link android.widget.RelativeLayout RelativeLayout} which will contains the player and
     * your Google developer API key.
     * You also need to Override the {@link #onEnd()} method to be notified when the current media end.
     * @param activity
     * @param relativeLayout
     * @param apiKey
     */
    public YouTube(Activity activity, RelativeLayout relativeLayout, String apiKey) {
        provider = Provider.YOUTUBE;
        this.apiKey = apiKey;
        this.relativeLayout = relativeLayout;
        this.activity = activity;
    }

    @Override
    public void play(final String url) {

        deletePlayer();

        final String youTubeId = getYouTubeVideoId(url);

        if (youTubeId != null) {
            youTubePlayerView = new YouTubePlayerView(activity);
            youTubePlayerView.initialize(apiKey, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                    youTubePlayer = player;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    youTubePlayer.setFullscreenControlFlags(0);
                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {

                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {
                            deletePlayer();
                            onEnd();
                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                    youTubePlayer.loadVideo(youTubeId);
                    Log.i("YouTubeInitialization", "YouTubePlayer is now ready to play");
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.e("YouTubeInitialization", "Error while trying to initialize YouTubePlayer");
                }
            });

            relativeLayout.addView(youTubePlayerView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            move(playerX, playerY);
            resize(playerWidth, playerHeight);
        } else {
            Log.i("YouTube player", "Invalid YouTube url");
        }
    }

    @Override
    public void resume() {
        if (youTubePlayer != null) {
            youTubePlayer.play();
        }
    }

    @Override
    public void pause() {
        if (youTubePlayer != null) {
            youTubePlayer.pause();
        }
    }

    @Override
    public void stop() {
        if (youTubePlayer != null) {
            deletePlayer();
        }
    }

    @Override
    public void move(int x, int y) {
        playerX = x;
        playerY = y;
        youTubePlayerView.setX(x);
        youTubePlayerView.setY(y);
    }

    @Override
    public void resize(int width, int height) {
        playerWidth = width;
        playerHeight = height;
        ViewGroup.LayoutParams params = youTubePlayerView.getLayoutParams();
        params.width = width;
        params.height = height;
        youTubePlayerView.setLayoutParams(params);
    }

    @Override
    public void seek(int mSec) {
        youTubePlayer.seekToMillis(mSec);
    }

    private void deletePlayer() {
        if (youTubePlayer != null) {
            youTubePlayer.release();
            youTubePlayer = null;
        }
        if (youTubePlayerView != null) {
            relativeLayout.removeView(youTubePlayerView);
            youTubePlayerView = null;
        }
    }

    /**
     * This method is called when the player has done playing the media.
     */
    public abstract void onEnd();

    /**
     * Parse a youtube url and return the youtube video ID. Return null if the url is invalid.
     * @param youTubeUrl
     * @return
     */
    public static String getYouTubeVideoId(String youTubeUrl) {

        if (youTubeUrl != null && youTubeUrl.length() > 0) {

            Uri video_uri = Uri.parse(youTubeUrl);
            String video_id = video_uri.getQueryParameter("v");

            if (video_id == null)
                video_id = parseYoutubeVideoId(youTubeUrl);

            return video_id;
        }
        return null;
    }

    private static String parseYoutubeVideoId(String youtubeUrl) {
        String video_id = null;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 &&
                youtubeUrl.startsWith("http")) {
            String expression = "^.*((youtu.be" + "\\/)"
                    + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
                else if (groupIndex1 != null && groupIndex1.length() == 10)
                    video_id = "v" + groupIndex1;
            }
        }
        return video_id;
    }

    @Override
    public boolean isPlaying() {
        if (youTubePlayer != null)
            return youTubePlayer.isPlaying();
        return false;
    }

    @Override
    public void bringToFront() {
        youTubePlayerView.bringToFront();
    }

    @Override
    public int getCurrentTime() {
        if (youTubePlayer != null) {
            return youTubePlayer.getCurrentTimeMillis()/1000;
        }
        return -1;
    }

    @Override
    public int getTotalTime() {
        if (youTubePlayer != null) {
            return youTubePlayer.getDurationMillis()/1000;
        }
        return -1;
    }
}

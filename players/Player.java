package fr.bouyguestelecom.bboxapi.android.tv.players;

/**
 * Player is the interface used to control various type of player. You can instantiate it
 * with a {@link ClassicPlayer ClassicPlayer}
 * or a {@link YouTube YouTube} player
 *
 * @author Pierre-Etienne Cherière PCHERIER@bouyguestelecom.fr
 */

public interface Player {

    /**
     * Play the provided media.
     * @param media Url or local file to play.
     */
    public void play(String media);

    /**
     * Resume the media where it was paused with {@link #pause()}.
     */
    public void resume();

    /**
     * Pause the current media. Can be resume with {@link #resume()}.
     */
    public void pause();

    /**
     * Stop the current media. After a call to this method, the media can't be resumed.
     */
    public void stop();

    /**
     * Move the upper left corner of the player to the given parameters. It's relative to the {@link android.widget.RelativeLayout RelativeLayout} you provide in the constructor.
     * @param x
     * @param y
     */
    public void move(int x, int y);

    /**
     * Resize the player with the given parameters.
     * @param width
     * @param height
     */
    public void resize(int width, int height);

    /**
     * Change the current media position to the given parameter in milliseconds
     * @param mSec
     */
    public void seek(int mSec);

    /**
     * Return the type of the current player.
     * @return provider
     * @see Provider Provider
     */
    public Provider getProvider();

    /**
     * Return true if the player is currently playing.
     * @return isPlaying
     */
    public boolean isPlaying();

    /**
     * Bring the current player to the front view.
     */
    public void bringToFront();

    /**
     * Get the current time of the media playing.
     * @return currentTime
     */
    public int getCurrentTime();

    /**
     * Get the total time of the media playing.
     * @return
     */
    public int getTotalTime();

    /**
     * Called when the media is done playing.
     */
    public void onEnd();
}

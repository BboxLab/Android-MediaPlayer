package fr.bouyguestelecom.bboxapi.android.tv.players;

import android.widget.RelativeLayout;

/**
 * @author Pierre-Etienne Cherière PCHERIER@bouyguestelecom.fr
 */
public abstract class AbstractPlayer implements Player {

    protected Provider provider = Provider.NONE;
    protected RelativeLayout relativeLayout;

    protected int playerWidth = 640;
    protected int playerHeight = 360;
    protected int playerX = 0;
    protected int playerY = 0;

    public Provider getProvider() { return provider; }
}

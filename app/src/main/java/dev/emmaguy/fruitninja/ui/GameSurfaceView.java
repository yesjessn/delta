package dev.emmaguy.fruitninja.ui;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import dev.emmaguy.fruitninja.FruitProjectileManager;
import dev.emmaguy.fruitninja.GameThread;
import dev.emmaguy.fruitninja.ProjectileManager;
import dev.emmaguy.fruitninja.TimedPath;
import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;

public class GameSurfaceView extends SurfaceView implements OnTouchListener, SurfaceHolder.Callback {

    private GameThread gameThread;
    private ProjectileManager projectileManager;
    private OnGameOver gameOverListener;
    private boolean isGameInitialised = false;
    private final SparseArrayCompat<TimedPath> paths = new SparseArrayCompat<TimedPath>();

    public GameSurfaceView(Context context) {
	super(context);

	initialise();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
	super(context, attrs);

	initialise();
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);

	initialise();
    }

    private void initialise() {
	this.setOnTouchListener(this);
	this.setFocusable(true);
	this.getHolder().addCallback(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        long timestamp = System.currentTimeMillis();
        switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    createNewPath(timestamp, event.getX(), event.getY(), event.getPointerId(0));
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:

                    int newPointerIndex = event.getActionIndex();
                    createNewPath(timestamp, event.getX(newPointerIndex), event.getY(newPointerIndex), event.getPointerId(newPointerIndex));

                    break;
                case MotionEvent.ACTION_MOVE:

                    for (int i = 0; i < paths.size(); i++) {
                        int pointerIndex = event.findPointerIndex(paths.indexOfKey(i));

                        if (pointerIndex >= 0) {
                            float x = event.getX(pointerIndex);
                            float y = event.getY(pointerIndex);
                            TimedPath tp = paths.valueAt(i);
                            Log.d("FN", String.format("Added point #%d to path @%s (%.2f, %.2f)", tp.size(), timestamp, x, y));
                            tp.addPoint(timestamp, x, y);
                        }
                    }
                    break;
        }
        gameThread.updateDrawnPath(paths);
        return true;
    }
    
    private void createNewPath(long timestamp, float x, float y, int ptrId) {
        TimedPath path = new TimedPath(timestamp, x, y);
        Log.d("FN", String.format("Created a new path @%s (%.2f, %.2f)", timestamp, x, y));
        paths.append(ptrId, path);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	if (isGameInitialised) {
	    gameThread.resumeGame(width, height);
	} else {
	    isGameInitialised = true;
	    projectileManager = new FruitProjectileManager(this.getContext());
	    gameThread = new GameThread(getHolder(), projectileManager, gameOverListener);
	    gameThread.startGame(width, height);
	}
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
	gameThread.pauseGame();
    }

    public void setGameOverListener(OnGameOver gameOverListener) {
	this.gameOverListener = gameOverListener;
    }
}

package droidrunjump;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;

import phoenix.delta.R;
import phoenix.delta.Utilities;

public class Game
{
    private Pothole[] m_potholes;
    private Pothole m_lastPothole;
    private long    m_spawnPotholeTime;

    private Droid m_droid;
    private boolean   m_playerTapFlag;
    private GameState m_gameState;
    
    private long            m_tapToStartTime;
    private boolean         m_showTapToStart;
    
    private long m_gameOverTime;
    
    private Paint m_clearPaint;
    
    private int     m_width;
    private int     m_height;

    private Paint   m_blackPaint;

    private int   m_highScore;
    private int   m_curScore;
    private long  m_scoreTime;
    private final Paint m_drawScore = new Paint();
    
    private ArrayList<Star> m_stars;
    private long m_spawnStarTime;
    
    private Road m_road;
    private Bitmap   m_backgroundImage;
    private Bitmap   m_pastryImage;
    private Bitmap   m_droidJumpImage;
    private Bitmap[] m_droidImages;
    
    private SoundPool m_soundPool;
    private int       m_droidJumpSnd;
    private int m_droidEatStarSnd;
    private int       m_droidCrashSnd;
    private GameState m_lastGameState;
    private long      m_pauseStartTime;
    
    public boolean getPlayerTapFlag()
    {
        return m_playerTapFlag;
    }
    
    public void setPlayerTapFlag(boolean p_playerTapFlag)
    {
        m_playerTapFlag = p_playerTapFlag;
    }
    
    public Paint getClearPaint()
    {
        return m_clearPaint;
    }
    
    public int getWidth()
    {
        return m_width;
    }

    public int getHeight()
    {
        return m_height;
    }


    public Bitmap getBackgroundImage()
    {
        return m_backgroundImage;
    }


    public Bitmap getStarImage()
    {
        return m_pastryImage;
    }

    public Bitmap getDroidJumpImage()
    {
        return m_droidJumpImage;
    }

    public Bitmap[] getDroidImages()
    {
        return m_droidImages;
    }

    public SoundPool getSoundPool()
    {
        return m_soundPool;
    }

    public int getDroidJumpSnd()
    {
        return m_droidJumpSnd;
    }

    public Droid getDroid() {
        return m_droid;
    }

    public ArrayList<Star> getStars() {
        return m_stars;
    }

    public Game(Context p_context)
    {
        m_clearPaint = new Paint();
        m_clearPaint.setARGB(DroidConstants.EIGHT_BIT_MAX, 0, 0, 0);
        m_clearPaint.setAntiAlias(true);


        loadImages(p_context);

        //Deprecated in build 21 - we're at build 16
        m_soundPool = new SoundPool(DroidConstants.MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        loadSounds(p_context);

        m_droid = new Droid(this);

        m_potholes = new Pothole[DroidConstants.MAX_POTHOLES];
        for (int i = 0; i < m_potholes.length; i++)
        {
            m_potholes[i] = new Pothole(i, this);
        }
        m_spawnPotholeTime = 0;

        m_blackPaint = new Paint();
        m_blackPaint.setAntiAlias(true);
        m_blackPaint.setARGB(DroidConstants.EIGHT_BIT_MAX, 0, 0, 0);
        m_blackPaint.setFakeBoldText(true);
        m_blackPaint.setTextSize(100.0f);

        m_stars = new ArrayList<>();
        m_road = new Road(this);

        m_highScore = DroidConstants.SCORE_DEFAULT;

        initOrResetGame();
    }

    public Pothole[] getPotholes()
    {
        return m_potholes;
    }

    public void setScreenSize(int p_width, int p_height)
    {
        m_width = p_width;
        m_height = p_height;
    }

    public void run(Canvas p_canvas)
    {
        //gets width of the entire surface screen
        final float scaleFactorX = getWidth()/(getBackgroundImage().getWidth()*1.f);
        //gets height of entire surface screen
        final float scaleFactorY = getHeight()/(getBackgroundImage().getHeight()*1.f);
        if (p_canvas != null) {
            final int savedState = p_canvas.save();

            //scales screen
            p_canvas.scale(scaleFactorX, scaleFactorY);

            switch (m_gameState) {
                case GAME_MENU:
                    gameMenu(p_canvas);
                    break;
                case GAME_PLAY:
                    gamePlay(p_canvas);
                    break;
                case GAME_PAUSE:
                    gamePause(p_canvas);
                    break;
            }
            p_canvas.restoreToCount(savedState);
        }
    }

    public void doTouch()
    {
        m_playerTapFlag = true;
    }

    public void initOrResetGame()
    {
        m_tapToStartTime = System.currentTimeMillis();
        m_showTapToStart = true;
        m_playerTapFlag = false;
        m_droid.reset();

        for (Pothole p : m_potholes)
        {
            p.reset();
        }
        m_spawnPotholeTime = 0;

        m_lastPothole = null;
        m_gameState = GameState.GAME_MENU;
        m_lastGameState = m_gameState;

        m_stars.clear();
        m_spawnStarTime = 0;
        m_road.reset();
    }
    
    public void initGameOver()
    {
        m_soundPool.play(m_droidCrashSnd, DroidConstants.FULL_VOLUME, DroidConstants.FULL_VOLUME,
                         DroidConstants.DEFAULT_PRIORITY, DroidConstants.LOOP_INDICATOR,
                         DroidConstants.PLAYBACK_RATE);
        initOrResetGame();
    }

    private void gamePlay(Canvas p_canvas)
    {
        p_canvas.drawRect(0, 0, m_width, m_height, m_clearPaint);

        m_road.update();
        m_road.draw(p_canvas);

        for (Pothole p : m_potholes)
        {
            if (p.isAlive())
            {
                p.update();
                p.draw(p_canvas);
            }
        }

        for (int i = m_stars.size()-1; i >= 0; i--)
        {
            Star s = m_stars.get(i);
            if (s.isAlive())
            {
                s.update();
                s.draw(p_canvas);
            } else {
                m_stars.remove(i);
            }
        }
        m_droid.update();

        p_canvas.drawText("Score: " + m_curScore, 0, 20, m_drawScore);
        m_droid.draw(p_canvas);

        spawnPothole();
        spawnStar();
    }

    private void gameMenu(Canvas p_canvas)
    {
        m_road.draw(p_canvas);

        m_gameState = GameState.GAME_PLAY;
        m_playerTapFlag = false;

        // spawn 1st chasm so player sees something at start of game
        m_potholes[0].spawn(0);
        m_lastPothole = m_potholes[0];
    }

    private void spawnPothole()
    {
        long now = System.currentTimeMillis();
        long timeSinceLastSpawn = now - m_spawnPotholeTime;

        if (timeSinceLastSpawn > DroidConstants.SPAWN_POTHOLE_TIME)
        {
            // randomly determine whether or not to spawn a new pothole
            if (Utilities.nextFloat() > DroidConstants.SPAWN_POTHOLE_CHANCE)
            {
                for (Pothole p : m_potholes)
                {
                    if (p.isAlive())
                    {
                        continue;
                    }

                    float xOffset = 0;

                    //
                    // if the last pothole is alive then use its width to adjust
                    // the position of the new pothole if the last pothole
                    // is too close to the right of the screen. this is to
                    // give the player some breathing room.
                    //

                    if (m_lastPothole != null && m_lastPothole.isAlive())
                    {
                        float rightX = m_lastPothole.getX() + m_lastPothole.getW();

                        if (rightX > m_width)
                        {
                            rightX = rightX - m_width;
                            xOffset = rightX + Utilities.random(10.0f);
                        }
                        else
                        {
                            rightX = m_width - rightX;
                            if (rightX < 20.0f)
                            {
                                xOffset = rightX + Utilities.random(10.0f);
                            }
                        }
                    }

                    p.spawn(xOffset);
                    m_lastPothole = p;
                    break;
                }
            }

            m_spawnPotholeTime = now;
        }
    }

    private void loadSounds(Context context)
    {
        m_droidCrashSnd = m_soundPool.load(context, R.raw.sfx_deathscream_robot1, 1);
        m_droidEatStarSnd = m_soundPool.load(context, R.raw.positive, 1);
        m_droidJumpSnd = m_soundPool.load(context, R.raw.sfx_movement_jump6, 1);
    }

    private void loadImages(Context context)
    {
        Resources res = context.getResources();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        m_backgroundImage = BitmapFactory.decodeResource(res, R.drawable.background, options);

        m_pastryImage = BitmapFactory.decodeResource(res, R.drawable.star);

        m_droidJumpImage = BitmapFactory.decodeResource(res, R.drawable.p1_jump);

        m_droidImages = new Bitmap[DroidConstants.MAX_DROID_IMAGES];
        m_droidImages[0] = BitmapFactory.decodeResource(res, R.drawable.p1_walk01);
        m_droidImages[1] = BitmapFactory.decodeResource(res, R.drawable.p1_walk02);
        m_droidImages[2] = BitmapFactory.decodeResource(res, R.drawable.p1_walk03);
        m_droidImages[3] = BitmapFactory.decodeResource(res, R.drawable.p1_walk04);
        m_droidImages[4] = BitmapFactory.decodeResource(res, R.drawable.p1_walk05);
        m_droidImages[5] = BitmapFactory.decodeResource(res, R.drawable.p1_walk06);
        m_droidImages[6] = BitmapFactory.decodeResource(res, R.drawable.p1_walk07);
        m_droidImages[7] = BitmapFactory.decodeResource(res, R.drawable.p1_walk08);
        m_droidImages[8] = BitmapFactory.decodeResource(res, R.drawable.p1_walk09);
        m_droidImages[9] = BitmapFactory.decodeResource(res, R.drawable.p1_walk10);
        m_droidImages[10] = BitmapFactory.decodeResource(res, R.drawable.p1_walk11);
    }

    private void gamePause(Canvas canvas)
    {
        // clear screen
        canvas.drawRect(0, 0, m_width, m_height, m_clearPaint);

        if (m_playerTapFlag)
        {
            m_playerTapFlag = false;
            m_gameState = m_lastGameState;

            // determine time elapsed between pause and unpause
            long deltaTime = System.currentTimeMillis() - m_pauseStartTime;

            // adjust timer variables based on elapsed time delta
            m_spawnPotholeTime += deltaTime;
            m_tapToStartTime += deltaTime;
            m_gameOverTime += deltaTime;
            m_scoreTime += deltaTime;
            m_spawnStarTime += deltaTime;
        }
    }

    public void pause()
    {
        // if game already paused don't pause it again - otherwise we'll lose the
        // game state and end up in an infinite loop
        if (m_gameState == GameState.GAME_PAUSE)
        {
            return;
        }

        m_lastGameState = m_gameState;
        m_gameState = GameState.GAME_PAUSE;
        m_pauseStartTime = System.currentTimeMillis();
    }

    private void spawnStar()
    {
        long now = System.currentTimeMillis();
        long timeSinceLastSpawn = now - m_spawnStarTime;

        if (timeSinceLastSpawn > DroidConstants.SPAWN_STAR_TIME)
        {
            // randomly determine whether or not to spawn a new star
            if (Utilities.nextFloat() > DroidConstants.SPAWN_STAR_CHANCE)
            {
                Star s = new Star(this);
                s.spawn();
                m_stars.add(s);
            }
            m_spawnStarTime = now;
        }
    }

    public void doPlayerEatStar(Star s)
    {
        // play eat pastry sound
        m_soundPool.play(m_droidEatStarSnd, 1.0f, 1.0f, 0, 0, 1.0f);

        // increase score
        m_curScore += DroidConstants.SCORE_STAR_BONUS;


        // reset pastry and spawn time
        s.setAlive(false);
        m_spawnStarTime = System.currentTimeMillis();
    }

    public void save(SharedPreferences.Editor map)
    {
        if (map == null)
        {
            return;
        }

        map.putInt("game_saved", 1);

        map.putInt("game_highScore", m_highScore);

        // save game vars
        if (m_lastPothole == null)
        {
            map.putInt("game_lastPotHole_id", -1);
        }
        else
        {
            map.putInt("game_lastPotHole_id", m_lastPothole.id);
        }

        map.putLong("game_spawnPotholeTicks", m_spawnPotholeTime);
        map.putBoolean("game_playerTap", m_playerTapFlag);
        map.putInt("game_gameState", Utilities.getOrdinal(m_gameState));
        map.putLong("game_tapToStartTime", m_tapToStartTime);
        map.putBoolean("game_showTapToStart", m_showTapToStart);
        map.putLong("game_gameOverTime", m_gameOverTime);

        map.putInt("game_lastGameState", Utilities.getOrdinal(m_lastGameState));
        map.putLong("game_pauseStartTime", m_pauseStartTime);

        map.putLong("game_spawnPastryTime", m_spawnStarTime);

        map.putLong("game_scoreTime", m_scoreTime);
        map.putInt("game_curScore", m_curScore);

        // save game entities

        m_droid.save(map);

        for (Pothole p : m_potholes)
        {
            p.save(map);
        }

        for (int i = 0; i < m_stars.size(); i++)
        {

            Star s = m_stars.get(i);
            s.save(map, i);
        }

        m_road.save(map);

        map.commit();
    }


}
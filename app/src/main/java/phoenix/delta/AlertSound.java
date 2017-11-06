package phoenix.delta;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class AlertSound {
    private static final SoundPool soundPool = new SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            .build();
    private static int alertSnd = -1;

    public static void play(Context ctx) {
        if (alertSnd == -1) {
            alertSnd = soundPool.load(ctx, R.raw.save, 1);
        }
        soundPool.play(alertSnd, 1, 1, 0, 0, 1);
    }
}

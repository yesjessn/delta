package phoenix.delta;

import java.io.Serializable;

public class WaitTime implements Serializable {
    private Session session;

    public static long LLR_GAMETIME = 20*1000L;
    public static long SSR_GAMETIME = 5*1000L;

    public WaitTime(Session session) {
        this.session = session;
    }

    public long getStartTrialTime() {
        if (session.getCurrTrialChoice() == ScheduleChoice.INSTANT_GAME_ACCESS){
            return 0;
        }
        if (session.getCurrTrialChoice() == ScheduleChoice.WAIT_FOR_GAME) {
            return getPrerewardDelay();
        }
        return 0;
    }

    public long getPrerewardDelay() {
        return session.sessionType.getDelay(session.initDelay, session.getCompletedBlocks());
    }

    public long getGameTime() {
        if (session.getCurrTrialChoice() == ScheduleChoice.WAIT_FOR_GAME){
            return LLR_GAMETIME;
        }
        else if (session.getCurrTrialChoice() == ScheduleChoice.INSTANT_GAME_ACCESS) {
            return SSR_GAMETIME;
        }
        return 0;
    }

    public long getEndTrialTime() {
        long iti = Math.round(Utilities.random(getPrerewardDelay()/5, getPrerewardDelay()/3));

        if (session.getCurrTrialChoice() == ScheduleChoice.WAIT_FOR_GAME){
            return iti;
        }
        else if (session.getCurrTrialChoice() == ScheduleChoice.INSTANT_GAME_ACCESS) {
            return getPrerewardDelay() + (LLR_GAMETIME-SSR_GAMETIME) + iti;
        }
        return 0;
    }
}

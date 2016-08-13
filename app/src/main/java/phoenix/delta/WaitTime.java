package phoenix.delta;

public class WaitTime {
    private Session session;

    public static long INIT_PREREWARD_DELAY = -1L;
    public static long LLR_GAMETIME = 15*1000L;
    public static long SSR_GAMETIME = 5*1000L;

    public WaitTime(Session session) {
        this.session = session;
    }

    public long getPrerewardDelay() {
        return INIT_PREREWARD_DELAY;
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

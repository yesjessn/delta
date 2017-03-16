package phoenix.delta;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable {
    public ArrayList<Trial> trials;
    public int blockNumber;
    private SessionType sessionType;

    public Block(int blockNumber, SessionType sessionType) {
        trials = new ArrayList<Trial>();
        this.blockNumber = blockNumber;
        this.sessionType = sessionType;
    }

    public boolean isComplete() {
        return (trials.size() == sessionType.trialsPerBlock);
    }

    public boolean allWait() {
        for (int i = 2; i < trials.size(); i++) { //start at 2 to skip the first two forced trials
        Trial t = trials.get(i);
            if (t.getChoice() == ScheduleChoice.INSTANT_GAME_ACCESS){
                return false;
        }
        }
        return true;
    }

    public boolean allNow() {
        for (int i = 2; i < trials.size(); i++) {
            Trial t = trials.get(i);
            if (t.getChoice() == ScheduleChoice.WAIT_FOR_GAME){
                return false;
            }
        }
        return true;
    }

    public void add(Trial m_currentTrial) {
        trials.add(m_currentTrial);
    }
}

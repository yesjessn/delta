package phoenix.delta;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable {
    public ArrayList<Trial> trials;
    private int blockNumber;
    private ProcedureType procedureType;

    public Block(int blockNumber, ProcedureType procedureType) {
        trials = new ArrayList<Trial>();
        this.blockNumber = blockNumber;
        this.procedureType = procedureType;
    }

    public boolean isComplete() {
        return (trials.size() == procedureType.trialsPerBlock);
    }

    public boolean allWait() {
        for (int i = 2; i < trials.size(); i++) {
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

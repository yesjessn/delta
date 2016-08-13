package phoenix.delta;

import java.util.ArrayList;

public class Block {
    private ArrayList<Trial> trials;
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

    public void add(Trial m_currentTrial) {
        trials.add(m_currentTrial);
    }
}

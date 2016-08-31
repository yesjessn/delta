package phoenix.delta;

import java.util.ArrayList;

public enum SessionType {
    ESTABLISH_INDIFFERENCE(4) {
        @Override
        public long getDelay(long initDelay, long delayChange, float adjustment, ArrayList<Block> completedBlocks) {
            float adjustmentMultiplier = 0;
            for (int i = 0; i < completedBlocks.size(); i++)
            {
                Block b = completedBlocks.get(i);
                if (b.allWait())
                {
                    adjustmentMultiplier += Math.pow(adjustment, i);
                }
                else if (b.allNow()){
                    adjustmentMultiplier -= Math.pow(adjustment, i);
                }
            }
            return Math.round(initDelay + delayChange * adjustmentMultiplier);
        }
    }, SHAPING(5) {
        @Override
        public long getDelay(long initDelay, long delayChange, float adjustment, ArrayList<Block> completedBlocks) {
            return 0;
        }
    };

    public int trialsPerBlock;

    SessionType(int trialsPerBlock) {
        this.trialsPerBlock = trialsPerBlock;
    }

    public abstract long getDelay(long initDelay, long delayChange, float adjustment, ArrayList<Block> completedBlocks);
}
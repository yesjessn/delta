package phoenix.delta;

import java.util.ArrayList;

public enum SessionType {
    ESTABLISH_INDIFFERENCE(4) {
        public long delayChange = 23226L; //23.2258064516 seconds -> 90 = 45 + (DC * [(0.5^0) + (0.5^1) + (0.5^2) + (0.5^3) + (0.5^4) + (0.5^5)])
        public float adjustment = 0.5f;

        @Override
        public long getDelay(long initDelay, ArrayList<Block> completedBlocks) {
            float adjustmentMultiplier = 0;
            for (int i = 0; i < completedBlocks.size(); i++)
            {
                Block b = completedBlocks.get(i);
                if (b.allWait())
                {
                    adjustmentMultiplier += Math.pow(adjustment, i);
                }
                else if (b.allNow())
                {
                    adjustmentMultiplier -= Math.pow(adjustment, i);
                }
            }
            return Math.round(initDelay + delayChange * adjustmentMultiplier);
        }
    }, SHAPING(5) {
        public float initDelayMultiplier = 0.75f;

        public float waitMultiplier = 1.5f;
        public float nowMultiplier = 0.5f;

        @Override
        public long getDelay(long initDelay, ArrayList<Block> completedBlocks) {
            float delay = initDelay * initDelayMultiplier;
            for (int i = 0; i < completedBlocks.size(); i++)
            {
                Block b = completedBlocks.get(i);
                if (b.allWait())
                {
                    delay *= waitMultiplier;
                }
                else if (b.allNow())
                {
                    delay *= nowMultiplier;
                }
            }
            return Math.round(delay);
        }
    };

    public int trialsPerBlock;

    SessionType(int trialsPerBlock) {
        this.trialsPerBlock = trialsPerBlock;
    }

    public abstract long getDelay(long initDelay, ArrayList<Block> completedBlocks);
}
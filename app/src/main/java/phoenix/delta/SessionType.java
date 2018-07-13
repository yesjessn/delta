package phoenix.delta;

import java.util.ArrayList;

public enum SessionType {
    ESTABLISH_INDIFFERENCE(4) {
        public long delayChange = 15238L; //15.238 seconds -> 30 = 10.0 + (DC * [(0.5^0) + (0.5^1) + (0.5^2) + (0.5^3) + (0.5^4) + (0.5^5)])
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
            return Math.max(Math.round(initDelay + delayChange * adjustmentMultiplier), minWaitTime);
        }
    }, SHAPING(5) {
        public float	initDelayMultiplier	= 0.75f;

		public float	waitDelta			= 2.5f;

		@Override
		public long getDelay(final long initDelay, final ArrayList<Block> completedBlocks)
		{
			float delay = initDelay * initDelayMultiplier;
			for (int i = 0; i < completedBlocks.size(); i++)
			{
				final Block b = completedBlocks.get(i);
				if (b.allWait())
				{
					delay += waitDelta;
				}
				else if (b.allNow())
				{
					delay -= waitDelta;
				}
			}
			return Math.max(Math.round(delay), minWaitTime);
		}
    };
    private static final long minWaitTime = 1000L;

    public int trialsPerBlock;

    SessionType(int trialsPerBlock) {
        this.trialsPerBlock = trialsPerBlock;
    }

    public abstract long getDelay(long initDelay, ArrayList<Block> completedBlocks);
}

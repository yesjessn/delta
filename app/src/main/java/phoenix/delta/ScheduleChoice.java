package phoenix.delta;

public enum ScheduleChoice {
    WAIT_FOR_GAME {
        @Override
        public String toString() {
            return "WAIT";
        }
    }, INSTANT_GAME_ACCESS {
        @Override
        public String toString() {
            return "NOW";
        }
    };

}

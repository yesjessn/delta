package phoenix.delta;

public enum ProcedureType {
    ESTABLISH_INDIFFERENCE(4), SHAPING(5);

    public int trialsPerBlock;

    ProcedureType(int trialsPerBlock) {
        this.trialsPerBlock = trialsPerBlock;
    }
}
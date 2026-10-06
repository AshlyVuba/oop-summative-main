package za.co.wethinkcode.ridehub.rentals;

public class LateReturnPenalty {

    public static final int GRACE_MINUTES = 15;
    public static final int BASE_PENALTY_LIMIT_MINUTES = 60;
    public static final int EXTRA_BLOCK_MINUTES = 30;
    public static final double BASE_PENALTY = 20.00;
    public static final double EXTRA_PER_BLOCK = 10.00;
    public static final double MAX_PENALTY = 100.00;

    public double calculate(int minutesOverdue) {
        if (minutesOverdue < 0) {
            throw new IllegalArgumentException("minutesOverdue cannot be negative");
        }
        if (minutesOverdue <= GRACE_MINUTES) {
            return 0.0;
        }
        if (minutesOverdue <= BASE_PENALTY_LIMIT_MINUTES) {
            return BASE_PENALTY;
        }
        int extraMinutes = minutesOverdue - BASE_PENALTY_LIMIT_MINUTES;
        int startedBlocks = (extraMinutes + EXTRA_BLOCK_MINUTES - 1) / EXTRA_BLOCK_MINUTES;
        return Math.min(BASE_PENALTY + startedBlocks * EXTRA_PER_BLOCK, MAX_PENALTY);
    }
}

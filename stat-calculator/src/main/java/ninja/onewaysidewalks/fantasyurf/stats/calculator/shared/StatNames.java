package ninja.onewaysidewalks.fantasyurf.stats.calculator.shared;

public class StatNames {
    public static String mean(String statName) {
        return String.format("%s-mean", statName);
    }

    public static String min(String statName) {
        return String.format("%s-min", statName);
    }

    public static String max(String statName) {
        return String.format("%s-max", statName);
    }

    public static String p50(String statName) {
        return String.format("%s-p50", statName);
    }

    public static String p75(String statName) {
        return String.format("%s-p75", statName);
    }

    public static String p90(String statName) {
        return String.format("%s-p90", statName);
    }

    public static String p99(String statName) {
        return String.format("%s-p99", statName);
    }
}

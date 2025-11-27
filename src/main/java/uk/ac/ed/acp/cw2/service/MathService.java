package uk.ac.ed.acp.cw2.service;

public class MathService {
    public static boolean compareInt(int val1, int val2, String operator) {
        switch (operator) {
            case "=":
                return val1 == val2;
            case "!=":
                return val1 != val2;
            case ">":
                return val1 > val2;
            case  "<":
                return val1 < val2;
            default:
                return false;
        }
    }
    public static boolean compareDouble(double val1, double val2, String operator) {
        switch (operator) {
            case "=":
                return val1 == val2;
            case "!=":
                return val1 != val2;
            case ">":
                return val1 > val2;
            case  "<":
                return val1 < val2;
            default:
                return false;
        }
    }
}

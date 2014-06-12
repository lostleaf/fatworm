package fatworm.util;

import fatworm.parser.FatwormParser;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Operator {

    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int MULTIPLY = 2;
    public static final int DIVIDE = 3;
    public static final int MOD = 4;

    public static int getOpFromType(int type) {
        switch (type) {
            case FatwormParser.T__105:
                return MOD;
            case FatwormParser.T__108:
                return MULTIPLY;
            case FatwormParser.T__109:
                return PLUS;
            case FatwormParser.T__111:
                return MINUS;
            case FatwormParser.T__113:
                return DIVIDE;
        }
        return -1;
    }
}

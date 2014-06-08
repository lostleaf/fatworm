package fatworm.pred;

import fatworm.parser.FatwormParser;

/**
 * Created by lostleaf on 14-6-5.
 */
public class CmpOp {
    public static final int LESS_THAN = 0;
    public static final int GREATER_THAN = 1;
    public static final int EQUAL = 2;
    public static final int GREATER_EQ = 3;
    public static final int LESS_EQ = 4;
    public static final int NOT_EQUAL = 5;

    public static int getCopFromType(int type) {
        switch (type) {
            case FatwormParser.T__114:
                return LESS_THAN;
            case FatwormParser.T__115:
                return LESS_EQ;
            case FatwormParser.T__116:
                return NOT_EQUAL;
            case FatwormParser.T__117:
                return EQUAL;
            case FatwormParser.T__118:
                return GREATER_THAN;
            case FatwormParser.T__119:
                return GREATER_EQ;
        }
        return -1;
    }

    public static boolean doCompare(int compareResult, int cmpOp){
        if (compareResult > 0 && (cmpOp == CmpOp.GREATER_THAN || cmpOp == CmpOp.GREATER_EQ
                || cmpOp == CmpOp.NOT_EQUAL))
            return true;

        if (compareResult == 0 && (cmpOp == CmpOp.GREATER_EQ || cmpOp == CmpOp.LESS_EQ
                || cmpOp == CmpOp.EQUAL))
            return true;

        if (compareResult < 0 && (cmpOp == CmpOp.LESS_EQ || cmpOp == CmpOp.LESS_THAN
                || cmpOp == CmpOp.NOT_EQUAL))
            return true;
        return false;
    }
}

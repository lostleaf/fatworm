package fatworm.util;

/**
 * Created by lostleaf on 14-6-5.
 */
public class TypeUtil {

    public static int getStrSize(int len) {
        return len * CHAR_SIZE + INT_SIZE;
    }

    public static int getDecimalSize(int len) {
        return len + INT_SIZE * 2;
    }

    public static final int INT_SIZE = Integer.SIZE / Byte.SIZE;
    public static final int DOUBLE_SIZE = Double.SIZE / Byte.SIZE;
    public static final int BOOLEAN_SIZE = 1;
    public static final int TIMESTAMP_SIZE = Long.SIZE / Byte.SIZE;
    public static final int CHAR_SIZE = 1;
    public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
}

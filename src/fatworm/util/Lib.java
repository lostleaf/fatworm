package fatworm.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Timestamp;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Lib {

    public static int getStrSize(int len) {
        return len * CHAR_SIZE + INT_SIZE;
    }

    public static int getDecimalSize(int len) {
        return len + INT_SIZE * 2;
    }

    private static void bytesFromInt(byte[] array, int offset, int value) {
        array[offset + 0] = (byte) ((value >> 0) & 0xFF);
        array[offset + 1] = (byte) ((value >> 8) & 0xFF);
        array[offset + 2] = (byte) ((value >> 16) & 0xFF);
        array[offset + 3] = (byte) ((value >> 24) & 0xFF);
    }

    private static int bytesToInt(byte[] array, int offset) {
        return (int) ((((int) array[offset + 0] & 0xFF) << 0)
                | (((int) array[offset + 1] & 0xFF) << 8)
                | (((int) array[offset + 2] & 0xFF) << 16) | (((int) array[offset + 3] & 0xFF) << 24));
    }

    private static void bytesFromLong(byte[] array, int offset, long value) {
        array[offset + 0] = (byte) ((value >> 0) & 0xFF);
        array[offset + 1] = (byte) ((value >> 8) & 0xFF);
        array[offset + 2] = (byte) ((value >> 16) & 0xFF);
        array[offset + 3] = (byte) ((value >> 24) & 0xFF);
        array[offset + 4] = (byte) ((value >> 32) & 0xFF);
        array[offset + 5] = (byte) ((value >> 40) & 0xFF);
        array[offset + 6] = (byte) ((value >> 48) & 0xFF);
        array[offset + 7] = (byte) ((value >> 56) & 0xFF);
    }

    private static long bytesToLong(byte[] array, int offset) {
        return (((long) array[0] & 0xFF) << 0)
                | (((long) array[offset + 1] & 0xFF) << 8)
                | (((long) array[offset + 2] & 0xFF) << 16)
                | (((long) array[offset + 3] & 0xFF) << 24)
                | (((long) array[offset + 4] & 0xFF) << 32)
                | (((long) array[offset + 5] & 0xFF) << 40)
                | (((long) array[offset + 6] & 0xFF) << 48)
                | (((long) array[offset + 6] & 0xFF) << 56);
    }

    public static byte[] bytesFromInt(int value) {
        byte[] array = new byte[4];
        bytesFromInt(array, 0, value);
        return array;
    }

    public static int bytesToInt(byte[] array) {
        return (int) ((((int) array[0] & 0xFF) << 0)
                | (((int) array[1] & 0xFF) << 8)
                | (((int) array[2] & 0xFF) << 16) | (((int) array[3] & 0xFF) << 24));
    }

    public static String bytesToString(byte[] array) {
        return new String(array);
    }

    public static byte[] bytesFromString(String s) {
        return s.getBytes();
    }

    public static double bytesToDouble(byte[] array) {
        return ByteBuffer.wrap(array).getDouble();
    }

    public static byte[] bytesFromDouble(double value) {
        byte[] array = new byte[DOUBLE_SIZE];
        ByteBuffer.wrap(array).putDouble(value);
        return array;
    }

    public static float bytesToFloat(byte[] array) {
        return ByteBuffer.wrap(array).getFloat();
    }

    public static byte[] bytesFromFloat(float value) {
        byte[] array = new byte[FLOAT_SIZE];
        ByteBuffer.wrap(array).putFloat(value);
        return array;
    }

    public static boolean bytesToBoolean(byte[] array) {
        return array[0] != 0;
    }

    public static byte[] bytesFromBoolean(boolean value) {
        byte[] array = new byte[BOOLEAN_SIZE];
        if (value) {
            array[0] = 1;
        } else {
            array[0] = 0;
        }
        return array;
    }

    public static BigDecimal bytesToDecimal(byte[] array) {
        int length, scale;
        length = bytesToInt(array, 0);
        scale = bytesToInt(array, length + INT_SIZE);
        byte[] vbytes = new byte[length];
        System.arraycopy(array, INT_SIZE, vbytes, 0, length);
        return new BigDecimal(new BigInteger(vbytes), scale);
    }

    public static byte[] bytesFromDecimal(BigDecimal value) {
        byte[] vbytes = value.unscaledValue().toByteArray();
        int length = vbytes.length;
        int scale = value.scale();
        byte[] array = new byte[length + INT_SIZE * 2];
        System.arraycopy(vbytes, 0, array, INT_SIZE, length);
        bytesFromInt(array, 0, length);
        bytesFromInt(array, INT_SIZE + length, scale);
        return array;
    }

    public static byte[] bytesFromTimestamp(Timestamp value) {
        byte[] array = new byte[TIMESTAMP_SIZE];
        bytesFromLong(array, 0, value.getTime());
        return array;
    }

    public static Timestamp bytesToTimestamp(byte[] array) {
        Long time = bytesToLong(array, 0);
        return new Timestamp(time);
    }

    public static final int BUFFER_SIZE = 1024;
    public static final int BLOCK_SIZE = 4096;
    public static final int INT_SIZE = Integer.SIZE / Byte.SIZE;
    public static final int DOUBLE_SIZE = Double.SIZE / Byte.SIZE;
    public static final int BOOLEAN_SIZE = 1;
    public static final int TIMESTAMP_SIZE = Long.SIZE / Byte.SIZE;
    public static final int CHAR_SIZE = 1;
    public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
}

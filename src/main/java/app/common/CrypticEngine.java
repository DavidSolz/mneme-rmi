package app.common;

public class CrypticEngine {

    public static String weakHash(byte[] data, Integer size)
    {
        Integer hash = 5381;

        for (int i = 0; i < size; i++) {
            hash = (hash * 33) ^ data[i];
        }

        return Integer.toHexString(hash & 0xffffffff);
    }

    public static String strongHash(String accumulator, byte[] data, long size)
    {
        final Integer MOD = 150190;
        final Integer BASE = 18371539;
        Integer hash = Integer.valueOf(accumulator, 16);

        for (int i = 1; i < size; i++) {
            hash ^= data[i];
            hash += data[i-1];
            hash *= BASE;
            hash %= MOD;
        }

        return Integer.toHexString(hash);
    }

}

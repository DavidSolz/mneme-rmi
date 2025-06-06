package app.apollo.common;

/**
 * Utility class providing cryptographic hash functions for data verification.
 * <p>
 * This class includes a simple XOR-based hash (weak) and a more complex
 * rolling-style hash (strong). These functions are designed for internal use
 * such as block verification or checksum calculations in distributed systems.
 * </p>
 *
 * <p>
 * <strong>Note:</strong> These hashing methods are not cryptographically secure
 * and should not be used for security-sensitive operations such as password
 * hashing
 * or digital signatures.
 * </p>
 */
public class CrypticEngine {

    /**
     * Computes a weak hash of the given byte array using a simple DJB2-like XOR
     * algorithm.
     *
     * @param data The byte array to hash.
     * @param size The number of bytes to read from the array.
     * @return A hexadecimal string representation of the hash value.
     */
    public static String weakHash(byte[] data, Integer size) {
        Integer hash = 5381;

        for (int i = 0; i < size; i++) {
            hash = (hash * 33) ^ data[i];
        }

        return Integer.toHexString(hash & 0xffffffff);
    }

    /**
     * Computes a stronger hash by combining values in the byte array using XOR,
     * addition,
     * multiplication, and modulo operations with a custom accumulator.
     *
     * @param accumulator Initial hash value represented as a hexadecimal string.
     * @param data        The byte array to hash.
     * @param size        The number of bytes to process.
     * @return A hexadecimal string representation of the resulting hash.
     */
    public static String strongHash(String accumulator, byte[] data, long size) {
        final Integer MOD = 150190;
        final Integer BASE = 18371539;
        Integer hash = Integer.valueOf(accumulator, 16);

        for (int i = 1; i < size; i++) {
            hash ^= data[i];
            hash += data[i - 1];
            hash *= BASE;
            hash %= MOD;
        }

        return Integer.toHexString(hash);
    }
}

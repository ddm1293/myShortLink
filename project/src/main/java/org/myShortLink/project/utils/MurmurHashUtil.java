package org.myShortLink.project.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class MurmurHashUtil {

    private static final char[] CHARS = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static final int SIZE = CHARS.length;

    private static Long readUnsignedLong(Long value) {
        return value >= 0 ? value : value & Long.MAX_VALUE;
    }

    private static String toBase62(Long hash) {
        StringBuilder sb = new StringBuilder();
        do {
            int digit = (int) (hash % SIZE);
            sb.append(CHARS[digit]);
            hash /= SIZE;
        } while (hash > 0);
        return sb.reverse().toString();
    }

    public static String convertWithGuava(String input) {
        HashFunction hashFunc = Hashing.murmur3_32_fixed();
        int hashed = hashFunc.hashString(input, StandardCharsets.UTF_8).asInt();
        return toBase62(readUnsignedLong((long) hashed));
    }

}

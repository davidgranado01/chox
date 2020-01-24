package idas.chox.web.security;

import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class ChoxPasswordEncoder implements PasswordEncoder {
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static final String NOOP_PREFIX = UUID.randomUUID().toString();
    private static final int NOOP_PREFIX_LENGTH = NOOP_PREFIX.length();

    @Override
    public String encode(CharSequence rawPassword) {
        if (isSpecialPassword(rawPassword)) {
            return rawPassword.subSequence(NOOP_PREFIX_LENGTH, rawPassword.length()).toString();
        }

        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (isSpecialPassword(rawPassword)) {
            return equals(encodedPassword, encode(rawPassword));
        }

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return passwordEncoder.upgradeEncoding(encodedPassword);
    }

    private boolean isSpecialPassword(CharSequence rawPassword) {
        return null != rawPassword && rawPassword.length() > NOOP_PREFIX_LENGTH &&
                NOOP_PREFIX.equals(rawPassword.subSequence(0, NOOP_PREFIX_LENGTH).toString());
    }

    private boolean equals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        int expectedLength = expectedBytes == null ? -1 : expectedBytes.length;
        int actualLength = actualBytes == null ? -1 : actualBytes.length;

        int result = expectedLength == actualLength ? 0 : 1;
        for (int i = 0; i < actualLength; i++) {
            byte expectedByte = expectedLength <= 0 ? 0 : expectedBytes[i % expectedLength];
            byte actualByte = actualBytes[i % actualLength];
            result |= expectedByte ^ actualByte;
        }
        return result == 0;
    }

    private byte[] bytesUtf8(String s) {
        if (s == null) {
            return null;
        }

        return Utf8.encode(s);
    }
}

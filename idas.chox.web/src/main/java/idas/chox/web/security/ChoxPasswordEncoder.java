package idas.chox.web.security;

import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public class ChoxPasswordEncoder implements PasswordEncoder {
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static final String HASHED_PASSWORD_SECRET = UUID.randomUUID().toString();
    private static final int HASHED_PASSWORD_SECRET_LENGTH = HASHED_PASSWORD_SECRET.length();

    @Override
    public String encode(CharSequence rawPassword) {
        if (isHashedPasswordSecretPrefixed(rawPassword)) {
            return extractRawPassword(rawPassword);
        }

        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (isHashedPasswordSecretPrefixed(rawPassword)) {
            return equals(encodedPassword, extractRawPassword(rawPassword));
        }

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return passwordEncoder.upgradeEncoding(encodedPassword);
    }

    private boolean isHashedPasswordSecretPrefixed(CharSequence rawPassword) {
        boolean hashedPasswordSecretPrefixed = false;
        Optional<CharSequence> rawPasswordOptional = Optional.ofNullable(rawPassword);
        if (rawPasswordOptional.isPresent()) {
            if (rawPassword.length() > HASHED_PASSWORD_SECRET_LENGTH) {
                String prefixedSecret = extractSecret(rawPassword);
                hashedPasswordSecretPrefixed = HASHED_PASSWORD_SECRET.equals(prefixedSecret);
            }
        }
        return hashedPasswordSecretPrefixed;
    }

    private String extractRawPassword(CharSequence prefixedRawPassword) {
        CharSequence rawPassword = prefixedRawPassword.subSequence(HASHED_PASSWORD_SECRET_LENGTH, prefixedRawPassword.length());
        return rawPassword.toString();
    }

    private String extractSecret(CharSequence prefixedRawPassword) {
        CharSequence rawPassword = prefixedRawPassword.subSequence(0, HASHED_PASSWORD_SECRET_LENGTH).toString();
        return rawPassword.toString();
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

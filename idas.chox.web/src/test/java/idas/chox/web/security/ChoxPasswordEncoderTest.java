package idas.chox.web.security;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class ChoxPasswordEncoderTest {
    private ChoxPasswordEncoder choxPasswordEncoder;
    private PasswordEncoder defaultPasswordEncoder;

    private String rawPassword;
    private String defaultHash;
    private String specialPassword;

    @Before
    public void setUp() {
        choxPasswordEncoder = new ChoxPasswordEncoder();
        defaultPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // deliberately make the password normal length
        rawPassword = UUID.randomUUID().toString().substring(0, 16);

        defaultHash = defaultPasswordEncoder.encode(rawPassword);
        // special password is simple special prefix with normal hash
        specialPassword = ChoxPasswordEncoder.HASHED_PASSWORD_SECRET + defaultHash;
    }

    @Test
    public void hashNormalPasswordShouldMatchTheDefaultEncoderHash() {
        //The bcrypt hash algorithm, by design, generates a different encrypted string every time you call it (it is salted).
        // Assert.assertEquals(choxPasswordEncoder.encode(rawPassword), defaultPasswordEncoder.encode(rawPassword));
        Assert.assertTrue(choxPasswordEncoder.matches(rawPassword, defaultHash));
    }

    @Test
    public void matchShouldFailToMatchIfNotSpecialPassword() {
        Assert.assertFalse(choxPasswordEncoder.matches(UUID.randomUUID().toString(), defaultHash));
        Assert.assertFalse(choxPasswordEncoder.matches(defaultHash, defaultHash));
    }

    @Test
    public void hashSpecialPasswordShouldReturnWithoutHash() {
        Assert.assertEquals(choxPasswordEncoder.encode(specialPassword), defaultHash);
        Assert.assertTrue(choxPasswordEncoder.matches(specialPassword, defaultHash));
    }

    @Test
    public void emptyPasswordShouldFailToMatch() {
        Assert.assertFalse(choxPasswordEncoder.matches("", defaultHash));
    }

    @Test(expected = NullPointerException.class)
    public void nullPasswordShouldThrowNullPointerException() {
        choxPasswordEncoder.matches(null, defaultHash);
    }

    @After
    public void tearDown() {
        choxPasswordEncoder = null;
        defaultPasswordEncoder = null;
    }
}

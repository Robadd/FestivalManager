package de.robadd.festivalmanager;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Crypto
{
    private static final Logger LOG = LoggerFactory.getLogger(Crypto.class);

    private Crypto()
    {
    }

    public static String generateHash(final String name, final Integer type, final Boolean tShirt)
    {
        String hash = "";
        try
        {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((name + type + tShirt + Config.getInstance().getSecretKey()).getBytes(
                StandardCharsets.UTF_8));
            hash = String.format("%032x", new BigInteger(1, messageDigest.digest()));
        }
        catch (final NoSuchAlgorithmException e)
        {
            LOG.error("This should never happen", e);
        }
        return hash;
    }

}

package de.robadd.festivalmanager;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto
{

	private Crypto()
	{
	}

	public static String generateHash(final String name, final Integer type, final Boolean tShirt)
	{
		String hash = "";
		MessageDigest messageDigest = null;
		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update((name + type + tShirt + Config.getInstance().getSecretKey()).getBytes(
				StandardCharsets.UTF_8));
			hash = String.format("%032x", new BigInteger(1, messageDigest.digest()));
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return hash;
	}

}

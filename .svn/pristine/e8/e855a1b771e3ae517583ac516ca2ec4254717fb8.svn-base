/*
 *  Copyright (C) 2012, 2013 Caixa Magica Software.
 *
 *  Authors:
 *
 *      Daniel Romão <daniel.romao@caixamagica.pt>
 *      Nuno Martins <nuno.martins@caixamagica.pt>
 *
 */
/* Depends on BouncyCastle */
/* http://www.bouncycastle.org/java.html */

package lib.api;

import java.security.PublicKey;

import org.bouncycastle.crypto.digests.SHA256Digest;

public class MessageDigest {

	public static byte[] hashPublicKey(PublicKey pk)
	{
		return SHA256Bytes(pk.getEncoded());
	}
	
	public static byte[] hashPublicKeyMAC(PublicKey pk, byte []mac)
	{
		byte []hash_key = SHA256Bytes(pk.getEncoded());
		byte []key = new byte[hash_key.length + mac.length];
		System.arraycopy(hash_key, 0, key, 0, hash_key.length);
		System.arraycopy(mac, 0, key, hash_key.length, mac.length);
		return key;
	}

	public static byte[] hashPublicKeyMACAlt(PublicKey pk, byte []mac)
	{
		byte []hash_key;
		byte []hash_mac;
		byte []concat_hash;

		hash_key = SHA256Bytes(pk.getEncoded());
		hash_mac = SHA256Bytes(mac);
		concat_hash = new byte[hash_key.length + hash_mac.length];
		System.arraycopy(hash_key, 0, concat_hash, 0, hash_key.length);
		System.arraycopy(hash_mac, 0, concat_hash, hash_key.length, hash_mac.length);
		return concat_hash;
	}

	public static byte[] SHA256Bytes(byte []key)
	{
		SHA256Digest digest = new SHA256Digest(); 
		byte[] retValue = new byte[digest.getDigestSize()];
		digest.update(key, 0, key.length);
		digest.doFinal(retValue, 0); 
		return retValue;
	}
}

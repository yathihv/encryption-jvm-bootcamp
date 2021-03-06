package com.ambientideas.cryptography;

import junit.framework.Assert;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.PaddedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Strings;
import org.junit.Test;

/*
 * Use BouncyCastle for DES encryption
 */
@SuppressWarnings("deprecation")
public class TestBCBlowfishAndDESDirectClasses {
    @Test
	public void testBlowfish() throws Exception {
		final String DATA = "Four score and seven years ago.";
		final byte[] KEY = "thisisak".getBytes();
		
        byte[] resultencrypt = doCryptBlowfish(true, KEY, Strings.toUTF8ByteArray(DATA));
        System.out.println("Blowfish Encrypt Result: " + resultencrypt.toString());
        
        byte[] resultdecrypt = doCryptBlowfish(false, KEY, resultencrypt);
        System.out.println("Blowfish Decrypt Result: " + Strings.fromUTF8ByteArray(resultdecrypt));
        Assert.assertEquals(DATA, Strings.fromUTF8ByteArray(resultdecrypt));
	}
    
    @Test
    public void testDES() throws Exception {
        final String DATA = "Four score and seven years ago.";
        final byte[] KEY = "thisisak".getBytes();
        
        byte[] resultencrypt = doCryptDES(true, KEY, Strings.toUTF8ByteArray(DATA));
        System.out.println("DES Encrypt Result: " + resultencrypt.toString());
        
        byte[] resultdecrypt = doCryptDES(false, KEY, resultencrypt);
        System.out.println("DES Decrypt Result: " + Strings.fromUTF8ByteArray(resultdecrypt));
        Assert.assertEquals(DATA, Strings.fromUTF8ByteArray(resultdecrypt));
    }

    public static byte[] doCryptBlowfish(boolean encrypt, byte[] key, byte[] data) throws InvalidCipherTextException {
        BufferedBlockCipher cipher = new PaddedBlockCipher(
                new CBCBlockCipher(
                new BlowfishEngine() ) );
      
        return doCrypt(cipher, encrypt, key, data);
    }
	
    public static byte[] doCryptDES(boolean encrypt, byte[] key, byte[] databytes) throws InvalidCipherTextException {
        BufferedBlockCipher cipher = new PaddedBlockCipher(
                new CBCBlockCipher(
                new DESEngine() ) );
        
        return doCrypt(cipher, encrypt, key, databytes);
    }
	
    private static byte[] doCrypt(BufferedBlockCipher cipher, boolean encrypt, byte[] key, byte[] databytes) throws InvalidCipherTextException {
        KeyParameter keyp = new KeyParameter( key );
        
        cipher.init( encrypt, keyp );
        int outputsize = cipher.getOutputSize(databytes.length);
        byte[] resultbytes = new byte[outputsize];
        int outputlength = cipher.processBytes(databytes, 0, databytes.length, resultbytes, 0);
        outputlength += cipher.doFinal(resultbytes, outputlength);

        if (outputlength < outputsize) {
            byte[] tmp = new byte[outputlength];
            System.arraycopy(resultbytes, 0, tmp, 0, outputlength);
            resultbytes = tmp;
        }
        return resultbytes;
    }	
}

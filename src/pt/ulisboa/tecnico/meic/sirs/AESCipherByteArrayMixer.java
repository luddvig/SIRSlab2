package pt.ulisboa.tecnico.meic.sirs;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implementation of the AES cipher as a ByteArrayMixer
 */
public class AESCipherByteArrayMixer implements ByteArrayMixer {

    private String keyFile;
    private String mode;
    private int opmode;

    // TESTING

    public void setParameters(String keyFile, String mode) {
        this.keyFile = keyFile;
        this.mode = mode;
    }

    public AESCipherByteArrayMixer(int opmode) {
        this.opmode = opmode;
    }

    @Override
    public byte[] mix(byte[] byteArray, byte[] byteArray2) throws Exception {

        // Key key = AESKeyGenerator.read(keyFile);
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        Key key1 = keyGen.generateKey();
        byte[] encoded = key1.getEncoded();
        Key key = new SecretKeySpec(encoded, 0, 16, "AES");

        // get a DES cipher object and print the provider
        Cipher cipher = Cipher.getInstance("AES/" + mode + "/PKCS5Padding");
        System.out.println(cipher.getProvider().getInfo());

        System.out.println("Ciphering ...");
        if (!mode.equals("ECB")) {
            // look! A null IV!
            cipher.init(this.opmode, key, new IvParameterSpec(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        } else {
            cipher.init(this.opmode, key);
        }

        return cipher.doFinal(byteArray);
    }
}

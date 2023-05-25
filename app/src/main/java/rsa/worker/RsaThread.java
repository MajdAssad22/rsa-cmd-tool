package rsa.worker;

import rsa.util.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaThread extends Thread{
    String operation;
    String suite;
    File inFile;
    File outFile;
    File keyFile;
    int keyLength;

    public RsaThread(String operation, String suite, String inFilePath, String outFilePath, String keyFilePath, int keyLength) throws FileNotFoundException, IllegalArgumentException {
        ValidateAndStoreParams(suite, operation, inFilePath, outFilePath, keyFilePath, keyLength);
    }

    @Override
    public void run() {
        // TODO the logic of encrypting / decrypting / generating a key
        try{
            if (operation.equals("encrypt") || operation.equals("decrypt")) {
                KeyPair pair = Utils.loadKey(keyFile.getPath());
                Cipher cipher = Cipher.getInstance(suite);
                byte[] data = Files.readAllBytes(Path.of(inFile.toURI()));

                if (operation.equals("encrypt")) {
                    cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
                    CipherStream(cipher, inFile, outFile);
//                    byte[] cipheredData = cipher.doFinal(data);
//                    File file = new File(outFile.toURI());
//                    Files.write(file.toPath(), cipheredData);
                } else {
                    cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
                    CipherStream(cipher, inFile, outFile);
//                    byte[] cipheredData = cipher.doFinal(data);
//                    Files.write(Path.of(outFile.toURI()), cipheredData);
                }
            } else if (operation.equals("generateKey")) {
                if (keyLength == -1) {
                    throw new IllegalArgumentException("KeyLength is missing");
                } else {
                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                    generator.initialize(keyLength);
                    KeyPair pair = generator.generateKeyPair();
                    Utils.saveKey(keyFile.getPath(), (RSAPrivateKey) pair.getPrivate(), (RSAPublicKey) pair.getPublic());
                }
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Suite must be RSA/ECB/PKCS1Padding or RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        } catch (IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void ValidateAndStoreParams(String suite, String operation, String inFilePath, String outFilePath, String keyFilePath, int keyLength) throws FileNotFoundException {
        // Key file
        try{
            this.keyFile = new File(keyFilePath);
        }catch (NullPointerException e){
            throw new NullPointerException("Must provide a keyFile");
        }
        // Operation
        this.operation = operation;

        if (operation.equalsIgnoreCase("encrypt") || operation.equalsIgnoreCase("decrypt")) {
            // In file
            try {
                this.inFile = new File(inFilePath);
            } catch (NullPointerException e) {
                throw new NullPointerException("Must provide an inFile");
            }
            if (!inFile.exists() || !inFile.isFile()) {
                throw new FileNotFoundException("File not found: \"" + inFilePath + "\"");
            }

            // Out file
            try {
                this.outFile = new File(outFilePath);
            } catch (NullPointerException e) {
                throw new NullPointerException("Must provide an outFile");
            }

            // Suite
            if (!suite.equalsIgnoreCase("RSA/ECB/PKCS1Padding") && !suite.equalsIgnoreCase("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")) {
                throw new IllegalArgumentException("Suite must be RSA/ECB/PKCS1Padding or RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            }
            this.suite = suite;
        }
        else if (operation.equalsIgnoreCase("generateKey")) {
            // Key length
            this.keyLength = keyLength;
        }
        else{
            throw new IllegalArgumentException("operation must be encrypt/decrypt/generateKey");
        }
    }

    private void CipherStream(Cipher cipher, File inFile, File outFile) throws IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] buffer = new byte[4020];
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        int readBytes;
        while((readBytes = fis.read(buffer)) != -1){
            fos.write(cipher.update(buffer,0, readBytes));
        }
        fos.write(cipher.doFinal());
    }
}

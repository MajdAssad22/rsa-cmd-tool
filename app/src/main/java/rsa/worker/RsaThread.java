package rsa.worker;

import java.io.File;
import java.io.FileNotFoundException;

public class RsaThread extends Thread{
    String operation;
    String suite;
    File inFile;
    File outFile;
    File keyFile;

    public RsaThread(String operation, String suite, String inFilePath, String outFilePath, String keyFilePath) throws FileNotFoundException, IllegalArgumentException {
        ValidateAndStoreParams(suite, operation, inFilePath, outFilePath, keyFilePath);
    }

    @Override
    public void run() {
        // TODO the logic of encrypting / decrypting / generating a key
    }

    private void ValidateAndStoreParams(String suite, String operation, String inFile, String outFile, String keyFile){
        // TODO a function that will check the validity of the params provided and store them in the fields
    }
}

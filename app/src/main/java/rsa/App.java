package rsa;

import rsa.util.Utils;
import rsa.worker.RsaThread;

import java.io.FileNotFoundException;
import java.util.Map;

public class App {
    static Thread.UncaughtExceptionHandler threadExceptionHandler = (t, e) -> System.err.println(e.getLocalizedMessage());
    public static void main(String[] args) {
        String suite, inFile, operation, outFile, keyFile;
        int keyLength = -1;

        try {
            Map<String, String> arguments = Utils.parseArguments(args);
            if (!arguments.containsKey("operation") || !arguments.containsKey("keyFile")) {
                showUsage();
                return;
            }
            operation = arguments.get("operation");
            keyFile = arguments.get("keyFile");
            outFile = arguments.get("outFile");
            inFile = arguments.get("inFile");
            suite = arguments.get("suite");
            if(arguments.containsKey("keyLength")){
                keyLength = Integer.parseInt(arguments.get("keyLength"));
            }
        } catch (Exception ex) {
            System.out.println("Error parsing arguments: " + ex.getMessage());
            showUsage();
            return;
        }
        try{
            RsaThread rsaThread = new RsaThread(operation, suite, inFile, outFile, keyFile, keyLength);
            rsaThread.setUncaughtExceptionHandler(threadExceptionHandler);
            rsaThread.start();
            rsaThread.join();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public static void showUsage() {
        System.out.println("Usage: RSAJavaCmd -operation=op -suite=suite -inFile=path/to/file -outFile=path/to/file -keyFile=path/to/file -keyLength=length\n" +
                "operation can be generateKey, encrypt, decrypt\n" +
                "keyLength is only required for generateKey operation\nsuite, inFile, outFile are only required for encrypt or decrypt");
    }
}

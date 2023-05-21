package rsa;

import rsa.util.Utils;

import java.util.Map;

public class App {
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

            // TODO create and execute a RsaThread with the arguments provided

        } catch (Exception ex) {
            System.out.println("Error parsing arguments: " + ex.getMessage());
            showUsage();
            return;
        }
    }

    public static void showUsage() {
        System.out.println("Usage: RSAJavaCmd -operation=op -suite=suite -inFile=path/to/file -outFile=path/to/file -keyFile=path/to/file -keyLength=length\n" +
                "operation can be generateKey, encrypt, decrypt\n" +
                "keyLength is only required for generateKey operation\nsuite, inFile, outFile are only required for encrypt or decrypt");
    }
}

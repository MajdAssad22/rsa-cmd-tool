package rsa.util;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, String> parseArguments(String[] args) {

        HashMap<String, String> properties = new HashMap<>();
        for (String part : args) {
            try {
                // parse this by =
                String[] parts = part.split("=");
                // put the part after the - in the key value
                properties.put(parts[0].substring(parts[0].indexOf("-") + 1), parts[1]);
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                throw new IllegalArgumentException("Parameter provided is not in the correct format: " + part);
            }
        }
        return properties;
    }

    /**
     * Saves a key provided in a file.
     *
     * @param keyFile    The output file
     * @param privateKey The private key
     * @param publicKey  The public key
     * @return True if the file has been successfully output.  False if the file fails.
     */
    public static boolean saveKey(String keyFile, RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        // let the user choose where to store the keys
        File selectedFile = new File(keyFile);

        // we have a file to write to
        try (PrintWriter pwOut = new PrintWriter(selectedFile)) {
            pwOut.println("Public Key");
            pwOut.println(ByteManipulation.bytesToHex(publicKey.getEncoded()));
            pwOut.println("Private Key");
            pwOut.println(ByteManipulation.bytesToHex(privateKey.getEncoded()));
            return true;
        } catch (IOException e) {
            System.out.println("Can't output the key: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrives the keys from the key file.
     *
     * @param keyFile The key file to read from
     * @return A KeyPair with the public and private key if the load succeeds. null otherwise.
     */
    public static KeyPair loadKey(String keyFile) {
        // let the user select where to load the key from
        File selectedFile = new File(keyFile);
        if (selectedFile.exists() && selectedFile.canRead()) {
            byte[] publicKeyBytes = null;
            byte[] privateKeyBytes = null;
            try (BufferedReader brIn = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile)))) {
                String line;
                brIn.readLine(); // ignore the first line
                line = brIn.readLine(); // this is the public key
                publicKeyBytes = ByteManipulation.hexToBytes(line);
                brIn.readLine(); // ignore the third line
                line = brIn.readLine(); // this is the private key
                privateKeyBytes = ByteManipulation.hexToBytes(line);
            } catch (FileNotFoundException e) {
                System.out.println("Can't find the key file: " + e.getMessage());
                return null;
            } catch (IOException e) {
                System.out.println("Can't read the key file: " + e.getMessage());
                return null;
            }

            // convert to a key
            try {
                X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes);
                PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);
                RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
                KeyPair pair = new KeyPair(publicKey, privateKey);
                return pair;
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                System.out.println("Parsing the key failed");
                return null;
            }
        } else {
            return null;
        }
    }
}

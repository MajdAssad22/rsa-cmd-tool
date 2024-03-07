# RSA CMD Tool
In this project we created a command like tool which is able to encrypt and decrypt a file according to the keyFile and generate keys according settings provided.

### Must Provide:
* Operation (encrypt, decrypt, generateKey)
* KeyFile (must be the file where the keys are stored)

### Settings For Each Operation:
* **encrypt / decrypt:**
  * inFile: The file to read the data from.
  * outFile: The file to write the encrypted data to.
  * suite: The suite to use.

* **generateKey:**
  * keyLength: The length of the key to generate.

## Key Features
- Encrypt / decrypt files 
- Generate keys

## Technologies Used
- Java
- Gradle

## Project Structure
- The code is located in 'app/src/main/java/rsa' folder.
- There are two folders worker and util, the worker folder includes the actual encryption/decryption process while the util folder include key and convertion functions.

## **How To Use:**
Run the jar or code with the following program arguments according to your desired functionality.
<br/>

* #### **To run the code:**
  Must provide the program arguments when trying to execute the code, if your using Intellij you can easily add them in the configurations.
  <br/>
  <br/>

* #### **To run the jar:**
  Run the jar file like this:
    ```
    java -jar RsaTool.jar {options}
    ```

### **Available Options:**
**-operation:** Must be (encrypt, decrypt, generateKey).
<br/>
**-keyFile:** Must be the file that the keys are stored or will be stored.
<br/>
**-inFile:** ``[Only in encrypt / decrpyt]`` Path to the file to encrypt or decrypt.
<br/>
**-outFile:** ``[Only in encrypt / decrpyt]`` Path to the file store the encrypted or decrypted data.
<br/>
**-suite:** ``[Only in encrypt / decrpyt]`` The suite to use must be RSA/ECB/OAEPWithSHA-256AndMGF1Padding or RSA/ECB/PKCS1Padding.
<br/>
**-keyLength:** ``[Only in generateKey]`` The length of the key to generate must be a proper key length.
<br/>

*  Example for key generation:
    ```
   -operation=generateKey
   -keyFile=Experiment1-Key.txt 
   -keyLength=1024
    ```

*  Example for encrypting or decrypting:
    ```
   -operation=encrypt
   -keyFile=Experiment1-Key.txt
   -inFile=smallTestFile1.txt
   -outFile=encryptedFile1.txt
   -suite=RSA/ECB/PKCS1Padding
    ```  

## Acknowledgments
External libraries:
- crypto

Contributers:
- Majd Assad
- Mohamed Sayed Ahmed

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

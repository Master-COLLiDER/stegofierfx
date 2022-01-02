package com.mastercollider.stegofierfx.CLI;

import com.mastercollider.stegofierfx.Color.ColorChannel;
import com.mastercollider.stegofierfx.Decoder.Decoder;
import com.mastercollider.stegofierfx.Encoder.Encoder;
import com.mastercollider.stegofierfx.Encryption.EncryptionType;
import com.mastercollider.stegofierfx.Encryption.RSA.RSA;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CLIExecutor {

    public static void encode(@NotNull EncodeCLIParameters encodeArgs) throws Exception {


        System.out.println("Message to encode: \n" + encodeArgs.message);
        System.out.println("Cover file name: \n" + encodeArgs.coverImageFile);
        System.out.println("Output file name: \n" + encodeArgs.outputImageFile);


        try {
            Encoder encoder = new Encoder(encodeArgs.coverImageFile.toString(),
                    encodeArgs.outputImageFile.toString(),
                    encodeArgs.message,
                    EncryptionType.getEncryptionType(encodeArgs.encryptionType),
                    ColorChannel.getColorChannel(encodeArgs.colorChannel),
                    encodeArgs.noOfLSB);
            encoder.Encode();
            encoder.saveImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String decode(@NotNull DecodeCLIParameters decodeArgs) throws Exception {

        String message = "";

        Decoder decoder = new Decoder(decodeArgs.coverImageFile.toString());
        decoder.Decode();


        message = decoder.getMessage();

        if (decodeArgs.outputFile != null) {
            File oFile = new File(decodeArgs.outputFile.toString());
            if (oFile.exists()) oFile.delete();
            oFile.createNewFile();
            FileWriter myWriter = new FileWriter(oFile);
            myWriter.write(message);
            myWriter.close();
            System.out.println("Successfully printed the result to " + decodeArgs.outputFile);
        }
        return message;
    }
}

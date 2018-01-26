package com.vehiclereader;

import com.google.zxing.NotFoundException;
import com.vehiclereader.registrationInfoDecoder.AztecDecoder;
import com.vehiclereader.registrationInfoDecoder.RegistrationInfo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, NotFoundException {
        String filePath = "Y5Vt7.png";
//        String uriFile = "https://i.stack.imgur.com/Y5Vt7.png";

        ImageBarcodeReader imageBarcodeReader = new ImageBarcodeReader();
        imageBarcodeReader.readAztecCodeFromFile(filePath);
        String readImage=imageBarcodeReader.readAztecCode();
        System.out.println("Data read from Aztec by filepath: ");
        System.out.println(readImage);

//        imageBarcodeReader.readAztecCodeFromUri(uriFile);
//        System.out.println("Data read from Aztec by uri: "
//                + imageBarcodeReader.readAztecCode());

        try {
            RegistrationInfo info = AztecDecoder.Decode(readImage);

            JAXBContext context = JAXBContext.newInstance(RegistrationInfo.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(info, System.out);

        } catch(Exception ex) {
            System.out.println("Error: Can't decode text, ensure that it is correctly scanned.");
        }

    }
}

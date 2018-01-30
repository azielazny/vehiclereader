package com.vehiclereader;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehiclereader.registrationInfoDecoder.AztecDecoder;
import com.vehiclereader.registrationInfoDecoder.AztecDecodingException;
import com.vehiclereader.registrationInfoDecoder.RegistrationInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Path("/car-registration-info")
@ApplicationScoped
public class RegistrationInfoWebservice {

    final private String ApiKey = "klucz123";

    @Inject
    RegistrationInfo registrationInfo;

    @Inject
    ImageBarcodeReader imageBarcodeReader;

    public String getApiKey() {
        return ApiKey;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response decodeRegistrationInfo(@FormDataParam("image") InputStream fileInputStream,
                                           @FormDataParam("image") FormDataContentDisposition fileMetaData,
                                           @FormDataParam("key") String key,
                                           @FormDataParam("command") String command,
                                           @FormDataParam("text") String text) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        if (!key.equals(getApiKey()))
            return Response.ok(mapper.writeValueAsString(Collections.singletonMap("Error1", "Błędna identyfikacja Klienta"))).build();
        RegistrationInfo info;
        try {
            if (command.equals("decodeFromImage")) {
                info = decodeRegistrationInfoFromImage(fileInputStream);
                return Response.ok(mapper.writeValueAsString(info)).build();
            }
            if (command.equals("decodeFromText")) {
                info = decodeRegistrationInfoFromText(text);
                return Response.ok(mapper.writeValueAsString(info)).build();
            }
        } catch (IllegalArgumentException ex) {
            return Response.ok(mapper.writeValueAsString(Collections.singletonMap("Error4", "Brak pliku"))).build();
        } catch (AztecDecodingException ex) {
            return Response.ok(mapper.writeValueAsString(Collections.singletonMap("Error5", "Nie rozpoznaliśmy kodu"))).build();
        } catch (Exception ex) {
            return null;
        }
        return Response.ok(mapper.writeValueAsString(Collections.singletonMap("Error3", "Nierozpoznana komanda"))).build();
    }

    private RegistrationInfo decodeRegistrationInfoFromImage(InputStream fileInputStream) throws Exception {

        BufferedImage bufferedImage = ImageIO.read(fileInputStream);
        imageBarcodeReader.setAztecFile(bufferedImage);
        String readImage = imageBarcodeReader.readAztecCode();

        return decodeRegistrationInfoFromText(readImage);
    }

    private RegistrationInfo decodeRegistrationInfoFromText(String textToDecode) throws Exception {
        return AztecDecoder.Decode(textToDecode);
    }


    @GET
    @Path("{fileId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRegistrationInfo(@PathParam("fileId") String fileId) throws IOException, NotFoundException, com.google.zxing.NotFoundException {
        String readImage = barcodeReader(fileId);
        barcodeDecoder(readImage);

        ObjectMapper mapper = new ObjectMapper();
        return Response.ok(mapper.writeValueAsString(registrationInfo)).build();
    }

    private void barcodeDecoder(String readImage) {
        try {
            registrationInfo = AztecDecoder.Decode(readImage);
        } catch (Exception ex) {
            System.out.println("Error: Can't decode text, ensure that it is correctly scanned.");
        }
    }

    private String barcodeReader(String fileId) throws IOException, NotFoundException, com.google.zxing.NotFoundException {
        imageBarcodeReader.readAztecCodeFromFile(fileId);
        return imageBarcodeReader.readAztecCode();
    }


}

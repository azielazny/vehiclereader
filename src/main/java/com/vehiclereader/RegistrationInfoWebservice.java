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

    @Inject
    RegistrationInfo registrationInfo;

    @Inject
    ImageBarcodeReader imageBarcodeReader;


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response decodeRegistrationInfoFromImage(@FormDataParam("image") InputStream fileInputStream,
                                  @FormDataParam("image") FormDataContentDisposition fileMetaData,
                                  @FormDataParam("key") String key) throws Exception {
        RegistrationInfo info;
        ObjectMapper mapper = new ObjectMapper();
        try {
            BufferedImage bufferedImage = ImageIO.read(fileInputStream);
            imageBarcodeReader.setAztecFile(bufferedImage);
            String readImage = imageBarcodeReader.readAztecCode();

            info = AztecDecoder.Decode(readImage);
        } catch (IllegalArgumentException ex) {
            return Response.ok(mapper.writeValueAsString(Collections.singletonMap("Error", "Brak pliku"))).build();
        } catch (AztecDecodingException ex) {
            return Response.ok(mapper.writeValueAsString(Collections.singletonMap("Error", "Nie znaleźliśmy kodu"))).build();
        }
        return Response.ok(mapper.writeValueAsString(info)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRegistrationInfo() {
        return Response.ok("ddddd").build();
    }

    @GET
    @Path("{fileId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRegistrationInfo(@PathParam("fileId") String fileId) throws IOException, NotFoundException, com.google.zxing.NotFoundException {
        String readImage = barcodeReader(fileId);

        barcodeDecoder(readImage);
//        Response result = Response.
//                status(200).
//                entity(info).
//                type("application/json").
//                build();
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

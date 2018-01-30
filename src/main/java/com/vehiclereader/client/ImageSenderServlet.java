package com.vehiclereader.client;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;

@WebServlet(urlPatterns = {"/imagesender"})
@MultipartConfig
public class ImageSenderServlet extends HttpServlet {

    public static byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result="";
        if (req.getPart("fileFromDisc") != null) {
            Part filePart = req.getPart("fileFromDisc");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();
            byte[] bytes = readFully(fileContent);
            result = new ClientSimulation("klucz123").decodeImageFromWebForm(bytes, fileName).toString();

        }
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.append(result);
        out.close();

    }

}
package com.vehiclereader.client;

import sun.misc.BASE64Decoder;

import javax.servlet.RequestDispatcher;
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
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Random;


@WebServlet(urlPatterns = {"/webinterface"})
@MultipartConfig
public class WebInterfaceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
        String decodedAztecText = "";
        String fileName = "";
        String result="";
        if (req.getParameter("fromFile") != null) {//file
            Part filePart = req.getPart("fileFromDisc");
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();
            byte[] bytes = readFully(fileContent);
            result = new ClientSimulation("klucz123").decodeImageFromWebForm(bytes, fileName).toString();

        } else if(req.getParameter("fromTextarea") != null) {//textarea
            decodedAztecText=req.getParameter("base64ToDecode");
            result = new ClientSimulation("klucz123").decodeText(decodedAztecText).toString();
        } else {//camera
            String data = "";
            try {
                StringBuffer buffer = new StringBuffer();
                Reader reader = req.getReader();
                int current;

                while ((current = reader.read()) >= 0)
                    buffer.append((char) current);

                data = new String(buffer);
                data = data.substring(data.indexOf(",") + 1);

                System.out.println("PNG image data on Base64: " + data);

                String newFileName = new Random().nextInt(100000) + ".png";
//                FileOutputStream output = new FileOutputStream(new File("/D:/repositorio/" + newFileName));
                byte[] base64DecodedData = new BASE64Decoder().decodeBuffer(data);
//                output.write(base64DecodedData);
//
//                output.flush();
//                output.close();
                result = new ClientSimulation("klucz123").decodeImageFromWebForm(base64DecodedData, newFileName).toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        req.setAttribute("uploadedFile", fileName);
        req.setAttribute("uploadedText", decodedAztecText);
        req.setAttribute("result", result);
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);

    }
}
package com.vehiclereader.client;

import sun.misc.BASE64Decoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;


@WebServlet(urlPatterns = {"/webinterface"})
@MultipartConfig
public class WebInterfaceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String decodedAztecText = "";
        String fileName = "";
        String result="";

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
                byte[] base64DecodedData = new BASE64Decoder().decodeBuffer(data);
//                FileOutputStream output = new FileOutputStream(new File("/D:/repositorio/" + newFileName));
//                output.write(base64DecodedData);
//                output.flush();
//                output.close();
                result = new ClientSimulation("klucz123").decodeImageFromWebForm(base64DecodedData, newFileName).toString();

            } catch (Exception e) {
                e.printStackTrace();
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
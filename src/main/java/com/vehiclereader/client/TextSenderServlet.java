package com.vehiclereader.client;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/textsender"})
public class TextSenderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result="";
        if(req.getParameter("base64ToDecode") != null) {
            String decodedAztecText = req.getParameter("base64ToDecode");
            result = new ClientSimulation("klucz123").decodeText(decodedAztecText).toString();
        } else
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.append(result);
        out.close();

    }

}
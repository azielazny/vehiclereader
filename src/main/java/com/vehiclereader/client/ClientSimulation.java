package com.vehiclereader.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientSimulation {

    private static final String API_URL = "http://localhost:8080/rest/car-registration-info/";
    private byte[] byteImage;

    private String ApiKey = "klucz123";

    public ClientSimulation(String ApiKey) {
        this.ApiKey = ApiKey;
    }

    public JSONObject decodeText(String Text) {
        Map<String, String> Params = new HashMap<>();

        Params.put("command", "decodeFromText");
        Params.put("text", Text);

        return this.postRequest(Params);
    }

    public JSONObject decodeTextFromFile(String TextFilePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(TextFilePath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
                line = br.readLine();
            }
            String Text = sb.toString();

            return this.decodeText(Text);
        } catch (Exception ex) {
            return null;
        }
    }

    public JSONObject decodeImageFromFile(String ImageFilePath) {
        Map<String, String> Params = new HashMap<>();

        Params.put("command", "decodeFromImage");
        Params.put("image", ImageFilePath);

        return this.postRequest(Params);
    }

    public JSONObject decodeImageFromWebForm(byte[] ImageFile, String ImageFilePath) {
        Map<String, String> Params = new HashMap<>();
        byteImage = ImageFile;

        Params.put("command", "decodeFromImage");
        Params.put("imageByte", ImageFilePath);

        return this.postRequest(Params);
    }

    private JSONObject postRequest(Map<String, String> ParamsArray) {
        if (this.ApiKey == null || this.ApiKey.length() == 0) {
            return null;
        }

        ParamsArray.put("key", this.ApiKey);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        try {
            CloseableHttpClient client;
            client = HttpClients.createDefault();


            HttpPost httpPost = new HttpPost(API_URL);

            if (ParamsArray.containsKey("image")) {
                File imageFile = new File(ParamsArray.get("image"));
                builder.addBinaryBody("image", imageFile, ContentType.APPLICATION_OCTET_STREAM, imageFile.getName());
                ParamsArray.remove("image");
            }
            if (ParamsArray.containsKey("imageByte")) {
                builder.addBinaryBody("image", byteImage, ContentType.APPLICATION_OCTET_STREAM, ParamsArray.get("imageByte"));
                ParamsArray.remove("image");
                byteImage=null;
            }


            for (Map.Entry<String, String> paramArray : ParamsArray.entrySet()) {
                builder.addTextBody(paramArray.getKey(), paramArray.getValue());
            }
            httpPost.setEntity(builder.build());

            CloseableHttpResponse response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code != 200) {
                client.close();
                return null;
            }

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null) {
                client.close();
                return null;
            }
            String json = EntityUtils.toString(responseEntity);
            JSONObject array = new JSONObject(json);
            client.close();
            return array;
        } catch (Exception ex) {
            return new JSONObject(Collections.singletonMap("Error", ex));
        }

    }

}

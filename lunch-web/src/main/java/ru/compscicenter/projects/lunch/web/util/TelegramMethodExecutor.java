package ru.compscicenter.projects.lunch.web.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Logger;

public class TelegramMethodExecutor {
    private static Logger logger = Logger.getLogger(TelegramMethodExecutor.class.getName());

    private TelegramMethodExecutor() {
    }


    public static String doMethod(final String methodName, final Map<String, String> params, final String token) {
        final String host = "api.telegram.org";
        final String path = "/bot" + token + "/" + methodName;

        logger.info("Method = " + methodName + ", params = " + params);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URIBuilder urlBuilder = new URIBuilder();
            urlBuilder.setScheme("https");
            urlBuilder.setHost(host).setPath(path);

            for (Map.Entry<String, String> param : params.entrySet()) {
                urlBuilder.addParameter(param.getKey(), param.getValue());
            }

            HttpGet post = new HttpGet(urlBuilder.build());
            return handleResponse(httpClient.execute(post));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String handleResponse(final CloseableHttpResponse response) {
        try (InputStream stream = response.getEntity().getContent()) {
            StringBuilder stringBuilder = new StringBuilder();

            while (stream.available() > 0) {
                stringBuilder.append((char) stream.read());
            }
            stream.close();
            logger.info("Response: " + stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

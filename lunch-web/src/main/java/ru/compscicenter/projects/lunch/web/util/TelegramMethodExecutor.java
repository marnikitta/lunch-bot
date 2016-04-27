package ru.compscicenter.projects.lunch.web.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

public final class TelegramMethodExecutor {
    private final static String HOST = "api.telegram.org";

    private static Logger logger = LoggerFactory.getLogger(TelegramMethodExecutor.class);

    private TelegramMethodExecutor() {
    }

    public static String doMethod(final String methodName, final Map<String, String> params, final String token) throws IOException {
        final String path = "/bot" + token + "/" + methodName;

        logger.debug("Method = {}, params = {}", methodName, params);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URIBuilder urlBuilder = new URIBuilder();
            urlBuilder.setScheme("https");
            urlBuilder.setHost(HOST).setPath(path);

            for (Map.Entry<String, String> param : params.entrySet()) {
                urlBuilder.addParameter(param.getKey(), param.getValue());
            }

            HttpGet post = new HttpGet(urlBuilder.build());
            return handleResponse(httpClient.execute(post));
        } catch (IOException | URISyntaxException e) {
            logger.error("Error execution method", e);
            throw new IOException(e);
        }
    }

    private static String handleResponse(final CloseableHttpResponse response) throws IOException {
        try (InputStream stream = response.getEntity().getContent()) {
            StringBuilder stringBuilder = new StringBuilder();

            while (stream.available() > 0) {
                stringBuilder.append((char) stream.read());
            }
            stream.close();
            logger.debug("Response: " + stringBuilder.toString());

            return stringBuilder.toString();
        } catch (IOException e) {
            logger.error("Error handling response", e);
            throw new IOException(e);
        }
    }
}

package Webhook;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class webhookSender {
    public static void sendToWebhook(String webhookUrl, String message) {
        try {

            URL url = new URL(webhookUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String payload = "{\"content\":\"" + message + "\"}";

            OutputStream outputStream = connection.getOutputStream();

            outputStream.write(payload.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Message sent to Discord webhook successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

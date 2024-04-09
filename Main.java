import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo! Meu nome é PepeIT, estou aqui para te auxiliar. Em que posso te ajudar hoje?  Digite 'exit' para sair.");

        while (true) {
            System.out.print("Usuário: ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Saindo do ChatGPT. Até mais!");
                break;
            }

            String response = chatGPT(userInput);
            System.out.println("PepeIT: " + response);
        }

        scanner.close();
    }

    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = ""; // Substitua pelo seu API Key
        String model = "gpt-3.5-turbo"; // Modelo atual da API ChatGPT

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Construa o corpo da requisição com a codificação UTF-8
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";

            try (OutputStream os = con.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            // Verifique o código de resposta HTTP
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Leia a resposta usando InputStreamReader configurado com UTF-8
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // Retorna o conteúdo extraído da resposta
                    return extractContentFromResponse(response.toString());
                }
            } else {
                // Trate a resposta de erro, se necessário
                System.out.println("Erro na solicitação. Código de resposta: " + responseCode);
                return null;
            }

        } catch (IOException e) {
            // Trate exceções de E/S
            e.printStackTrace();
            return null;
        }
    }

    // Este método extrai a resposta esperada do ChatGPT e a retorna.
    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content") + 11; // Marcador onde o conteúdo começa.
        int endMarker = response.indexOf("\"", startMarker); // Marcador onde o conteúdo termina.
        return response.substring(startMarker, endMarker); // Retorna a substring contendo apenas a resposta.
    }
}

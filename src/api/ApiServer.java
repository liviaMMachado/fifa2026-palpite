package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApiServer {

    private static final int PORTA = 8080;

    public static void main(String[] args) throws IOException {
        DadosIniciais.carregarPartidasDoDia();

        Path frontendDir = Paths.get("frontend").toAbsolutePath().normalize();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORTA), 0);

        server.createContext("/api/partidas", new PartidasHandler());
        server.createContext("/api/palpites", new PalpitesHandler());
        server.createContext("/", new StaticFileHandler(frontendDir));

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor iniciado em http://localhost:" + PORTA);
        System.out.println("Frontend disponivel em http://localhost:" + PORTA + "/");
        System.out.println("API de partidas: http://localhost:" + PORTA + "/api/partidas");
    }

    public static void adicionarCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    public static void enviarErro(HttpExchange exchange, int codigo, String mensagem) throws IOException {
        String json = "{\"erro\":\"" + JsonUtil.escapar(mensagem) + "\"}";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(codigo, bytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }
}

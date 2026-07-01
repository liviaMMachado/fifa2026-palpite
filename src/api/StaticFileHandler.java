package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticFileHandler implements HttpHandler {

    private final Path frontendDir;

    public StaticFileHandler(Path frontendDir) {
        this.frontendDir = frontendDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ApiServer.adicionarCors(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String caminho = exchange.getRequestURI().getPath();
        if ("/".equals(caminho)) {
            caminho = "/index.html";
        }

        Path arquivo = frontendDir.resolve(caminho.substring(1)).normalize();
        if (!arquivo.startsWith(frontendDir) || !Files.exists(arquivo) || Files.isDirectory(arquivo)) {
            ApiServer.enviarErro(exchange, 404, "Arquivo nao encontrado");
            return;
        }

        byte[] bytes = Files.readAllBytes(arquivo);
        String contentType = obterContentType(arquivo.getFileName().toString());

        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }

    private String obterContentType(String nomeArquivo) {
        if (nomeArquivo.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (nomeArquivo.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (nomeArquivo.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        if (nomeArquivo.endsWith(".png")) {
            return "image/png";
        }
        if (nomeArquivo.endsWith(".jpg") || nomeArquivo.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (nomeArquivo.endsWith(".svg")) {
            return "image/svg+xml";
        }
        return "application/octet-stream";
    }
}

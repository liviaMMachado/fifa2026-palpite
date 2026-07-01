package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import singleton.GerenciadorPartidas;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PartidasHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ApiServer.adicionarCors(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            ApiServer.enviarErro(exchange, 405, "Metodo nao permitido");
            return;
        }

        String json = JsonUtil.partidasParaJson(GerenciadorPartidas.getInstance().listarPartidas());
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }
}

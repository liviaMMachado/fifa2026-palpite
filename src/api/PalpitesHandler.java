package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Palpite;
import model.Partida;
import observer.NotificadorPalpite;
import observer.UsuarioObservador;
import singleton.GerenciadorPartidas;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PalpitesHandler implements HttpHandler {

    private static final NotificadorPalpite NOTIFICADOR = criarNotificador();

    private static NotificadorPalpite criarNotificador() {
        NotificadorPalpite notificador = new NotificadorPalpite();
        notificador.adicionarObservador(new UsuarioObservador());
        return notificador;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ApiServer.adicionarCors(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            ApiServer.enviarErro(exchange, 405, "Metodo nao permitido");
            return;
        }

        String body = lerCorpo(exchange);
        Integer partidaId = extrairInteiro(body, "partidaId");
        Integer golsCasa = extrairInteiro(body, "golsCasa");
        Integer golsVisitante = extrairInteiro(body, "golsVisitante");

        if (partidaId == null || golsCasa == null || golsVisitante == null) {
            ApiServer.enviarErro(exchange, 400, "Dados invalidos");
            return;
        }

        List<Partida> partidas = GerenciadorPartidas.getInstance().listarPartidas();
        if (partidaId < 0 || partidaId >= partidas.size()) {
            ApiServer.enviarErro(exchange, 404, "Partida nao encontrada");
            return;
        }

        if (golsCasa < 0 || golsCasa > 99 || golsVisitante < 0 || golsVisitante > 99) {
            ApiServer.enviarErro(exchange, 400, "Placar invalido");
            return;
        }

        Partida partida = partidas.get(partidaId);
        Palpite palpite = new Palpite(partida, golsCasa, golsVisitante);
        NOTIFICADOR.notificar("Palpite enviado!\nSeu palpite: " + palpite);

        String json = "{\"mensagem\":\"Palpite enviado com sucesso!\",\"palpite\":\"" +
                JsonUtil.escapar(palpite.toString()) + "\"}";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(201, bytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }

    private String lerCorpo(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private Integer extrairInteiro(String json, String campo) {
        Pattern pattern = Pattern.compile("\"" + campo + "\"\\s*:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return null;
        }
        return Integer.parseInt(matcher.group(1));
    }
}

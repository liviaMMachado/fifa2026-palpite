package api;

import model.Partida;

import java.util.List;

public class JsonUtil {

    public static String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static String partidasParaJson(List<Partida> partidas) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < partidas.size(); i++) {
            Partida partida = partidas.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{")
                    .append("\"id\":").append(i).append(",")
                    .append("\"grupo\":\"").append(escapar(partida.getGrupo())).append("\",")
                    .append("\"timeCasa\":\"").append(escapar(partida.getTimeCasa())).append("\",")
                    .append("\"timeVisitante\":\"").append(escapar(partida.getTimeVisitante())).append("\",")
                    .append("\"horario\":\"").append(escapar(partida.getHorario())).append("\",")
                    .append("\"data\":\"23/06/2026\"")
                    .append("}");
        }
        json.append("]");
        return json.toString();
    }
}

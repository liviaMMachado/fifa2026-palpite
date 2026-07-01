package api;

import model.PartidaBuilder;
import singleton.GerenciadorPartidas;

public class DadosIniciais {

    public static void carregarPartidasDoDia() {
        GerenciadorPartidas gerenciador = GerenciadorPartidas.getInstance();

        if (!gerenciador.listarPartidas().isEmpty()) {
            return;
        }

        gerenciador.adicionarPartida(new PartidaBuilder().grupo("Grupo J").timeCasa("Jordânia").timeVisitante("Argélia").horario("00:00").build());
        gerenciador.adicionarPartida(new PartidaBuilder().grupo("Grupo K").timeCasa("Portugal").timeVisitante("Uzbequistão").horario("14:00").build());
        gerenciador.adicionarPartida(new PartidaBuilder().grupo("Grupo L").timeCasa("Inglaterra").timeVisitante("Gana").horario("17:00").build());
        gerenciador.adicionarPartida(new PartidaBuilder().grupo("Grupo L").timeCasa("Panamá").timeVisitante("Croácia").horario("20:00").build());
        gerenciador.adicionarPartida(new PartidaBuilder().grupo("Grupo K").timeCasa("Colômbia").timeVisitante("RD Congo").horario("23:00").build());
    }
}

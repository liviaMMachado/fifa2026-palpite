package adapter;

import model.Partida;

public class ExibicaoPartidaAdapter implements ExibicaoPartida {
    private Partida partida;
    private PartidaView view;

    public ExibicaoPartidaAdapter(Partida partida){
        this.partida = partida;
        this.view = new PartidaView();
    }

    public void exibir(){
        view.mostrarPartida(partida);
    }
}
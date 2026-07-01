package adapter;

import model.Partida;

public class PartidaView {
    public void mostrarPartida(Partida partida){
        System.out.println(partida.getTimeCasa() + " x " + partida.getTimeVisitante() + " - " + partida.getHorario());
    }
}
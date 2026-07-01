package model;

public class Palpite {
    private Partida partida;
    private int golsCasa;
    private int golsVisitante;

    public Palpite(Partida partida, int golsCasa, int golsVisitante){
        this.partida = partida;
        this.golsCasa = golsCasa;
        this.golsVisitante = golsVisitante;
    }

    public String toString(){
        return partida.getTimeCasa() + " " + golsCasa + " x " + golsVisitante + " " + partida.getTimeVisitante();
    }
}
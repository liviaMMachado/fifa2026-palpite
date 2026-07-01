package model;

public class PartidaBuilder {
    private String grupo;
    private String timeCasa;
    private String timeVisitante;
    private String horario;

    public PartidaBuilder grupo(String grupo){ this.grupo = grupo; return this; }
    public PartidaBuilder timeCasa(String timeCasa){ this.timeCasa = timeCasa; return this; }
    public PartidaBuilder timeVisitante(String timeVisitante){ this.timeVisitante = timeVisitante; return this; }
    public PartidaBuilder horario(String horario){ this.horario = horario; return this; }

    public Partida build(){
        return new Partida(grupo, timeCasa, timeVisitante, horario);
    }
}
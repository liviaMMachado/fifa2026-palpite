package model;

public class Partida {
    private String grupo;
    private String timeCasa;
    private String timeVisitante;
    private String horario;

    public Partida(String grupo, String timeCasa, String timeVisitante, String horario) {
        this.grupo = grupo;
        this.timeCasa = timeCasa;
        this.timeVisitante = timeVisitante;
        this.horario = horario;
    }

    public String getGrupo() { return grupo; }
    public String getTimeCasa() { return timeCasa; }
    public String getTimeVisitante() { return timeVisitante; }
    public String getHorario() { return horario; }
}
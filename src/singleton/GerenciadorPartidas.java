package singleton;

import java.util.ArrayList;
import java.util.List;
import model.Partida;

public class GerenciadorPartidas {
    private static GerenciadorPartidas instancia;
    private List<Partida> partidas;

    private GerenciadorPartidas(){
        partidas = new ArrayList<>();
    }

    public static GerenciadorPartidas getInstance(){
        if(instancia == null){
            instancia = new GerenciadorPartidas();
        }
        return instancia;
    }

    public void adicionarPartida(Partida partida){ partidas.add(partida); }
    public List<Partida> listarPartidas(){ return partidas; }
}
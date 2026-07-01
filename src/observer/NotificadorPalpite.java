package observer;

import java.util.ArrayList;
import java.util.List;

public class NotificadorPalpite {
    private List<ObservadorPalpite> observadores = new ArrayList<>();

    public void adicionarObservador(ObservadorPalpite obs){
        observadores.add(obs);
    }

    public void notificar(String mensagem){
        for(ObservadorPalpite obs : observadores){
            obs.atualizar(mensagem);
        }
    }
}
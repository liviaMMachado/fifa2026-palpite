package observer;

public class UsuarioObservador implements ObservadorPalpite {
    public void atualizar(String mensagem){
        System.out.println(mensagem);
    }
}
import java.util.Scanner;

import api.DadosIniciais;
import model.Partida;
import model.Palpite;
import observer.NotificadorPalpite;
import observer.UsuarioObservador;
import singleton.GerenciadorPartidas;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        DadosIniciais.carregarPartidasDoDia();
        GerenciadorPartidas gerenciador = GerenciadorPartidas.getInstance();

        NotificadorPalpite notificador = new NotificadorPalpite();
        notificador.adicionarObservador(new UsuarioObservador());

        int opcao;

        do {
            System.out.println("\n=== JOGOS DO DIA 23/06/2026 ===");

            for (int i = 0; i < gerenciador.listarPartidas().size(); i++) {
                Partida partida = gerenciador.listarPartidas().get(i);
                System.out.println((i + 1) + " - " + partida.getTimeCasa() + " x " + partida.getTimeVisitante());
            }

            System.out.print("\nEscolha uma partida: ");
            int escolha = scanner.nextInt();

            Partida partidaEscolhida = gerenciador.listarPartidas().get(escolha - 1);

            System.out.println("\n" + partidaEscolhida.getTimeCasa() + " x " + partidaEscolhida.getTimeVisitante());

            System.out.print("Digite os gols de " + partidaEscolhida.getTimeCasa() + ": ");
            int golsCasa = scanner.nextInt();

            System.out.print("Digite os gols de " + partidaEscolhida.getTimeVisitante() + ": ");
            int golsVisitante = scanner.nextInt();

            Palpite palpite = new Palpite(partidaEscolhida, golsCasa, golsVisitante);

            notificador.notificar("Palpite enviado!\nSeu palpite: " + palpite);

            System.out.println("\n1 - Fazer outro palpite");
            System.out.println("2 - Sair");
            opcao = scanner.nextInt();

        } while (opcao == 1);

        System.out.println("\nObrigado pelos seus palpites!");
        scanner.close();
    }
}
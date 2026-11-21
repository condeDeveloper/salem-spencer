package br.com.conde.salemspencer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/** As tabelas do README. Cada uma roda sozinha: {@code exec:java -Dexec.args="nome"}. */
public final class Medidor {

    /** Ate onde a tabela de r3 vai no medidor e nos testes. */
    public static final int ATE = 75;

    private static final List<String> TABELAS =
            List.of("maximos", "guloso", "contagem", "poda", "juiz");

    public static void main(String[] argumentos) {
        String tabela = argumentos.length > 0 ? argumentos[0] : "todas";
        if (tabela.equals("todas")) {
            for (String nome : TABELAS) {
                roda(nome);
                System.out.println();
            }
            return;
        }
        roda(tabela);
    }

    private static void roda(String tabela) {
        switch (tabela) {
            case "maximos" -> maximos();
            case "guloso" -> guloso();
            case "contagem" -> contagem();
            case "poda" -> poda();
            case "juiz" -> juiz();
            default -> {
                System.out.println("tabela desconhecida: " + tabela);
                System.out.println("tabelas: " + String.join(", ", TABELAS));
            }
        }
    }

    private static void maximos() {
        titulo("r3(n): o maior subconjunto de {1..n} sem progressao de tres termos");
        long inicio = System.nanoTime();
        Busca.Tabela t = Busca.tabela(ATE, true, true);
        long ms = (System.nanoTime() - inicio) / 1_000_000;
        System.out.println("n   r3  nos          um exemplo");
        for (int n = 1; n <= ATE; n++) {
            boolean subiu = t.r3()[n] > t.r3()[n - 1];
            System.out.printf(Locale.ROOT, "%-3d %-3d %-12s %s%n", n, t.r3()[n],
                    pontos(t.nos()[n]), subiu ? Arrays.toString(t.exemplos().get(n)) : "");
        }
        long soma = 0;
        for (long x : t.nos()) {
            soma += x;
        }
        System.out.println();
        System.out.println("nos ao todo: " + pontos(soma) + " (" + ms + " ms)");
    }

    private static void guloso() {
        titulo("O guloso contra o otimo");
        Busca.Tabela t = Busca.tabela(ATE, true, true);
        System.out.println("n   otimo  guloso  base tres  falta");
        int primeiraPerda = 0;
        int maiorFalta = 0;
        int ondeMaior = 0;
        for (int n = 1; n <= ATE; n++) {
            int g = Guloso.tamanho(n);
            int b = Juiz.gulosoPelaBaseTres(n);
            int falta = t.r3()[n] - g;
            if (falta > 0 && primeiraPerda == 0) {
                primeiraPerda = n;
            }
            if (falta > maiorFalta) {
                maiorFalta = falta;
                ondeMaior = n;
            }
            if (n % 5 == 0 || n == primeiraPerda) {
                System.out.printf(Locale.ROOT, "%-3d %-6d %-7d %-10d %d%s%n", n, t.r3()[n], g, b,
                        falta, g == b ? "" : "  GULOSO != BASE TRES");
            }
        }
        System.out.println();
        System.out.println("primeiro n em que o guloso perde: " + primeiraPerda);
        System.out.println("maior falta ate " + ATE + ": " + maiorFalta + " (primeira vez em n = "
                + ondeMaior + ")");
    }

    private static void contagem() {
        titulo("Quantos subconjuntos atingem o maximo");
        Busca.Tabela t = Busca.tabela(50, true, true);
        System.out.println("n   r3  maximos  quais (ate dois)");
        for (int n = 1; n <= 50; n++) {
            long c = Busca.contaMaximos(n, t.r3());
            StringBuilder quais = new StringBuilder();
            if (c <= 2) {
                for (int[] m : Busca.listaMaximos(n, t.r3())) {
                    quais.append(Arrays.toString(m)).append(' ');
                }
            }
            System.out.printf(Locale.ROOT, "%-3d %-3d %-8d %s%n", n, t.r3()[n], c,
                    quais.toString().trim());
        }
    }

    private static void poda() {
        titulo("Quanto cada ideia corta, uma com a outra fixada");
        System.out.println("nos para montar a tabela de 1 ate n");
        System.out.println();
        System.out.println("n   nada            so a poda       so o truque     os dois");
        for (int n = 20; n <= ATE; n += 5) {
            String nada = n <= 35 ? pontos(somaNos(n, false, false)) : "-";
            String truque = n <= 60 ? pontos(somaNos(n, true, false)) : "-";
            long soPoda = somaNos(n, false, true);
            long dois = somaNos(n, true, true);
            System.out.printf(Locale.ROOT, "%-3d %-15s %-15s %-15s %-15s %s%n", n, nada,
                    pontos(soPoda), truque, pontos(dois), dois < soPoda ? "o truque ajuda" : "o truque atrapalha");
        }
        System.out.println();
        System.out.println("A coluna 'nada' para em 35 e a 'so o truque' em 60: dali em diante");
        System.out.println("cada uma leva minutos.");
    }

    /** Soma dos nos de 1 a n numa configuracao. */
    public static long somaNos(int n, boolean truque, boolean poda) {
        long soma = 0;
        for (long x : Busca.tabela(n, truque, poda).nos()) {
            soma += x;
        }
        return soma;
    }

    private static void juiz() {
        titulo("A busca contra a forca bruta");
        Busca.Tabela t = Busca.tabela(22, true, true);
        System.out.println("n   subconjuntos  r3  maximos  busca");
        for (int n = 1; n <= 22; n++) {
            long[] j = Juiz.maximo(n);
            long c = Busca.contaMaximos(n, t.r3());
            boolean ok = j[0] == t.r3()[n] && j[1] == c;
            System.out.printf(Locale.ROOT, "%-3d %-13s %-3d %-8d %s%n", n, pontos(1L << n),
                    j[0], j[1], ok ? "bate" : "DIVERGE");
        }
    }

    // --------------------------------------------------------------- utilidades

    private static void titulo(String texto) {
        System.out.println("== " + texto);
        System.out.println();
    }

    /** 1234567 vira 1.234.567. */
    public static String pontos(long valor) {
        return String.format(Locale.ROOT, "%,d", valor).replace(',', '.');
    }
}

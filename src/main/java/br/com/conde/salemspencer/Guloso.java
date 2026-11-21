package br.com.conde.salemspencer;

import java.util.ArrayList;
import java.util.List;

/**
 * O guloso: percorre 1, 2, 3, ... e pega todo numero que nao fecha progressao com dois ja
 * pegos. Simulado de verdade, sem a formula de base tres (que fica no {@link Juiz}).
 */
public final class Guloso {

    private Guloso() {}

    /** Os numeros que o guloso pega em {1..n}. */
    public static List<Integer> pega(int n) {
        List<Integer> pegos = new ArrayList<>();
        boolean[] proibido = new boolean[2 * n + 2];
        for (int x = 1; x <= n; x++) {
            if (proibido[x]) {
                continue;
            }
            for (int a : pegos) {
                int terceiro = 2 * x - a;
                if (terceiro < proibido.length) {
                    proibido[terceiro] = true;
                }
            }
            pegos.add(x);
        }
        return pegos;
    }

    /** Quantos o guloso pega em {1..n}. */
    public static int tamanho(int n) {
        return pega(n).size();
    }
}

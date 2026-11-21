package br.com.conde.salemspencer;

import java.util.Arrays;

/**
 * A definicao: um conjunto de inteiros positivos tem uma progressao de tres termos quando
 * contem a, a + d e a + 2d com d maior que zero. Conjunto de Salem-Spencer e o que nao tem.
 */
public final class Progressao {

    private Progressao() {}

    /** Procura a, b, c no conjunto com b - a = c - b, testando todo par contra o meio. */
    public static boolean temTresTermos(int[] conjunto) {
        int[] a = conjunto.clone();
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++) {
            for (int k = i + 1; k < a.length; k++) {
                int soma = a[i] + a[k];
                if (soma % 2 != 0) {
                    continue;
                }
                int meio = soma / 2;
                for (int j = i + 1; j < k; j++) {
                    if (a[j] == meio) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** A primeira progressao achada, ou nulo. Serve para mostrar o defeito. */
    public static int[] primeira(int[] conjunto) {
        int[] a = conjunto.clone();
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                for (int k = j + 1; k < a.length; k++) {
                    if (a[j] - a[i] == a[k] - a[j]) {
                        return new int[] {a[i], a[j], a[k]};
                    }
                }
            }
        }
        return null;
    }
}

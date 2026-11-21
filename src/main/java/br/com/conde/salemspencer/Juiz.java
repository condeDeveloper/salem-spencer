package br.com.conde.salemspencer;

/**
 * A forca bruta: todos os 2^n subconjuntos de {1..n}, cada um passado pela definicao.
 *
 * <p>Nao conhece a tabela de valores menores, nem a ordem da busca, nem o truque de que o
 * maximo novo precisa conter 1 e n. Nao importa nada da {@link Busca}.
 */
public final class Juiz {

    private Juiz() {}

    /** O maior tamanho e quantos subconjuntos o atingem. */
    public static long[] maximo(int n) {
        if (n > 24) {
            throw new IllegalArgumentException("forca bruta so ate 24: " + n);
        }
        int maior = 0;
        long quantos = 1;
        for (long m = 1; m < 1L << n; m++) {
            int t = Long.bitCount(m);
            if (t < maior) {
                continue;
            }
            int[] conjunto = new int[t];
            int k = 0;
            for (int i = 0; i < n; i++) {
                if ((m >>> i & 1) != 0) {
                    conjunto[k++] = i + 1;
                }
            }
            if (Progressao.temTresTermos(conjunto)) {
                continue;
            }
            if (t > maior) {
                maior = t;
                quantos = 0;
            }
            quantos++;
        }
        return new long[] {maior, quantos};
    }

    /**
     * O guloso pela formula: o k-esimo termo da sequencia de Stanley que comeca em 1 e 1
     * mais o numero cuja escrita em base 3 e a de k - 1 em base 2. Entao, em {1..n}, o
     * guloso pega os x tais que x - 1 nao tem o algarismo 2 em base tres.
     */
    public static int gulosoPelaBaseTres(int n) {
        int conta = 0;
        for (int x = 1; x <= n; x++) {
            if (Integer.toString(x - 1, 3).indexOf('2') < 0) {
                conta++;
            }
        }
        return conta;
    }
}

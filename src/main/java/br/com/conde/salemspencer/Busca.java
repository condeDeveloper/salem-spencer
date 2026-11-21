package br.com.conde.salemspencer;

import java.util.ArrayList;
import java.util.List;

/**
 * r3(n): o maior subconjunto de {1..n} sem progressao de tres termos, calculado para n = 1,
 * 2, 3, ... em sequencia, cada um usando os anteriores.
 *
 * <p>Os numeros entram em ordem crescente. Ao entrar x, cada a ja escolhido proibe 2x - a,
 * o terceiro termo da progressao (a, x, 2x - a). A poda: o que falta escolher esta num
 * intervalo de m numeros, e nenhum intervalo de m numeros comporta mais que r3(m).
 *
 * <p>O truque: r3(n) e r3(n - 1) ou r3(n - 1) + 1, e no segundo caso todo conjunto maximo
 * contem 1 e n (senao caberia em n - 1 numeros). Entao basta procurar um conjunto com 1 e n
 * e r3(n - 1) + 1 elementos.
 */
public final class Busca {

    /** A tabela de r3 de 0 a nMax, e quantos nos cada n custou. */
    public record Tabela(int[] r3, long[] nos, List<int[]> exemplos) {}

    private final int n;
    private final boolean poda;
    private final int[] r3;
    private final int[] escolhidos;
    private long nos;
    private int[] achado;

    private Busca(int n, int[] r3, boolean poda) {
        this.n = n;
        this.r3 = r3;
        this.poda = poda;
        this.escolhidos = new int[n + 1];
    }

    /** r3 de 0 a nMax, com o truque de 1 e n, e com ou sem a poda pela tabela. */
    public static Tabela tabela(int nMax, boolean truque, boolean poda) {
        int[] r3 = new int[nMax + 1];
        long[] nos = new long[nMax + 1];
        List<int[]> exemplos = new ArrayList<>();
        exemplos.add(new int[0]);
        for (int n = 1; n <= nMax; n++) {
            Busca b = new Busca(n, r3, poda);
            if (truque) {
                int[] novo = b.comUmEn(r3[n - 1] + 1);
                if (novo != null) {
                    r3[n] = novo.length;
                    exemplos.add(novo);
                } else {
                    r3[n] = r3[n - 1];
                    exemplos.add(exemplos.get(n - 1));
                }
            } else {
                int[] melhor = b.semTruque();
                r3[n] = melhor.length;
                exemplos.add(melhor);
            }
            nos[n] = b.nos;
        }
        return new Tabela(r3, nos, exemplos);
    }

    /** Quantos subconjuntos de {1..n} tem exatamente r3(n) elementos sem progressao. */
    public static long contaMaximos(int n, int[] r3) {
        Busca b = new Busca(n, r3, true);
        return b.conta(1, 0, new long[palavras(n)], r3[n]);
    }

    /** Todos os maximos de {1..n}, para listar quando sao poucos. */
    public static List<int[]> listaMaximos(int n, int[] r3) {
        Busca b = new Busca(n, r3, true);
        List<int[]> lista = new ArrayList<>();
        b.lista(1, 0, new long[palavras(n)], r3[n], lista);
        return lista;
    }

    // ------------------------------------------------------------- com 1 e n

    private int[] comUmEn(int alvo) {
        if (n == 1) {
            nos++;
            return alvo == 1 ? new int[] {1} : null;
        }
        if (alvo > n) {
            return null;
        }
        long[] proibidos = new long[palavras(n)];
        escolhidos[0] = 1;
        // (1, (1 + n)/2, n): o meio fica proibido desde o comeco.
        if ((1 + n) % 2 == 0) {
            liga(proibidos, (1 + n) / 2);
        }
        if (desceComN(2, 1, proibidos, alvo)) {
            return achado;
        }
        return null;
    }

    private boolean desceComN(int x, int k, long[] proibidos, int alvo) {
        nos++;
        if (k + 1 == alvo) {
            int[] r = new int[alvo];
            System.arraycopy(escolhidos, 0, r, 0, k);
            r[k] = n;
            achado = r;
            return true;
        }
        for (int y = x; y < n; y++) {
            // o que sobra depois de y esta em [y, n - 1], intervalo de n - y numeros.
            if (poda && k + 1 + r3[n - y] < alvo) {
                return false;
            }
            if (k + 1 + (n - y) < alvo) {
                return false;
            }
            if (ligado(proibidos, y)) {
                continue;
            }
            long[] novos = proibidos.clone();
            for (int i = 0; i < k; i++) {
                int terceiro = 2 * y - escolhidos[i];
                if (terceiro < n) {
                    liga(novos, terceiro);
                } else if (terceiro == n) {
                    // (a, y, n) e progressao: y nao pode entrar.
                    novos = null;
                    break;
                }
            }
            if (novos == null) {
                continue;
            }
            if ((y + n) % 2 == 0) {
                liga(novos, (y + n) / 2);
            }
            escolhidos[k] = y;
            if (desceComN(y + 1, k + 1, novos, alvo)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------- sem truque

    private int[] semTruque() {
        int[] guardado = new int[n];
        int[] tamanho = {0};
        descePlano(1, 0, new long[palavras(n)], guardado, tamanho);
        int[] r = new int[tamanho[0]];
        System.arraycopy(guardado, 0, r, 0, tamanho[0]);
        return r;
    }

    private void descePlano(int x, int k, long[] proibidos, int[] guardado, int[] tamanho) {
        nos++;
        if (k > tamanho[0]) {
            tamanho[0] = k;
            System.arraycopy(escolhidos, 0, guardado, 0, k);
        }
        for (int y = x; y <= n; y++) {
            // o que sobra esta em [y, n], intervalo de m = n - y + 1 numeros. Para m = n o
            // proprio r3(n) ainda nao existe, e o teto e r3(n - 1) + 1.
            int m = n - y + 1;
            int limite = !poda ? m : m < n ? r3[m] : r3[n - 1] + 1;
            if (k + limite <= tamanho[0]) {
                return;
            }
            if (ligado(proibidos, y)) {
                continue;
            }
            long[] novos = proibidos.clone();
            for (int i = 0; i < k; i++) {
                int terceiro = 2 * y - escolhidos[i];
                if (terceiro <= n) {
                    liga(novos, terceiro);
                }
            }
            escolhidos[k] = y;
            descePlano(y + 1, k + 1, novos, guardado, tamanho);
        }
    }

    // ------------------------------------------------------------- contagem

    private long conta(int x, int k, long[] proibidos, int alvo) {
        nos++;
        if (k == alvo) {
            return 1;
        }
        long total = 0;
        for (int y = x; y <= n; y++) {
            if (k + r3[n - y + 1] < alvo) {
                break;
            }
            if (ligado(proibidos, y)) {
                continue;
            }
            long[] novos = proibidos.clone();
            for (int i = 0; i < k; i++) {
                int terceiro = 2 * y - escolhidos[i];
                if (terceiro <= n) {
                    liga(novos, terceiro);
                }
            }
            escolhidos[k] = y;
            total += conta(y + 1, k + 1, novos, alvo);
        }
        return total;
    }

    private void lista(int x, int k, long[] proibidos, int alvo, List<int[]> saida) {
        if (k == alvo) {
            int[] r = new int[k];
            System.arraycopy(escolhidos, 0, r, 0, k);
            saida.add(r);
            return;
        }
        for (int y = x; y <= n; y++) {
            if (k + r3[n - y + 1] < alvo) {
                break;
            }
            if (ligado(proibidos, y)) {
                continue;
            }
            long[] novos = proibidos.clone();
            for (int i = 0; i < k; i++) {
                int terceiro = 2 * y - escolhidos[i];
                if (terceiro <= n) {
                    liga(novos, terceiro);
                }
            }
            escolhidos[k] = y;
            lista(y + 1, k + 1, novos, alvo, saida);
        }
    }

    // ------------------------------------------------------------- bits

    private static int palavras(int n) {
        return n / 64 + 1;
    }

    private static void liga(long[] bits, int i) {
        bits[i >>> 6] |= 1L << (i & 63);
    }

    private static boolean ligado(long[] bits, int i) {
        return (bits[i >>> 6] >>> (i & 63) & 1L) != 0;
    }
}

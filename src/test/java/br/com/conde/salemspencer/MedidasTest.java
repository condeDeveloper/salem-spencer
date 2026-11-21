package br.com.conde.salemspencer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Os numeros que estao escritos no README, presos aqui.
 *
 * <p>Se uma medida mudar, este arquivo quebra antes de o texto ficar mentindo. A tabela de
 * 1 a 75 e montada uma vez so.
 */
class MedidasTest {

    private static int[] r3;

    @BeforeAll
    static void montaATabela() {
        r3 = Busca.tabela(Medidor.ATE, true, true).r3();
    }

    @Test
    void ondeR3Sobe() {
        int[] sobe = {1, 2, 4, 5, 9, 11, 13, 14, 20, 24, 26, 30, 32, 36, 40, 41, 51, 54, 58,
            63, 71, 74};
        int k = 0;
        for (int n = 1; n <= Medidor.ATE; n++) {
            if (r3[n] > r3[n - 1]) {
                assertThat(n).as("degrau %d", k + 1).isEqualTo(sobe[k]);
                k++;
            }
        }
        assertThat(k).isEqualTo(sobe.length);
        assertThat(r3[75]).isEqualTo(22);
        assertThat(r3[50]).isEqualTo(16);
    }

    @Test
    void oGulosoPerdeEm9EFicaSeisAtrasEm74() {
        int primeira = 0;
        int maior = 0;
        int onde = 0;
        for (int n = 1; n <= Medidor.ATE; n++) {
            int falta = r3[n] - Guloso.tamanho(n);
            if (falta > 0 && primeira == 0) {
                primeira = n;
            }
            if (falta > maior) {
                maior = falta;
                onde = n;
            }
        }
        assertThat(primeira).isEqualTo(9);
        assertThat(maior).isEqualTo(6);
        assertThat(onde).isEqualTo(74);
        for (int n = 40; n <= 50; n++) {
            assertThat(r3[n] - Guloso.tamanho(n)).as("n=%d", n).isZero();
        }
        // n - 1 = 11, 111 e 1111 em base tres: o guloso empata.
        for (int n : new int[] {5, 14, 41}) {
            assertThat(Integer.toString(n - 1, 3)).matches("1+");
            assertThat(Guloso.tamanho(n)).as("n=%d", n).isEqualTo(r3[n]);
        }
        assertThat(Guloso.pega(81)).hasSize(16).doesNotContain(82);
        assertThat(Guloso.pega(82)).hasSize(17).contains(82);
    }

    @Test
    void aContagemDosMaximos() {
        assertThat(Busca.contaMaximos(19, r3)).isEqualTo(220);
        assertThat(Busca.contaMaximos(39, r3)).isEqualTo(307);
        assertThat(Busca.contaMaximos(40, r3)).isEqualTo(20);
        assertThat(Busca.contaMaximos(50, r3)).isEqualTo(7_726);
    }

    @Test
    void osMaximosUnicosDe6a50SaoTresESimetricos() {
        int[] unicos = new int[3];
        int k = 0;
        for (int n = 6; n <= 50; n++) {
            if (Busca.contaMaximos(n, r3) == 1) {
                unicos[k++] = n;
                List<int[]> lista = Busca.listaMaximos(n, r3);
                int[] m = lista.get(0);
                for (int i = 0; i < m.length; i++) {
                    assertThat(m[i] + m[m.length - 1 - i]).as("n=%d", n).isEqualTo(n + 1);
                }
            }
        }
        assertThat(unicos).containsExactly(14, 30, 41);
    }

    @Test
    void aTabelaCruzadaDaPoda() {
        assertThat(Medidor.somaNos(35, false, false)).isEqualTo(1_690_643);
        assertThat(Medidor.somaNos(35, false, true)).isEqualTo(10_647);
        assertThat(Medidor.somaNos(35, true, false)).isEqualTo(223_772);
        assertThat(Medidor.somaNos(35, true, true)).isEqualTo(11_933);
        assertThat(Medidor.somaNos(30, false, true)).isEqualTo(7_000);
        assertThat(Medidor.somaNos(30, true, true)).isEqualTo(6_187);
        assertThat(Medidor.somaNos(60, false, true)).isEqualTo(2_285_291);
        assertThat(Medidor.somaNos(60, true, true)).isEqualTo(2_417_473);
    }

    @Test
    void oTruqueAjudaAte30AtrapalhaDe35a60EVoltaAAjudar() {
        for (int n = 20; n <= 65; n += 5) {
            boolean ajuda = Medidor.somaNos(n, true, true) < Medidor.somaNos(n, false, true);
            assertThat(ajuda).as("n=%d", n).isEqualTo(n <= 30 || n >= 65);
        }
    }

    @Test
    void oJuizAte22() {
        assertThat(Juiz.maximo(22)).containsExactly(9, 62);
    }
}

package br.com.conde.salemspencer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** A busca contra a forca bruta, e as quatro configuracoes umas contra as outras. */
class BuscaTest {

    @Test
    void bateComAForcaBrutaAte20() {
        Busca.Tabela t = Busca.tabela(20, true, true);
        for (int n = 1; n <= 20; n++) {
            long[] juiz = Juiz.maximo(n);
            assertThat(t.r3()[n]).as("n=%d", n).isEqualTo((int) juiz[0]);
            assertThat(Busca.contaMaximos(n, t.r3())).as("n=%d", n).isEqualTo(juiz[1]);
        }
    }

    @Test
    void asQuatroConfiguracoesDaoAMesmaTabela() {
        int[] nada = Busca.tabela(30, false, false).r3();
        int[] poda = Busca.tabela(60, false, true).r3();
        int[] truque = Busca.tabela(45, true, false).r3();
        int[] dois = Busca.tabela(60, true, true).r3();
        for (int n = 0; n <= 60; n++) {
            assertThat(poda[n]).as("n=%d", n).isEqualTo(dois[n]);
            if (n <= 45) {
                assertThat(truque[n]).as("n=%d", n).isEqualTo(dois[n]);
            }
            if (n <= 30) {
                assertThat(nada[n]).as("n=%d", n).isEqualTo(dois[n]);
            }
        }
    }

    @Test
    void todoExemploTemOTamanhoCertoENaoTemProgressao() {
        Busca.Tabela t = Busca.tabela(60, true, true);
        for (int n = 1; n <= 60; n++) {
            int[] e = t.exemplos().get(n);
            assertThat(e).as("n=%d", n).hasSize(t.r3()[n]);
            assertThat(Progressao.temTresTermos(e)).as("n=%d", n).isFalse();
            for (int x : e) {
                assertThat(x).isBetween(1, n);
            }
        }
    }

    @Test
    void r3SobeNoMaximoUmPorVez() {
        int[] r3 = Busca.tabela(60, true, true).r3();
        for (int n = 1; n <= 60; n++) {
            assertThat(r3[n] - r3[n - 1]).as("n=%d", n).isBetween(0, 1);
        }
    }

    @Test
    void todoMaximoListadoEValido() {
        int[] r3 = Busca.tabela(30, true, true).r3();
        for (int n = 1; n <= 30; n++) {
            for (int[] m : Busca.listaMaximos(n, r3)) {
                assertThat(m).hasSize(r3[n]);
                assertThat(Progressao.temTresTermos(m)).isFalse();
            }
            assertThat(Busca.listaMaximos(n, r3)).hasSize((int) Busca.contaMaximos(n, r3));
        }
    }

    @Test
    void oGulosoSimuladoEAFormulaDeBaseTres() {
        for (int n = 1; n <= 300; n++) {
            assertThat(Guloso.tamanho(n)).as("n=%d", n).isEqualTo(Juiz.gulosoPelaBaseTres(n));
        }
    }

    @Test
    void umaBuscaQueEsquecesseOTerceiroTermoSeriaPega() {
        // controle: sem a regra, o maior subconjunto de {1..9} teria 9 elementos.
        assertThat(Busca.tabela(9, true, true).r3()[9]).isLessThan(9);
        assertThat(Progressao.temTresTermos(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9})).isTrue();
    }
}

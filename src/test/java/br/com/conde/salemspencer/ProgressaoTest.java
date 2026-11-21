package br.com.conde.salemspencer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** A definicao em casos que se conferem de cabeca. */
class ProgressaoTest {

    @Test
    void umDoisTresEProgressao() {
        assertThat(Progressao.temTresTermos(new int[] {1, 2, 3})).isTrue();
        assertThat(Progressao.primeira(new int[] {3, 1, 2})).containsExactly(1, 2, 3);
    }

    @Test
    void umDoisQuatroCincoNaoTem() {
        assertThat(Progressao.temTresTermos(new int[] {1, 2, 4, 5})).isFalse();
        assertThat(Progressao.primeira(new int[] {1, 2, 4, 5})).isNull();
    }

    @Test
    void aProgressaoNaoPrecisaSerDeVizinhos() {
        // 1, 5, 9 com passo 4, escondida no meio de outros.
        assertThat(Progressao.temTresTermos(new int[] {1, 2, 5, 7, 9})).isTrue();
        assertThat(Progressao.primeira(new int[] {9, 2, 5, 1})).containsExactly(1, 5, 9);
    }

    @Test
    void doisElementosNuncaTem() {
        assertThat(Progressao.temTresTermos(new int[] {4, 8})).isFalse();
        assertThat(Progressao.temTresTermos(new int[0])).isFalse();
    }

    @Test
    void oGulosoComecaComoAsTabelasDeStanley() {
        assertThat(Guloso.pega(14)).containsExactly(1, 2, 4, 5, 10, 11, 13, 14);
        assertThat(Juiz.gulosoPelaBaseTres(14)).isEqualTo(8);
    }

    @Test
    void aForcaBrutaDeCabecaEmQuatro() {
        // {1,2,4} e {1,3,4}: os unicos de tres elementos em {1..4} sem progressao.
        assertThat(Juiz.maximo(4)).containsExactly(3, 2);
    }
}

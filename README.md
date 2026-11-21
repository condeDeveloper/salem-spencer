# salem-spencer

Quantos números de 1 a n dá para escolher sem que três deles formem uma progressão
aritmética? Com 1 a 9, o conjunto {1, 2, 4, 8, 9} tem cinco e não tem nenhum trio a, a+d,
a+2d. Seis já não cabem.

Esse máximo se chama r3(n). Salem e Spencer mostraram em 1942 que ele pode crescer quase
tão rápido quanto n; Roth, que ele é uma fração cada vez menor de n. Os valores exatos só
saem por busca. Este repositório calcula r3(n) de 1 a 75, cada um usando os anteriores,
conta os conjuntos máximos até 50 e mede o que cada ideia da busca corta de verdade.

```bash
mvn -B -ntp verify                              # 20 testes
mvn -B -ntp exec:java -Dexec.args="maximos"     # r3(n) de 1 a 75, com um exemplo
mvn -B -ntp exec:java -Dexec.args="guloso"      # o guloso de base três contra o ótimo
mvn -B -ntp exec:java -Dexec.args="contagem"    # quantos conjuntos atingem o máximo
mvn -B -ntp exec:java -Dexec.args="poda"        # a tabela cruzada das duas ideias
mvn -B -ntp exec:java -Dexec.args="juiz"        # a busca contra a força bruta
```

Java 21, sem dependência fora de JUnit e AssertJ.

## Quatro achados

1. **r3(75) = 22**, e a tabela de 1 a 75 sai igual nas quatro configurações da busca onde
   cada uma alcança. Até n = 22 ela bate com a força bruta sobre os 2^n subconjuntos.
2. **O guloso perde pela primeira vez em n = 9, empata de novo de 40 a 50, e em n = 74 já
   está 6 atrás.** O guloso é a sequência de Stanley: pega x quando x − 1 não tem o
   algarismo 2 em base três. Ele trava em 16 de 41 até 81, enquanto o ótimo segue subindo.
3. **Entre n = 6 e n = 50, o conjunto máximo é único só em n = 14, 30 e 41**, e os três são
   simétricos: x está no conjunto exatamente quando n + 1 − x está.
4. **O truque clássico da busca ajuda até n = 30, atrapalha de 35 a 60 e volta a ajudar de
   65 a 75.** Com a poda ligada, juntar o truque custa 11.933 nós em n = 35 contra 10.647
   sem ele.

## A busca

Os números entram em ordem crescente. Ao entrar x, cada a já escolhido proíbe 2x − a, o
terceiro termo da progressão (a, x, 2x − a). Duas ideias cortam a árvore:

- **A poda pela tabela.** O que falta escolher está num intervalo de m números, e nenhum
  intervalo de m números comporta mais que r3(m), que já foi calculado. Se o que tenho mais
  r3(m) não passa do melhor, volto.
- **O truque de 1 e n.** r3(n) é r3(n−1) ou r3(n−1)+1, e no segundo caso todo conjunto
  máximo contém 1 e n: se não contivesse, caberia em n−1 números. Então basta procurar um
  conjunto com 1 e n e r3(n−1)+1 elementos.

```
n   r3  nós          um exemplo (quando sobe)
14  8   7            [1, 2, 4, 5, 10, 11, 13, 14]
20  9   83           [1, 2, 6, 7, 9, 14, 15, 18, 20]
30  12  1.135        [1, 3, 4, 8, 9, 11, 20, 22, 23, 27, 28, 30]
41  16  15           [1, 2, 4, 5, 10, 11, 13, 14, 28, 29, 31, 32, 37, 38, 40, 41]
51  17  5.061        [1, 2, 4, 5, 10, 13, 14, 17, 31, 35, 37, 38, 40, 46, 47, 50, 51]
63  20  431.207      [1, 2, 5, 7, 11, 16, 18, 19, 24, 26, 38, 39, 42, 44, 48, 53, 55, 56, 61, 63]
74  22  7.885.694    [1, 2, 7, 9, 10, 14, 20, 22, 23, 25, 29, 46, 50, 52, 53, 55, 61, 65, 66, 68, 73, 74]
```

r3 sobe em n = 1, 2, 4, 5, 9, 11, 13, 14, 20, 24, 26, 30, 32, 36, 40, 41, 51, 54, 58, 63,
71 e 74. O teste prende os 22 degraus.

## O guloso

```
n   ótimo  guloso  base três  falta
9   5      4       4          1
20  9      8       8          1
30  12     10      10         2
40  15     15      15         0
50  16     16      16         0
60  19     16      16         3
70  20     16      16         4
75  22     16      16         6
```

O guloso simulado e a fórmula de base três são dois caminhos que não se falam; o teste
confere que dão o mesmo até n = 300. Em n = 5, 14 e 41, onde n − 1 se escreve 11, 111 e
1111 em base três, o guloso empata com o ótimo. Entre 41 e 81 ele não pega mais nada: o
próximo número sem o algarismo 2 é 82.

## Os máximos

```
n   r3  máximos
14  8   1
19  8   220
20  9   2
30  12  1
39  14  307
40  15  20
41  16  1
50  16  7.726
```

A contagem enche enquanto r3 fica parado, e despenca quando ele sobe: 307 conjuntos de 14
em n = 39, 20 de 15 em n = 40, um só de 16 em n = 41. Os únicos são simétricos, o que faz
sentido: se o espelho de um máximo fosse outro conjunto, já seriam dois.

## A tabela cruzada

Nós para montar a tabela inteira de 1 até n, com cada ideia ligada e desligada:

```
n   nada            só a poda       só o truque     os dois
20  10.610          563             1.506           475             o truque ajuda
30  362.517         7.000           44.628          6.187           o truque ajuda
35  1.690.643       10.647          223.772         11.933          o truque atrapalha
45  -               17.923          3.731.651       35.467          o truque atrapalha
50  -               348.048         24.305.934      550.371         o truque atrapalha
60  -               2.285.291       297.415.267     2.417.473       o truque atrapalha
65  -               8.527.312       -               6.804.820       o truque ajuda
75  -               112.177.106     -               97.636.846      o truque ajuda
```

A poda é a ideia que importa: sozinha ela corta 159 vezes em n = 35, e o truque sozinho
corta 7,6. Junto com a poda, o truque troca de sinal duas vezes. Quando r3 não sobe, a
busca com o truque precisa provar que não existe conjunto com 1 e n, e essa prova não
aproveita o conjunto bom que a busca sem truque acha logo de cara.

## O que as medidas me corrigiram

**Eu ia usar como gabarito a sequência que eu lembrava de cabeça.** Ela tinha r3(18) = 9.
A força bruta sobre os 262.144 subconjuntos dá 8, e o 9 só aparece em n = 20. Ficou como
juiz só o que o programa mede: a força bruta até 22 e as quatro configurações umas contra
as outras.

**Eu achava que o truque de 1 e n só podia ajudar.** É o truque dos livros, e ele de fato
corta 7,6 vezes sozinho. Com a poda ligada, ele atrapalha em metade das linhas da tabela.
Se eu tivesse medido as duas ideias separadas e multiplicado os ganhos, teria escrito o
contrário.

**Na primeira versão, a busca sem o truque lia r3(n) antes de ele existir.** O intervalo
que sobra quando o primeiro número é 1 tem n números, e a poda pedia r3(n), que ainda era
zero. O teto ali é r3(n−1)+1.

## Dentro

| arquivo | o que faz |
|---|---|
| `Progressao` | a definição: três termos com a mesma diferença |
| `Busca` | r3(n) em sequência, com a poda e o truque ligáveis, e a contagem dos máximos |
| `Guloso` | o guloso simulado, número por número |
| `Juiz` | a força bruta sobre 2^n subconjuntos e a fórmula de base três; não importa a busca |
| `Medidor` | as cinco tabelas acima |

20 testes. Os números deste README estão presos em `MedidasTest`.

## Licença

MIT.

# Fundamentos de Java: guia de estudo

Guia prático para aprender, com exemplos curtos e exercícios, os temas (incluindo Arrays, List e Set):

1. [Arrays, List, Set e Queue/Deque](#1-arrays-list-set-e-queuedeque)
2. [Map e Wrappers](#2-map-e-wrappers)
3. [String, StringBuilder e StringBuffer](#3-string-stringbuilder-e-stringbuffer)
4. [BigDecimal](#4-bigdecimal)
5. [Enums](#5-enums)
6. [Optional](#6-optional)
7. [API de Streams](#7-api-de-streams)
8. [Generics](#8-generics)
9. [Date e Calendar](#9-date-e-calendar)
10. [API java.time (LocalDate, LocalTime, LocalDateTime, OffsetDateTime, OffsetTime)](#10-api-javatime)
11. [Thread e Runnable](#11-thread-e-runnable)
12. [Exercício final integrador](#12-exercício-final-integrador)

**Como estudar:** leia a seção, digite os exemplos você mesmo (não copie e cole), rode, altere valores e observe o que muda. Só depois faça os exercícios. Tente resolver sem olhar a teoria; consulte apenas se travar.

---

## 1. Arrays, List, Set e Queue/Deque

Antes de `Map`, vale dominar as outras coleções. Todas (menos `Map`) fazem parte da hierarquia `Collection`:

```
Iterable
 └── Collection
      ├── List   → ordem + aceita repetidos   (ArrayList, LinkedList)
      ├── Set    → sem repetidos              (HashSet, LinkedHashSet, TreeSet)
      └── Queue  → fila                       (ArrayDeque, PriorityQueue, LinkedList)
Map (separado)  → chave → valor               (HashMap, LinkedHashMap, TreeMap)
```

### 1.1 Arrays

Um **array** tem tamanho **fixo**, definido na criação, e guarda elementos de um único tipo.

```java
int[] notas = new int[5];                 // [0, 0, 0, 0, 0]  (padrão: 0, false ou null)
String[] nomes = {"Ana", "Bruno", "Carla"};
int[] pares = new int[]{2, 4, 6};

notas[0] = 10;                            // índices vão de 0 a length - 1
nomes.length;                             // 3  (atributo, sem parênteses)
notas[5] = 1;                             // ArrayIndexOutOfBoundsException
```

**Percorrendo:**

```java
for (int i = 0; i < nomes.length; i++) {
    System.out.println(i + ": " + nomes[i]);
}
for (String n : nomes) {                  // for-each: não dá acesso ao índice
    System.out.println(n);
}
```

**Matriz (array de arrays):**

```java
int[][] m = new int[3][4];                // 3 linhas, 4 colunas
int[][] fixa = {{1, 2}, {3, 4}, {5, 6}};
fixa[1][0];                               // 3
for (int[] linha : fixa) {
    System.out.println(Arrays.toString(linha));
}
```

**A classe utilitária `Arrays`:**

```java
int[] v = {5, 3, 9, 1};

System.out.println(v);                    // [I@1b6d3586  (inútil, é a referência)
System.out.println(Arrays.toString(v));   // [5, 3, 9, 1]

Arrays.sort(v);                           // [1, 3, 5, 9]  (altera o próprio array)
Arrays.binarySearch(v, 5);                // 2   (só funciona em array ORDENADO)
Arrays.fill(v, 0);                        // [0, 0, 0, 0]
int[] copia = Arrays.copyOf(v, 6);        // copia e completa com zeros
int[] parte = Arrays.copyOfRange(v, 1, 3);
Arrays.equals(new int[]{1}, new int[]{1});// true  (v1.equals(v2) seria false!)
Arrays.deepToString(fixa);                // [[1, 2], [3, 4], [5, 6]]
```

**Array ↔ List:**

```java
String[] arr = {"b", "a", "c"};

List<String> view = Arrays.asList(arr);   // tamanho FIXO: add/remove lançam
                                          // UnsupportedOperationException
List<String> lista = new ArrayList<>(Arrays.asList(arr));  // lista de verdade
String[] volta = lista.toArray(new String[0]);

int[] prim = {1, 2, 3};
List<Integer> boxed = Arrays.stream(prim).boxed().toList(); // int[] precisa de stream
```

**Armadilhas:**

```java
int[] a = {1, 2, 3};
int[] b = a;                              // NÃO copia: as duas variáveis apontam
b[0] = 99;                                // para o MESMO array; a[0] também é 99
a == b;                                   // true (mesma referência)
```

### 1.2 List

`List<E>` é uma sequência **ordenada por posição**, que **aceita repetidos** e `null`, e cresce sozinha.

| Implementação | Como funciona por dentro | Bom para | Ruim para |
|---|---|---|---|
| `ArrayList` | array que redimensiona | acesso por índice (`get`), percorrer | inserir/remover no meio |
| `LinkedList` | nós encadeados | inserir/remover nas pontas | `get(i)` (percorre tudo) |
| `List.of(...)` | imutável | constantes | qualquer alteração |

Na prática, **use `ArrayList` por padrão**.

```java
List<String> nomes = new ArrayList<>();

nomes.add("Ana");                         // [Ana]
nomes.add("Bruno");                       // [Ana, Bruno]
nomes.add(1, "Carla");                    // [Ana, Carla, Bruno]  (insere na posição)
nomes.get(0);                             // "Ana"
nomes.set(0, "Aline");                    // troca o elemento da posição 0
nomes.size();                             // 3
nomes.contains("Bruno");                  // true
nomes.indexOf("Bruno");                   // 2  (-1 se não achar)
nomes.isEmpty();
nomes.addAll(List.of("Dani", "Eva"));
nomes.subList(1, 3);                      // visão do índice 1 até 2
nomes.clear();
```

**A pegadinha clássica do `remove`:**

```java
List<Integer> n = new ArrayList<>(List.of(10, 20, 30));
n.remove(1);                              // remove o ÍNDICE 1  → [10, 30]
n.remove(Integer.valueOf(10));            // remove o OBJETO 10 → [30]
```

**Percorrendo e removendo com segurança:**

```java
for (String s : nomes) {
    if (s.startsWith("A")) nomes.remove(s);   // ConcurrentModificationException!
}

nomes.removeIf(s -> s.startsWith("A"));       // jeito certo

Iterator<String> it = nomes.iterator();       // alternativa clássica
while (it.hasNext()) {
    if (it.next().startsWith("A")) it.remove();
}
```

**Ordenação:**

```java
List<String> l = new ArrayList<>(List.of("banana", "uva", "kiwi"));

Collections.sort(l);                          // ordem natural (alfabética)
l.sort(Comparator.naturalOrder());            // equivalente
l.sort(Comparator.comparing(String::length)); // por tamanho
l.sort(Comparator.comparing(String::length).reversed());
l.sort(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()));
Collections.reverse(l);
Collections.shuffle(l);
```

**Classe sua em lista (`Comparable` x `Comparator`):**

```java
record Pessoa(String nome, int idade) {}

List<Pessoa> ps = new ArrayList<>(List.of(new Pessoa("Ana", 30), new Pessoa("Bia", 25)));
ps.sort(Comparator.comparingInt(Pessoa::idade));          // por idade
ps.sort(Comparator.comparing(Pessoa::nome).reversed());   // por nome, decrescente
```

`Comparable<T>` define a ordem **natural** da própria classe (`compareTo`); `Comparator<T>` define uma ordem **externa**, e você pode ter várias.

**Imutáveis e cópias:**

```java
List<Integer> fixa = List.of(1, 2, 3);        // add/remove/set lançam exceção; não aceita null
List<Integer> copia = List.copyOf(outra);
List<Integer> soLeitura = Collections.unmodifiableList(outra);  // visão: acompanha a original
```

### 1.3 Set

`Set<E>` **não aceita repetidos**. Adicionar um elemento que já existe simplesmente é ignorado (e `add` devolve `false`).

| Implementação | Ordem | Velocidade | Quando usar |
|---|---|---|---|
| `HashSet` | nenhuma garantida | mais rápida | padrão |
| `LinkedHashSet` | ordem de inserção | rápida | tirar repetidos mantendo a ordem |
| `TreeSet` | ordenado | mais lenta (árvore) | precisa ordenado / intervalos |

```java
Set<String> tags = new HashSet<>();

tags.add("java");                         // true
tags.add("java");                         // false (já existia)
tags.add("spring");
tags.contains("java");                    // true  (rápido, ao contrário de List)
tags.remove("spring");
tags.size();
```

**Remover repetidos de uma lista:**

```java
List<Integer> com = List.of(3, 1, 3, 2, 1);
Set<Integer> sem = new LinkedHashSet<>(com);   // [3, 1, 2], mantém a ordem
List<Integer> volta = new ArrayList<>(sem);
```

**Operações de conjuntos:**

```java
Set<Integer> a = new TreeSet<>(Set.of(1, 2, 3, 4));
Set<Integer> b = new TreeSet<>(Set.of(3, 4, 5));

Set<Integer> uniao = new TreeSet<>(a);
uniao.addAll(b);                          // [1, 2, 3, 4, 5]

Set<Integer> inter = new TreeSet<>(a);
inter.retainAll(b);                       // [3, 4]

Set<Integer> dif = new TreeSet<>(a);
dif.removeAll(b);                         // [1, 2]

a.containsAll(Set.of(1, 2));              // true (é subconjunto?)
```

**`TreeSet` (ordenado):**

```java
TreeSet<Integer> t = new TreeSet<>(List.of(10, 5, 20, 15));
t.first();                                // 5
t.last();                                 // 20
t.floor(12);                              // 10  (maior elemento <= 12)
t.ceiling(12);                            // 15  (menor elemento >= 12)
t.headSet(15);                            // [5, 10]        (< 15)
t.tailSet(15);                            // [15, 20]       (>= 15)
t.descendingSet();                        // [20, 15, 10, 5]
```

**Atenção: o que é "repetido" em objetos seus?**

```java
class Ponto {
    int x, y;
    Ponto(int x, int y) { this.x = x; this.y = y; }
}
Set<Ponto> s = new HashSet<>();
s.add(new Ponto(1, 2));
s.add(new Ponto(1, 2));                   // size() == 2 !  (sem equals/hashCode)

record PontoOk(int x, int y) {}           // record gera equals/hashCode
// Com PontoOk, o segundo add é ignorado e size() == 1.
```

- `HashSet` usa `hashCode` + `equals`: **implemente os dois juntos**.
- `TreeSet` usa `compareTo`/`Comparator`: dois elementos "iguais" para o comparador contam como repetidos.

### 1.4 Queue e Deque

- **`Queue`** (fila): entra no fim, sai do início (FIFO).
- **`Deque`** (fila de duas pontas): entra e sai pelas duas pontas; também serve de **pilha** (LIFO).
- **`PriorityQueue`**: sai sempre o "menor" elemento (ou o de maior prioridade, via `Comparator`).

```java
Queue<String> fila = new ArrayDeque<>();
fila.offer("A");                          // entra no fim
fila.offer("B");
fila.peek();                              // "A"  (olha sem remover; null se vazia)
fila.poll();                              // "A"  (remove e devolve; null se vazia)

Deque<Integer> pilha = new ArrayDeque<>();
pilha.push(1);                            // topo
pilha.push(2);
pilha.peek();                             // 2
pilha.pop();                              // 2  (lança exceção se vazia)

PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 3));
pq.poll();                                // 1 (sempre o menor)
PriorityQueue<Integer> maior = new PriorityQueue<>(Comparator.reverseOrder());
```

> Para pilha e fila, prefira `ArrayDeque`. A classe `Stack` é antiga e não é recomendada.

### 1.5 Escolhendo a coleção

| Preciso de... | Use |
|---|---|
| Tamanho fixo, tipo primitivo, desempenho máximo | array |
| Sequência com acesso por posição | `ArrayList` |
| Sem repetidos, ordem não importa | `HashSet` |
| Sem repetidos, mantendo a ordem de entrada | `LinkedHashSet` |
| Sem repetidos, sempre ordenado | `TreeSet` |
| Fila / pilha | `ArrayDeque` |
| Sempre processar o mais prioritário | `PriorityQueue` |
| Buscar valor por chave | `Map` (seção 2) |

Programe para a **interface**: `List<String> x = new ArrayList<>();`, e não `ArrayList<String> x = ...`.

### Exercícios: Arrays, List, Set e Queue

**Arrays**

1. Crie um `int[]` com 10 números. Imprima (com `Arrays.toString`) o array, a soma, a média, o maior e o menor valor, **sem** usar Streams.
2. Inverta um array **sem** criar outro (troque os elementos das pontas indo até o meio).
3. Crie uma matriz 3×3 e imprima a soma de cada linha, de cada coluna e da diagonal principal.
4. Explique, com um programa, por que `int[] b = a;` não copia o array. Depois copie de verdade com `Arrays.copyOf` e mostre que alterar `b` não afeta `a`.
5. Converta `String[] {"c", "a", "b"}` em `List`, ordene, e converta de volta para array. Teste o que acontece com `add` na lista retornada por `Arrays.asList`.

**List**

6. Leia (simule com `List.of`) 8 números, guarde em uma `ArrayList<Integer>` e remova todos os pares com `removeIf`. Depois mostre o erro `ConcurrentModificationException` fazendo a mesma remoção dentro de um `for-each`.
7. Dada `List<Integer> n = [10, 20, 30]`, mostre a diferença entre `n.remove(1)` e `n.remove(Integer.valueOf(10))`.
8. Crie `record Aluno(String nome, double nota)` e uma lista com 6 alunos. Ordene (a) por nota decrescente e (b) por nome; em caso de empate de nota, desempate por nome.
9. Escreva um método genérico `<T> List<T> inverter(List<T> lista)` que **devolva uma nova lista** sem alterar a original.
10. Implemente uma "lista de tarefas" no console com `ArrayList<String>`: adicionar, listar com índice, editar (`set`), remover por índice, tratando índice inválido sem derrubar o programa.
11. Meça (com `System.nanoTime()`) o tempo de inserir 100.000 elementos **no início** de uma `ArrayList` e de uma `LinkedList` (`add(0, x)`). Depois meça 100.000 chamadas de `get(i)` em cada uma. Explique os resultados.

**Set**

12. Dada a lista `[3, 1, 3, 2, 1, 5, 2]`, remova os repetidos (a) sem ordem, (b) mantendo a ordem de entrada e (c) ordenado. Use um `Set` diferente em cada caso.
13. Dados dois `Set<String>` de alunos (`aulaJava` e `aulaSpring`), calcule: quem faz as duas, quem faz só Java, e quem faz pelo menos uma.
14. Crie a classe `Ponto` (sem `equals`/`hashCode`), adicione dois pontos iguais a um `HashSet` e veja `size()`. Corrija implementando `equals`/`hashCode` e depois refaça com `record`.
15. Dada uma frase, use um `Set<Character>` para dizer quantas letras **diferentes** ela tem e se é um *pangrama* (contém as 26 letras do alfabeto).
16. Com `TreeSet<Integer>`, dado `[10, 5, 20, 15, 30]`, imprima o menor, o maior, o maior número `<= 17`, o menor número `>= 17` e os elementos entre 10 e 20.

**Queue / Deque**

17. Verifique se uma expressão como `"{[()]}"` tem os parênteses, colchetes e chaves bem balanceados usando uma pilha (`ArrayDeque`).
18. Simule uma fila de atendimento de banco: clientes entram com `offer` e são atendidos com `poll`. Mostre o próximo (`peek`) a cada passo.
19. Com `PriorityQueue<Tarefa>` (`record Tarefa(String nome, int prioridade)`), processe tarefas da maior para a menor prioridade.

**Desafio de coleções (consolida o capítulo)**

20. Dada uma lista de 15 palavras com repetições, produza: (a) as palavras únicas ordenadas, (b) quantas vezes cada uma aparece (veja a seção 2, `Map`), (c) as 3 mais frequentes, e (d) as palavras que aparecem só uma vez. Escolha **a coleção certa** para cada item e justifique em um comentário.

---

## 2. Map e Wrappers

### 2.1 Wrappers

Java tem tipos **primitivos** (`int`, `double`...) e tipos **objeto** equivalentes, chamados **wrappers** (`Integer`, `Double`...). Os wrappers existem porque coleções e generics só trabalham com objetos (`List<int>` não existe).

| Primitivo | Wrapper |
|---|---|
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

**Autoboxing e unboxing** (conversão automática):

```java
Integer a = 10;     // boxing:   int → Integer
int b = a;          // unboxing: Integer → int
```

**Métodos úteis dos wrappers:**

```java
int n = Integer.parseInt("123");          // String → int
Integer m = Integer.valueOf("456");       // String → Integer
double d = Double.parseDouble("3.14");
boolean ok = Boolean.parseBoolean("true");

Integer.MAX_VALUE;                         // 2147483647
Integer.MIN_VALUE;
Integer.compare(3, 5);                     // -1
Integer.toBinaryString(10);                // "1010"
Character.isDigit('7');                    // true
Character.isLetter('a');                   // true
Character.toUpperCase('a');                // 'A'
```

**Armadilhas:**

```java
Integer x = 1000, y = 1000;
x == y;          // false! compara referências
x.equals(y);     // true  (sempre use equals para wrappers)

Integer c = 100, d = 100;
c == d;          // true por acaso (cache de -128 a 127)

Integer nulo = null;
int z = nulo;    // NullPointerException (unboxing de null)

Integer.parseInt("abc");   // NumberFormatException
```

### 2.2 Map

Um `Map<K, V>` guarda pares **chave → valor**. Chaves são únicas; valores podem se repetir.

| Implementação | Ordem | Aceita `null` | Quando usar |
|---|---|---|---|
| `HashMap` | nenhuma garantida | 1 chave `null` | padrão, mais rápido |
| `LinkedHashMap` | ordem de inserção | sim | quando a ordem importa |
| `TreeMap` | ordenado pelas chaves | não (na chave) | quando precisa ordenado |

```java
Map<String, Integer> idades = new HashMap<>();

idades.put("Ana", 25);
idades.put("Bruno", 30);
idades.put("Ana", 26);            // substitui o valor da chave existente

idades.get("Ana");                // 26
idades.get("Zé");                 // null (não existe)
idades.getOrDefault("Zé", 0);     // 0
idades.containsKey("Bruno");      // true
idades.containsValue(30);         // true
idades.remove("Bruno");
idades.size();
idades.isEmpty();
```

**Percorrendo:**

```java
for (String nome : idades.keySet()) { ... }           // só chaves
for (Integer idade : idades.values()) { ... }         // só valores

for (Map.Entry<String, Integer> e : idades.entrySet()) {
    System.out.println(e.getKey() + " → " + e.getValue());
}

idades.forEach((nome, idade) -> System.out.println(nome + " → " + idade));
```

**Métodos modernos (muito úteis):**

```java
idades.putIfAbsent("Carla", 20);          // só insere se a chave não existir

// Contador de palavras
Map<String, Integer> contagem = new HashMap<>();
for (String palavra : "a b a c b a".split(" ")) {
    contagem.merge(palavra, 1, Integer::sum);   // se existe soma, senão coloca 1
}
// {a=3, b=2, c=1}

// Agrupar em listas
Map<Integer, List<String>> porTamanho = new HashMap<>();
for (String s : List.of("oi", "java", "ok")) {
    porTamanho.computeIfAbsent(s.length(), k -> new ArrayList<>()).add(s);
}
// {2=[oi, ok], 4=[java]}
```

**Mapas imutáveis:**

```java
Map<String, Integer> fixo = Map.of("a", 1, "b", 2);   // não aceita put
```

**Importante:** se usar um objeto seu como chave, ele precisa implementar `equals` e `hashCode` corretamente (records já fazem isso).

### Exercícios: Map e Wrappers

1. Converta a `String` `"2024"` em `int`, some 1 e imprima. Depois tente converter `"abc"` e trate o erro com `try/catch`.
2. Explique, escrevendo um pequeno programa, por que `Integer a = 200, b = 200; a == b` dá `false` e como corrigir.
3. Dada a frase `"java é legal e java é divertido"`, conte quantas vezes cada palavra aparece usando `Map` e `merge`.
4. Crie um `Map<String, Double>` de produtos e preços. Imprima o produto mais caro percorrendo o `entrySet`.
5. Crie um `Map<String, List<String>>` que agrupe uma lista de nomes pela primeira letra (use `computeIfAbsent`).
6. Repita o exercício 3 usando `TreeMap` e depois `LinkedHashMap`. Observe a diferença na ordem de impressão.

---

## 3. String, StringBuilder e StringBuffer

### 3.1 String

`String` é **imutável**: toda "alteração" cria uma nova String.

```java
String s = "Java";
s.toUpperCase();          // retorna "JAVA", mas s continua "Java"
s = s.toUpperCase();      // agora sim s é "JAVA"
```

**String pool:** literais iguais compartilham o mesmo objeto.

```java
String a = "oi";
String b = "oi";
String c = new String("oi");

a == b;          // true  (mesmo objeto no pool)
a == c;          // false (objeto novo)
a.equals(c);     // true  (compare conteúdo SEMPRE com equals)
```

**Métodos principais:**

```java
String t = "  Java é Legal  ";

t.length();                     // 16
t.trim();                       // "Java é Legal" (remove bordas)
t.strip();                      // idem (Java 11+, mais completo)
t.isEmpty();                    // false
t.isBlank();                    // false (true se só tiver espaços)
"Java".charAt(0);               // 'J'
"Java".substring(1);            // "ava"
"Java".substring(1, 3);         // "av" (fim exclusivo)
"Java".indexOf("a");            // 1
"Java".lastIndexOf("a");        // 3
"Java".contains("av");          // true
"Java".startsWith("Ja");        // true
"Java".endsWith("va");          // true
"Java".toUpperCase();           // "JAVA"
"Java".toLowerCase();           // "java"
"banana".replace("a", "o");     // "bonono"
"a,b,c".split(",");             // ["a", "b", "c"]
"ab".repeat(3);                 // "ababab"
"java".equalsIgnoreCase("JAVA");// true
"a".compareTo("b");             // negativo (a vem antes)
```

**Montar textos:**

```java
String.join(", ", "a", "b", "c");                       // "a, b, c"
String.format("%s tem %d anos", "Ana", 25);             // "Ana tem 25 anos"
String.format("%.2f", 3.14159);                         // "3.14"
String.format("%05d", 42);                              // "00042"
"Olá, %s!".formatted("Gabriel");                        // Java 15+
```

**Text block (Java 15+):**

```java
String json = """
    {
      "nome": "Ana"
    }
    """;
```

### 3.2 StringBuilder

Resolve o problema de desempenho: concatenar com `+` em laço cria muitas Strings novas. O `StringBuilder` é **mutável**.

```java
// RUIM: cria uma String nova a cada volta
String r = "";
for (int i = 0; i < 1000; i++) r += i;

// BOM
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) sb.append(i);
String resultado = sb.toString();
```

Métodos:

```java
StringBuilder sb = new StringBuilder("Java");
sb.append(" é legal");        // "Java é legal"
sb.insert(0, ">> ");          // ">> Java é legal"
sb.reverse();
sb.deleteCharAt(0);
sb.delete(0, 3);
sb.replace(0, 2, "XX");
sb.setCharAt(0, 'Z');
sb.length();
sb.toString();

new StringBuilder("arara").reverse().toString();   // "arara" (palíndromo)
```

Encadeamento: `sb.append("a").append(1).append('c');`

### 3.3 StringBuffer

É igual ao `StringBuilder`, porém **thread-safe** (métodos `synchronized`), o que o torna mais lento.

| | String | StringBuilder | StringBuffer |
|---|---|---|---|
| Mutável | não | sim | sim |
| Thread-safe | sim (imutável) | não | sim |
| Velocidade | lenta p/ muitas alterações | mais rápida | intermediária |
| Uso | textos fixos | padrão para montar texto | montar texto compartilhado entre threads |

**Regra prática:** use `String` para textos comuns, `StringBuilder` para montar texto em laços, `StringBuffer` apenas se várias threads alteram o mesmo objeto (raro; hoje se prefere outras soluções).

### Exercícios: String, StringBuilder e StringBuffer

1. Verifique se uma palavra é palíndromo (ignorando maiúsculas/minúsculas) usando `StringBuilder.reverse()`.
2. Conte as vogais de uma frase usando `charAt` ou `toCharArray`.
3. Dada a String `"  Maria da Silva  "`, remova as bordas, converta para maiúsculas e imprima só o último sobrenome.
4. Receba `"joao,maria,pedro"` e imprima cada nome com a primeira letra maiúscula (`split`, `substring`, `toUpperCase`).
5. Gere uma String com os números de 1 a 20 separados por vírgula usando `StringBuilder` (sem vírgula sobrando no final).
6. Mostre com um programa a diferença entre `==` e `equals` usando `new String("x")`.
7. Meça o tempo (`System.nanoTime()`) de concatenar 50.000 vezes com `+` e com `StringBuilder`. Compare.
8. Formate uma linha de tabela: nome com 10 caracteres alinhado à esquerda, preço com 2 casas alinhado à direita (`%-10s|%8.2f`).

---

## 4. BigDecimal

Usado para **precisão decimal exata** (dinheiro, impostos). `double` não representa vários decimais com exatidão:

```java
0.1 + 0.2;     // 0.30000000000000004
```

**Criação:**

```java
new BigDecimal("10.50");        // CERTO: String
BigDecimal.valueOf(10.5);       // CERTO
new BigDecimal(0.1);            // ERRADO: carrega o erro do double
BigDecimal.ZERO; BigDecimal.ONE; BigDecimal.TEN;
```

**Operações** (a classe é imutável: sempre guarde o resultado):

```java
BigDecimal a = new BigDecimal("100.00");
BigDecimal b = new BigDecimal("30.00");

a.add(b);                 // 130.00
a.subtract(b);            // 70.00
a.multiply(b);            // 3000.0000
a.divide(b, 2, RoundingMode.HALF_UP);   // 3.33  (informe escala e arredondamento!)
a.negate();
a.pow(2);
a.max(b);
a.min(b);
```

`a.divide(b)` sem escala pode lançar `ArithmeticException` em dízimas.

**Arredondamento:**

```java
new BigDecimal("2.345").setScale(2, RoundingMode.HALF_UP);    // 2.35
new BigDecimal("2.345").setScale(2, RoundingMode.HALF_EVEN);  // 2.34 (bancário)
new BigDecimal("2.349").setScale(2, RoundingMode.DOWN);       // 2.34 (trunca)
```

**Comparação:**

```java
new BigDecimal("2.0").equals(new BigDecimal("2.00"));       // false (escala difere)
new BigDecimal("2.0").compareTo(new BigDecimal("2.00"));    // 0 → iguais em valor
```

Use `compareTo`: `== 0` igual, `> 0` maior, `< 0` menor.

**Somar uma lista:**

```java
BigDecimal total = precos.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
```

**Formatar como moeda:**

```java
NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
moeda.format(new BigDecimal("1234.5"));    // R$ 1.234,50
```

### Exercícios: BigDecimal

1. Mostre o erro de `0.1 + 0.2` com `double` e o resultado correto com `BigDecimal`.
2. Calcule o valor de um produto de R$ 199,90 com 15% de desconto, arredondado para 2 casas.
3. Divida R$ 100,00 por 3 e imprima com 2 casas. O que acontece se você tirar o `RoundingMode`?
4. Compare `new BigDecimal("5.0")` e `new BigDecimal("5.00")` com `equals` e com `compareTo`. Explique o resultado.
5. Calcule juros compostos: R$ 1.000,00 a 1,5% ao mês durante 12 meses (use um laço e `setScale(2, HALF_UP)` no final).
6. Dada uma `List<BigDecimal>` de preços, calcule soma, média (com `divide`) e o maior valor usando Streams.
7. Crie um método `BigDecimal parcela(BigDecimal total, int parcelas)` que divide e arredonda corretamente.

---

## 5. Enums

Um `enum` define um conjunto **fixo** de constantes. Cada constante é uma instância única do tipo.

```java
public enum DiaUtil { SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA }

DiaUtil d = DiaUtil.SEGUNDA;
```

**Métodos automáticos:**

```java
DiaUtil.values();               // array com todas as constantes
DiaUtil.valueOf("SEXTA");       // String → enum (IllegalArgumentException se não existir)
d.name();                       // "SEGUNDA"
d.ordinal();                    // 0 (posição)
d.compareTo(DiaUtil.QUARTA);    // negativo
d == DiaUtil.SEGUNDA;           // use == para comparar enums
```

**Com atributos, construtor e métodos:**

```java
public enum Status {
    ATIVO("Ativo", 1),
    INATIVO("Inativo", 0),
    BLOQUEADO("Bloqueado", -1);

    private final String descricao;
    private final int codigo;

    Status(String descricao, int codigo) {   // construtor é sempre privado
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getDescricao() { return descricao; }
    public int getCodigo() { return codigo; }

    public static Status porCodigo(int codigo) {
        for (Status s : values()) {
            if (s.codigo == codigo) return s;
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
```

**Com comportamento por constante:**

```java
public enum Operacao {
    SOMA("+")  { public double aplicar(double a, double b) { return a + b; } },
    SUB("-")   { public double aplicar(double a, double b) { return a - b; } };

    private final String simbolo;
    Operacao(String simbolo) { this.simbolo = simbolo; }
    public String getSimbolo() { return simbolo; }

    public abstract double aplicar(double a, double b);
}

Operacao.SOMA.aplicar(2, 3);   // 5.0
```

Também funciona com lambdas:

```java
public enum Op {
    SOMA((a, b) -> a + b),
    MULT((a, b) -> a * b);

    private final DoubleBinaryOperator f;
    Op(DoubleBinaryOperator f) { this.f = f; }
    public double aplicar(double a, double b) { return f.applyAsDouble(a, b); }
}
```

**Em `switch`:**

```java
String msg = switch (status) {
    case ATIVO -> "Pode acessar";
    case INATIVO -> "Reative a conta";
    case BLOQUEADO -> "Fale com o suporte";
};
```

**Coleções especializadas:** `EnumMap` e `EnumSet` (rápidas e ordenadas pela ordem do enum).

```java
Map<Status, Integer> contagem = new EnumMap<>(Status.class);
```

**Características:** pode ter atributos, métodos e construtor; pode implementar interfaces; **não** pode estender outra classe; as constantes são imutáveis (`public static final`) e únicas.

### Exercícios: Enums

1. Crie `DiaDaSemana` com os 7 dias. Imprima todos com `values()` e seu `ordinal()`.
2. Crie `Planeta` com massa e raio (atributos `double`) e um método que calcula a gravidade (`G * massa / raio²`).
3. Crie `Status` com código e descrição, e um método estático `porCodigo`. Teste com código válido e inválido.
4. Implemente `Operacao` (SOMA, SUB, MULT, DIV) com método abstrato `aplicar`. Faça uma calculadora que escolhe a operação por um símbolo digitado.
5. Use um `switch` com enum que devolva o valor do frete: `PAC`, `SEDEX`, `RETIRADA`.
6. Conte, com `EnumMap`, quantos pedidos existem em cada `StatusPedido` a partir de uma lista.
7. Converta uma String vinda do usuário em enum com `valueOf`, tratando a exceção quando ela for inválida.

---

## 6. Optional

`Optional<T>` é uma "caixa" que contém um valor **ou** está vazia. Serve para tornar explícito que um método pode não ter resultado, evitando `null` e `NullPointerException`.

**Criar:**

```java
Optional.of("Java");               // valor obrigatório (null → NPE)
Optional.ofNullable(talvezNull);   // aceita null → vira vazio
Optional.empty();                  // vazio
```

**Ler o valor (do pior para o melhor):**

```java
opt.get();                                   // evite: NoSuchElementException se vazio
opt.orElse("padrão");                        // valor padrão
opt.orElseGet(() -> calcularPadrao());       // padrão calculado só se necessário
opt.orElseThrow(() -> new RuntimeException("Não achei"));
opt.orElseThrow();                           // Java 10+: NoSuchElementException
```

**Agir conforme o caso:**

```java
opt.isPresent();                             // true se tem valor
opt.isEmpty();                               // Java 11+
opt.ifPresent(v -> System.out.println(v));
opt.ifPresentOrElse(v -> ..., () -> ...);    // Java 9+
```

**Transformar:**

```java
Optional.of("Java").map(String::length);               // Optional[4]
Optional.of(17).filter(i -> i >= 18);                  // Optional.empty
buscarUsuario(1).flatMap(Usuario::getEmail);           // quando a função já devolve Optional
opt.or(() -> Optional.of("outro"));                    // Java 9+: alternativa
```

**Cadeia no lugar de vários `if`:**

```java
String cidade = Optional.ofNullable(usuario)
    .map(Usuario::getEndereco)
    .map(Endereco::getCidade)
    .map(String::toUpperCase)
    .orElse("Desconhecida");
```

**Boas práticas:**
- Use como **tipo de retorno** de métodos que podem não achar resultado.
- Não use como campo de classe nem como parâmetro de método.
- Não devolva `null` de um método que retorna `Optional`.
- Prefira `map`/`orElse`/`ifPresent` a `isPresent()` + `get()`.
- `orElse(x)` sempre avalia `x`; `orElseGet(() -> x)` só se estiver vazio.

### Exercícios: Optional

1. Escreva `Optional<Integer> parse(String s)` que devolve o número ou vazio se não for numérico.
2. Dada uma `List<String>`, ache o primeiro nome que começa com "M" e imprima, ou imprima "Ninguém" se não houver.
3. Crie as classes `Usuario → Endereco → Cidade` (com possíveis `null`) e obtenha o nome da cidade em maiúsculas usando uma cadeia de `map`, com padrão "N/D".
4. Escreva um método `Optional<Produto> buscarPorId(List<Produto>, int)` e lance `IllegalArgumentException` quando não achar (`orElseThrow`).
5. Mostre a diferença entre `orElse` e `orElseGet` colocando um `System.out.println` dentro da expressão do padrão.
6. Use `filter` para aceitar apenas idades maiores ou iguais a 18, devolvendo "Menor de idade" caso contrário.

---

## 7. API de Streams

Um **Stream** processa uma sequência de elementos com uma cadeia de operações, de forma declarativa. Não guarda dados e não altera a fonte.

```
fonte → operações intermediárias (lazy) → operação terminal (executa tudo)
```

```java
List<String> r = nomes.stream()          // fonte
    .filter(n -> n.length() > 3)         // intermediária
    .map(String::toUpperCase)            // intermediária
    .toList();                           // terminal
```

- Operações intermediárias são **preguiçosas**: nada roda sem a terminal.
- Um Stream só pode ser **consumido uma vez**.

### 7.1 Criação

```java
lista.stream();
Arrays.stream(array);
Stream.of("a", "b");
IntStream.range(0, 5);            // 0..4
IntStream.rangeClosed(1, 5);      // 1..5
Stream.iterate(1, n -> n * 2).limit(5);   // 1,2,4,8,16
Stream.generate(() -> "x").limit(3);
```

### 7.2 Intermediárias

| Operação | Função |
|---|---|
| `filter(p)` | mantém os que passam no teste |
| `map(f)` | transforma cada elemento |
| `flatMap(f)` | transforma e achata streams aninhados |
| `distinct()` | remove repetidos |
| `sorted()` / `sorted(cmp)` | ordena |
| `limit(n)` / `skip(n)` | pega / pula n |
| `peek(c)` | espia (depuração) |
| `mapToInt/Double/Long` | converte para stream primitivo |

### 7.3 Terminais

| Operação | Devolve |
|---|---|
| `forEach(c)` | nada |
| `toList()` / `collect(...)` | coleção/mapa/texto |
| `count()` | `long` |
| `reduce(inicial, op)` | valor acumulado |
| `findFirst()` / `findAny()` | `Optional` |
| `min(cmp)` / `max(cmp)` | `Optional` |
| `anyMatch` / `allMatch` / `noneMatch` | `boolean` |
| `sum()` / `average()` | em streams primitivos |

### 7.4 `reduce`

Acumula tudo em um valor:

```java
int soma = List.of(1, 2, 3, 4).stream().reduce(0, (acc, x) -> acc + x);   // 10
// acc começa em 0 (elemento neutro); cada resultado vira o acc do próximo passo
```

### 7.5 Streams primitivos e boxing

`Stream<Integer>` trabalha com objetos (boxing). `IntStream`, `LongStream`, `DoubleStream` evitam esse custo e têm `sum()`, `average()`, `max()`, `summaryStatistics()`.

```java
int total = lista.stream().mapToInt(Integer::intValue).sum();
IntStream.of(4, 8, 15).summaryStatistics();    // min, max, média, soma, contagem
intStream.boxed();                              // volta para Stream<Integer>
```

### 7.6 Collectors

```java
record Pessoa(String nome, String cidade, int idade) {}

.collect(Collectors.toList());
.collect(Collectors.toSet());
.collect(Collectors.toMap(Pessoa::nome, Pessoa::idade));
.collect(Collectors.joining(", ", "[", "]"));
.collect(Collectors.counting());
.collect(Collectors.averagingInt(Pessoa::idade));
.collect(Collectors.summingInt(Pessoa::idade));

// Agrupar
Map<String, List<Pessoa>> porCidade =
    pessoas.stream().collect(Collectors.groupingBy(Pessoa::cidade));

Map<String, Long> qtdPorCidade =
    pessoas.stream().collect(Collectors.groupingBy(Pessoa::cidade, Collectors.counting()));

// Dividir em dois grupos (true / false)
Map<Boolean, List<Pessoa>> r =
    pessoas.stream().collect(Collectors.partitioningBy(p -> p.idade() >= 18));
```

### 7.7 Ordenação com Comparator

```java
pessoas.stream().sorted(Comparator.comparing(Pessoa::nome));
pessoas.stream().sorted(Comparator.comparingInt(Pessoa::idade).reversed());
pessoas.stream().sorted(Comparator.comparing(Pessoa::cidade).thenComparing(Pessoa::nome));
```

### 7.8 Cuidados

- Não altere a fonte nem variáveis externas dentro das lambdas.
- Stream já consumido lança `IllegalStateException`.
- `parallelStream()` só vale a pena com muitos dados e operações independentes.
- Para laços simples, o `for` pode ser mais claro.

### Exercícios: Streams

Use esta lista nos exercícios 1 a 8:

```java
record Produto(String nome, String categoria, BigDecimal preco, int estoque) {}
```

1. Liste os nomes dos produtos com estoque maior que zero, em ordem alfabética.
2. Calcule o preço total do estoque (`preco × estoque` somado) com `BigDecimal` e `reduce`.
3. Descubra o produto mais caro (`max`) e imprima o nome com `ifPresent`.
4. Agrupe os produtos por categoria (`groupingBy`).
5. Conte quantos produtos existem por categoria.
6. Gere uma única String com os nomes separados por `" | "`.
7. Separe em dois grupos: preço acima de R$ 100 e até R$ 100 (`partitioningBy`).
8. Verifique com `anyMatch`/`allMatch`/`noneMatch`: existe produto sem estoque? Todos custam mais de zero?
9. Com `IntStream.rangeClosed(1, 100)`, some os pares e liste os 10 primeiros múltiplos de 7.
10. Dada `List<List<Integer>>`, use `flatMap` para obter uma lista única, sem repetidos e ordenada.
11. Reescreva (com Streams) um laço `for` seu qualquer que filtra e transforma uma lista.

---

## 8. Generics

Generics permitem criar classes e métodos que funcionam com **vários tipos**, mantendo a verificação do compilador (sem casts e sem erros em tempo de execução).

**Sem generics (problema):**

```java
List lista = new ArrayList();
lista.add("texto");
lista.add(10);
String s = (String) lista.get(1);   // ClassCastException em tempo de execução
```

**Com generics:**

```java
List<String> lista = new ArrayList<>();
lista.add("texto");
// lista.add(10);     // erro de compilação
```

### 8.1 Classe genérica

```java
public class Caixa<T> {
    private T conteudo;

    public void guardar(T conteudo) { this.conteudo = conteudo; }
    public T pegar() { return conteudo; }
}

Caixa<String> c1 = new Caixa<>();
c1.guardar("Java");
String s = c1.pegar();            // sem cast

Caixa<Integer> c2 = new Caixa<>();
c2.guardar(42);
```

Convenção de nomes: `T` (type), `E` (element), `K` (key), `V` (value), `N` (number).

Mais de um parâmetro:

```java
public class Par<A, B> {
    private final A primeiro;
    private final B segundo;
    public Par(A primeiro, B segundo) { this.primeiro = primeiro; this.segundo = segundo; }
    public A getPrimeiro() { return primeiro; }
    public B getSegundo() { return segundo; }
}

Par<String, Integer> p = new Par<>("Ana", 25);
```

### 8.2 Método genérico

```java
public static <T> void imprimir(List<T> lista) {
    for (T item : lista) System.out.println(item);
}

public static <T> T primeiro(List<T> lista) {
    return lista.isEmpty() ? null : lista.get(0);
}
```

### 8.3 Limites (bounded types)

Restringe o tipo aceito:

```java
public static <T extends Comparable<T>> T maior(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

maior(3, 7);            // 7
maior("a", "z");        // "z"

public static <N extends Number> double somar(List<N> lista) {
    double total = 0;
    for (N n : lista) total += n.doubleValue();
    return total;
}
```

### 8.4 Wildcards (`?`)

```java
List<?> qualquer;                         // lista de algum tipo desconhecido (só leitura)
List<? extends Number> numeros;           // Number ou subclasses: PODE LER, não adicionar
List<? super Integer> aceita;             // Integer ou superclasses: PODE ADICIONAR Integer
```

Regra **PECS**: *Producer Extends, Consumer Super*.

```java
public static double soma(List<? extends Number> lista) { ... }   // produz valores (lê)
public static void preencher(List<? super Integer> lista) { lista.add(1); }  // consome (grava)
```

Importante: `List<Integer>` **não** é subtipo de `List<Number>`. Por isso existem os wildcards.

### 8.5 Limitações

- Não aceita primitivos: `List<int>` não existe, use `List<Integer>`.
- *Type erasure*: o tipo genérico é apagado em tempo de execução, então não se faz `new T()`, `T[]` nem `instanceof List<String>`.
- Não se pode criar um array de tipo genérico.

### Exercícios: Generics

1. Crie `Caixa<T>` com `guardar`, `pegar` e `estaVazia`. Teste com `String`, `Integer` e `LocalDate`.
2. Crie `Par<A, B>` e um método `trocar()` que devolve `Par<B, A>`.
3. Escreva `static <T> void trocarPosicoes(List<T> lista, int i, int j)`.
4. Escreva `static <T extends Comparable<T>> T maior(List<T> lista)`. Teste com números e Strings.
5. Escreva `static double soma(List<? extends Number> lista)` e passe `List<Integer>` e `List<Double>`.
6. Crie uma pilha genérica `Pilha<T>` com `empilhar`, `desempilhar` e `topo` (use uma `ArrayList` por dentro). Lance exceção ao desempilhar vazia.
7. Crie a interface `Repositorio<T, ID>` com `salvar`, `buscarPorId` (devolvendo `Optional<T>`) e `listarTodos`, e uma implementação em memória com `Map<ID, T>`.
8. Explique, com um teste de compilação, por que `List<Number> n = new ArrayList<Integer>();` não compila.

---

## 9. Date e Calendar

São as classes **antigas** (pacote `java.util`, Java 1.0/1.1). Hoje se usa `java.time` (próxima seção), mas elas ainda aparecem em código legado, bibliotecas e provas.

### 9.1 `java.util.Date`

Representa um instante em milissegundos desde 01/01/1970 (UTC).

```java
Date agora = new Date();
agora.getTime();                  // milissegundos
new Date(0);                      // 01/01/1970
agora.before(outra);
agora.after(outra);
agora.compareTo(outra);
```

Quase todos os métodos de campo (`getYear`, `getMonth`, `getDate`) são **obsoletos** (*deprecated*). Para formatar:

```java
SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
String texto = fmt.format(agora);
Date data = fmt.parse("25/12/2024 10:30:00");   // lança ParseException
```

Atenção: `SimpleDateFormat` **não é thread-safe**.

Problemas conhecidos do `Date`: mutável, nome enganoso (guarda instante, não "data"), sem fuso claro, API confusa.

### 9.2 `java.util.Calendar`

Permite ler e alterar campos de data/hora.

```java
Calendar cal = Calendar.getInstance();       // agora

cal.get(Calendar.YEAR);
cal.get(Calendar.MONTH);                     // ATENÇÃO: janeiro = 0!
cal.get(Calendar.DAY_OF_MONTH);
cal.get(Calendar.DAY_OF_WEEK);               // domingo = 1
cal.get(Calendar.HOUR_OF_DAY);

cal.set(2024, Calendar.DECEMBER, 25);        // use as constantes para o mês
cal.set(Calendar.HOUR_OF_DAY, 10);

cal.add(Calendar.DAY_OF_MONTH, 10);          // soma 10 dias
cal.add(Calendar.MONTH, -1);                 // subtrai 1 mês

Date d = cal.getTime();                      // Calendar → Date
cal.setTime(new Date());                     // Date → Calendar
```

Armadilhas: mês começa em 0; mutável (alterar um objeto muda todas as referências a ele); verboso.

### 9.3 Ponte para java.time

```java
Date d = new Date();
Instant i = d.toInstant();
LocalDateTime ldt = LocalDateTime.ofInstant(i, ZoneId.systemDefault());

Date volta = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
```

### Exercícios: Date e Calendar

1. Imprima a data e hora atuais com `Date` formatadas como `dd/MM/yyyy HH:mm`.
2. Converta o texto `"15/08/2023"` em `Date` com `SimpleDateFormat`, tratando `ParseException`.
3. Com `Calendar`, some 45 dias à data de hoje e imprima o resultado formatado.
4. Imprima o ano, mês (de 1 a 12) e dia da semana de hoje com `Calendar`. Cuidado com o mês começando em 0.
5. Descubra se um ano informado é bissexto usando `Calendar` (`getActualMaximum(Calendar.DAY_OF_YEAR)` ou `GregorianCalendar.isLeapYear`).
6. Calcule quantos dias faltam até o próximo Natal usando `Calendar` e milissegundos. Depois compare com a solução do `java.time` (exercício da próxima seção).
7. Converta um `Date` em `LocalDateTime` e de volta para `Date`.

---

## 10. API java.time

Introduzida no Java 8: **imutável**, thread-safe, clara. Substitui `Date` e `Calendar`. Principais classes:

| Classe | Representa | Exemplo |
|---|---|---|
| `LocalDate` | só data | 2024-12-25 |
| `LocalTime` | só hora | 14:30:15 |
| `LocalDateTime` | data + hora (sem fuso) | 2024-12-25T14:30 |
| `OffsetTime` | hora + deslocamento UTC | 14:30-03:00 |
| `OffsetDateTime` | data + hora + deslocamento UTC | 2024-12-25T14:30-03:00 |
| `ZonedDateTime` | data + hora + fuso (regras de horário de verão) | 2024-12-25T14:30-03:00[America/Sao_Paulo] |
| `Instant` | momento na linha do tempo (UTC) | 2024-12-25T17:30:00Z |
| `Period` | quantidade de tempo em dias/meses/anos | P1Y2M |
| `Duration` | quantidade de tempo em horas/min/seg | PT2H30M |

**Regra do nome:** "Local" = sem fuso nem deslocamento; "Offset" = com deslocamento fixo em relação a UTC.

Todas são imutáveis: `plusDays` devolve **um novo objeto**.

### 10.1 LocalDate

```java
LocalDate hoje = LocalDate.now();
LocalDate natal = LocalDate.of(2024, 12, 25);     // mês de 1 a 12 (ou Month.DECEMBER)
LocalDate d = LocalDate.parse("2024-12-25");

natal.getYear();
natal.getMonthValue();        // 12
natal.getMonth();             // DECEMBER
natal.getDayOfMonth();        // 25
natal.getDayOfWeek();         // WEDNESDAY
natal.getDayOfYear();
natal.isLeapYear();

natal.plusDays(10);
natal.minusMonths(2);
natal.plusYears(1);
natal.withDayOfMonth(1);      // primeiro dia do mês

natal.isBefore(hoje);
natal.isAfter(hoje);
natal.isEqual(hoje);
```

### 10.2 LocalTime

```java
LocalTime agora = LocalTime.now();
LocalTime almoco = LocalTime.of(12, 30);          // 12:30
LocalTime t = LocalTime.parse("08:15:30");

almoco.plusHours(2);
almoco.minusMinutes(15);
almoco.getHour();
almoco.isBefore(LocalTime.NOON);
LocalTime.MIDNIGHT;
```

### 10.3 LocalDateTime

```java
LocalDateTime agora = LocalDateTime.now();
LocalDateTime dt = LocalDateTime.of(2024, 12, 25, 14, 30);
LocalDateTime p = LocalDateTime.parse("2024-12-25T14:30:00");

LocalDateTime junto = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
dt.toLocalDate();
dt.toLocalTime();
dt.plusHours(5);
```

### 10.4 OffsetDateTime e OffsetTime

Guardam o **deslocamento em relação a UTC** (ex.: `-03:00` no Brasil). Bom para registrar um momento exato (logs, APIs, banco de dados).

```java
OffsetDateTime agora = OffsetDateTime.now();
OffsetDateTime odt = OffsetDateTime.of(2024, 12, 25, 14, 30, 0, 0, ZoneOffset.of("-03:00"));
OffsetDateTime p = OffsetDateTime.parse("2024-12-25T14:30:00-03:00");

// Mesmo instante, em outro deslocamento
odt.withOffsetSameInstant(ZoneOffset.UTC);        // 2024-12-25T17:30Z

// Mesmo relógio local, outro deslocamento (muda o instante!)
odt.withOffsetSameLocal(ZoneOffset.UTC);          // 2024-12-25T14:30Z

// A partir de LocalDateTime
LocalDateTime.of(2024, 12, 25, 14, 30).atOffset(ZoneOffset.ofHours(-3));

// OffsetTime: só hora + deslocamento
OffsetTime ot = OffsetTime.of(14, 30, 0, 0, ZoneOffset.ofHours(-3));
OffsetTime.now();
ot.withOffsetSameInstant(ZoneOffset.UTC);         // 17:30Z
```

`Offset` não conhece horário de verão. Para isso use `ZonedDateTime` com `ZoneId.of("America/Sao_Paulo")`.

### 10.5 Cálculos entre datas

```java
LocalDate inicio = LocalDate.of(2024, 1, 1);
LocalDate fim = LocalDate.of(2024, 12, 25);

ChronoUnit.DAYS.between(inicio, fim);        // total em dias
ChronoUnit.MONTHS.between(inicio, fim);

Period p = Period.between(inicio, fim);      // p.getMonths(), p.getDays() ...
Duration d = Duration.between(
    LocalTime.of(8, 0), LocalTime.of(17, 30));
d.toHours();         // 9
d.toMinutes();       // 570
d.toMinutesPart();   // 30
```

### 10.6 Formatação e leitura de texto

```java
DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

LocalDateTime.now().format(f);                         // "25/12/2024 14:30"
LocalDate.parse("25/12/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
```

Padrões: `dd` dia, `MM` mês numérico, `MMM` mês abreviado, `yyyy` ano, `HH` hora (0–23), `hh` hora (1–12), `mm` minuto, `ss` segundo. Atenção: `MM` (mês) ≠ `mm` (minuto). Formatadores prontos: `DateTimeFormatter.ISO_LOCAL_DATE`, `ISO_OFFSET_DATE_TIME` etc.

### 10.7 Conversões úteis

```java
LocalDateTime ldt = LocalDateTime.now();

ldt.atZone(ZoneId.of("America/Sao_Paulo"));              // → ZonedDateTime
ldt.atOffset(ZoneOffset.ofHours(-3));                    // → OffsetDateTime
odt.toLocalDateTime();                                   // descarta o offset
odt.toInstant();                                         // → Instant

LocalDate.ofInstant(instant, ZoneId.systemDefault());
```

### Exercícios: java.time

1. Calcule sua idade em anos, meses e dias a partir da data de nascimento (`Period`).
2. Quantos dias faltam para o próximo Natal? E para o seu próximo aniversário?
3. Descubra em que dia da semana você nasceu e se o ano era bissexto.
4. Leia `"31/12/2024 23:59"` com `DateTimeFormatter` e some 1 minuto. Imprima o resultado no mesmo formato.
5. Calcule a duração entre 08:00 e 17:45 em horas e minutos (`Duration`).
6. Dada uma reunião às `15:00` em `OffsetTime` com `-03:00`, mostre o horário equivalente em UTC e em `+09:00` (Tóquio).
7. Crie um `OffsetDateTime` com `-03:00`, converta para UTC (`withOffsetSameInstant`) e explique a diferença para `withOffsetSameLocal`.
8. Crie um método que receba um `LocalDate` e devolva o próximo dia útil (pule sábado e domingo; use `getDayOfWeek()`).
9. Liste todos os dias de um mês informado que caem numa sexta-feira (`LocalDate.lengthOfMonth()` + `datesUntil` ou laço).
10. Converta um `Date` antigo em `LocalDate` e de volta, usando `ZoneId.systemDefault()`.

---

## 11. Thread e Runnable

Uma **thread** é uma linha de execução independente dentro do programa. Com várias threads, tarefas rodam "ao mesmo tempo" (concorrência), como baixar um arquivo enquanto a interface continua responsiva.

Todo programa já começa com uma thread: a `main`.

### 11.1 Formas de criar uma thread

**1. Estendendo `Thread`:**

```java
class MinhaThread extends Thread {
    @Override
    public void run() {
        System.out.println("Rodando em: " + getName());
    }
}

new MinhaThread().start();
```

**2. Implementando `Runnable` (preferida):**

```java
class Tarefa implements Runnable {
    @Override
    public void run() {
        System.out.println("Rodando em: " + Thread.currentThread().getName());
    }
}

Thread t = new Thread(new Tarefa());
t.start();
```

**3. Com lambda** (`Runnable` é uma interface funcional):

```java
Thread t = new Thread(() -> System.out.println("Olá da thread!"));
t.start();
```

**Por que preferir `Runnable`?** Java não tem herança múltipla; implementar a interface deixa a classe livre para estender outra, e separa *a tarefa* (Runnable) de *quem a executa* (Thread).

### 11.2 `start()` vs `run()`

```java
t.start();   // cria uma NOVA thread e executa run() nela
t.run();     // só chama o método na thread atual (sem concorrência!)
```

Chamar `start()` duas vezes na mesma thread lança `IllegalThreadStateException`.

### 11.3 Métodos principais

```java
Thread.sleep(1000);              // pausa a thread atual por 1s (lança InterruptedException)
t.join();                        // espera t terminar
t.getName(); t.setName("x");
Thread.currentThread();          // thread que está executando
t.isAlive();
t.setDaemon(true);               // thread de "fundo": não impede o programa de encerrar
t.interrupt();                   // pede para a thread parar
t.setPriority(Thread.MAX_PRIORITY);
```

```java
Thread t = new Thread(() -> {
    for (int i = 1; i <= 3; i++) {
        System.out.println("Contando " + i);
        try { Thread.sleep(500); } catch (InterruptedException e) { return; }
    }
});
t.start();
t.join();    // main espera t terminar
System.out.println("Fim");
```

### 11.4 Ciclo de vida

```
NEW → RUNNABLE → (BLOCKED | WAITING | TIMED_WAITING) → TERMINATED
```

`t.getState()` mostra o estado atual.

### 11.5 O grande problema: condição de corrida (race condition)

```java
class Contador {
    int valor = 0;
    void incrementar() { valor++; }     // NÃO é atômico: lê, soma, grava
}

Contador c = new Contador();
Runnable r = () -> { for (int i = 0; i < 10_000; i++) c.incrementar(); };

Thread t1 = new Thread(r), t2 = new Thread(r);
t1.start(); t2.start();
t1.join(); t2.join();
System.out.println(c.valor);    // esperado 20000, mas costuma ser MENOR
```

As duas threads leem e gravam o mesmo valor ao mesmo tempo e uma sobrescreve a outra.

**Soluções:**

```java
// 1. synchronized: só uma thread por vez entra no método
synchronized void incrementar() { valor++; }

// 2. Bloco synchronized
void incrementar() { synchronized (this) { valor++; } }

// 3. Classes atômicas (melhor para contadores)
AtomicInteger valor = new AtomicInteger();
valor.incrementAndGet();

// 4. Coleções concorrentes
ConcurrentHashMap<String, Integer> mapa = new ConcurrentHashMap<>();
```

`volatile` garante que a alteração de uma variável seja visível para todas as threads, mas **não** torna `valor++` atômico.

**Deadlock:** duas threads esperam, cada uma, um recurso que a outra segura. Evite travar vários recursos em ordens diferentes.

### 11.6 ExecutorService (jeito moderno)

Em vez de criar threads manualmente, use um *pool*:

```java
ExecutorService pool = Executors.newFixedThreadPool(4);

pool.submit(() -> System.out.println("Tarefa 1"));
pool.submit(() -> System.out.println("Tarefa 2"));

pool.shutdown();                                   // não aceita novas tarefas
pool.awaitTermination(10, TimeUnit.SECONDS);       // espera terminar
```

Com resultado, use `Callable` e `Future`:

```java
Future<Integer> f = pool.submit(() -> 42);   // Callable<Integer>
int r = f.get();                              // bloqueia até ter o resultado
```

`Runnable.run()` não devolve valor nem lança exceção verificada; `Callable.call()` devolve valor e pode lançar.

### Exercícios: Thread e Runnable

1. Crie uma thread (por `Runnable` com lambda) que imprime os números de 1 a 5 com `sleep(300)` entre eles.
2. Crie duas threads, "A" e "B", que imprimem 5 vezes seu nome. Rode várias vezes e observe a ordem variar.
3. Use `join()` para que a `main` imprima "Todas terminaram" somente depois das duas threads do exercício 2.
4. Mostre a diferença entre `start()` e `run()` imprimindo `Thread.currentThread().getName()` nos dois casos.
5. Reproduza a condição de corrida do `Contador` (2 threads × 10.000 incrementos). Rode e veja o resultado errado. Corrija de três formas: `synchronized`, `AtomicInteger` e um `ReentrantLock`.
6. Implemente a classe `Tarefa implements Runnable` que recebe um nome e um tempo, "trabalha" (`sleep`) e imprime quando termina. Dispare 5 delas em um `ExecutorService` de 2 threads.
7. Use `Callable<Integer>` e `Future` para calcular, em 3 tarefas paralelas, a soma de três faixas de números (1–1000, 1001–2000, 2001–3000), somando os resultados no final.
8. Crie uma thread `daemon` com um laço infinito e verifique que o programa termina mesmo assim quando a `main` acaba.

---

## 12. Exercício final integrador

### Sistema de pedidos de uma loja (console)

Construa um mini sistema que une **todos** os tópicos. Faça por etapas, rodando e testando a cada uma.

#### Requisitos

**Etapa 1: Modelagem (Enums, Wrappers, Generics, BigDecimal)**

- `enum Categoria { ELETRONICO, LIVRO, ALIMENTO }`, cada uma com uma `taxaImposto` (`BigDecimal`: 15%, 0%, 5%).
- `enum StatusPedido { CRIADO, PAGO, ENVIADO, ENTREGUE, CANCELADO }` com método `podeAvancarPara(StatusPedido outro)` que impeça transições inválidas (ex.: `ENTREGUE → CRIADO`).
- `record Produto(int id, String nome, Categoria categoria, BigDecimal preco)`.
- `record ItemPedido(Produto produto, int quantidade)` com método `subtotal()` (`BigDecimal`).
- `class Pedido` com `id`, lista de itens, `StatusPedido`, `LocalDateTime criadoEm` e `OffsetDateTime pagoEm` (nulo até pagar).
- Interface genérica `Repositorio<T, ID>` com `salvar(T)`, `buscarPorId(ID)` retornando `Optional<T>`, e `listarTodos()`.
- Implementação `RepositorioEmMemoria<T, ID>` usando `Map<ID, T>` (receba uma `Function<T, ID>` para extrair o id).

**Etapa 2: Carga de dados (String, Wrappers, Map)**

- Receba os produtos como linhas de texto (CSV simulado em uma `List<String>`, por exemplo `"1;Notebook;ELETRONICO;3500.00"`).
- Faça o *parse* com `split`, `Integer.parseInt`, `Categoria.valueOf` e `new BigDecimal(String)`.
- Linhas inválidas (campos faltando, número ou categoria inválidos) devem ser **ignoradas e registradas** em uma lista de erros, sem derrubar o programa.

**Etapa 3: Regras de negócio (BigDecimal, Optional, Enums)**

- `Pedido.total()`: soma dos subtotais **mais o imposto** de cada item conforme a categoria, arredondado para 2 casas (`HALF_UP`).
- Desconto de 10% se o total passar de R$ 5.000,00.
- `pagar()`: só avança se o status permitir; grava `pagoEm` com `OffsetDateTime.now(ZoneOffset.ofHours(-3))`.
- Buscas sempre retornam `Optional`; ao não achar um produto/pedido, lance exceção com `orElseThrow` e uma mensagem clara.

**Etapa 4: Relatórios (Streams, String/StringBuilder, java.time)**

Usando uma lista de pedidos de exemplo, gere com Streams:

1. Faturamento total dos pedidos `PAGO`/`ENVIADO`/`ENTREGUE` (`reduce` com `BigDecimal`).
2. Quantidade de pedidos por `StatusPedido` (`groupingBy` + `counting`, em um `EnumMap` se quiser).
3. Faturamento por `Categoria`.
4. Os 3 produtos mais vendidos (soma das quantidades) em ordem decrescente.
5. O pedido de maior valor (`Optional`).
6. Pedidos criados nos últimos 7 dias (`LocalDateTime`/`ChronoUnit`).

Monte o relatório final com `StringBuilder`, usando `String.format` para alinhar colunas, `DateTimeFormatter` para as datas (`dd/MM/yyyy HH:mm`) e `NumberFormat` para moeda `R$`.

**Etapa 5: Concorrência (Thread, Runnable)**

- Simule o processamento de 20 pedidos por um `ExecutorService` com 4 threads. Cada tarefa (`Runnable`) faz `sleep` curto, **paga** o pedido e incrementa um contador de pedidos processados.
- O contador deve ser correto (`AtomicInteger`) e o repositório deve ser seguro entre threads (`ConcurrentHashMap` ou `synchronized`).
- Aguarde o fim (`shutdown` + `awaitTermination`) e só então imprima o relatório.
- Bônus: mostre o nome da thread que processou cada pedido.

**Etapa 6: Legado (Date e Calendar)**

- Escreva um método `Date paraDate(OffsetDateTime)` e outro `OffsetDateTime paraOffset(Date)` para integrar com um "sistema antigo".
- Com `Calendar`, calcule a data prevista de entrega: data do pagamento + 7 dias. Compare o resultado com a versão em `LocalDate.plusDays(7)`.

#### Estrutura sugerida

```
src/
 ├── Main.java
 ├── modelo/   Categoria, StatusPedido, Produto, ItemPedido, Pedido
 ├── repo/     Repositorio, RepositorioEmMemoria
 ├── servico/  ImportadorCsv, RelatorioService, ProcessadorPedidos
 └── util/     ConversorData
```

#### Critérios de conclusão (checklist)

- [ ] Nenhum `double` para dinheiro; só `BigDecimal` criado a partir de `String`.
- [ ] Nenhum `==` para comparar Strings, wrappers ou `BigDecimal`.
- [ ] Nenhum `get()` direto em `Optional`.
- [ ] Nenhum `null` retornado onde caberia `Optional`.
- [ ] Linhas inválidas do CSV não encerram o programa.
- [ ] Transições inválidas de status são rejeitadas.
- [ ] O contador final com threads é sempre 20 (rode 10 vezes).
- [ ] Relatório formatado com colunas alinhadas, datas e moeda no padrão brasileiro.
- [ ] Todos os cálculos de relatório feitos com Streams.
- [ ] Conversões `Date` ↔ `OffsetDateTime` funcionando.

#### Dados de exemplo para começar

```java
List<String> linhas = List.of(
    "1;Notebook;ELETRONICO;3500.00",
    "2;Java Efetivo;LIVRO;120.90",
    "3;Chocolate;ALIMENTO;12.50",
    "4;Mouse;ELETRONICO;89.99",
    "5;Sem preco;LIVRO",                  // inválida: faltam campos
    "6;Item X;BRINQUEDO;10.00",           // inválida: categoria inexistente
    "7;Teclado;ELETRONICO;abc"            // inválida: preço não numérico
);
```

---

## Roteiro de estudo sugerido

| Dia | Tema |
|---|---|
| 1 | Arrays, List, Set e Queue/Deque |
| 2 | Wrappers e Map |
| 3 | String, StringBuilder, StringBuffer |
| 4 | BigDecimal e Enums |
| 5 | Optional |
| 6 | Streams |
| 7 | Generics |
| 8 | Date, Calendar e java.time |
| 9 | Thread e Runnable |
| 10–11 | Exercício final |

**Dicas finais:**

- Digite o código; não copie.
- Quando um exercício falhar, leia a mensagem da exceção com calma: ela diz a linha e o motivo.
- Depois de resolver, tente reescrever de forma mais simples ou com menos linhas.
- Quando terminar um exercício, envie para revisão: posso apontar erros, melhorias e boas práticas.

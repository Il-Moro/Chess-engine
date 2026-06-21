# Chess Engine (Java) 

Il progetto sviluppa un motore scacchichisto in java, seguendo l'approccio TDD per il corso di studi Software Development Methods all'università degli studi di Trieste. 


Caratteristiche principali:
- uso di programmazione OOP
- svulippo mediante test automatizzati
- implementazione di tutte le regole scacchistiche
## Agente
L'algoritmo utilizzato dall'agente è il minimax con potatura alpha-beta pruning, oltre  a utilizzare altre tecniche per migliorare l'efficienza del motore.


## Software di sviluppo

* **Linguaggio:** Java (Versione 17 o superiore consigliata)
* **Build System:** Gradle / Maven
* **Testing:** JUnit 5 per i test unitari automatici

---

## Requisiti e Installazione

### Prerequisiti
Assicurati di avere installato sul tuo sistema:
- Java Development Kit (JDK) 17 o superiore.

### Clona la repository
```bash
git clone git@github.com:Il-Moro/Chess-engine.git
cd chess-engine
```
### Compilazione ed Esecuzione

Se utilizzi Gradle:

``` Bash
./gradlew build
./gradlew run
```

### Testing

Per eseguire l'intera suite di test unitari e verificare la correttezza della logica di movimento ed evitare regressioni:

``` Bash
./gradlew test
```

# Crediti:
- Morello Filippo: link https://github.com/Il-Moro
- Sasa Pahor: link https://github.com/SasaPahor

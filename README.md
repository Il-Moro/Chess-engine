# Chess Engine (Java) 
### NOTE: progetto ancora in fase di completamento
- elo teorico stimato finale: 1500-1800, dipenderà molto dalla funzione di valutazione e dall'algoritmo di ricerca con ottimizzazioni
- da aggiungere ancora una libreria di aperture
--- 
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
- Un build tool come Gradle o Maven (opzionale, se gestito tramite IDE).

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
Se utilizzi Maven:

``` Bash
mvn clean package
java -jar target/chess-engine-1.0.jar
```


### Testing

Per eseguire l'intera suite di test unitari e verificare la correttezza della logica di movimento ed evitare regressioni:

``` Bash
./gradlew test
```
(o mvn test se utilizzi Maven)

# Crediti:
- Morello Filippo: link https://github.com/Il-Moro
- Sasa Pahor: link https://github.com/SasaPahor

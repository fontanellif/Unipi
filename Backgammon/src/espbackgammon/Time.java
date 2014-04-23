package espbackgammon;
/**
 * La classe Time permette di verificare il tempo di esecuzione di un blocco di
 * codice.
 *
 * @author filippofontanelli
 */
public class Time {
    /**
     * Time in cui il timer viene attivato.
     */
    private long fStart;
    /**
     * Time in cui il timer viene stoppato.
     */
    private long fStop;
    /**
     * Identifica se il timer é in esecuzione oppure no.
     */
    private boolean fIsRunning;
    /**
     * Identifica se il timer non é mai stato utilizzato.
     */
    private boolean fHasBeenUsedOnce;
    /**
     * Start del timer.
     *
     * @throws IllegalStateException se il timer é gia in esecuzione.
     */
    public void start() {
        if (fIsRunning) {
            throw new IllegalStateException("Must stop before calling start again.");
        }
        /*
         * Reset dei valori del timer
         */
        fStart = System.currentTimeMillis();
        fStop = 0;
        fIsRunning = true;
        fHasBeenUsedOnce = true;
    }
    /**
     * Stop del timer.
     *
     * @throws IllegalStateException se il timer non é in esecuzione.
     */
    public void stop() {
        if (!fIsRunning) {
            throw new IllegalStateException("Cannot stop if not currently running.");
        }
        fStop = System.currentTimeMillis();
        fIsRunning = false;
    }
    /**
     * Stampa in millisecondi del tempo di esecuzione del timer.
     *
     * @throws IllegalStateException se il timer non é ancora mai stato
     * utilizzato oppure se non é ancora stato stoppato.
     */
    @Override
    public String toString() {
        validateIsReadable();
        StringBuilder result = new StringBuilder();
        result.append(fStop - fStart);
        result.append(" ms");
        return result.toString();
    }
    /**
     * Ritorna il valore del timer.
     *
     * @return differenza tra il tempo iniziale e quello finale.
     * @throws IllegalStateException se il timer non é ancora mai stato
     * utilizzato oppure se non é ancora stato stoppato.
     */
    public long toValue() {
        validateIsReadable();
        return fStop - fStart;
    }
    /**
     * Controlla che il valore del timer sia leggibile.
     *
     * @throws IllegalStateException se il timer non é ancora mai stato
     * utilizzato oppure se non é ancora stato stoppato.
     */
    private void validateIsReadable() {
        if (fIsRunning) {
            String message = "Non posso leggere il valore del timer perché é ancora in esecuzione.";
            throw new IllegalStateException(message);
        }
        if (!fHasBeenUsedOnce) {
            String message = "Non posso leggere il valore del timer perché non é mai stato utilizzato.";
            throw new IllegalStateException(message);
        }
    }
}

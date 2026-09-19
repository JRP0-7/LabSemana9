package labsemana9;

public class Estadisticas {
    private int generados=0;
    private int entregados=0;
    private int devueltos=0;
    private long tiempoContado=0;
    private int timerTiempo=0;

    public synchronized void sumarGenerados(){
        generados++;
    }

    public synchronized void sumarDevueltos(){
        devueltos++;
    }

    public synchronized void registrarEntrega(long tiempo){
        entregados++;  
        tiempoContado+=tiempo;
        timerTiempo++;
    }

    public synchronized int getGenerados() {
        return generados;
    }

    public synchronized int getEntregados() {
        return entregados;
    }

    public synchronized int getDevueltos() {
        return devueltos;
    }

    public synchronized int getEnProceso() {
        return generados - entregados - devueltos;
    }

    public synchronized double tiempoPromedio() {
        if (timerTiempo == 0) {
            return 0.0;
        }
        return (double) tiempoContado / timerTiempo;
    }

    public synchronized void reiniciar() {
        generados = 0;
        entregados = 0;
        devueltos = 0;
        tiempoContado = 0;
        timerTiempo = 0;
    }

    
}

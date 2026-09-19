package labsemana9;

import labsemana9.error.EstadoInvalidoException;

public class Empaquetar extends Thread{
    private Logistica log;
    private final int numero;
    private volatile Paquete packActual;

    public Empaquetar(Logistica log, int numero){
        super("Empaquetador-" + numero);
        this.log=log;
        this.numero=numero;
    }

    public void run() {
        while (log.isRunning()) {
            try {
                log.verificarPausa();
                Paquete p = log.getClasificacion().extraerPrioritario(Comparacion.prioridad);
                packActual = p;
                p.cambiarEstado(Estados.EMPAQUETANDO);
                Thread.sleep(TiempoProcesamiento.tiempo(p.getPeso()) * 1000L);
                p.cambiarEstado(Estados.EMPAQUETADO);
                log.getEmpaquetado().agregar(p);
                log.getRegistro().registrar(p.getCodigo() + " empaquetado");
                packActual = null;
            } catch (InterruptedException e) {
                break;
            } catch (EstadoInvalidoException e) {
                log.getRegistro().registrar("Error: " + e.getMessage());
                break;
            }
        }
    }

    public int getNumero() {
        return numero;
    }

    public Paquete getPaqueteActual() {
        return packActual;
    }
}

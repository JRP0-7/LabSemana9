package labsemana9;

import labsemana9.error.EstadoInvalidoException;

public class Clasificador extends Thread{
    private Logistica log;
    private final int numero;
    private volatile Paquete packActual;

    public Clasificador(Logistica log, int numero){
        super("Clasificador-" + numero);
        this.log=log;
        this.numero=numero;
    }

    public void run() {
        while (log.isRunning()) {
            try {
                log.verificarPausa();
                Paquete p = log.getAlmacen().extraerPrioritario(Comparacion.prioridad);
                packActual = p;
                p.cambiarEstado(Estados.CLASIFICANDO);
                Thread.sleep(200);
                String ruta = Rutas.asignarRuta(p.getCiudad());
                p.setRuta(ruta);
                p.cambiarEstado(Estados.CLASIFICADO);
                log.getClasificacion().agregar(p);
                log.getRegistro().registrar(p.getCodigo() + " clasificado -> " + ruta);
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

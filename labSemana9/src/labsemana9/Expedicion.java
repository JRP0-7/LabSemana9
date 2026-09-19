package labsemana9;

import labsemana9.error.EstadoInvalidoException;

public class Expedicion extends Thread{
    private Logistica log;

    public Expedicion(Logistica log){
        super("Expedicion");
        this.log=log;
    }

    public void run() {
        while (log.isRunning()) {
            try {
                log.verificarPausa();
                Paquete p = log.getEmpaquetado().extraerPrioritario(Comparacion.prioridad);
                p.cambiarEstado(Estados.EN_EXPEDICION);
                log.getExpedicion().agregar(p);
                log.getRegistro().registrar(p.getCodigo() + " asignado a " + p.getRuta());
            } catch (InterruptedException e) {
                break;
            } catch (EstadoInvalidoException e) {
                log.getRegistro().registrar("Error: " + e.getMessage());
                break;
            }
        }
    }
}

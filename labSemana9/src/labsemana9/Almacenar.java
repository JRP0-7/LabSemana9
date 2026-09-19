package labsemana9;

import labsemana9.error.EstadoInvalidoException;

public class Almacenar extends Thread{
    private Logistica log;

    public Almacenar(Logistica log){
        super("Almacenamiento");
        this.log=log;
    }

    public void run() {
        while (log.isRunning()) {
            try {
                log.verificarPausa();
                Paquete p = log.getRecepcion().extraer();
                p.cambiarEstado(Estados.ALMACENADO);
                log.getAlmacen().agregar(p);
                log.getRegistro().registrar(p.getCodigo() + " almacenado");
            } catch (InterruptedException e) {
                break;
            } catch (EstadoInvalidoException e) {
                log.getRegistro().registrar("Error: " + e.getMessage());
                break;
            }
        }
    }
}

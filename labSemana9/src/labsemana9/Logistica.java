package labsemana9;

import java.util.ArrayList;
import java.util.List;

public class Logistica{
    private static final String[] rutasT = {"Ruta1", "Ruta2", "Ruta3", "Ruta4"};
    private static final int[] capacidadVehiculos = {5, 4, 6, 5};
    private static final String[] nRepartidores = {"Repartidor 1", "Repartidor 2", "Repartidor 3", "Repartidor 4"};

    private final ListaSincronizada<Paquete> recepcion = new ListaSincronizada<>(10);
    private final ListaSincronizada<Paquete> almacen = new ListaSincronizada<>(20);
    private final ListaSincronizada<Paquete> clasificacion = new ListaSincronizada<>(10);
    private final ListaSincronizada<Paquete> empaquetado = new ListaSincronizada<>(8);
    private final ListaSincronizada<Paquete> expedicion = new ListaSincronizada<>(15);
    private final ListaSincronizada<Paquete> entregados = new ListaSincronizada<>(Integer.MAX_VALUE);
    private final ListaSincronizada<Paquete> devueltos = new ListaSincronizada<>(Integer.MAX_VALUE);

    private final Estadisticas estadisticas = new Estadisticas();
    private final RegistroEventos registro = new RegistroEventos();
    private final List<Repartir> repartidores = new ArrayList<>();
    private final List<Thread> hilos = new ArrayList<>();
    private final List<Clasificador> clasificadores = new ArrayList<>();
    private final List<Empaquetar> empaquetadores = new ArrayList<>();

    private volatile boolean running = false;
    private volatile boolean pausado = false;

    public ListaSincronizada<Paquete> getRecepcion() {
        return recepcion;
    }

    public ListaSincronizada<Paquete> getAlmacen() {
        return almacen;
    }

    public ListaSincronizada<Paquete> getClasificacion() {
        return clasificacion;
    }

    public ListaSincronizada<Paquete> getEmpaquetado() {
        return empaquetado;
    }

    public ListaSincronizada<Paquete> getExpedicion() {
        return expedicion;
    }

    public ListaSincronizada<Paquete> getEntregados() {
        return entregados;
    }

    public ListaSincronizada<Paquete> getDevueltos() {
        return devueltos;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public RegistroEventos getRegistro() {
        return registro;
    }

    public List<Repartir> getRepartidores() {
        return repartidores;
    }

    public List<Clasificador> getClasificadores() {
        return clasificadores;
    }

    public List<Empaquetar> getEmpaquetadores() {
        return empaquetadores;
    }

    public boolean isRunning(){
        return running;
    }

    public synchronized void verificarPausa() throws InterruptedException{
        while(pausado && running){
            wait();
        }
    }

    public void pausar(){
        pausado=true;
        registro.registrar("Proceso Pausado");
    }

    public synchronized void reanudar(){
        pausado=false;
        notifyAll();
        registro.registrar("Proceso Reanudado");
    }

    public void reiniciarEstado(){
        recepcion.vaciarSinc();
        almacen.vaciarSinc();
        clasificacion.vaciarSinc();
        empaquetado.vaciarSinc();
        expedicion.vaciarSinc();
        entregados.vaciarSinc();
        devueltos.vaciarSinc();
        estadisticas.reiniciar();
        registro.limpiar();
        repartidores.clear();
    }

    public synchronized void iniciar() {
        if (running) {
            return;
        }
        running = true;
        pausado = false;
        hilos.clear();
        repartidores.clear();

        agregarYArrancar(new GenPaquetes(this));
        agregarYArrancar(new Almacenar(this));
        clasificadores.clear();
        empaquetadores.clear();
        for (int i = 1; i <= 3; i++) {
            Clasificador c = new Clasificador(this, i);
            clasificadores.add(c);
            agregarYArrancar(c);
        }
        for (int i = 1; i <= 2; i++) {
            Empaquetar e = new Empaquetar(this, i);
            empaquetadores.add(e);
            agregarYArrancar(e);
        }
        agregarYArrancar(new Expedicion(this));
        for (int i = 0; i < rutasT.length; i++) {
            Repartir r = new Repartir(this, i + 1, nRepartidores[i], capacidadVehiculos[i], rutasT[i]);
            repartidores.add(r);
            agregarYArrancar(r);
        }

        registro.registrar("Simulación iniciada");
    }

    private void agregarYArrancar(Thread hilo) {
        hilos.add(hilo);
        hilo.start();
    }

    public void detener() {
        running = false;
        synchronized (this) {
            notifyAll();
        }
        for (Thread hilo : hilos) {
            hilo.interrupt();
        }
        for (Thread hilo : hilos) {
            try {
                hilo.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        hilos.clear();
        registro.registrar("Simulación detenida");
    }

    public void reiniciar() {
        detener();
        reiniciarEstado();
        iniciar();
    }

}
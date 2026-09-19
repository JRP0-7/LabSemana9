package labsemana9;

import java.util.Random;

import labsemana9.error.EstadoInvalidoException;

public class Repartir extends Thread{
    private static final int MAX_INTENTOS = 3;
    private static final int PROB_AUSENTE = 25;
    private static final long tViaje = 2000;

    private final Logistica log;
    private final int numero;
    private final String nombre;
    private final int capacidad;
    private final String ruta;
    private Random aleatorio = new Random();

    private volatile EstadosRepartidor estado = EstadosRepartidor.DISPONIBLE;
    private volatile int cargaActual = 0;
    private volatile int paquetesEntregados = 0;

    public Repartir(Logistica log, int numero, String nombre, int capacidad, String ruta) {
        super("Repartidor-" + numero);
        this.log = log;
        this.numero = numero;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.ruta = ruta;
    }

    public void run() {
        while (log.isRunning()) {
            try {
                log.verificarPausa();
                ListaEnlazada<Paquete> cargados = cargarVehiculo();
                viajarYEntregar(cargados);
            } catch (InterruptedException e) {
                break;
            } catch (EstadoInvalidoException e) {
                log.getRegistro().registrar("Error: " + e.getMessage());
                break;
            }
        }
        estado = EstadosRepartidor.FUERA_DE_SERVICIO;
    }

    private ListaEnlazada<Paquete> cargarVehiculo() throws InterruptedException {
        estado = EstadosRepartidor.CARGANDO;
        ListaEnlazada<Paquete> cargados = new ListaEnlazada<>();
        while (cargados.getSize() < capacidad) {
            Paquete p = log.getExpedicion().extraerPrioritarioFiltrado(
                    pkg -> ruta.equals(pkg.getRuta()), Comparacion.prioridad);
            cargados.agregar(p);
            cargaActual = cargados.getSize();
            log.getRegistro().registrar(p.getCodigo() + " cargado en " + nombre);
        }
        return cargados;
    }

    private void viajarYEntregar(ListaEnlazada<Paquete> cargados) throws InterruptedException {
        estado = EstadosRepartidor.EN_RUTA;
        Thread.sleep(tViaje);

        for (Paquete p : cargados.aLista()) {
            p.cambiarEstado(Estados.EN_REPARTO);
            estado = EstadosRepartidor.ENTREGANDO;
            entregar(p);
        }

        cargados.vaciar();
        cargaActual = 0;
        estado = EstadosRepartidor.REGRESANDO;
        Thread.sleep(tViaje);
        estado = EstadosRepartidor.DISPONIBLE;
    }

    private void entregar(Paquete p) {
        while (true) {
            if (aleatorio.nextInt(100) >= PROB_AUSENTE) {
                p.cambiarEstado(Estados.ENTREGADO);
                log.getEntregados().forzarAdicion(p);
                log.getEstadisticas().registrarEntrega(System.currentTimeMillis() - p.getHoraCreacion());
                paquetesEntregados++;
                log.getRegistro().registrar(p.getCodigo() + " entregado");
                return;
            }

            int intentos = p.incrementarIntentos();
            log.getRegistro().registrar(p.getCodigo() + " intento " + intentos + " -> cliente ausente");
            p.cambiarEstado(Estados.NUEVO_INTENTO);
            if (intentos >= MAX_INTENTOS) {
                p.cambiarEstado(Estados.DEVUELTO);
                log.getDevueltos().forzarAdicion(p);
                log.getEstadisticas().sumarDevueltos();
                log.getRegistro().registrar(p.getCodigo() + " devuelto tras " + intentos + " intentos");
                return;
            }
            p.cambiarEstado(Estados.EN_REPARTO);
        }
    }

    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getRuta() {
        return ruta;
    }

    public EstadosRepartidor getEstado() {
        return estado;
    }

    public int getCargaActual() {
        return cargaActual;
    }

    public int getPaquetesEntregados() {
        return paquetesEntregados;
    }
}

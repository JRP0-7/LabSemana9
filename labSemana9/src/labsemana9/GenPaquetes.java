package labsemana9;

import java.util.Random;

public class GenPaquetes extends Thread{
    private static int contadorCodigos=0;
    private static String[] clientes = {"Juan Ramirez", "Sergio Reyes", "Lisa Calderon", "Mario Sanchez"};
    private static final String[] ciudades = Rutas.getCiudades();
    private static String[] direcciones = {"Primer Calle", "Circunvalacion", "Lima Vieja", "Agua Blanca", "Segunda Avenida"};

    private static final int minMS = 600;
    private static final int maxMS = 800;

    private Logistica log;
    private Random aleatorio = new Random();

    public GenPaquetes(Logistica log){
        super("Generador");
        this.log = log;
    }

    public void run() {
        while (log.isRunning()) {
            try {
                log.verificarPausa();
                int espera = minMS + aleatorio.nextInt(maxMS - minMS + 1);
                Thread.sleep(espera);
                if (!log.isRunning()) {
                    break;
                }
                Paquete paquete = crearPaqueteAleatorio();
                log.getRecepcion().agregar(paquete);
                log.getEstadisticas().sumarGenerados();
                log.getRegistro().registrar(paquete.getCodigo() + " recibido");
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private static synchronized int siguienteNumero() {
        contadorCodigos++;
        return contadorCodigos;
    }

    private Paquete crearPaqueteAleatorio() {
        int numero = siguienteNumero();
        String codigo = String.format("PKG-%03d", numero);
        String cliente = clientes[aleatorio.nextInt(clientes.length)];
        String direccion = direcciones[aleatorio.nextInt(direcciones.length)];
        String ciudad = ciudades[aleatorio.nextInt(ciudades.length)];
        double peso = 0.5 + aleatorio.nextDouble() * 7.5;
        Prioridad prioridad = Prioridad.values()[aleatorio.nextInt(Prioridad.values().length)];
        return new Paquete(codigo, cliente, direccion, ciudad, peso, prioridad);
    }

}
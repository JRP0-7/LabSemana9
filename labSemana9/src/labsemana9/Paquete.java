package labsemana9;

import java.util.Objects;

import labsemana9.error.EstadoInvalidoException;

public class Paquete {
    private String codigo;
    private String cliente;
    private String direccion;
    private String ciudad;
    private double peso;
    private Prioridad prioridad;
    private Estados estado;
    private String ruta;
    private int nIntentos;
    private long horaCreacion;

    public Paquete(String cod, String nombre, String direc, String ciudad, double peso, Prioridad prio){
        this.codigo=cod;
        this.cliente=nombre;
        this.direccion=direc;
        this.ciudad= ciudad;
        this.peso=peso;
        this.prioridad=prio;
        this.estado=Estados.RECIBIDO;
        this.nIntentos=0;
        this.horaCreacion=System.currentTimeMillis();
    }

    public synchronized void cambiarEstado(Estados nuevo) throws EstadoInvalidoException {
        if (!estado.puedeTransicionarA(nuevo)) {
            throw new EstadoInvalidoException("No se puede pasar de " + estado + " a " + nuevo + " (paquete " + codigo + ")");
        }
        estado = nuevo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public double getPeso() {
        return peso;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Estados getEstado() {
        return estado;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String rutaAsignada) {
        this.ruta = rutaAsignada;
    }

    public int getIntentos() {
        return nIntentos;
    }

    public synchronized int incrementarIntentos() {
        nIntentos++;
        return nIntentos;
    }

    public long getHoraCreacion() {
        return horaCreacion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Paquete)) {
            return false;
        }
        return codigo.equals(((Paquete) obj).codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo;
    }
    
}

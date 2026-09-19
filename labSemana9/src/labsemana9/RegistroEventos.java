package labsemana9;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RegistroEventos{
    private static DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static int maxLineas = 50;

    private ListaEnlazada<String> lineas =  new ListaEnlazada<>();

    public synchronized void registrar(String mensaje){
        String linea = LocalTime.now().format(formato) + " - " + mensaje;
        lineas.agregar(linea);

        while(lineas.getSize()>maxLineas){
            lineas.eliminarPrimero();
        }
    }

    public synchronized List<String> obtenerLineas() {
        return lineas.aLista();
    }

    public synchronized void limpiar() {
        lineas.vaciar();
    }

}
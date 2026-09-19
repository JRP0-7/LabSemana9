package labsemana9;

import java.util.HashMap;
import java.util.Map;

public class Rutas {
    private static Map<String, String> rutasEntrega = new HashMap<>();

    static {
        rutasEntrega.put("San Pedro Sula", "Ruta1");
        rutasEntrega.put("Lima", "Ruta1");
        rutasEntrega.put("Santa Rita", "Ruta2");
        rutasEntrega.put("Tela", "Ruta3");
        rutasEntrega.put("Choloma", "Ruta4");
    }

    public Rutas() {
    }

    public static String asignarRuta(String ciudad) {
        return rutasEntrega.getOrDefault(ciudad, "Ruta1");
    }

    public static String[] getCiudades() {
        return rutasEntrega.keySet().toArray(new String[0]);
    }
}
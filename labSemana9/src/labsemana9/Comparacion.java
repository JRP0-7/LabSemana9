package labsemana9;

import java.util.Comparator;

public class Comparacion {
    public static Comparator<Paquete> prioridad = Comparator.comparingInt(p->p.getPrioridad().ordinal());

}

package labsemana9;

public enum Estados {
    RECIBIDO,
    ALMACENADO,
    CLASIFICANDO,
    CLASIFICADO,
    EMPAQUETANDO,
    EMPAQUETADO,
    EN_EXPEDICION,
    EN_REPARTO,
    NUEVO_INTENTO,
    ENTREGADO,
    DEVUELTO;

    public boolean puedeTransicionarA(Estados siguiente) {
    switch (this) {
        case RECIBIDO:
            return siguiente == ALMACENADO;
        case ALMACENADO:
            return siguiente == CLASIFICANDO;
        case CLASIFICANDO:
            return siguiente == CLASIFICADO;
        case CLASIFICADO:
            return siguiente == EMPAQUETANDO;
        case EMPAQUETANDO:
            return siguiente == EMPAQUETADO;
        case EMPAQUETADO:
            return siguiente == EN_EXPEDICION;
        case EN_EXPEDICION:
            return siguiente == EN_REPARTO;
        case EN_REPARTO:
            return siguiente == ENTREGADO || siguiente == NUEVO_INTENTO;
        case NUEVO_INTENTO:
            return siguiente == EN_REPARTO || siguiente == DEVUELTO;
        default:
            return false; // ENTREGADO y DEVUELTO son terminales
    }
}
}

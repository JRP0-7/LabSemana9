package labsemana9;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ListaSincronizada<T> {
    private ListaEnlazada<T> interna = new ListaEnlazada<>();
    private int size;

    public ListaSincronizada(int size){
        this.size=size;
    }

    public synchronized void agregar(T dato) throws InterruptedException{
        while(interna.getSize()==size){
            wait();
        }
        interna.agregar(dato);
        notifyAll();
    }

    public synchronized T extraer() throws InterruptedException{
        while(interna.estaVacia()){
            wait();
        }
        T item = interna.eliminarPrimero();
        notifyAll();
        return item;
    }

    public synchronized T extraerPrioritario(Comparator<T> comparador) throws InterruptedException {
        return extraerPrioritarioFiltrado(x -> true, comparador);
    }

    public synchronized T extraerPrioritarioFiltrado(Predicate<T> filtro, Comparator<T> comparador) throws InterruptedException {
        T elegido = buscarMejor(filtro, comparador);
        while (elegido == null) {
            wait();
            elegido = buscarMejor(filtro, comparador);
        }
        interna.eliminar(elegido);
        notifyAll();
        return elegido;
    }

    private T buscarMejor(Predicate<T> filtro, Comparator<T> comparador) {
        T mejor = null;
        for (T item : interna.aLista()) {
            if (filtro.test(item) && (mejor == null || comparador.compare(item, mejor) < 0)) {
                mejor = item;
            }
        }
        return mejor;
    }

    public synchronized int tamaño() {
        return interna.getSize();
    }

    public synchronized boolean estaVacia() {
        return interna.estaVacia();
    }

    public synchronized boolean estaLlena() {
        return interna.getSize() == size;
    }

    public int getSize() {
        return size;
    }

    public synchronized void vaciarSinc() {
        interna.vaciar();
        notifyAll();
    }

    public synchronized List<T> snapshot() {
        return interna.aLista();
    }

    public void forzarAdicion(T item) {
        try {
            agregar(item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

package labsemana9;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListaEnlazada<T> {

    Nodo<T> cabeza;
    int size;

    public ListaEnlazada() {
        cabeza = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    // LISTA ENLAZADA SIMPLE: recorre los nodos hasta el último y enlaza allí el
    // nuevo dato.
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<T>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getNext() != null) {
                actual = actual.getNext();
            }
            actual.setNext(nuevo);
        }
        size++;
    }

    // LISTA ENLAZADA SIMPLE: recorre la cadena desde cabeza siguiendo next.
    public void mostrar() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getInfo());
            actual = actual.getNext();
        }
    }

    // LISTA ENLAZADA SIMPLE: busca secuencialmente el dato sin usar colecciones de
    // Java.
    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getInfo().equals(dato)) {
                return true;
            }
            actual = actual.getNext();
        }
        return false;
    }

    public T buscar(Predicate<T> condicion) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (condicion.test(actual.getInfo())) {
                return actual.getInfo();
            }
            actual = actual.getNext();
        }
        return null;
    }

    // LISTA ENLAZADA SIMPLE: elimina el nodo encontrado actualizando el enlace
    // previo.
    public boolean eliminar(T dato) {
        Nodo<T> actual = cabeza;
        Nodo<T> previo = null;

        if (dato == null) {
            return false;
        }
        if (actual != null && actual.getInfo().equals(dato)) {
            cabeza = actual.getNext();
            size--;
            return true;
        }

        while (actual != null && !actual.getInfo().equals(dato)) {
            previo = actual;
            actual = actual.getNext();
        }

        if (actual == null) {
            return false;
        }
        previo.setNext(actual.getNext());
        size--;
        return true;
    }

    public T eliminarPrimero() {
        if (cabeza == null)
            return null;
        T dato = cabeza.getInfo();
        cabeza = cabeza.getNext();
        size--;
        return dato;
    }

    // LISTA ENLAZADA SIMPLE: obtiene un elemento avanzando desde la cabeza hasta el
    // índice.
    public T get(int indice) {
        Nodo<T> aux = cabeza;

        if (indice < 0 || indice >= size)
            return null;

        for (int i = 0; i < indice; i++) {
            aux = aux.getNext();
        }
        return aux.getInfo();
    }

    // LISTA ENLAZADA SIMPLE: inserta el nodo y reajusta los enlaces de la posición
    // indicada.
    public void agregarEn(int indice, T dato) {
        if (indice < 0 || indice > size) {
            return;
        }
        Nodo<T> nuevo = new Nodo<T>(dato);
        if (indice == 0) {
            nuevo.setNext(cabeza);
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getNext();
            }
            nuevo.setNext(actual.getNext());
            actual.setNext(nuevo);
        }
        size++;
    }

    public boolean estaVacia() {
        return size == 0;
    }

    public void vaciar() {
        cabeza = null;
        size = 0;
    }

    public List<T> aLista() {
        List<T> copia = new ArrayList<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            copia.add(actual.getInfo());
            actual = actual.getNext();
        }
        return copia;
    }
}

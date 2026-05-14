package org.proyecto2.structure.hash; // Definir el paquete de la tabla hash

public class CustomHashTable<K, V> { // Crear tabla hash genérica

    private HashNode<K, V>[] tabla; // Crear arreglo principal de nodos

    private int capacidad; // Guardar capacidad total de la tabla

    private int tamanio; // Guardar cantidad real de elementos insertados

    public CustomHashTable() { // Crear constructor vacío
        capacidad = 20; // Definir capacidad inicial de la tabla
        tabla = new HashNode[capacidad]; // Crear arreglo de nodos
        tamanio = 0; // Inicializar cantidad de elementos en cero
    } // Finalizar constructor

    private int obtenerIndice(K clave) { // Calcular posición de una clave en la tabla
        return Math.abs(clave.hashCode()) % capacidad; // Retornar índice calculado usando hashCode
    } // Finalizar metodo obtenerIndice

    public void insertar(K clave, V valor) { // Insertar un nuevo elemento en la tabla

        int indice = obtenerIndice(clave); // Obtener índice donde se almacenará el dato

        HashNode<K, V> nuevoNodo = new HashNode<>(clave, valor); // Crear nodo nuevo

        if (tabla[indice] == null) { // Verificar si la posición está vacía
            tabla[indice] = nuevoNodo; // Insertar nodo directamente
        } else { // Ejecutar lógica cuando exista colisión

            HashNode<K, V> actual = tabla[indice]; // Obtener primer nodo de la lista enlazada

            while (actual.getSiguiente() != null) { // Recorrer mientras exista otro nodo

                if (actual.getClave().equals(clave)) { // Verificar si la clave ya existe
                    actual.setValor(valor); // Actualizar valor existente
                    return; // Finalizar metodo
                } // Finalizar validación de clave

                actual = actual.getSiguiente(); // Avanzar al siguiente nodo
            } // Finalizar recorrido

            if (actual.getClave().equals(clave)) { // Verificar último nodo
                actual.setValor(valor); // Actualizar valor del último nodo
                return; // Finalizar metodo
            } // Finalizar validación final

            actual.setSiguiente(nuevoNodo); // Enlazar nuevo nodo al final de la lista
        } // Finalizar manejo de colisiones

        tamanio++; // Aumentar cantidad de elementos
    } // Finalizar metodo insertar

    public V obtener(K clave) { // Buscar un valor usando su clave

        int indice = obtenerIndice(clave); // Obtener índice correspondiente

        HashNode<K, V> actual = tabla[indice]; // Obtener primer nodo de la posición

        while (actual != null) { // Recorrer lista enlazada de la posición

            if (actual.getClave().equals(clave)) { // Verificar coincidencia de clave
                return actual.getValor(); // Retornar valor encontrado
            } // Finalizar validación de coincidencia

            actual = actual.getSiguiente(); // Avanzar al siguiente nodo
        } // Finalizar recorrido

        return null; // Retornar null si la clave no existe
    } // Finalizar metodo obtener

    public boolean contieneClave(K clave) { // Verificar si una clave existe en la tabla
        return obtener(clave) != null; // Retornar verdadero si la clave existe
    } // Finalizar metodo contieneClave

    public void eliminar(K clave) { // Eliminar un elemento usando su clave

        int indice = obtenerIndice(clave); // Obtener índice correspondiente

        HashNode<K, V> actual = tabla[indice]; // Obtener nodo inicial

        HashNode<K, V> anterior = null; // Guardar referencia al nodo anterior

        while (actual != null) { // Recorrer lista enlazada

            if (actual.getClave().equals(clave)) { // Verificar coincidencia de clave

                if (anterior == null) { // Verificar si el nodo es el primero
                    tabla[indice] = actual.getSiguiente(); // Mover inicio de la lista
                } else { // Ejecutar cuando exista nodo anterior
                    anterior.setSiguiente(actual.getSiguiente()); // Saltar nodo eliminado
                } // Finalizar condición de eliminación

                tamanio--; // Reducir cantidad de elementos
                return; // Finalizar metodo
            } // Finalizar validación de clave

            anterior = actual; // Guardar nodo actual como anterior
            actual = actual.getSiguiente(); // Avanzar al siguiente nodo
        } // Finalizar recorrido
    } // Finalizar metodo eliminar

    public int getTamanio() { // Obtener cantidad de elementos almacenados
        return tamanio; // Retornar tamaño actual
    } // Finalizar metodo getTamanio

    public void limpiar() { // Vaciar completamente la tabla hash
        tabla = new HashNode[capacidad]; // Crear una tabla nueva con la misma capacidad
        tamanio = 0; // Reiniciar la cantidad de elementos almacenados
    } // Finalizar metodo limpiar
} // Finalizar clase CustomHashTable
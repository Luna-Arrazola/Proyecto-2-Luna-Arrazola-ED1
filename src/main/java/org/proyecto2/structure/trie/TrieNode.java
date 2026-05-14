package org.proyecto2.structure.trie; // Definir el paquete del Trie

public class TrieNode<T> { // Crear nodo genérico para el árbol de prefijos

    private char letra; // Guardar la letra que representa este nodo

    private boolean finPalabra; // Marcar si en este nodo termina una palabra completa

    private T dato; // Guardar el objeto asociado a la palabra completa

    private TrieNode<T>[] hijos; // Guardar los hijos del nodo usando arreglo propio

    public TrieNode(char letra) { // Crear constructor del nodo
        this.letra = letra; // Asignar la letra recibida
        this.finPalabra = false; // Iniciar indicando que no termina palabra
        this.dato = null; // Iniciar sin dato asociado
        this.hijos = new TrieNode[256]; // Crear arreglo para caracteres comunes ASCII
    } // Finalizar constructor

    public char getLetra() { // Obtener la letra del nodo
        return letra; // Retornar la letra almacenada
    } // Finalizar getLetra

    public boolean isFinPalabra() { // Verificar si el nodo termina una palabra
        return finPalabra; // Retornar el estado de fin de palabra
    } // Finalizar isFinPalabra

    public void setFinPalabra(boolean finPalabra) { // Modificar si el nodo termina palabra
        this.finPalabra = finPalabra; // Asignar el nuevo estado
    } // Finalizar setFinPalabra

    public T getDato() { // Obtener el dato guardado en el nodo
        return dato; // Retornar el dato asociado
    } // Finalizar getDato

    public void setDato(T dato) { // Modificar el dato asociado al nodo
        this.dato = dato; // Asignar el nuevo dato
    } // Finalizar setDato

    public TrieNode<T> getHijo(char letra) { // Obtener un hijo según una letra
        return hijos[(int) letra]; // Retornar el hijo ubicado en la posición ASCII
    } // Finalizar getHijo

    public void setHijo(char letra, TrieNode<T> nodo) { // Asignar un hijo según una letra
        hijos[(int) letra] = nodo; // Guardar el nodo hijo en la posición ASCII
    } // Finalizar setHijo

    public boolean tieneHijos() { // Verificar si el nodo tiene al menos un hijo
        for (int i = 0; i < hijos.length; i++) { // Recorrer todas las posiciones de hijos
            if (hijos[i] != null) { // Verificar si existe un hijo
                return true; // Retornar verdadero si se encuentra un hijo
            } // Finalizar validación de hijo
        } // Finalizar recorrido

        return false; // Retornar falso si no existen hijos
    } // Finalizar tieneHijos

    public TrieNode<T>[] getHijos() { // Obtener todos los hijos del nodo
        return hijos; // Retornar arreglo de hijos
    } // Finalizar getHijos
} // Finalizar clase TrieNode
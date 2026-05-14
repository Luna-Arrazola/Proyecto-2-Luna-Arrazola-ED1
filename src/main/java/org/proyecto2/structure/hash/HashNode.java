package org.proyecto2.structure.hash; // Definir el paquete de la estructura hash

public class HashNode<K, V> { // Crear nodo genérico para guardar pares clave valor

    private K clave; // Guardar la clave del nodo

    private V valor; // Guardar el valor asociado a la clave

    private HashNode<K, V> siguiente; // Guardar referencia al siguiente nodo para manejar colisiones

    public HashNode(K clave, V valor) { // Crear constructor del nodo
        this.clave = clave; // Asignar la clave recibida
        this.valor = valor; // Asignar el valor recibido
        this.siguiente = null; // Inicializar el siguiente nodo como vacío
    } // Finalizar constructor

    public K getClave() { // Obtener la clave almacenada
        return clave; // Retornar la clave actual
    } // Finalizar getClave

    public void setClave(K clave) { // Modificar la clave almacenada
        this.clave = clave; // Asignar la nueva clave
    } // Finalizar setClave

    public V getValor() { // Obtener el valor almacenado
        return valor; // Retornar el valor actual
    } // Finalizar getValor

    public void setValor(V valor) { // Modificar el valor almacenado
        this.valor = valor; // Asignar el nuevo valor
    } // Finalizar setValor

    public HashNode<K, V> getSiguiente() { // Obtener el siguiente nodo enlazado
        return siguiente; // Retornar referencia al siguiente nodo
    } // Finalizar getSiguiente

    public void setSiguiente(HashNode<K, V> siguiente) { // Modificar referencia al siguiente nodo
        this.siguiente = siguiente; // Asignar el nuevo siguiente nodo
    } // Finalizar setSiguiente
} // Finalizar clase HashNode
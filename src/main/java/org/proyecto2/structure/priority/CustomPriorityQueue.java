package org.proyecto2.structure.priority; // Definir el paquete donde estará la cola de prioridad propia

import java.util.Comparator; // Importar Comparator para permitir inyectar criterios de orden
import org.proyecto2.structure.common.CustomArrayList; // Importar lista propia para devolver resultados sin usar ArrayList

public class CustomPriorityQueue<T> { // Crear cola de prioridad genérica usando heap propio

    private Object[] heap; // Guardar los elementos dentro de un arreglo que representa el heap

    private int tamanio; // Guardar la cantidad real de elementos dentro del heap

    private Comparator<T> comparador; // Guardar el criterio de comparación recibido desde afuera

    public CustomPriorityQueue(Comparator<T> comparador) { // Crear constructor recibiendo el comportamiento de orden
        this.heap = new Object[10]; // Crear arreglo inicial con capacidad de diez elementos
        this.tamanio = 0; // Iniciar el heap vacío
        this.comparador = comparador; // Guardar el comparador recibido para ordenar los datos
    } // Finalizar constructor

    public void insertar(T dato) { // Insertar un nuevo dato en la cola de prioridad
        if (tamanio == heap.length) { // Verificar si el arreglo interno está lleno
            crecer(); // Aumentar la capacidad del arreglo
        } // Finalizar validación de capacidad

        heap[tamanio] = dato; // Colocar el nuevo dato al final del heap
        subir(tamanio); // Reacomodar el dato hacia arriba según su prioridad
        tamanio++; // Aumentar la cantidad real de elementos
    } // Finalizar metodo insertar

    public T extraer() { // Extraer el dato con mayor prioridad según el comparador
        if (estaVacia()) { // Verificar si no hay elementos
            return null; // Retornar null si el heap está vacío
        } // Finalizar validación de vacío

        T raiz = (T) heap[0]; // Guardar el dato de mayor prioridad ubicado en la raíz
        heap[0] = heap[tamanio - 1]; // Mover el último dato hacia la raíz
        heap[tamanio - 1] = null; // Limpiar la última posición
        tamanio--; // Reducir la cantidad real de elementos

        if (!estaVacia()) { // Verificar si todavía quedan elementos
            bajar(0); // Reacomodar la nueva raíz hacia abajo
        } // Finalizar validación de reacomodo

        return raiz; // Retornar el dato extraído
    } // Finalizar metodo extraer

    public T verPrimero() { // Ver el dato con mayor prioridad sin eliminarlo
        if (estaVacia()) { // Verificar si el heap está vacío
            return null; // Retornar null cuando no hay datos
        } // Finalizar validación de vacío

        return (T) heap[0]; // Retornar el dato ubicado en la raíz
    } // Finalizar metodo verPrimero

    public boolean estaVacia() { // Verificar si la cola no tiene elementos
        return tamanio == 0; // Retornar verdadero si el tamaño es cero
    } // Finalizar metodo estaVacia

    public int getTamanio() { // Obtener la cantidad de elementos almacenados
        return tamanio; // Retornar el tamaño actual
    } // Finalizar metodo getTamanio

    public CustomArrayList<T> extraerTodos() { // Extraer todos los elementos en orden de prioridad
        CustomArrayList<T> resultados = new CustomArrayList<>(); // Crear lista propia para guardar resultados ordenados

        while (!estaVacia()) { // Repetir mientras existan elementos en el heap
            resultados.agregar(extraer()); // Extraer el primero y agregarlo a la lista
        } // Finalizar ciclo de extracción

        return resultados; // Retornar lista ordenada según la prioridad
    } // Finalizar metodo extraerTodos

    public CustomArrayList<T> extraerConLimite(int limite) { // Extraer elementos aplicando límite de resultados
        CustomArrayList<T> resultados = new CustomArrayList<>(); // Crear lista propia para guardar resultados limitados

        int contador = 0; // Crear contador para controlar cuántos datos se extraen

        while (!estaVacia() && contador < limite) { // Repetir mientras haya datos y no se alcance el límite
            resultados.agregar(extraer()); // Extraer el dato con mayor prioridad
            contador++; // Aumentar contador de resultados extraídos
        } // Finalizar ciclo limitado

        return resultados; // Retornar resultados ordenados y limitados
    } // Finalizar metodo extraerConLimite

    private void subir(int indice) { // Subir un elemento mientras tenga mayor prioridad que su padre
        while (indice > 0) { // Repetir mientras el elemento no esté en la raíz
            int padre = (indice - 1) / 2; // Calcular índice del padre

            T datoActual = (T) heap[indice]; // Obtener dato actual
            T datoPadre = (T) heap[padre]; // Obtener dato del padre

            if (comparador.compare(datoActual, datoPadre) <= 0) { // Verificar si el actual no tiene mayor prioridad que el padre
                break; // Detener subida cuando el orden ya sea correcto
            } // Finalizar validación de prioridad

            intercambiar(indice, padre); // Intercambiar actual con padre
            indice = padre; // Actualizar índice para continuar subiendo
        } // Finalizar ciclo de subida
    } // Finalizar metodo subir

    private void bajar(int indice) { // Bajar un elemento hasta recuperar la propiedad del heap
        while (true) { // Repetir hasta que el dato quede en posición correcta
            int izquierdo = indice * 2 + 1; // Calcular índice del hijo izquierdo
            int derecho = indice * 2 + 2; // Calcular índice del hijo derecho
            int mayor = indice; // Suponer que el mayor es el índice actual

            if (izquierdo < tamanio) { // Verificar si existe hijo izquierdo
                T datoIzquierdo = (T) heap[izquierdo]; // Obtener dato del hijo izquierdo
                T datoMayor = (T) heap[mayor]; // Obtener dato considerado mayor

                if (comparador.compare(datoIzquierdo, datoMayor) > 0) { // Verificar si el hijo izquierdo tiene mayor prioridad
                    mayor = izquierdo; // Actualizar mayor con el hijo izquierdo
                } // Finalizar comparación con hijo izquierdo
            } // Finalizar validación del hijo izquierdo

            if (derecho < tamanio) { // Verificar si existe hijo derecho
                T datoDerecho = (T) heap[derecho]; // Obtener dato del hijo derecho
                T datoMayor = (T) heap[mayor]; // Obtener dato considerado mayor

                if (comparador.compare(datoDerecho, datoMayor) > 0) { // Verificar si el hijo derecho tiene mayor prioridad
                    mayor = derecho; // Actualizar mayor con el hijo derecho
                } // Finalizar comparación con hijo derecho
            } // Finalizar validación del hijo derecho

            if (mayor == indice) { // Verificar si el dato actual ya está en posición correcta
                break; // Detener bajada cuando no hay intercambio necesario
            } // Finalizar validación de posición

            intercambiar(indice, mayor); // Intercambiar dato actual con el hijo de mayor prioridad
            indice = mayor; // Actualizar índice para continuar bajando
        } // Finalizar ciclo de bajada
    } // Finalizar metodo bajar

    private void intercambiar(int primero, int segundo) { // Intercambiar dos posiciones del arreglo interno
        Object temporal = heap[primero]; // Guardar temporalmente el primer dato
        heap[primero] = heap[segundo]; // Mover el segundo dato a la primera posición
        heap[segundo] = temporal; // Mover el dato temporal a la segunda posición
    } // Finalizar metodo intercambiar

    private void crecer() { // Aumentar la capacidad del arreglo interno
        Object[] nuevoHeap = new Object[heap.length * 2]; // Crear arreglo nuevo con doble capacidad

        for (int i = 0; i < heap.length; i++) { // Recorrer el arreglo anterior
            nuevoHeap[i] = heap[i]; // Copiar cada dato al nuevo arreglo
        } // Finalizar ciclo de copia

        heap = nuevoHeap; // Reemplazar arreglo anterior por el nuevo
    } // Finalizar metodo crecer
} // Finalizar clase CustomPriorityQueue
package org.proyecto2.structure.trie; // Definir el paquete del Trie

import org.proyecto2.structure.common.CustomArrayList; // Importar lista propia para guardar resultados
import org.proyecto2.structure.common.ExtractorClave; // Importar interfaz funcional para extraer palabras

public class Trie<T> { // Crear Trie genérico para almacenar objetos usando una clave tipo texto

    private TrieNode<T> raiz; // Guardar la raíz del árbol

    private ExtractorClave<T, String> extractorClave; // Guardar función para obtener la palabra desde el objeto

    public Trie(ExtractorClave<T, String> extractorClave) { // Crear constructor con inyección de comportamiento
        this.raiz = new TrieNode<>('\0'); // Crear raíz vacía del Trie
        this.extractorClave = extractorClave; // Guardar función recibida para extraer la clave
    } // Finalizar constructor

    public void insertar(T dato) { // Insertar un objeto dentro del Trie
        String palabra = extractorClave.obtenerClave(dato); // Obtener la palabra usando la función inyectada

        if (palabra == null) { // Verificar si la palabra es nula
            return; // Finalizar si no hay palabra válida
        } // Finalizar validación nula

        palabra = palabra.toLowerCase(); // Convertir a minúsculas para búsquedas uniformes

        TrieNode<T> actual = raiz; // Iniciar recorrido desde la raíz

        for (int i = 0; i < palabra.length(); i++) { // Recorrer cada letra de la palabra
            char letra = palabra.charAt(i); // Obtener la letra actual

            if (actual.getHijo(letra) == null) { // Verificar si el hijo no existe
                actual.setHijo(letra, new TrieNode<>(letra)); // Crear nuevo nodo para esa letra
            } // Finalizar creación de hijo

            actual = actual.getHijo(letra); // Avanzar al hijo correspondiente
        } // Finalizar recorrido de letras

        actual.setFinPalabra(true); // Marcar que aquí termina una palabra
        actual.setDato(dato); // Guardar el objeto completo en el nodo final
    } // Finalizar insertar

    public T buscarExacto(String palabra) { // Buscar una palabra exacta dentro del Trie
        TrieNode<T> nodo = buscarNodo(palabra); // Buscar el nodo final de la palabra

        if (nodo != null && nodo.isFinPalabra()) { // Verificar que el nodo exista y termine palabra
            return nodo.getDato(); // Retornar el dato asociado
        } // Finalizar validación

        return null; // Retornar null si no existe
    } // Finalizar buscarExacto

    public CustomArrayList<T> buscarPorPrefijo(String prefijo) { // Buscar todas las palabras que inician con un prefijo
        CustomArrayList<T> resultados = new CustomArrayList<>(); // Crear lista propia para resultados

        TrieNode<T> nodoPrefijo = buscarNodo(prefijo); // Buscar nodo final del prefijo

        if (nodoPrefijo == null) { // Verificar si el prefijo no existe
            return resultados; // Retornar lista vacía
        } // Finalizar validación

        recolectar(nodoPrefijo, resultados); // Recolectar todos los datos debajo del prefijo

        return resultados; // Retornar resultados encontrados
    } // Finalizar buscarPorPrefijo

    public CustomArrayList<T> buscarPorComodin(String patron) { // Buscar palabras usando comodín con asterisco
        CustomArrayList<T> resultados = new CustomArrayList<>(); // Crear lista propia para resultados

        if (patron == null) { // Verificar si el patrón es nulo
            return resultados; // Retornar lista vacía
        } // Finalizar validación nula

        buscarComodinRecursivo(raiz, patron.toLowerCase(), 0, resultados); // Iniciar búsqueda recursiva desde la raíz

        return resultados; // Retornar resultados encontrados
    } // Finalizar buscarPorComodin

    public void eliminar(String palabra) { // Eliminar una palabra del Trie
        if (palabra == null) { // Verificar si la palabra es nula
            return; // Finalizar si no hay palabra válida
        } // Finalizar validación nula

        eliminarRecursivo(raiz, palabra.toLowerCase(), 0); // Iniciar eliminación recursiva
    } // Finalizar eliminar

    private TrieNode<T> buscarNodo(String palabra) { // Buscar el nodo final de una palabra o prefijo
        if (palabra == null) { // Verificar si la palabra es nula
            return null; // Retornar null si no hay texto válido
        } // Finalizar validación nula

        palabra = palabra.toLowerCase(); // Convertir a minúsculas para mantener consistencia

        TrieNode<T> actual = raiz; // Iniciar recorrido en la raíz

        for (int i = 0; i < palabra.length(); i++) { // Recorrer cada letra
            char letra = palabra.charAt(i); // Obtener letra actual

            if (actual.getHijo(letra) == null) { // Verificar si no existe el camino
                return null; // Retornar null porque la palabra o prefijo no existe
            } // Finalizar validación de hijo

            actual = actual.getHijo(letra); // Avanzar al siguiente nodo
        } // Finalizar recorrido

        return actual; // Retornar nodo final encontrado
    } // Finalizar buscarNodo

    private void recolectar(TrieNode<T> nodo, CustomArrayList<T> resultados) { // Recolectar palabras desde un nodo
        if (nodo.isFinPalabra()) { // Verificar si el nodo representa una palabra completa
            resultados.agregar(nodo.getDato()); // Agregar el dato a resultados
        } // Finalizar validación de palabra

        TrieNode<T>[] hijos = nodo.getHijos(); // Obtener todos los hijos del nodo

        for (int i = 0; i < hijos.length; i++) { // Recorrer todos los posibles hijos
            if (hijos[i] != null) { // Verificar si existe hijo en la posición
                recolectar(hijos[i], resultados); // Recolectar recursivamente desde el hijo
            } // Finalizar validación de hijo
        } // Finalizar recorrido de hijos
    } // Finalizar recolectar

    private void buscarComodinRecursivo(TrieNode<T> nodo, String patron, int posicion, CustomArrayList<T> resultados) { // Buscar coincidencias con comodín
        if (nodo == null) { // Verificar si el nodo no existe
            return; // Finalizar rama inválida
        } // Finalizar validación nula

        if (posicion == patron.length()) { // Verificar si se llegó al final del patrón
            if (nodo.isFinPalabra()) { // Verificar si el nodo es una palabra completa
                resultados.agregar(nodo.getDato()); // Agregar coincidencia encontrada
            } // Finalizar validación de palabra

            return; // Finalizar búsqueda en esta rama
        } // Finalizar validación de fin de patrón

        char simbolo = patron.charAt(posicion); // Obtener símbolo actual del patrón

        if (simbolo == '*') { // Verificar si el símbolo es comodín
            buscarComodinRecursivo(nodo, patron, posicion + 1, resultados); // Permitir que el comodín represente vacío

            TrieNode<T>[] hijos = nodo.getHijos(); // Obtener hijos para probar más letras

            for (int i = 0; i < hijos.length; i++) { // Recorrer hijos existentes
                if (hijos[i] != null) { // Verificar si existe hijo
                    buscarComodinRecursivo(hijos[i], patron, posicion, resultados); // Permitir que el comodín consuma una letra
                } // Finalizar validación de hijo
            } // Finalizar recorrido de hijos
        } else { // Ejecutar cuando el símbolo sea una letra normal
            buscarComodinRecursivo(nodo.getHijo(simbolo), patron, posicion + 1, resultados); // Avanzar siguiendo la letra exacta
        } // Finalizar validación de comodín
    } // Finalizar buscarComodinRecursivo

    private boolean eliminarRecursivo(TrieNode<T> nodo, String palabra, int posicion) { // Eliminar palabra y limpiar nodos innecesarios
        if (posicion == palabra.length()) { // Verificar si se llegó al final de la palabra
            if (!nodo.isFinPalabra()) { // Verificar si realmente no existe palabra completa
                return false; // Retornar falso porque no había nada que eliminar
            } // Finalizar validación de palabra

            nodo.setFinPalabra(false); // Quitar marca de fin de palabra
            nodo.setDato(null); // Quitar dato asociado

            return !nodo.tieneHijos(); // Indicar si el nodo puede eliminarse
        } // Finalizar caso base

        char letra = palabra.charAt(posicion); // Obtener letra actual

        TrieNode<T> hijo = nodo.getHijo(letra); // Obtener hijo correspondiente

        if (hijo == null) { // Verificar si el camino no existe
            return false; // Retornar falso porque la palabra no existe
        } // Finalizar validación de camino

        boolean eliminarHijo = eliminarRecursivo(hijo, palabra, posicion + 1); // Intentar eliminar desde el hijo

        if (eliminarHijo) { // Verificar si el hijo quedó sin uso
            nodo.setHijo(letra, null); // Quitar referencia al hijo
            return !nodo.isFinPalabra() && !nodo.tieneHijos(); // Indicar si este nodo también puede limpiarse
        } // Finalizar limpieza de hijo

        return false; // Retornar falso si no se debe eliminar este nodo
    } // Finalizar eliminarRecursivo
} // Finalizar clase Trie
package org.proyecto2.structure.common; // Definir el paquete de estructuras comunes

public class CustomArrayList<T> { // Crear una lista dinámica propia usando genéricos

    private Object[] datos; // Guardar los elementos en un arreglo interno

    private int tamanio; // Guardar la cantidad real de elementos insertados

    public CustomArrayList() { // Crear constructor de la lista
        datos = new Object[10]; // Crear arreglo inicial con capacidad de diez elementos
        tamanio = 0; // Iniciar la lista sin elementos
    } // Finalizar constructor

    public void agregar(T dato) { // Agregar un dato al final de la lista
        if (tamanio == datos.length) { // Verificar si el arreglo está lleno
            crecer(); // Aumentar la capacidad del arreglo interno
        } // Finalizar condición de crecimiento

        datos[tamanio] = dato; // Insertar el dato en la siguiente posición disponible
        tamanio++; // Aumentar la cantidad real de elementos
    } // Finalizar metodo agregar

    public T obtener(int indice) { // Obtener un dato por su posición
        validarIndice(indice); // Validar que el índice sea correcto
        return (T) datos[indice]; // Retornar el dato convertido al tipo genérico
    } // Finalizar metodo obtener

    public void actualizar(int indice, T dato) { // Actualizar un dato existente
        validarIndice(indice); // Validar que el índice exista
        datos[indice] = dato; // Reemplazar el dato anterior por el nuevo
    } // Finalizar metodo actualizar

    public T eliminarEn(int indice) { // Eliminar un dato por posición
        validarIndice(indice); // Validar que el índice exista

        T eliminado = (T) datos[indice]; // Guardar el dato que será eliminado

        for (int i = indice; i < tamanio - 1; i++) { // Recorrer desde el índice eliminado hasta el penúltimo dato
            datos[i] = datos[i + 1]; // Mover cada dato una posición a la izquierda
        } // Finalizar ciclo de desplazamiento

        datos[tamanio - 1] = null; // Limpiar la última posición repetida
        tamanio--; // Reducir la cantidad real de elementos

        return eliminado; // Retornar el dato eliminado
    } // Finalizar metodo eliminarEn

    public int tamanio() { // Obtener la cantidad de elementos guardados
        return tamanio; // Retornar el tamaño actual
    } // Finalizar metodo tamanio

    public boolean estaVacia() { // Verificar si la lista está vacía
        return tamanio == 0; // Retornar verdadero si no hay elementos
    } // Finalizar metodo estaVacia

    public Object[] convertirArreglo() { // Convertir la lista a un arreglo simple
        Object[] copia = new Object[tamanio]; // Crear una copia con tamaño exacto

        for (int i = 0; i < tamanio; i++) { // Recorrer todos los elementos existentes
            copia[i] = datos[i]; // Copiar cada elemento al nuevo arreglo
        } // Finalizar ciclo de copia

        return copia; // Retornar el arreglo copiado
    } // Finalizar metodo convertirArreglo

    public void limpiar() { // Vaciar todos los elementos de la lista
        for (int i = 0; i < tamanio; i++) { // Recorrer las posiciones ocupadas
            datos[i] = null; // Eliminar la referencia del dato
        } // Finalizar ciclo de limpieza

        tamanio = 0; // Reiniciar el tamaño de la lista
    } // Finalizar metodo limpiar

    private void crecer() { // Aumentar la capacidad del arreglo interno
        Object[] nuevoArreglo = new Object[datos.length * 2]; // Crear arreglo con el doble de capacidad

        for (int i = 0; i < datos.length; i++) { // Recorrer el arreglo anterior
            nuevoArreglo[i] = datos[i]; // Copiar cada dato al nuevo arreglo
        } // Finalizar ciclo de copia

        datos = nuevoArreglo; // Reemplazar el arreglo anterior por el nuevo
    } // Finalizar metodo crecer

    private void validarIndice(int indice) { // Validar que una posición exista en la lista
        if (indice < 0 || indice >= tamanio) { // Verificar si el índice está fuera del rango permitido
            throw new IndexOutOfBoundsException("Índice fuera del rango de la lista"); // Lanzar error cuando el índice no sea válido
        } // Finalizar condición de validación
    } // Finalizar metodo validarIndice
} // Finalizar clase CustomArrayList
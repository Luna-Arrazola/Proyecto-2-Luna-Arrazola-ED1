package org.proyecto2.structure.common; // Definir el paquete de estructuras comunes

@FunctionalInterface // Indicar que la interfaz permite usarse con lambdas
public interface ExtractorClave<T, K> { // Crear interfaz genérica para extraer claves desde objetos

    K obtenerClave(T dato); // Obtener una clave específica desde un dato recibido
} // Finalizar interfaz ExtractorClave
package org.proyecto2.model; // Definir el paquete donde guardar solicitudes del API

public class PalabraRequest { // Crear clase para recibir datos al guardar una palabra nueva

    private String palabra; // Guardar la palabra enviada desde Postman

    private String significado; // Guardar el significado enviado desde Postman

    public PalabraRequest() { // Crear constructor vacío para permitir conversión desde JSON
    } // Finalizar constructor vacío

    public String getPalabra() { // Obtener la palabra enviada
        return palabra; // Retornar la palabra recibida
    } // Finalizar getPalabra

    public void setPalabra(String palabra) { // Modificar la palabra enviada
        this.palabra = palabra; // Asignar la palabra recibida
    } // Finalizar setPalabra

    public String getSignificado() { // Obtener el significado enviado
        return significado; // Retornar el significado recibido
    } // Finalizar getSignificado

    public void setSignificado(String significado) { // Modificar el significado enviado
        this.significado = significado; // Asignar el significado recibido
    } // Finalizar setSignificado
} // Finalizar clase PalabraRequest
package org.proyecto2.model; // Definir el paquete donde guardar el modelo principal

public class Palabra implements Comparable<Palabra> { // Crear la clase Palabra y permitir comparación alfabética natural

    private int id; // Guardar el identificador único de la palabra

    private String palabra; // Guardar el texto de la palabra

    private String significado; // Guardar el significado de la palabra

    private int frecuencia; // Guardar la frecuencia de uso de la palabra

    public Palabra() { // Crear constructor vacío para permitir recibir JSON desde Spring
    } // Finalizar constructor vacío

    public Palabra(int id, String palabra, String significado, int frecuencia) { // Crear constructor con todos los datos de una palabra
        this.id = id; // Asignar el id recibido
        this.palabra = palabra; // Asignar la palabra recibida
        this.significado = significado; // Asignar el significado recibido
        this.frecuencia = frecuencia; // Asignar la frecuencia recibida
    } // Finalizar constructor con parámetros

    public int getId() { // Obtener el id de la palabra
        return id; // Retornar el id actual
    } // Finalizar getId

    public void setId(int id) { // Modificar el id de la palabra
        this.id = id; // Asignar el nuevo id
    } // Finalizar setId

    public String getPalabra() { // Obtener el texto de la palabra
        return palabra; // Retornar la palabra actual
    } // Finalizar getPalabra

    public void setPalabra(String palabra) { // Modificar el texto de la palabra
        this.palabra = palabra; // Asignar la nueva palabra
    } // Finalizar setPalabra

    public String getSignificado() { // Obtener el significado de la palabra
        return significado; // Retornar el significado actual
    } // Finalizar getSignificado

    public void setSignificado(String significado) { // Modificar el significado de la palabra
        this.significado = significado; // Asignar el nuevo significado
    } // Finalizar setSignificado

    public int getFrecuencia() { // Obtener la frecuencia de uso
        return frecuencia; // Retornar la frecuencia actual
    } // Finalizar getFrecuencia

    public void setFrecuencia(int frecuencia) { // Modificar la frecuencia de uso
        this.frecuencia = frecuencia; // Asignar la nueva frecuencia
    } // Finalizar setFrecuencia

    public void aumentarFrecuencia() { // Aumentar la frecuencia cuando se consulta la palabra
        this.frecuencia++; // Sumar uno a la frecuencia actual
    } // Finalizar aumentarFrecuencia

    @Override // Indicar que se sobrescribe el metodo compareTo
    public int compareTo(Palabra otraPalabra) { // Comparar dos palabras alfabéticamente
        return this.palabra.compareTo(otraPalabra.getPalabra()); // Retornar comparación usando el texto de la palabra
    } // Finalizar compareTo
} // Finalizar clase Palabra
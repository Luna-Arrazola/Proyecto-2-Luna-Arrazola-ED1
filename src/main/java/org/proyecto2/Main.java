package org.proyecto2; // Definir el paquete principal del proyecto

import org.springframework.boot.SpringApplication; // Importar clase para iniciar aplicación Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Importar anotación para configurar automáticamente Spring

@SpringBootApplication // Indicar que esta es la clase principal del proyecto Spring Boot
public class Main { // Crear clase principal para iniciar el sistema del diccionario

    public static void main(String[] args) { // Crear metodo principal que ejecuta el programa

        SpringApplication.run(Main.class, args); // Iniciar Spring Boot y levantar el servidor Tomcat en el puerto 8080

    } // Finalizar metodo main
} // Finalizar clase Main
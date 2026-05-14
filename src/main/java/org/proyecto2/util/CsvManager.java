package org.proyecto2.util; // Definir el paquete de utilidades del proyecto

// Para esta sección se visualizaron algunos videos de YouTube y se utilizó apoyo de IA para comprender cómo realizar la importación, exportación y persistencia básica de csv sin usar colecciones de Java

import java.io.BufferedReader; // Importar lector eficiente de archivos
import java.io.BufferedWriter; // Importar escritor eficiente de archivos
import java.io.File; // Importar clase File para manejar archivos
import java.io.FileReader; // Importar lector de archivos
import java.io.FileWriter; // Importar escritor de archivos
import java.io.IOException; // Importar manejo de errores de entrada y salida

import org.proyecto2.model.Palabra; // Importar modelo Palabra
import org.proyecto2.structure.common.CustomArrayList; // Importar lista propia

public class CsvManager { // Crear clase encargada de importar y exportar CSV

    private static final String RUTA = "data/diccionario.csv"; // Guardar ruta del archivo CSV

    public static CustomArrayList<Palabra> cargarCsv() { // Cargar todas las palabras desde el archivo CSV

        CustomArrayList<Palabra> palabras = new CustomArrayList<>(); // Crear lista para guardar palabras cargadas

        try { // Intentar abrir y leer el archivo

            File archivo = new File(RUTA); // Crear referencia al archivo CSV

            if (!archivo.exists()) { // Verificar si el archivo no existe
                archivo.createNewFile(); // Crear archivo automáticamente si no existe
                return palabras; // Retornar lista vacía
            } // Finalizar validación de existencia

            BufferedReader lector = new BufferedReader(new FileReader(archivo)); // Crear lector del archivo

            String linea = lector.readLine(); // Leer encabezado del CSV

            while ((linea = lector.readLine()) != null) { // Leer línea por línea hasta llegar al final

                String[] partes = linea.split(","); // Separar los datos usando coma

                if (partes.length < 4) { // Verificar que la línea tenga todos los datos necesarios
                    continue; // Saltar línea inválida
                } // Finalizar validación de datos

                int id = Integer.parseInt(partes[0]); // Convertir id desde texto a entero

                String palabra = partes[1]; // Obtener palabra desde el CSV

                String significado = partes[2]; // Obtener significado desde el CSV

                int frecuencia = Integer.parseInt(partes[3]); // Convertir frecuencia a entero

                Palabra nuevaPalabra = new Palabra(id, palabra, significado, frecuencia); // Crear objeto Palabra

                palabras.agregar(nuevaPalabra); // Agregar palabra cargada a la lista
            } // Finalizar lectura de líneas

            lector.close(); // Cerrar lector del archivo

        } catch (IOException e) { // Capturar errores de lectura o escritura
            System.out.println("Error al cargar CSV"); // Mostrar mensaje de error simple
        } // Finalizar captura de errores

        return palabras; // Retornar lista de palabras cargadas
    } // Finalizar metodo cargarCsv

    public static void guardarCsv(CustomArrayList<Palabra> palabras) { // Guardar todas las palabras dentro del CSV

        try { // Intentar escribir archivo

            BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA)); // Crear escritor del archivo

            escritor.write("id,palabra,significado,frecuencia"); // Encabezado del CSV

            escritor.newLine(); // Saltar a la siguiente línea

            for (int i = 0; i < palabras.tamanio(); i++) { // Recorrer todas las palabras de la lista

                Palabra palabra = palabras.obtener(i); // Obtener palabra actual

                String linea = palabra.getId() + "," + // Construir línea con el id
                        palabra.getPalabra() + "," + // Agregar palabra
                        palabra.getSignificado() + "," + // Agregar significado
                        palabra.getFrecuencia(); // Agregar frecuencia

                escritor.write(linea); // Escribir línea en el archivo

                escritor.newLine(); // Crear nueva línea en el CSV
            } // Finalizar recorrido de palabras

            escritor.close(); // Cerrar escritor del archivo

        } catch (IOException e) { // Capturar errores de escritura
            System.out.println("Error al guardar CSV"); // Mostrar mensaje simple de error
        } // Finalizar captura de errores
    } // Finalizar metodo guardarCsv
} // Finalizar clase CsvManager
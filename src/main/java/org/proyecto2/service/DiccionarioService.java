package org.proyecto2.service; // Definir el paquete donde estará la lógica principal del diccionario

import java.util.Comparator; // Importar Comparator para cambiar criterios de ordenamiento

import org.proyecto2.model.Palabra; // Importar el modelo Palabra
import org.proyecto2.model.PalabraRequest; // Importar el modelo de solicitud para crear palabras
import org.proyecto2.structure.common.CustomArrayList; // Importar lista propia para no usar ArrayList
import org.proyecto2.structure.hash.CustomHashTable; // Importar tabla hash propia
import org.proyecto2.structure.priority.CustomPriorityQueue; // Importar cola de prioridad propia
import org.proyecto2.structure.trie.Trie; // Importar árbol de prefijos propio
import org.proyecto2.util.CsvManager; // Importar administrador de CSV
import org.springframework.stereotype.Service; // Importar anotación para indicar servicio de Spring

@Service // Indicar que Spring debe manejar esta clase como servicio
public class DiccionarioService { // Crear servicio para conectar API, estructuras y CSV

    private Trie<Palabra> trie; // Guardar Trie para búsquedas por prefijo y comodín

    private CustomHashTable<String, Palabra> tablaPorPalabra; // Guardar tabla hash para búsqueda exacta por palabra

    private CustomHashTable<Integer, Palabra> tablaPorId; // Guardar tabla hash para búsqueda por ID

    private CustomArrayList<Palabra> listaPalabras; // Guardar todas las palabras en una lista propia

    private int siguienteId; // Guardar el siguiente ID secuencial disponible

    public DiccionarioService() { // Crear constructor del servicio

        this.trie = new Trie<>(palabra -> palabra.getPalabra()); // Crear Trie usando lambda para extraer el texto de Palabra

        this.tablaPorPalabra = new CustomHashTable<>(); // Crear tabla hash para guardar palabras por texto

        this.tablaPorId = new CustomHashTable<>(); // Crear tabla hash para guardar palabras por ID

        this.listaPalabras = new CustomArrayList<>(); // Crear lista propia para guardar todas las palabras

        this.siguienteId = 1; // Iniciar el ID secuencial en uno

        cargarDatosIniciales(); // Cargar datos guardados en CSV al iniciar el programa

    } // Finalizar constructor

    public Palabra guardarPalabra(PalabraRequest request) { // Guardar una nueva palabra recibida desde el API

        validarTexto(request.getPalabra(), "La palabra no puede estar vacía"); // Validar que la palabra tenga texto

        validarTexto(request.getSignificado(), "El significado no puede estar vacío"); // Validar que el significado tenga texto

        String textoNormalizado = normalizarTexto(request.getPalabra()); // Normalizar palabra para evitar diferencias por mayúsculas

        Palabra existente = tablaPorPalabra.obtener(textoNormalizado); // Buscar si la palabra ya existe en la tabla hash

        if (existente != null) { // Verificar si ya existe una palabra igual
            throw new IllegalArgumentException("La palabra ya existe en el diccionario"); // Lanzar error para evitar duplicados
        } // Finalizar validación de duplicado

        Palabra nuevaPalabra = new Palabra(siguienteId, textoNormalizado, request.getSignificado(), 1); // Crear palabra nueva con frecuencia inicial uno

        siguienteId++; // Aumentar siguiente ID para mantener asignación secuencial

        insertarEnEstructuras(nuevaPalabra); // Insertar palabra en Hash, Trie y lista propia

        guardarEstado(); // Guardar cambios en CSV para conservar estado

        return nuevaPalabra; // Retornar palabra creada

    } // Finalizar metodo guardarPalabra

    public Palabra actualizarPalabra(Palabra palabraActualizada) { // Actualizar una palabra existente

        Palabra palabraExistente = tablaPorId.obtener(palabraActualizada.getId()); // Buscar palabra actual por ID

        if (palabraExistente == null) { // Verificar si el ID no existe
            throw new IllegalArgumentException("No existe una palabra con ese ID"); // Lanzar error si no se encuentra
        } // Finalizar validación de existencia

        validarTexto(palabraActualizada.getPalabra(), "La palabra no puede estar vacía"); // Validar nueva palabra

        validarTexto(palabraActualizada.getSignificado(), "El significado no puede estar vacío"); // Validar nuevo significado

        String palabraAnterior = palabraExistente.getPalabra(); // Guardar palabra anterior para eliminarla de estructuras

        String palabraNueva = normalizarTexto(palabraActualizada.getPalabra()); // Normalizar nueva palabra

        if (!palabraAnterior.equals(palabraNueva)) { // Verificar si realmente cambió el texto de la palabra

            Palabra repetida = tablaPorPalabra.obtener(palabraNueva); // Buscar si la nueva palabra ya existe

            if (repetida != null) { // Verificar duplicado por palabra
                throw new IllegalArgumentException("Ya existe otra palabra con ese texto"); // Lanzar error por duplicado
            } // Finalizar validación de duplicado

            tablaPorPalabra.eliminar(palabraAnterior); // Eliminar palabra anterior de la tabla por texto

            trie.eliminar(palabraAnterior); // Eliminar palabra anterior del Trie

        } // Finalizar validación de cambio de palabra

        palabraExistente.setPalabra(palabraNueva); // Actualizar texto de la palabra existente

        palabraExistente.setSignificado(palabraActualizada.getSignificado()); // Actualizar significado de la palabra existente

        palabraExistente.setFrecuencia(palabraActualizada.getFrecuencia()); // Actualizar frecuencia de la palabra existente

        tablaPorPalabra.insertar(palabraNueva, palabraExistente); // Insertar palabra actualizada en tabla por texto

        tablaPorId.insertar(palabraExistente.getId(), palabraExistente); // Actualizar palabra en tabla por ID

        trie.insertar(palabraExistente); // Insertar palabra actualizada en el Trie

        guardarEstado(); // Guardar cambios en CSV

        return palabraExistente; // Retornar palabra actualizada

    } // Finalizar metodo actualizarPalabra

    public Palabra buscarPorValor(String valor) { // Buscar palabra por ID o por texto exacto

        validarTexto(valor, "El valor de búsqueda no puede estar vacío"); // Validar valor recibido

        Palabra encontrada; // Crear variable para guardar resultado

        if (esNumero(valor)) { // Verificar si el valor recibido es numérico

            int id = Integer.parseInt(valor); // Convertir texto numérico a entero

            encontrada = tablaPorId.obtener(id); // Buscar palabra por ID en tabla hash

        } else { // Ejecutar búsqueda por texto cuando no sea número

            String textoNormalizado = normalizarTexto(valor); // Normalizar texto recibido

            encontrada = tablaPorPalabra.obtener(textoNormalizado); // Buscar palabra exacta en tabla hash

        } // Finalizar decisión de búsqueda

        if (encontrada == null) { // Verificar si no se encontró resultado
            return null; // Retornar null si no existe
        } // Finalizar validación de resultado

        encontrada.aumentarFrecuencia(); // Aumentar frecuencia por consulta exacta

        guardarEstado(); // Guardar frecuencia actualizada en CSV

        return encontrada; // Retornar palabra encontrada

    } // Finalizar metodo buscarPorValor

    public CustomArrayList<Palabra> buscarPorPrefijo(String prefijo, Integer limite, String orden, String ordenarPor) { // Buscar palabras por prefijo

        validarTexto(prefijo, "El prefijo no puede estar vacío"); // Validar que el prefijo tenga texto

        CustomArrayList<Palabra> resultados = trie.buscarPorPrefijo(normalizarTexto(prefijo)); // Buscar coincidencias usando Trie

        return ordenarYLimitar(resultados, limite, orden, ordenarPor); // Ordenar y limitar resultados usando heap propio

    } // Finalizar metodo buscarPorPrefijo

    public CustomArrayList<Palabra> buscarPorComodin(String patron, Integer limite, String orden, String ordenarPor) { // Buscar palabras usando comodín

        validarTexto(patron, "El patrón no puede estar vacío"); // Validar que el patrón tenga texto

        CustomArrayList<Palabra> resultados = trie.buscarPorComodin(normalizarTexto(patron)); // Buscar coincidencias usando Trie y comodín

        return ordenarYLimitar(resultados, limite, orden, ordenarPor); // Ordenar y limitar resultados usando heap propio

    } // Finalizar metodo buscarPorComodin

    public boolean eliminarPalabra(int id) { // Eliminar palabra por ID

        Palabra palabra = tablaPorId.obtener(id); // Buscar palabra existente por ID

        if (palabra == null) { // Verificar si no existe la palabra
            return false; // Retornar falso si no se puede eliminar
        } // Finalizar validación de existencia

        tablaPorId.eliminar(id); // Eliminar palabra de tabla por ID

        tablaPorPalabra.eliminar(palabra.getPalabra()); // Eliminar palabra de tabla por texto

        trie.eliminar(palabra.getPalabra()); // Eliminar palabra del Trie

        eliminarDeLista(id); // Eliminar palabra de la lista propia

        guardarEstado(); // Guardar eliminación en CSV

        return true; // Retornar verdadero si se eliminó correctamente

    } // Finalizar metodo eliminarPalabra

    public CustomArrayList<Palabra> obtenerTodas() { // Obtener todas las palabras del diccionario

        return listaPalabras; // Retornar lista propia con todas las palabras

    } // Finalizar metodo obtenerTodas

    private void cargarDatosIniciales() { // Cargar datos desde CSV al iniciar el servicio

        CustomArrayList<Palabra> palabrasCargadas = CsvManager.cargarCsv(); // Leer palabras guardadas en el archivo CSV

        int mayorId = 0; // Guardar el ID más alto encontrado

        for (int i = 0; i < palabrasCargadas.tamanio(); i++) { // Recorrer palabras cargadas desde CSV

            Palabra palabra = palabrasCargadas.obtener(i); // Obtener palabra actual

            insertarEnEstructuras(palabra); // Insertar palabra cargada en las estructuras

            if (palabra.getId() > mayorId) { // Verificar si el ID actual es mayor al registrado
                mayorId = palabra.getId(); // Actualizar mayor ID encontrado
            } // Finalizar validación de mayor ID

        } // Finalizar recorrido de palabras cargadas

        siguienteId = mayorId + 1; // Calcular siguiente ID disponible

    } // Finalizar metodo cargarDatosIniciales

    private void insertarEnEstructuras(Palabra palabra) { // Insertar una palabra en todas las estructuras principales

        tablaPorPalabra.insertar(palabra.getPalabra(), palabra); // Insertar palabra en hash por texto

        tablaPorId.insertar(palabra.getId(), palabra); // Insertar palabra en hash por ID

        trie.insertar(palabra); // Insertar palabra en Trie

        listaPalabras.agregar(palabra); // Agregar palabra a lista propia general

    } // Finalizar metodo insertarEnEstructuras

    private void eliminarDeLista(int id) { // Eliminar una palabra de la lista propia usando ID

        for (int i = 0; i < listaPalabras.tamanio(); i++) { // Recorrer lista completa de palabras

            Palabra palabra = listaPalabras.obtener(i); // Obtener palabra actual

            if (palabra.getId() == id) { // Verificar si el ID coincide
                listaPalabras.eliminarEn(i); // Eliminar palabra de la lista
                return; // Finalizar metodo después de eliminar
            } // Finalizar validación de coincidencia

        } // Finalizar recorrido

    } // Finalizar metodo eliminarDeLista

    private CustomArrayList<Palabra> ordenarYLimitar(CustomArrayList<Palabra> resultados, Integer limite, String orden, String ordenarPor) { // Ordenar resultados y aplicar límite

        Comparator<Palabra> comparador = crearComparador(orden, ordenarPor); // Crear comparador según parámetros recibidos

        CustomPriorityQueue<Palabra> cola = new CustomPriorityQueue<>(comparador); // Crear heap propio con criterio inyectado

        for (int i = 0; i < resultados.tamanio(); i++) { // Recorrer resultados sin ordenar

            cola.insertar(resultados.obtener(i)); // Insertar cada resultado en cola de prioridad

        } // Finalizar recorrido de resultados

        if (limite == null || limite <= 0) { // Verificar si no se recibió límite válido
            return cola.extraerTodos(); // Retornar todos los resultados ordenados
        } // Finalizar validación de límite

        return cola.extraerConLimite(limite); // Retornar resultados ordenados y limitados

    } // Finalizar metodo ordenarYLimitar

    private Comparator<Palabra> crearComparador(String orden, String ordenarPor) { // Crear comparador dinámico por alfabeto o frecuencia

        Comparator<Palabra> comparadorBase; // Guardar comparador base antes de aplicar ascendente o descendente

        if ("frecuencia".equalsIgnoreCase(ordenarPor)) { // Verificar si se desea ordenar por frecuencia

            comparadorBase = (a, b) -> Integer.compare(a.getFrecuencia(), b.getFrecuencia()); // Crear lambda para comparar por frecuencia

        } else { // Usar alfabeto cuando no se indique frecuencia

            comparadorBase = (a, b) -> a.getPalabra().compareTo(b.getPalabra()); // Crear lambda para comparar alfabéticamente

        } // Finalizar selección de criterio

        if ("desc".equalsIgnoreCase(orden)) { // Verificar si el orden solicitado es descendente

            return comparadorBase; // Retornar comparador base para que el heap extraiga mayores primero

        } // Finalizar validación de descendente

        return (a, b) -> comparadorBase.compare(b, a); // Invertir comparador para que el heap extraiga menores primero

    } // Finalizar metodo crearComparador

    private void guardarEstado() { // Guardar estado actual del diccionario

        CsvManager.guardarCsv(listaPalabras); // Escribir lista completa en el archivo CSV

    } // Finalizar metodo guardarEstado

    private String normalizarTexto(String texto) { // Normalizar texto para búsquedas consistentes

        return texto.trim().toLowerCase(); // Quitar espacios externos y convertir a minúsculas

    } // Finalizar metodo normalizarTexto

    private void validarTexto(String texto, String mensaje) { // Validar que un texto no sea nulo ni vacío

        if (texto == null || texto.trim().isEmpty()) { // Verificar si el texto es nulo o vacío
            throw new IllegalArgumentException(mensaje); // Lanzar error con mensaje específico
        } // Finalizar validación

    } // Finalizar metodo validarTexto

    private boolean esNumero(String texto) { // Verificar si un texto representa un número entero

        for (int i = 0; i < texto.length(); i++) { // Recorrer cada carácter del texto

            if (!Character.isDigit(texto.charAt(i))) { // Verificar si algún carácter no es dígito
                return false; // Retornar falso si hay un carácter no numérico
            } // Finalizar validación de carácter

        } // Finalizar recorrido

        return true; // Retornar verdadero si todos los caracteres son dígitos

    } // Finalizar metodo esNumero

    public String importarDesdeCsv() { // Importar diccionario desde el archivo CSV

        reiniciarEstructuras(); // Limpiar estructuras actuales antes de cargar nuevamente

        cargarDatosIniciales(); // Cargar datos desde el archivo CSV hacia Trie, Hash y lista

        return "Diccionario importado correctamente desde CSV"; // Retornar mensaje de éxito

    } // Finalizar metodo importarDesdeCsv

    public String exportarHaciaCsv() { // Exportar diccionario actual hacia el archivo CSV

        guardarEstado(); // Guardar lista actual de palabras dentro del archivo CSV

        return "Diccionario exportado correctamente hacia CSV"; // Retornar mensaje de éxito

    } // Finalizar metodo exportarHaciaCsv

    private void reiniciarEstructuras() { // Reiniciar estructuras para evitar datos duplicados al importar

        this.trie = new Trie<>(palabra -> palabra.getPalabra()); // Crear nuevamente el Trie usando lambda para extraer palabra

        this.tablaPorPalabra = new CustomHashTable<>(); // Crear nuevamente la tabla hash por palabra

        this.tablaPorId = new CustomHashTable<>(); // Crear nuevamente la tabla hash por ID

        this.listaPalabras = new CustomArrayList<>(); // Crear nuevamente la lista propia de palabras

        this.siguienteId = 1; // Reiniciar el siguiente ID antes de recalcularlo desde CSV

    } // Finalizar metodo reiniciarEstructuras

} // Finalizar clase DiccionarioService
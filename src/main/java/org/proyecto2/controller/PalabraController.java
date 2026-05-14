package org.proyecto2.controller; // Definir el paquete de controladores del API

import org.proyecto2.model.Palabra; // Importar modelo Palabra
import org.proyecto2.model.PalabraRequest; // Importar modelo de solicitud
import org.proyecto2.service.DiccionarioService; // Importar servicio principal del diccionario
import org.proyecto2.structure.common.CustomArrayList; // Importar lista propia

import org.springframework.beans.factory.annotation.Autowired; // Importar inyección automática de dependencias
import org.springframework.http.HttpStatus; // Importar códigos HTTP
import org.springframework.http.ResponseEntity; // Importar respuestas HTTP
import org.springframework.web.bind.annotation.DeleteMapping; // Importar anotación DELETE
import org.springframework.web.bind.annotation.GetMapping; // Importar anotación GET
import org.springframework.web.bind.annotation.PathVariable; // Importar variables de URL
import org.springframework.web.bind.annotation.PostMapping; // Importar anotación POST
import org.springframework.web.bind.annotation.PutMapping; // Importar anotación PUT
import org.springframework.web.bind.annotation.RequestBody; // Importar cuerpo JSON
import org.springframework.web.bind.annotation.RequestMapping; // Importar ruta base
import org.springframework.web.bind.annotation.RequestParam; // Importar parámetros de consulta
import org.springframework.web.bind.annotation.RestController; // Importar controlador REST

@RestController // Indicar que esta clase manejará solicitudes REST
@RequestMapping("/") // Definir ruta base del API
public class PalabraController { // Crear controlador principal del diccionario

    @Autowired // Inyectar automáticamente el servicio del diccionario
    private DiccionarioService diccionarioService; // Guardar referencia al servicio principal

    @PostMapping("/palabra") // Crear endpoint POST para guardar palabras nuevas
    public ResponseEntity<?> guardarPalabra(@RequestBody PalabraRequest request) { // Recibir solicitud JSON para crear palabra

        try { // Intentar guardar la palabra

            Palabra nuevaPalabra = diccionarioService.guardarPalabra(request); // Guardar palabra usando el servicio

            return ResponseEntity.ok(nuevaPalabra); // Retornar palabra creada con código 200

        } catch (IllegalArgumentException e) { // Capturar errores de validación

            return ResponseEntity.badRequest().body(e.getMessage()); // Retornar error 400 con mensaje

        } // Finalizar captura de errores

    } // Finalizar endpoint guardarPalabra

    @PutMapping("/palabra") // Crear endpoint PUT para actualizar palabras
    public ResponseEntity<?> actualizarPalabra(@RequestBody Palabra palabra) { // Recibir palabra completa para actualizar

        try { // Intentar actualizar palabra

            Palabra actualizada = diccionarioService.actualizarPalabra(palabra); // Actualizar palabra usando el servicio

            return ResponseEntity.ok(actualizada); // Retornar palabra actualizada

        } catch (IllegalArgumentException e) { // Capturar errores de validación

            return ResponseEntity.badRequest().body(e.getMessage()); // Retornar error 400

        } // Finalizar captura de errores

    } // Finalizar endpoint actualizarPalabra

    @GetMapping("/palabra/{valor}") // Crear endpoint GET para buscar por ID o palabra exacta
    public ResponseEntity<?> buscarPorValor(@PathVariable String valor) { // Recibir valor desde la URL

        Palabra encontrada = diccionarioService.buscarPorValor(valor); // Buscar palabra usando el servicio

        if (encontrada == null) { // Verificar si no existe resultado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Palabra no encontrada"); // Retornar error 404
        } // Finalizar validación de resultado

        return ResponseEntity.ok(encontrada); // Retornar palabra encontrada

    } // Finalizar endpoint buscarPorValor

    @GetMapping("/prefijo/{prefijo}") // Crear endpoint GET para búsqueda por prefijo
    public ResponseEntity<?> buscarPorPrefijo(
            @PathVariable String prefijo, // Recibir prefijo desde URL
            @RequestParam(required = false) Integer limite, // Recibir límite opcional
            @RequestParam(defaultValue = "asc") String orden, // Recibir tipo de orden ascendente o descendente
            @RequestParam(defaultValue = "alfabeto") String ordenarPor // Recibir criterio de ordenamiento
    ) { // Iniciar endpoint de búsqueda por prefijo

        CustomArrayList<Palabra> resultados = diccionarioService.buscarPorPrefijo(prefijo, limite, orden, ordenarPor); // Buscar resultados usando el servicio

        return ResponseEntity.ok(resultados.convertirArreglo()); // Retornar resultados convertidos a arreglo simple

    } // Finalizar endpoint buscarPorPrefijo

    @GetMapping("/comodin/{patron}") // Crear endpoint GET para búsqueda por comodín
    public ResponseEntity<?> buscarPorComodin(
            @PathVariable String patron, // Recibir patrón desde URL
            @RequestParam(required = false) Integer limite, // Recibir límite opcional
            @RequestParam(defaultValue = "asc") String orden, // Recibir orden ascendente o descendente
            @RequestParam(defaultValue = "alfabeto") String ordenarPor // Recibir criterio de ordenamiento
    ) { // Iniciar endpoint de búsqueda por comodín

        CustomArrayList<Palabra> resultados = diccionarioService.buscarPorComodin(patron, limite, orden, ordenarPor); // Buscar coincidencias usando el servicio

        return ResponseEntity.ok(resultados.convertirArreglo()); // Retornar resultados convertidos a arreglo

    } // Finalizar endpoint buscarPorComodin

    @DeleteMapping("/palabra/{id}") // Crear endpoint DELETE para eliminar palabras
    public ResponseEntity<?> eliminarPalabra(@PathVariable int id) { // Recibir ID desde la URL

        boolean eliminada = diccionarioService.eliminarPalabra(id); // Intentar eliminar palabra usando el servicio

        if (!eliminada) { // Verificar si no se pudo eliminar
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe palabra con ese ID"); // Retornar error 404
        } // Finalizar validación de eliminación

        return ResponseEntity.ok("Palabra eliminada correctamente"); // Retornar mensaje exitoso

    } // Finalizar endpoint eliminarPalabra

} // Finalizar clase PalabraController
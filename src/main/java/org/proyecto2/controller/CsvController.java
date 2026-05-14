package org.proyecto2.controller; // Definir el paquete de controladores del API

import org.proyecto2.service.DiccionarioService; // Importar servicio principal del diccionario
import org.springframework.beans.factory.annotation.Autowired; // Importar inyección automática de dependencias
import org.springframework.http.ResponseEntity; // Importar respuesta HTTP
import org.springframework.web.bind.annotation.GetMapping; // Importar anotación para solicitudes GET
import org.springframework.web.bind.annotation.PostMapping; // Importar anotación para solicitudes POST
import org.springframework.web.bind.annotation.RequestMapping; // Importar anotación para ruta base
import org.springframework.web.bind.annotation.RestController; // Importar anotación para controlador REST

@RestController // Indicar que esta clase manejará endpoints REST
@RequestMapping("/csv") // Definir ruta base para operaciones relacionadas con CSV
public class CsvController { // Crear controlador específico para importar y exportar CSV

    @Autowired // Inyectar automáticamente el servicio del diccionario
    private DiccionarioService diccionarioService; // Guardar referencia al servicio principal

    @PostMapping("/importar") // Crear endpoint para importar datos desde el CSV
    public ResponseEntity<?> importarCsv() { // Ejecutar importación desde Postman

        String mensaje = diccionarioService.importarDesdeCsv(); // Llamar al servicio para importar CSV

        return ResponseEntity.ok(mensaje); // Retornar mensaje exitoso con código 200

    } // Finalizar endpoint importarCsv

    @GetMapping("/exportar") // Crear endpoint para exportar datos hacia el CSV
    public ResponseEntity<?> exportarCsv() { // Ejecutar exportación desde Postman

        String mensaje = diccionarioService.exportarHaciaCsv(); // Llamar al servicio para guardar CSV

        return ResponseEntity.ok(mensaje); // Retornar mensaje exitoso con código 200

    } // Finalizar endpoint exportarCsv

} // Finalizar clase CsvController
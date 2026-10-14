package com.soa.apartamentos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soa.apartamentos.models.PropiedadModel;
import com.soa.apartamentos.models.UnidadRentableModel;
import com.soa.apartamentos.services.PropiedadService;
import com.soa.apartamentos.services.UnidadRentableService;

@RestController
@RequestMapping("/api/unidades-rentables")
public class UnidadRentableController {

    @Autowired
    private UnidadRentableService unidadService;

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping
    public List<UnidadRentableModel> listar() {
        return unidadService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        UnidadRentableModel unidad = unidadService.buscarPorId(id);
        if (unidad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la unidad");
        }
        return ResponseEntity.ok(unidad);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody UnidadRentableModel unidad) {
        if (unidad.getNombre() == null || unidad.getPropiedad() == null) {
            return ResponseEntity.badRequest().body("Faltan datos de la unidad");
        }
        if (unidad.getPrecioNoche() <= 0) {
            return ResponseEntity.badRequest().body("El precio por noche debe ser mayor a 0");
        }
        PropiedadModel propiedad = propiedadService.buscarPorId(unidad.getPropiedad().getIdPropiedad());
        if (propiedad == null) {
            return ResponseEntity.badRequest().body("La propiedad no existe");
        }
        unidad.setPropiedad(propiedad);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadService.guardar(unidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody UnidadRentableModel datos) {
        UnidadRentableModel unidad = unidadService.buscarPorId(id);
        if (unidad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la unidad");
        }
        datos.setIdUnidad(id);
        datos.setPropiedad(unidad.getPropiedad());
        return ResponseEntity.ok(unidadService.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (unidadService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la unidad");
        }
        if (!unidadService.sePuedeBorrar(id)) {
            return ResponseEntity.badRequest().body("No se puede borrar, la unidad tiene reservas o disponibilidades");
        }
        unidadService.borrar(id);
        return ResponseEntity.ok("Unidad eliminada");
    }
}

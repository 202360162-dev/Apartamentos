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

import com.soa.apartamentos.models.DisponibilidadModel;
import com.soa.apartamentos.models.UnidadRentableModel;
import com.soa.apartamentos.services.DisponibilidadService;
import com.soa.apartamentos.services.UnidadRentableService;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private UnidadRentableService unidadService;

    @GetMapping
    public List<DisponibilidadModel> listar() {
        return disponibilidadService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        DisponibilidadModel disponibilidad = disponibilidadService.buscarPorId(id);
        if (disponibilidad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la disponibilidad");
        }
        return ResponseEntity.ok(disponibilidad);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody DisponibilidadModel disponibilidad) {
        if (disponibilidad.getFechaInicio() == null || disponibilidad.getFechaFin() == null || disponibilidad.getUnidad() == null) {
            return ResponseEntity.badRequest().body("Faltan datos de la disponibilidad");
        }
        UnidadRentableModel unidad = unidadService.buscarPorId(disponibilidad.getUnidad().getIdUnidad());
        if (unidad == null) {
            return ResponseEntity.badRequest().body("La unidad no existe");
        }
        disponibilidad.setUnidad(unidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(disponibilidadService.guardar(disponibilidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody DisponibilidadModel datos) {
        DisponibilidadModel disponibilidad = disponibilidadService.buscarPorId(id);
        if (disponibilidad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la disponibilidad");
        }
        if (datos.getFechaInicio() == null || datos.getFechaFin() == null) {
            return ResponseEntity.badRequest().body("Faltan las fechas");
        }
        if (datos.getFechaFin().isBefore(datos.getFechaInicio()) || datos.getFechaFin().isEqual(datos.getFechaInicio())) {
            return ResponseEntity.badRequest().body("La fecha fin tiene que ser despues de la fecha inicio");
        }
        datos.setIdDisponibilidad(id);
        datos.setUnidad(disponibilidad.getUnidad());
        return ResponseEntity.ok(disponibilidadService.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (disponibilidadService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la disponibilidad");
        }
        disponibilidadService.borrar(id);
        return ResponseEntity.ok("Disponibilidad eliminada");
    }
}

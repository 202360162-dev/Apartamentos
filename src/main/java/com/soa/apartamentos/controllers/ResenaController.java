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

import com.soa.apartamentos.models.ResenaModel;
import com.soa.apartamentos.models.ReservaModel;
import com.soa.apartamentos.services.ResenaService;
import com.soa.apartamentos.services.ReservaService;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public List<ResenaModel> listar() {
        return resenaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        ResenaModel resena = resenaService.buscarPorId(id);
        if (resena == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la resena");
        }
        return ResponseEntity.ok(resena);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody ResenaModel resena) {
        if (resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            return ResponseEntity.badRequest().body("La calificacion va de 1 a 5");
        }
        if (resena.getReserva() == null) {
            return ResponseEntity.badRequest().body("Falta la reserva");
        }
        ReservaModel reserva = reservaService.buscarPorId(resena.getReserva().getIdReserva());
        if (reserva == null) {
            return ResponseEntity.badRequest().body("La reserva no existe");
        }
        resena.setReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.guardar(resena));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody ResenaModel datos) {
        ResenaModel resena = resenaService.buscarPorId(id);
        if (resena == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la resena");
        }
        if (datos.getCalificacion() < 1 || datos.getCalificacion() > 5) {
            return ResponseEntity.badRequest().body("La calificacion va de 1 a 5");
        }
        datos.setIdResena(id);
        datos.setReserva(resena.getReserva());
        return ResponseEntity.ok(resenaService.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (resenaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la resena");
        }
        resenaService.borrar(id);
        return ResponseEntity.ok("Resena eliminada");
    }
}

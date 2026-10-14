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

import com.soa.apartamentos.models.ClienteModel;
import com.soa.apartamentos.models.ReservaModel;
import com.soa.apartamentos.models.UnidadRentableModel;
import com.soa.apartamentos.services.ClienteService;
import com.soa.apartamentos.services.ReservaService;
import com.soa.apartamentos.services.UnidadRentableService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UnidadRentableService unidadService;

    @GetMapping
    public List<ReservaModel> listar() {
        return reservaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        ReservaModel reserva = reservaService.buscarPorId(id);
        if (reserva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la reserva");
        }
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody ReservaModel reserva) {
        if (reserva.getCliente() == null || reserva.getUnidad() == null) {
            return ResponseEntity.badRequest().body("Faltan el cliente o la unidad");
        }
        String error = validar(reserva);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        ClienteModel cliente = clienteService.buscarPorId(reserva.getCliente().getIdCliente());
        if (cliente == null) {
            return ResponseEntity.badRequest().body("El cliente no existe");
        }
        UnidadRentableModel unidad = unidadService.buscarPorId(reserva.getUnidad().getIdUnidad());
        if (unidad == null) {
            return ResponseEntity.badRequest().body("La unidad no existe");
        }
        if (reservaService.unidadConfirmada(unidad.getIdUnidad())) {
            return ResponseEntity.badRequest().body("La unidad ya tiene una reserva confirmada");
        }
        reserva.setCliente(cliente);
        reserva.setUnidad(unidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.guardar(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody ReservaModel datos) {
        ReservaModel reserva = reservaService.buscarPorId(id);
        if (reserva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la reserva");
        }
        String error = validar(datos);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        boolean seConfirma = "CONFIRMADA".equals(datos.getEstado()) && !"CONFIRMADA".equals(reserva.getEstado());
        if (seConfirma && reservaService.unidadConfirmada(reserva.getUnidad().getIdUnidad())) {
            return ResponseEntity.badRequest().body("La unidad ya tiene una reserva confirmada");
        }
        datos.setIdReserva(id);
        datos.setCliente(reserva.getCliente());
        datos.setUnidad(reserva.getUnidad());
        return ResponseEntity.ok(reservaService.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (reservaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la reserva");
        }
        if (!reservaService.sePuedeBorrar(id)) {
            return ResponseEntity.badRequest().body("No se puede borrar, la reserva tiene pagos o resenas");
        }
        reservaService.borrar(id);
        return ResponseEntity.ok("Reserva eliminada");
    }

    private String validar(ReservaModel reserva) {
        if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null) {
            return "Faltan las fechas de la reserva";
        }
        if (reserva.getFechaSalida().isBefore(reserva.getFechaEntrada()) || reserva.getFechaSalida().isEqual(reserva.getFechaEntrada())) {
            return "La fecha de salida tiene que ser despues de la de entrada";
        }
        if (reserva.getImporteTotal() <= 0) {
            return "El importe debe ser mayor a 0";
        }
        return null;
    }
}

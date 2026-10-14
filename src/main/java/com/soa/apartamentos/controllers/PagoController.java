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

import com.soa.apartamentos.models.PagoModel;
import com.soa.apartamentos.models.ReservaModel;
import com.soa.apartamentos.services.PagoService;
import com.soa.apartamentos.services.ReservaService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public List<PagoModel> listar() {
        return pagoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        PagoModel pago = pagoService.buscarPorId(id);
        if (pago == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el pago");
        }
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody PagoModel pago) {
        if (pago.getReserva() == null || pago.getImporte() == null) {
            return ResponseEntity.badRequest().body("Faltan datos del pago");
        }
        ReservaModel reserva = reservaService.buscarPorId(pago.getReserva().getIdReserva());
        if (reserva == null) {
            return ResponseEntity.badRequest().body("La reserva no existe");
        }
        if (!importeValido(pago.getImporte(), reserva)) {
            return ResponseEntity.badRequest().body("El importe debe ser mayor a 0 y no pasar del total de la reserva");
        }
        pago.setReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.guardar(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody PagoModel datos) {
        PagoModel pago = pagoService.buscarPorId(id);
        if (pago == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el pago");
        }
        if (datos.getImporte() == null) {
            return ResponseEntity.badRequest().body("Falta el importe");
        }
        if (!importeValido(datos.getImporte(), pago.getReserva())) {
            return ResponseEntity.badRequest().body("El importe debe ser mayor a 0 y no pasar del total de la reserva");
        }
        datos.setIdPago(id);
        datos.setReserva(pago.getReserva());
        return ResponseEntity.ok(pagoService.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (pagoService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el pago");
        }
        pagoService.borrar(id);
        return ResponseEntity.ok("Pago eliminado");
    }

    private boolean importeValido(double importe, ReservaModel reserva) {
        return importe > 0 && importe <= reserva.getImporteTotal();
    }
}

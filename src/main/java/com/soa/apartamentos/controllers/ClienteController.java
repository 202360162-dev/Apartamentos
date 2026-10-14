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
import com.soa.apartamentos.services.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteModel> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        ClienteModel cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el cliente");
        }
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody ClienteModel cliente) {
        String error = validar(cliente);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.guardar(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody ClienteModel cliente) {
        if (clienteService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el cliente");
        }
        String error = validar(cliente);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        cliente.setIdCliente(id);
        return ResponseEntity.ok(clienteService.guardar(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (clienteService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el cliente");
        }
        if (!clienteService.sePuedeBorrar(id)) {
            return ResponseEntity.badRequest().body("No se puede borrar, el cliente tiene reservas o propiedades");
        }
        clienteService.borrar(id);
        return ResponseEntity.ok("Cliente eliminado");
    }

    private String validar(ClienteModel cliente) {
        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            return "El nombre es obligatorio";
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            return "El email es obligatorio";
        }
        if (!cliente.getEmail().contains("@")) {
            return "El email no es valido";
        }
        return null;
    }
}

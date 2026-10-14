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
import com.soa.apartamentos.models.PropiedadModel;
import com.soa.apartamentos.services.ClienteService;
import com.soa.apartamentos.services.PropiedadService;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<PropiedadModel> listar() {
        return propiedadService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        PropiedadModel propiedad = propiedadService.buscarPorId(id);
        if (propiedad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la propiedad");
        }
        return ResponseEntity.ok(propiedad);
    }

    @GetMapping("/tipo/{tipo}")
    public List<PropiedadModel> buscarPorTipo(@PathVariable String tipo) {
        return propiedadService.buscarPorTipo(tipo);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody PropiedadModel propiedad) {
        if (propiedad.getTitulo() == null || propiedad.getCiudad() == null || propiedad.getPropietario() == null) {
            return ResponseEntity.badRequest().body("Faltan datos de la propiedad");
        }
        if (propiedad.getPrecioNoche() <= 0) {
            return ResponseEntity.badRequest().body("El precio por noche debe ser mayor a 0");
        }
        ClienteModel propietario = clienteService.buscarPorId(propiedad.getPropietario().getIdCliente());
        if (propietario == null) {
            return ResponseEntity.badRequest().body("El propietario no existe");
        }
        propiedad.setPropietario(propietario);
        return ResponseEntity.status(HttpStatus.CREATED).body(propiedadService.guardar(propiedad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody PropiedadModel datos) {
        PropiedadModel propiedad = propiedadService.buscarPorId(id);
        if (propiedad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la propiedad");
        }
        datos.setIdPropiedad(id);
        datos.setPropietario(propiedad.getPropietario());
        return ResponseEntity.ok(propiedadService.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (propiedadService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la propiedad");
        }
        if (!propiedadService.sePuedeBorrar(id)) {
            return ResponseEntity.badRequest().body("No se puede borrar, la propiedad tiene unidades o imagenes");
        }
        propiedadService.borrar(id);
        return ResponseEntity.ok("Propiedad eliminada");
    }
}

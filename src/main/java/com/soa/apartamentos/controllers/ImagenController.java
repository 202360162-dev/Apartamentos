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

import com.soa.apartamentos.models.ImagenModel;
import com.soa.apartamentos.models.PropiedadModel;
import com.soa.apartamentos.services.ImagenService;
import com.soa.apartamentos.services.PropiedadService;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping
    public List<ImagenModel> listar() {
        return imagenService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable Long id) {
        ImagenModel imagen = imagenService.buscarPorId(id);
        if (imagen == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la imagen");
        }
        return ResponseEntity.ok(imagen);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody ImagenModel imagen) {
        if (imagen.getPropiedad() == null) {
            return ResponseEntity.badRequest().body("Falta la propiedad de la imagen");
        }
        PropiedadModel propiedad = propiedadService.buscarPorId(imagen.getPropiedad().getIdPropiedad());
        if (propiedad == null) {
            return ResponseEntity.badRequest().body("La propiedad no existe");
        }
        imagen.setPropiedad(propiedad);
        return ResponseEntity.status(HttpStatus.CREATED).body(imagenService.guardar(imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody ImagenModel datos) {
        ImagenModel imagen = imagenService.buscarPorId(id);
        if (imagen == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la imagen");
        }
        if (datos.getUrl() == null || datos.getUrl().isBlank()) {
            return ResponseEntity.badRequest().body("La url es obligatoria");
        }
        datos.setIdImagen(id);
        datos.setPropiedad(imagen.getPropiedad());
        return ResponseEntity.ok(imagenService.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrar(@PathVariable Long id) {
        if (imagenService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la imagen");
        }
        imagenService.borrar(id);
        return ResponseEntity.ok("Imagen eliminada");
    }
}

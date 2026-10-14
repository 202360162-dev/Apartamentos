package com.soa.apartamentos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.PropiedadModel;
import com.soa.apartamentos.repositories.IImagenRepository;
import com.soa.apartamentos.repositories.IPropiedadRepository;
import com.soa.apartamentos.repositories.IUnidadRentableRepository;

@Service
public class PropiedadService {

    @Autowired
    private IPropiedadRepository propiedadRepository;

    @Autowired
    private IUnidadRentableRepository unidadRepository;

    @Autowired
    private IImagenRepository imagenRepository;

    public List<PropiedadModel> listar() {
        return propiedadRepository.findAll();
    }

    public PropiedadModel buscarPorId(Long id) {
        return propiedadRepository.findById(id).orElse(null);
    }

    public List<PropiedadModel> buscarPorTipo(String tipo) {
        return propiedadRepository.findByTipoIgnoreCase(tipo);
    }

    public PropiedadModel guardar(PropiedadModel propiedad) {
        if (propiedad.getEstado() == null) {
            propiedad.setEstado("DISPONIBLE");
        }
        return propiedadRepository.save(propiedad);
    }

    public boolean sePuedeBorrar(Long id) {
        return !unidadRepository.existsByPropiedadIdPropiedad(id) && !imagenRepository.existsByPropiedadIdPropiedad(id);
    }

    public void borrar(Long id) {
        propiedadRepository.deleteById(id);
    }
}

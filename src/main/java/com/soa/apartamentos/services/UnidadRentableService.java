package com.soa.apartamentos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.UnidadRentableModel;
import com.soa.apartamentos.repositories.IDisponibilidadRepository;
import com.soa.apartamentos.repositories.IReservaRepository;
import com.soa.apartamentos.repositories.IUnidadRentableRepository;

@Service
public class UnidadRentableService {

    @Autowired
    private IUnidadRentableRepository unidadRepository;

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IDisponibilidadRepository disponibilidadRepository;

    public List<UnidadRentableModel> listar() {
        return unidadRepository.findAll();
    }

    public UnidadRentableModel buscarPorId(Long id) {
        return unidadRepository.findById(id).orElse(null);
    }

    public UnidadRentableModel guardar(UnidadRentableModel unidad) {
        return unidadRepository.save(unidad);
    }

    public boolean sePuedeBorrar(Long id) {
        return !reservaRepository.existsByUnidadIdUnidad(id) && !disponibilidadRepository.existsByUnidadIdUnidad(id);
    }

    public void borrar(Long id) {
        unidadRepository.deleteById(id);
    }
}

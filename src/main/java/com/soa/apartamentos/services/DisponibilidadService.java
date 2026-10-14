package com.soa.apartamentos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.DisponibilidadModel;
import com.soa.apartamentos.repositories.IDisponibilidadRepository;

@Service
public class DisponibilidadService {

    @Autowired
    private IDisponibilidadRepository disponibilidadRepository;

    public List<DisponibilidadModel> listar() {
        return disponibilidadRepository.findAll();
    }

    public DisponibilidadModel buscarPorId(Long id) {
        return disponibilidadRepository.findById(id).orElse(null);
    }

    public DisponibilidadModel guardar(DisponibilidadModel disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }

    public void borrar(Long id) {
        disponibilidadRepository.deleteById(id);
    }
}

package com.soa.apartamentos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.ResenaModel;
import com.soa.apartamentos.repositories.IResenaRepository;

@Service
public class ResenaService {

    @Autowired
    private IResenaRepository resenaRepository;

    public List<ResenaModel> listar() {
        return resenaRepository.findAll();
    }

    public ResenaModel buscarPorId(Long id) {
        return resenaRepository.findById(id).orElse(null);
    }

    public ResenaModel guardar(ResenaModel resena) {
        return resenaRepository.save(resena);
    }

    public void borrar(Long id) {
        resenaRepository.deleteById(id);
    }
}

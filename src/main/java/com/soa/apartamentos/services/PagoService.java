package com.soa.apartamentos.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.PagoModel;
import com.soa.apartamentos.repositories.IPagoRepository;

@Service
public class PagoService {

    @Autowired
    private IPagoRepository pagoRepository;

    public List<PagoModel> listar() {
        return pagoRepository.findAll();
    }

    public PagoModel buscarPorId(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public PagoModel guardar(PagoModel pago) {
        if (pago.getFecha() == null) {
            pago.setFecha(LocalDate.now());
        }
        if (pago.getMetodo() == null) {
            pago.setMetodo("TRANSFERENCIA");
        }
        if (pago.getEstado() == null) {
            pago.setEstado("PENDIENTE");
        }
        return pagoRepository.save(pago);
    }

    public void borrar(Long id) {
        pagoRepository.deleteById(id);
    }
}

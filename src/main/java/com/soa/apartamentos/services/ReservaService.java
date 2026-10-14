package com.soa.apartamentos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.ReservaModel;
import com.soa.apartamentos.repositories.IPagoRepository;
import com.soa.apartamentos.repositories.IResenaRepository;
import com.soa.apartamentos.repositories.IReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IPagoRepository pagoRepository;

    @Autowired
    private IResenaRepository resenaRepository;

    public List<ReservaModel> listar() {
        return reservaRepository.findAll();
    }

    public ReservaModel buscarPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public boolean unidadConfirmada(Long idUnidad) {
        return reservaRepository.existsByUnidadIdUnidadAndEstado(idUnidad, "CONFIRMADA");
    }

    public ReservaModel guardar(ReservaModel reserva) {
        if (reserva.getEstado() == null) {
            reserva.setEstado("PENDIENTE");
        }
        return reservaRepository.save(reserva);
    }

    public boolean sePuedeBorrar(Long id) {
        return !pagoRepository.existsByReservaIdReserva(id) && !resenaRepository.existsByReservaIdReserva(id);
    }

    public void borrar(Long id) {
        reservaRepository.deleteById(id);
    }
}

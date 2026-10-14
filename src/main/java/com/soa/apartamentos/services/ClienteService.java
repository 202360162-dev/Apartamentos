package com.soa.apartamentos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.ClienteModel;
import com.soa.apartamentos.repositories.IClienteRepository;
import com.soa.apartamentos.repositories.IPropiedadRepository;
import com.soa.apartamentos.repositories.IReservaRepository;

@Service
public class ClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IPropiedadRepository propiedadRepository;

    public List<ClienteModel> listar() {
        return clienteRepository.findAll();
    }

    public ClienteModel buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public ClienteModel guardar(ClienteModel cliente) {
        if (cliente.getTipo() == null || cliente.getTipo().isBlank()) {
            cliente.setTipo("CLIENTE");
        }
        return clienteRepository.save(cliente);
    }

    public boolean sePuedeBorrar(Long id) {
        return !reservaRepository.existsByClienteIdCliente(id) && !propiedadRepository.existsByPropietarioIdCliente(id);
    }

    public void borrar(Long id) {
        clienteRepository.deleteById(id);
    }
}

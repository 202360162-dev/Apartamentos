package com.soa.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.ReservaModel;

public interface IReservaRepository extends JpaRepository<ReservaModel, Long> {

    boolean existsByClienteIdCliente(Long idCliente);

    boolean existsByUnidadIdUnidad(Long idUnidad);

    boolean existsByUnidadIdUnidadAndEstado(Long idUnidad, String estado);
}

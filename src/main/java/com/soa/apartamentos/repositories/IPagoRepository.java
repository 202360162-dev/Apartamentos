package com.soa.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.PagoModel;

public interface IPagoRepository extends JpaRepository<PagoModel, Long> {

    boolean existsByReservaIdReserva(Long idReserva);
}

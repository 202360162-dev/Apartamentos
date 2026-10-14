package com.soa.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.ResenaModel;

public interface IResenaRepository extends JpaRepository<ResenaModel, Long> {

    boolean existsByReservaIdReserva(Long idReserva);
}

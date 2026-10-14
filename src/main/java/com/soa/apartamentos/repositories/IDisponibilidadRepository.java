package com.soa.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.DisponibilidadModel;

public interface IDisponibilidadRepository extends JpaRepository<DisponibilidadModel, Long> {

    boolean existsByUnidadIdUnidad(Long idUnidad);
}

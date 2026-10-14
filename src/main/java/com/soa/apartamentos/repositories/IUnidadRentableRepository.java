package com.soa.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.UnidadRentableModel;

public interface IUnidadRentableRepository extends JpaRepository<UnidadRentableModel, Long> {

    boolean existsByPropiedadIdPropiedad(Long idPropiedad);
}

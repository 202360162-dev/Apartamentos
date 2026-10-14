package com.soa.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.ImagenModel;

public interface IImagenRepository extends JpaRepository<ImagenModel, Long> {

    boolean existsByPropiedadIdPropiedad(Long idPropiedad);
}

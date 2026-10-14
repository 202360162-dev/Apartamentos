package com.soa.apartamentos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.PropiedadModel;

public interface IPropiedadRepository extends JpaRepository<PropiedadModel, Long> {

    List<PropiedadModel> findByTipoIgnoreCase(String tipo);

    boolean existsByPropietarioIdCliente(Long idCliente);
}

package com.soa.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soa.apartamentos.models.ClienteModel;

public interface IClienteRepository extends JpaRepository<ClienteModel, Long> {
}

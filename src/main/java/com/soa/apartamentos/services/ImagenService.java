package com.soa.apartamentos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.apartamentos.models.ImagenModel;
import com.soa.apartamentos.repositories.IImagenRepository;

@Service
public class ImagenService {

    @Autowired
    private IImagenRepository imagenRepository;

    public List<ImagenModel> listar() {
        return imagenRepository.findAll();
    }

    public ImagenModel buscarPorId(Long id) {
        return imagenRepository.findById(id).orElse(null);
    }

    public ImagenModel guardar(ImagenModel imagen) {
        return imagenRepository.save(imagen);
    }

    public void borrar(Long id) {
        imagenRepository.deleteById(id);
    }
}

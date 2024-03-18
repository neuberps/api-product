package com.ms.product.controller;


import com.ms.product.dto.ProductDTO;
import com.ms.product.exceptions.ServiceException;
import com.ms.product.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        try {
            List<ProductDTO> products = service.findAll();
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(products);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDTO entity) {
        try {
            ProductDTO createdProduct = service.create(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping(value="/getId/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable String id) {
        try {
            ProductDTO product = service.findById(id);
            return ResponseEntity.ok(product);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value="/getName/{name}")
    public ResponseEntity<ProductDTO> findByName(@PathVariable String name) {
        try {
            ProductDTO client = service.findByName(name);
            if (client != null) {
                return ResponseEntity.ok(client);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @PutMapping(value="/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable String id, @Valid @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = service.update(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
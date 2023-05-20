/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pratti.pesquisa.controller;

import com.pratti.pesquisa.dtos.KeyDto;
import com.pratti.pesquisa.model.KeyModel;
import com.pratti.pesquisa.service.KeyService;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Roger
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class KeyController {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    final KeyService keyService;
    
    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }
    
    @GetMapping("/key")
    public ResponseEntity<List<KeyModel>> getAllSectors(){
        return ResponseEntity.status(HttpStatus.OK).body(keyService.findAll());
    }
    
    @GetMapping("/key/{id}")
    public ResponseEntity<Object> getOneSector(@PathVariable(value= "id") UUID id){
        Optional<KeyModel> sectorModelOptional = keyService.findById(id);
        if(!sectorModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sector not found");
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(sectorModelOptional.get());
    }
    
    @PostMapping("/key")
    public ResponseEntity<Object> createAccessKey(@RequestBody @Validated KeyDto keyDto) {
        var accessKey = new KeyModel();
       
        accessKey.setKey_access(generateKey());
        
        BeanUtils.copyProperties(keyDto, accessKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(keyService.save(accessKey));
    }
    
    private String generateKey() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    @PutMapping("/key/{id}")
    public ResponseEntity<Object> updateSector(@PathVariable(value ="id") UUID id, @RequestBody @Validated KeyDto keyDto){
        Optional<KeyModel> keyModelOptional = keyService.findById(id);
        if(!keyModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Key not found");
        }
    
        var keyModel = keyModelOptional.get();

        return ResponseEntity.status(HttpStatus.OK).body(keyService.save(keyModel));
    }
}

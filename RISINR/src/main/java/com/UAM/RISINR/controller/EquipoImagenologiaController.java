/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.UAM.RISINR.controller;

import com.UAM.RISINR.model.EquipoImagenologia;
import com.UAM.RISINR.model.dto.EquipoImagenologiaDTO;
import com.UAM.RISINR.model.dto.PruebaEquipoImagenologiaDTO;
import com.UAM.RISINR.service.EquipoImagenologiaManager;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vsfs2
 */
@RestController
@RequestMapping("/EquipoImagenologia")
public class EquipoImagenologiaController {
    
    @Autowired
    public EquipoImagenologiaManager manager; 
    
    
    @PostMapping("/requestALL")
    public ResponseEntity<List<EquipoImagenologiaDTO>> consultarTodos(){
        System.out.println("Entró al controlador");
        List<EquipoImagenologiaDTO> datos = manager.consultarTodos();
        System.out.println("Salio del manager");
        return ResponseEntity.ok(datos);
    }
    
    @PostMapping("/addEquipo")
    public ResponseEntity<Object> addEquipo(@RequestParam Map<String, String> formData) {
            EquipoImagenologiaDTO equipoDTO = manager.addEquipo(formData);
            
            if(equipoDTO != null){
                 return ResponseEntity.ok(equipoDTO);
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El equipo ya está registrado con ese número de serie.");
            }
        }
    
    
    @PostMapping("/editEquipo")
    public ResponseEntity<Object> editEquipo(@RequestParam Map<String, String> formData) {
            EquipoImagenologia equipo = manager.edit(formData);
            List<Object> equipos = new ArrayList();
            
            if (equipo == null) {
                equipos.add(0);
                equipos.add(null);
                 return ResponseEntity.status(HttpStatus.CONFLICT).body("El equipo no se pudo editar correctamente");
            } else {
                equipos.add(0);
                equipos.add(equipo);
                return ResponseEntity.ok(equipos); 
            }
        }
  
}

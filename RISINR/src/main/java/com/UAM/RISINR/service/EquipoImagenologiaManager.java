/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.UAM.RISINR.service;

import com.UAM.RISINR.model.EquipoImagenologia;
import com.UAM.RISINR.model.dto.EquipoImagenologiaDTO;
import com.UAM.RISINR.repository.EquipoImagenologiaRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vsfs2
 */
@Service
public class EquipoImagenologiaManager {
    
    @Autowired
    public EquipoImagenologiaRepository repository; 
    
     @Transactional(readOnly = true)
    public List<EquipoImagenologiaDTO> consultarTodos(){
        System.out.println("Llego a consultarTodos(): antes");
        
        List<EquipoImagenologia> equipos = repository.findAll();
         System.out.println("LLA CANTIAD DE ELEMENTOS ES: " + equipos.size());
        System.out.println("Despues del find all");
         System.out.println("equipos antes de pasar a dto: " + equipos);
        List<EquipoImagenologiaDTO> equiposDTO = convertirDTO(equipos);
        System.out.println("equipos despues de pasar a dto: " + equiposDTO);
        return equiposDTO;
    }    
    
    public List<EquipoImagenologiaDTO> convertirDTO(List<EquipoImagenologia> equipos){
        List<EquipoImagenologiaDTO> equiposDTO = new ArrayList();
         System.out.println("Antes del  for en el manager");
          System.out.println(equipos);
        
        for (EquipoImagenologia eqp: equipos){
            System.out.println("Entró al for en el manager");
            System.out.println("Antes del contenido del for" + equipos);
            
             String nSerie = eqp.getNSerie();
             String nombreEquipo = eqp.getNombre();
             String marca = eqp.getMarca();
             String modelo = eqp.getModelo();
             String modalidad = eqp.getModalidad();
             Integer idArea = eqp.getAreaDeServicioidArea().getIdArea();
             String nombreArea = eqp.getAreaDeServicioidArea().getNombre(); 
             Date fechaInstalacion = eqp.getFechaInstalacion();
             
             EquipoImagenologiaDTO equipoDTO = new EquipoImagenologiaDTO(nSerie, nombreEquipo, marca, modelo, modalidad, idArea, nombreArea, fechaInstalacion);
             equiposDTO.add(equipoDTO);
             
 
        }
        
        System.out.println(equiposDTO);
        return equiposDTO;
        
    }
    

}

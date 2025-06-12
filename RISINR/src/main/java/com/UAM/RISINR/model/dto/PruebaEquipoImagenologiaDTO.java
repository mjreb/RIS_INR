/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.UAM.RISINR.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 *
 * @author vsfs2
 */
public class PruebaEquipoImagenologiaDTO {
    
        private String nserEQP;
        private String nomEQP;
        private String marcaEQP ;
        private String modeloEQP;
        private String modalEqp;
        private Integer idarea;
        private String nombreArea; 
        private String edoEqp; 
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date fechaInstalacion;

    public PruebaEquipoImagenologiaDTO(String nserEQP, String nomEQP, String marcaEQP, String modeloEQP, String modalEqp, Integer idarea, String nombreArea, String edoEqp) {
        this.nserEQP = nserEQP;
        this.nomEQP = nomEQP;
        this.marcaEQP = marcaEQP;
        this.modeloEQP = modeloEQP;
        this.modalEqp = modalEqp;
        this.idarea = idarea;
        this.nombreArea = nombreArea;
        this.edoEqp = edoEqp;
    }

 
    
    public PruebaEquipoImagenologiaDTO(){
    }

    public String getNserEQP() {
        return nserEQP;
    }

    public void setNserEQP(String nserEQP) {
        this.nserEQP = nserEQP;
    }

    public String getNomEQP() {
        return nomEQP;
    }

    public void setNomEQP(String nomEQP) {
        this.nomEQP = nomEQP;
    }

    public String getMarcaEQP() {
        return marcaEQP;
    }

    public void setMarcaEQP(String marcaEQP) {
        this.marcaEQP = marcaEQP;
    }

    public String getModeloEQP() {
        return modeloEQP;
    }

    public void setModeloEQP(String modeloEQP) {
        this.modeloEQP = modeloEQP;
    }

    public String getModalEqp() {
        return modalEqp;
    }

    public void setModalEqp(String modalEqp) {
        this.modalEqp = modalEqp;
    }

    public Integer getIdarea() {
        return idarea;
    }

    public void setIdarea(Integer idarea) {
        this.idarea = idarea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }

    public String getEdoEqp() {
        return edoEqp;
    }

    public void setEdoEqp(String edoEqp) {
        this.edoEqp = edoEqp;
    }

    public Date getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(Date fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }


@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("PruebaEquipoImagenologiaDTO{");
    sb.append("nserEQP='").append(nserEQP).append('\'');
    sb.append(", nomEQP='").append(nomEQP).append('\'');
    sb.append(", marcaEQP='").append(marcaEQP).append('\'');
    sb.append(", modeloEQP='").append(modeloEQP).append('\'');
    sb.append(", modalEqp='").append(modalEqp).append('\'');
    sb.append(", idarea=").append(idarea);
    sb.append(", nombreArea='").append(nombreArea).append('\'');
    sb.append(", edoEqp='").append(edoEqp).append('\'');
    sb.append(", fechaInstalacion=").append(fechaInstalacion != null ? 
        new java.text.SimpleDateFormat("yyyy-MM-dd").format(fechaInstalacion) : "null");
    sb.append('}');
    return sb.toString();
}

}
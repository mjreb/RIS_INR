/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.UAM.RISINR.model;

import java.io.Serializable;
import java.math.BigInteger;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Sesion")
public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SesionPK sesionPK;
    @Size(max = 30)
    @Column(name = "Rol_Nombre")
    private String rolNombre;
    @Column(name = "horaFin")
    private BigInteger horaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "IPDispositivo")
    private String iPDispositivo;
    @Size(max = 25)
    @Column(name = "TipoCierre")
    private String tipoCierre;

    public Sesion() {
    }

    public Sesion(SesionPK sesionPK) {
        this.sesionPK = sesionPK;
    }

    public Sesion(SesionPK sesionPK, String iPDispositivo) {
        this.sesionPK = sesionPK;
        this.iPDispositivo = iPDispositivo;
    }

    public Sesion(long horaInicio, String usuarioID) {
        this.sesionPK = new SesionPK(horaInicio, usuarioID);
    }
    public Sesion(long horaInicio, String usuarioID, int usuarioNumEmpleado, String usuarioCURP) {
        this.sesionPK = new SesionPK(horaInicio, usuarioID, usuarioNumEmpleado, usuarioCURP);
    }

    public SesionPK getSesionPK() {
        return sesionPK;
    }

    public void setSesionPK(SesionPK sesionPK) {
        this.sesionPK = sesionPK;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public BigInteger getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(BigInteger horaFin) {
        this.horaFin = horaFin;
    }

    public String getIPDispositivo() {
        return iPDispositivo;
    }

    public void setIPDispositivo(String iPDispositivo) {
        this.iPDispositivo = iPDispositivo;
    }

    public String getTipoCierre() {
        return tipoCierre;
    }

    public void setTipoCierre(String tipoCierre) {
        this.tipoCierre = tipoCierre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sesionPK != null ? sesionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sesion)) {
            return false;
        }
        Sesion other = (Sesion) object;
        if ((this.sesionPK == null && other.sesionPK != null) || (this.sesionPK != null && !this.sesionPK.equals(other.sesionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.Sesion[ sesionPK=" + sesionPK + " ]";
    }
    
}

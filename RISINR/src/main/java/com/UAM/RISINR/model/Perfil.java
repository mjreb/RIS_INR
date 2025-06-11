package com.UAM.RISINR.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Perfil")

public class Perfil implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PerfilPK perfilPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estado")
    private int estado;
    @JoinColumn(name = "Rol_idRol", referencedColumnName = "idRol", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Rol rol;
    @JoinColumns({
        @JoinColumn(name = "Usuario_NumEmpleado", referencedColumnName = "NumEmpleado", insertable = false, updatable = false),
        @JoinColumn(name = "Usuario_CURP", referencedColumnName = "CURP", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Perfil() {
    }

    public Perfil(PerfilPK perfilPK) {
        this.perfilPK = perfilPK;
    }

    public Perfil(PerfilPK perfilPK, int estado) {
        this.perfilPK = perfilPK;
        this.estado = estado;
    }

    public Perfil(int usuarioNumEmpleado, String usuarioCURP, int rolidRol) {
        this.perfilPK = new PerfilPK(usuarioNumEmpleado, usuarioCURP, rolidRol);
    }

    public PerfilPK getPerfilPK() {
        return perfilPK;
    }

    public void setPerfilPK(PerfilPK perfilPK) {
        this.perfilPK = perfilPK;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perfilPK != null ? perfilPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
        if ((this.perfilPK == null && other.perfilPK != null) || (this.perfilPK != null && !this.perfilPK.equals(other.perfilPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.Perfil[ perfilPK=" + perfilPK + " ]";
    }
    
}

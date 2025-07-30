package com.UAM.RISINR.model;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioPK usuarioPK;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Nombre")
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ApellidoPaterno")
    private String apellidoPaterno;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ApellidoMaterno")
    private String apellidoMaterno;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UsuarioID")
    private String usuarioID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Passwd")
    private String passwd;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Estado")
    private String estado;
    
    @JoinColumn(name = "Area_idArea", referencedColumnName = "idArea")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AreaDeServicio areaidArea;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Collection<Perfil> perfilCollection;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Collection<ControlEstudios> controlEstudiosCollection;
    

    public Usuario() {
    }

    public Usuario(UsuarioPK usuarioPK) {
        this.usuarioPK = usuarioPK;
    }

    public Usuario(UsuarioPK usuarioPK, String nombre, String apellidoPaterno, String apellidoMaterno, String usuarioID, String passwd, String estado) {
        this.usuarioPK = usuarioPK;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.usuarioID = usuarioID;
        this.passwd = passwd;
        this.estado = estado;
    }

    public Usuario(int numEmpleado, String curp) {
        this.usuarioPK = new UsuarioPK(numEmpleado, curp);
    }

    public UsuarioPK getUsuarioPK() {
        return usuarioPK;
    }

    public void setUsuarioPK(UsuarioPK usuarioPK) {
        this.usuarioPK = usuarioPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public AreaDeServicio getAreaidArea() {
        return areaidArea;
    }

    public void setAreaidArea(AreaDeServicio areaidArea) {
        this.areaidArea = areaidArea;
    }

  //Aquí hay que ver las anotaciones @JSsonManageReferece y @JsonBackReference
    public Collection<Perfil> getPerfilCollection() {
        return perfilCollection;
    }

    public void setPerfilCollection(Collection<Perfil> perfilCollection) {
        this.perfilCollection = perfilCollection;
    }

  //Aquí hay que ver las anotaciones @JSsonManageReferece y @JsonBackReference
    public Collection<ControlEstudios> getControlEstudiosCollection() {
        return controlEstudiosCollection;
    }

    public void setControlEstudiosCollection(Collection<ControlEstudios> controlEstudiosCollection) {
        this.controlEstudiosCollection = controlEstudiosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioPK != null ? usuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Usuario)) return false;
        Usuario other = (Usuario) object;
        return java.util.Objects.equals(this.usuarioPK, other.usuarioPK);
    }

    @Override
    public String toString() {
        return "Usuario{usuarioPK=" + usuarioPK + ", usuarioID='" + usuarioID + "'}";
    }
    
}

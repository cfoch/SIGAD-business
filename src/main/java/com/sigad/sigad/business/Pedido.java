/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import com.grupo1.simulated_annealing.Locacion;
import com.grupo1.simulated_annealing.Servicio;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Alexandra
 */
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private Timestamp fechaVenta;
    private Date fechaEntregaEsperada;
    private Date fechaEntrega;
    @NotNull
    private Double total;
    @NotNull
    private Boolean activo;
    @NotNull
    private Boolean modificable;
    @NotNull
    private String mensajeDescripicion;
    @NotNull
    private String direccionDeEnvio;
    @NotNull
    private Double cooXDireccion;
    @NotNull
    private Double cooYDireccion;
    private String rucFactura;
    private String nombreEmpresa;

    @ManyToOne(optional = false)
    private PedidoEstado estado;
    private String turno;
    private double volumenTotal;
    @ManyToOne
    private Reparto reparto;
    private Integer secuenciaReparto;
    private String codigoBanco;
    @OneToOne(mappedBy = "pedidoPago", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NotaCredito notacreditopago;//Pago con nota de credito
    @OneToOne(mappedBy = "pedidoOrigen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NotaCredito notacreditogenerada;//Pago con nota de credito

    //fk
    @ManyToOne
    private Usuario vendedor;

    @ManyToOne(optional = false)
    private Usuario cliente;

    @ManyToOne
    private TipoPago tipoPago;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EstadoPedido> estadosPedido = new HashSet<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetallePedido> detallePedido = new HashSet<>();

    @ManyToOne
    private Tienda tienda;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentoLegal> documentos = new HashSet<>();

    @ManyToOne
    private ClienteDescuento descuentoCliente;

    private Timestamp horaEntrega;

    public Pedido() {
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the fechaVenta
     */
    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    /**
     * @param fechaVenta the fechaVenta to set
     */
    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * @return the modificable
     */
    public boolean isModificable() {
        return modificable;
    }

    /**
     * @param modificable the modificable to set
     */
    public void setModificable(boolean modificable) {
        this.modificable = modificable;
    }

    /**
     * @return the mensajeDescripicion
     */
    public String getMensajeDescripicion() {
        return mensajeDescripicion;
    }

    /**
     * @param mensajeDescripicion the mensajeDescripicion to set
     */
    public void setMensajeDescripicion(String mensajeDescripicion) {
        this.mensajeDescripicion = mensajeDescripicion;
    }

    /**
     * @return the direccionDeEnvio
     */
    public String getDireccionDeEnvio() {
        return direccionDeEnvio;
    }

    /**
     * @param direccionDeEnvio the direccionDeEnvio to set
     */
    public void setDireccionDeEnvio(String direccionDeEnvio) {
        this.direccionDeEnvio = direccionDeEnvio;
    }

    /**
     * @return the cooXDireccion
     */
    public double getCooXDireccion() {
        return cooXDireccion;
    }

    /**
     * @param cooXDireccion the cooXDireccion to set
     */
    public void setCooXDireccion(double cooXDireccion) {
        this.cooXDireccion = cooXDireccion;
    }

    /**
     * @return the cooYDireccion
     */
    public double getCooYDireccion() {
        return cooYDireccion;
    }

    /**
     * @param cooYDireccion the cooYDireccion to set
     */
    public void setCooYDireccion(double cooYDireccion) {
        this.cooYDireccion = cooYDireccion;
    }

    /**
     * @return the horaEntrega
     */
    public Timestamp getHoraEntrega() {
        return horaEntrega;
    }

    /**
     * @param horaEntrega the horaEntrega to set
     */
    public void setHoraEntrega(Timestamp horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    /**
     * @return the detallePedido
     */
    public Set<DetallePedido> getDetallePedido() {
        return detallePedido;
    }

    /**
     * @param detallePedido the detallePedido to set
     */
    public void setDetallePedido(Set<DetallePedido> detallePedido) {
        this.detallePedido.clear();
        this.detallePedido.addAll(detallePedido);
    }

    /**
     * @return the vendedor
     */
    public Usuario getVendedor() {
        return vendedor;
    }

    /**
     * @param vendedor the vendedor to set
     */
    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    /**
     * @return the cliente
     */
    public Usuario getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the tipoPago
     */
    public TipoPago getTipoPago() {
        return tipoPago;
    }

    /**
     * @param tipoPago the tipoPago to set
     */
    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    /**
     * @return the estadosPedido
     */
    public Set<EstadoPedido> getEstadosPedido() {
        return estadosPedido;
    }

    /**
     * @param estadosPedido the estadosPedido to set
     */
    public void setEstadosPedido(Set<EstadoPedido> estadosPedido) {
        this.estadosPedido = estadosPedido;
    }

    /**
     * @return the estado
     */
    public PedidoEstado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(PedidoEstado estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la Locacion de un pedido en la estructura de SimmulatedAnnealing.
     *
     * @return Una locacion de SimulatedAnnealing.
     */
    public Locacion getLocacion() {
        Locacion locacion;
        Servicio servicio;
        locacion = new Locacion(id, direccionDeEnvio, Locacion.Tipo.OTRO,
                cooXDireccion, cooYDireccion);
        servicio = new Servicio("", locacion, (int) volumenTotal);
        locacion.setServicio(servicio);
        return locacion;
    }

    /**
     * @return the volumen
     */
    public double getVolumenTotal() {
        return volumenTotal;
    }

    /**
     * @param volumen the volumen to set
     */
    public void setVolumenTotal(double volumen) {
        this.volumenTotal = volumen;
    }

    /**
     * @return the reparto
     */
    public Reparto getReparto() {
        return reparto;
    }

    /**
     * @param reparto the reparto to set
     */
    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    public void addEstado(PedidoEstado ep) {
        EstadoPedido np = new EstadoPedido(new Timestamp(System.currentTimeMillis()), this, ep);
        this.estadosPedido.add(np);
    }

    /**
     * @return the turno
     */
    public String getTurno() {
        return turno;
    }

    /**
     * @param turno the turno to set
     */
    public void setTurno(String turno) {
        this.turno = turno;
    }

    /**
     * @return the tienda
     */
    public Tienda getTienda() {
        return tienda;
    }

    /**
     * @param tienda the tienda to set
     */
    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    /**
     * @return the documentos
     */
    public Set<DocumentoLegal> getDocumentos() {
        return documentos;
    }

    /**
     * @param documentos the documentos to set
     */
    public void setDocumentos(Set<DocumentoLegal> documentos) {
        this.documentos = documentos;
    }

    /**
     * @return the descuentoCliente
     */
    public ClienteDescuento getDescuentoCliente() {
        return descuentoCliente;
    }

    /**
     * @param descuentoCliente the descuentoCliente to set
     */
    public void setDescuentoCliente(ClienteDescuento descuentoCliente) {
        this.descuentoCliente = descuentoCliente;
    }

    /**
     * @return the rucFactura
     */
    public String getRucFactura() {
        return rucFactura;
    }

    /**
     * @param rucFactura the rucFactura to set
     */
    public void setRucFactura(String rucFactura) {
        this.rucFactura = rucFactura;
    }

    /**
     * @return the nombreEmpresa
     */
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    /**
     * @param nombreEmpresa the nombreEmpresa to set
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * @return the secuenciaReparto
     */
    public Integer getSecuenciaReparto() {
        return secuenciaReparto;
    }

    /**
     * @param secuenciaReparto the secuenciaReparto to set
     */
    public void setSecuenciaReparto(Integer secuenciaReparto) {
        this.secuenciaReparto = secuenciaReparto;
    }

    /**
     * @return the fechaEntregaEsperada
     */
    public Date getFechaEntregaEsperada() {
        return fechaEntregaEsperada;
    }

    /**
     * @param fechaEntregaEsperada the fechaEntregaEsperada to set
     */
    public void setFechaEntregaEsperada(Date fechaEntregaEsperada) {
        this.fechaEntregaEsperada = fechaEntregaEsperada;
    }

    /**
     * @return the fechaEntrega
     */
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * @param fechaEntrega the fechaEntrega to set
     */
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * @return the notacredito
     */
    public NotaCredito getNotacredito() {
        return notacreditopago;
    }

    /**
     * @param notacredito the notacredito to set
     */
    public void setNotacredito(NotaCredito notacredito) {
        this.notacreditopago = notacredito;
    }

    /**
     * @return the notacreditogenerada
     */
    public NotaCredito getNotacreditogenerada() {
        return notacreditogenerada;
    }

    /**
     * @param notacreditogenerada the notacreditogenerada to set
     */
    public void setNotacreditogenerada(NotaCredito notacreditogenerada) {
        this.notacreditogenerada = notacreditogenerada;
    }

    /**
     * @return the codigoBanco
     */
    public String getCodigoBanco() {
        return codigoBanco;
    }

    /**
     * @param codigoBanco the codigoBanco to set
     */
    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }
}

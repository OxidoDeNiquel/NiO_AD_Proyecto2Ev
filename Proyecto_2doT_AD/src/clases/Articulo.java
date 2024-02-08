package clases;

import excepciones.ArticuloSinCodigoException;

public class Articulo {
	private String codigo;
	private String descripcion;
	private String familia;
	private String fechaDeAlta;
	
	public Articulo(String cod, String descr, String familia, String fechaAlta) throws ArticuloSinCodigoException {
		this.codigo = cod;
		this.descripcion = descr;
		this.familia = familia;
		this.fechaDeAlta = fechaAlta;
		
		if(codigo == null || codigo.isEmpty()) {
			throw new ArticuloSinCodigoException("El código del articulo es obligatorio.");
		}
		
	}
	
	//GETTERS Y SETTERS

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getFechaDeAlta() {
		return fechaDeAlta;
	}

	public void setFechaDeAlta(String fechaDeAlta) {
		this.fechaDeAlta = fechaDeAlta;
	}

}

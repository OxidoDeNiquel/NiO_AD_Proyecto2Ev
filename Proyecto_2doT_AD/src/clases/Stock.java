package clases;

public class Stock {
	
	private String codArticulo;
	private String cantidad;
	
	public Stock(String cod, String cant) {
		this.codArticulo = cod;
		this.cantidad = cant;
	}

	public String getCodArticulo() {
		return codArticulo;
	}

	public void setCodArticulo(String codArticulo) {
		this.codArticulo = codArticulo;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	

}

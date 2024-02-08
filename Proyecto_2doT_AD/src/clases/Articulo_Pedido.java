package clases;

public class Articulo_Pedido{
	private String codigo;
	private String cantidad;
	
	public Articulo_Pedido(String vcodigo, String vcantidad) {
		//Se asigna cada valor de entrada a su variable correspondiente
		this.codigo = vcodigo;
		this.cantidad = vcantidad;
	}
	
	public void imprimirInformacion() {
        System.out.printf("%-20s %-10s%n", "Código", "Cantidad");
        System.out.printf("%-20s %-10s%n\n", codigo, cantidad);
    }

	//GETTERS Y SETTERS
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
}

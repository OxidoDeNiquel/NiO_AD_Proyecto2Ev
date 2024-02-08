package clases;

public class Pedido {
	private String numCliente;
	private String numPedido;
	private String fecha; 
	
	public Pedido(String numeroCliente, String numeroPedido, String fecha) {
		//Se asigna cada valor de entrada a su variable correspondiente
		this.numCliente = numeroCliente;
		this.numPedido = numeroPedido;
		this.fecha = fecha;
	}
	
	public void imprimirInformacion() {
        System.out.printf("%-20s %-20s %-20s%n", "Nº Cliente", "Nº Pedido", "Fecha");
        System.out.printf("%-20s %-20s %-20s%n\n", numCliente, numPedido, fecha);
    }
	
	//GETTERS Y SETTERS
	public String getNumCliente() {
		return numCliente;
	}

	public void setNumCliente(String numCliente) {
		this.numCliente = numCliente;
	}

	public String getNumPedido() {
		return numPedido;
	}

	public void setNumPedido(String numPedido) {
		this.numPedido = numPedido;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
}

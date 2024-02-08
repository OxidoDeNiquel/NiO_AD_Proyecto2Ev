package clases;

import excepciones.ClienteSinCodigoException;

public class Cliente {
	private String numCliente;
	private String nombre;
	private String direccion;
	private String telefono;
	
	public Cliente(String nCliente, String nom, String direcc, String telef) throws ClienteSinCodigoException {
		this.numCliente = nCliente;
		this.nombre = nom;
		this.direccion = direcc;
		this.telefono = telef;
		
		if (numCliente == null || numCliente.isEmpty()) {
            throw new ClienteSinCodigoException("El código del cliente es obligatorio.");
        }
	}
	//GETTERS Y SETTERS
	public String getNumCliente() {
		return numCliente;
	}

	public void setNumCliente(String numCliente) {
		this.numCliente = numCliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
}

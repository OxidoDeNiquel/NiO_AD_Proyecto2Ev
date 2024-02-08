package excepciones;

public class PedidoDuplicadoException extends Exception{
	public PedidoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}

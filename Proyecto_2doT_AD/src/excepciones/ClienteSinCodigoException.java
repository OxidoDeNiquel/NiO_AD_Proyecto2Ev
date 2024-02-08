package excepciones;

public class ClienteSinCodigoException extends Exception{
	public ClienteSinCodigoException(String mensaje) {
        super(mensaje);
    }
}

package excepciones;

public class ClienteNoExistenteException extends Exception{
	public ClienteNoExistenteException(String mensaje) {
        super(mensaje);
    }
}

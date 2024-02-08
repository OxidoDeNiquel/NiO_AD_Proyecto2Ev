package excepciones;

public class ArticuloNoExisteException extends Exception{
	public ArticuloNoExisteException(String mensaje) {
        super(mensaje);
    }
}

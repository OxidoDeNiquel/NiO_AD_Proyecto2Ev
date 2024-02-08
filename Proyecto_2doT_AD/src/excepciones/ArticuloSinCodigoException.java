package excepciones;

public class ArticuloSinCodigoException extends Exception{
	public ArticuloSinCodigoException(String mensaje) {
        super(mensaje);
    }
}

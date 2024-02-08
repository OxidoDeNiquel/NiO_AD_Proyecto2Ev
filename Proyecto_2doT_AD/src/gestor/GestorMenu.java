package gestor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import clases.Cliente;
import clases.Pedido;
import excepciones.ArticuloNoExisteException;
import excepciones.ArticuloSinCodigoException;
import excepciones.Articulo_PedidoDuplicadoException;
import excepciones.ClienteNoExistenteException;
import excepciones.ClienteSinCodigoException;
import excepciones.PedidoDuplicadoException;

public class GestorMenu {
	ArrayList<Pedido> listaPedidosXML;
	ArrayList<Pedido> listaPedidoXML_fallidos;
	Scanner teclado = new Scanner(System.in);
	Gestor gestor;
	
	public GestorMenu() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, ClienteSinCodigoException, ClienteNoExistenteException, ArticuloSinCodigoException{
		this.gestor = new Gestor();
	}
	
	public void menu(int i) throws ParserConfigurationException, SAXException, IOException, SQLException, ClienteSinCodigoException, ClienteNoExistenteException, ArticuloSinCodigoException, ArticuloNoExisteException, PedidoDuplicadoException, Articulo_PedidoDuplicadoException {
		
		switch(i) {
		case 1: 
			leerDatosXML();

			int j;
			do {
				System.out.println("¿Quieres insertar los siguientes datos?");
				System.out.println("1. Si");
				System.out.println("0. No");
				j = teclado.nextInt();
				
				menuInsertar(j);
			}while(j != 0 && j != 1);
			
			break;
		case 0:
			gestor.cerrarConexion();
			break;
		default:
			break;
				
		}
	}
	
	public void menuInsertar(int j) throws SQLException, PedidoDuplicadoException, ClienteSinCodigoException, ClienteNoExistenteException, Articulo_PedidoDuplicadoException, ArticuloSinCodigoException {
		switch(j) {
		case 1: 
			insertarDatosXMLEnBBDD();
			break;
		case 0:
			gestor.cerrarConexion();
			break;
		default:
			System.out.println("Elige una de las opciones disponibles");
			break;
		}
	}
	
	public void insertarDatosXMLEnBBDD() throws SQLException, PedidoDuplicadoException, ClienteSinCodigoException, ClienteNoExistenteException, Articulo_PedidoDuplicadoException, ArticuloSinCodigoException {
		listaPedidosXML = gestor.getListaPedidosXML();
		mostrarPantallaDeCarga("Insertando datos en la base de datos");
		gestor.insertarPedidos(listaPedidosXML);
		gestor.cerrarConexion();
	}
	
	public void leerDatosXML() throws ParserConfigurationException, SAXException, IOException, SQLException, ClienteSinCodigoException, ArticuloSinCodigoException, ArticuloNoExisteException, ClienteNoExistenteException {
		mostrarPantallaDeCarga("Leyendo XML");
		gestor.leerXML();
		listaPedidoXML_fallidos = gestor.getListaPedidoXML_fallidos();
		
	}
	
	public static void mostrarPantallaDeCarga(String mensaje) {
        System.out.print(mensaje);
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            // Puedes simular un retraso con Thread.sleep para hacer más realista la carga
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}
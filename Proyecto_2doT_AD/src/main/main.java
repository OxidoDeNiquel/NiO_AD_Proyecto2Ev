package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import clases.Articulo_Pedido;
import clases.Cliente;
import clases.Pedido;
import excepciones.ArticuloNoExisteException;
import excepciones.ArticuloSinCodigoException;
import excepciones.Articulo_PedidoDuplicadoException;
import excepciones.ClienteNoExistenteException;
import excepciones.ClienteSinCodigoException;
import excepciones.PedidoDuplicadoException;
import gestor.Gestor;
import gestor.GestorMenu;

public class main {

	public static void main(String[] args) throws SAXException, ArticuloNoExisteException, Articulo_PedidoDuplicadoException {
		Scanner teclado = new Scanner(System.in);
			GestorMenu gestor;
			
			try {
				gestor = new GestorMenu();
				int i;
				do {
					System.out.println("---- MENÚ ----");
					System.out.println("1. Leer los datos XML");
					System.out.println("\n0. Salir");
					i=teclado.nextInt();
					gestor.menu(i);
				}while(i!=1 && i!=0);
				
			} catch (ClassNotFoundException | SQLException | ParserConfigurationException | SAXException | IOException
					| ClienteSinCodigoException  | ArticuloSinCodigoException | ClienteNoExistenteException | PedidoDuplicadoException e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR: " + e.getMessage());
			}
			
		
		
	}

}

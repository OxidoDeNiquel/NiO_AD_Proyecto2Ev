package gestor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import clases.Articulo;
import clases.Articulo_Pedido;
import clases.Cliente;
import clases.Pedido;
import clases.Stock;
import excepciones.ArticuloNoExisteException;
import excepciones.ArticuloSinCodigoException;
import excepciones.Articulo_PedidoDuplicadoException;
import excepciones.ClienteNoExistenteException;
import excepciones.ClienteSinCodigoException;
import excepciones.PedidoDuplicadoException;

public class Gestor {
	Connection connection;
	ArrayList<Pedido> listaPedidosXML;
	ArrayList<Pedido> listaPedidoXML_fallidos;
	ArrayList<Articulo_Pedido> listaArticulosXML;

	public Gestor() throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException,
			IOException, ClienteSinCodigoException, ClienteNoExistenteException, ArticuloSinCodigoException {
		// inicializamos todos los parámetros, pero no nos conectamos a la base
		Class.forName("org.sqlite.JDBC");
		this.connection = DriverManager.getConnection(
				"jdbc:sqlite:/Users/niobeclaveria/eclipse-workspace/Proyecto_2doT_AD/bbdd_ProyectoNiO_prueba.db");
		listaPedidosXML = new ArrayList<Pedido>();
		listaPedidoXML_fallidos = new ArrayList<Pedido>();
		listaArticulosXML = new ArrayList<Articulo_Pedido>();
	}
	
	public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
            	connection.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

	public void leerXML()
			throws ParserConfigurationException, SAXException, IOException, SQLException, ClienteSinCodigoException,
			ClienteNoExistenteException, ArticuloSinCodigoException, ArticuloNoExisteException {
		// Ruta donde se encuentra el XML

		String rutaRelativa = "src/xml/Pedidos_Tiendas2.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(rutaRelativa));
		doc.getDocumentElement().normalize();

		NodeList pedidos = doc.getElementsByTagName("pedido");

		for (int i = 0; i < pedidos.getLength(); i++) {
			Node pedido = pedidos.item(i); // obtener pedido
			if (pedido.hasChildNodes()) {
				Element elemento = (Element) pedido;
				String numCliente = getNode("numero-cliente", elemento);
				String numPedido = getNode("numero-pedido", elemento);
				String fecha = getNode("fecha", elemento);

				Pedido p = new Pedido(numCliente, numPedido, fecha);

					System.out.println("Pedido nº " + (i + 1) + " leido con éxito\n");
					p.imprimirInformacion();
				//clasificar en listas
					if(verificarCliente(p)) {
						listaPedidosXML.add(p);
					}else {
						listaPedidoXML_fallidos.add(p);
					}
					
				// Obtener la lista de nodos de artículos dentro de cada pedido
				NodeList articulos = elemento.getElementsByTagName("articulo");

				// Recorrer artículos de cada pedido
				for (int j = 0; j < articulos.getLength(); j++) {
					Node articulo = articulos.item(j);
					if (articulo.hasChildNodes()) {
						Element element = (Element) articulo;
						String codigo = getNode("codigo", element);
						String cantidad = getNode("cantidad", element);

						Articulo_Pedido a = new Articulo_Pedido(codigo, cantidad);
						System.out.println("- Articulo nº " + (j + 1) + " leido con éxito\n");
						a.imprimirInformacion();
						
						if (verificarArticulo(a)) {
							listaArticulosXML.add(a);
						}
					}
				}
			}
		}
	}

	public void insertarCliente(Cliente c) {
		insertarCliente(c);
	}

	public void insertarPedidos(ArrayList<Pedido> listaPedidos) throws SQLException, PedidoDuplicadoException, ClienteSinCodigoException, ClienteNoExistenteException, Articulo_PedidoDuplicadoException, ArticuloSinCodigoException {
		verificarPedido(listaPedidos);
		ArrayList<Articulo_Pedido> listaArticulos = getListaArticulosXML();
		insertarArticulos(listaArticulos);
	}

	public void insertarArticulos(ArrayList<Articulo_Pedido> listaArticulos)
			throws SQLException, Articulo_PedidoDuplicadoException, ClienteSinCodigoException, ClienteNoExistenteException, ArticuloSinCodigoException {
		verificarArticulos(listaArticulos);
	}

	private boolean verificarCliente(Pedido p)
			throws SQLException, ClienteSinCodigoException, ClienteNoExistenteException {
		ArrayList<Cliente> listaClientesBBDD = getListaClientes();

		boolean clienteEncontrado = false;
		for (Cliente cliente : listaClientesBBDD) {
			if (cliente.getNumCliente().equals(p.getNumCliente())) {
				clienteEncontrado = true;
				break;
			}
		}
		return clienteEncontrado;

	}

	private boolean verificarArticulo(Articulo_Pedido a)
			throws SQLException, ClienteSinCodigoException, ClienteNoExistenteException, ArticuloSinCodigoException {
		ArrayList<Articulo> listaArticulosBBDD = getListaArticulos();

		boolean articuloEncontrado = false;
		for (Articulo articulo : listaArticulosBBDD) {
			if (articulo.getCodigo().equals(a.getCodigo())) {
				articuloEncontrado = true;
				break;
			}
		}
		return articuloEncontrado;

	}

	private void verificarPedido(ArrayList<Pedido> listaPedidosAInsertar)
			throws SQLException, PedidoDuplicadoException, ClienteSinCodigoException, ClienteNoExistenteException {
		ArrayList<Pedido> listaPedidosExistentes = getListaPedidos();

		for (Pedido pedidoAInsertar : listaPedidosAInsertar) {
			if (!listaPedidosExistentes.contains(pedidoAInsertar)) {
				if (verificarCliente(pedidoAInsertar)) {
					insertarPedidoBBDD(pedidoAInsertar);
					System.out.println("Insertado con éxito el pedido nº" + pedidoAInsertar.getNumPedido());
				} else {
					//throw new ClienteNoExistenteException("El cliente que ha realizado el pedido nº" + pedidoAInsertar.getNumPedido() + " no existe.");
					System.out.println("ERROR: El cliente que ha realizado el pedido nº" + pedidoAInsertar.getNumPedido() + " no existe.");
				}
			} else {
				//throw new ClienteSinCodigoException("El pedido ya está registrado en la base de datos.");
				System.out.println("ERROR: El pedido ya está registrado en la base de datos.");

			}
		}
	}

	private void verificarArticulos(ArrayList<Articulo_Pedido> listaArticulosAInsertar)
			throws SQLException, Articulo_PedidoDuplicadoException, ClienteSinCodigoException, ClienteNoExistenteException, ArticuloSinCodigoException {
		ArrayList<Articulo_Pedido> listaArticulosExistentes = getListaArticulos_Pedidos();

		for (Articulo_Pedido articuloAInsertar : listaArticulosAInsertar) {
			if (!listaArticulosExistentes.contains(articuloAInsertar)) {
				if(verificarArticulo(articuloAInsertar)) {
					insertarArticulo_PedidoBBDD(articuloAInsertar);
					updateStock(articuloAInsertar);
				}else {
					System.out.println("ERROR: El articulo con código " + articuloAInsertar.getCodigo() + " no existe. ");
				}
				
			} else {
				System.out.println("ERROR: El articulo ya está registrado en la base de datos.");
			}
		}
	}

	private void insertarArticuloBBDD(Articulo a) throws SQLException {
		String insertPedidoQuery = "INSERT INTO Clientes VALUES (?, ?, ?, ?)";
		try (PreparedStatement preparedStatementPedido = connection.prepareStatement(insertPedidoQuery)) {
			preparedStatementPedido.setString(1, a.getCodigo());
			preparedStatementPedido.setString(2, a.getDescripcion());
			preparedStatementPedido.setString(3, a.getFamilia());
			preparedStatementPedido.setString(4, a.getFechaDeAlta());
			preparedStatementPedido.executeUpdate();
			System.out.println("El pedido ha sido insertado con éxito");
		}
	}

	private void insertarClienteBBDD(Cliente c) throws SQLException {
		String insertPedidoQuery = "INSERT INTO Clientes VALUES (?, ?, ?, ?)";
		try (PreparedStatement preparedStatementPedido = connection.prepareStatement(insertPedidoQuery)) {
			preparedStatementPedido.setString(1, c.getNumCliente());
			preparedStatementPedido.setString(2, c.getNombre());
			preparedStatementPedido.setString(3, c.getDireccion());
			preparedStatementPedido.setString(4, c.getTelefono());
			preparedStatementPedido.executeUpdate();
			System.out.println("El pedido ha sido insertado con éxito");
		}
	}

	private void insertarPedidoBBDD(Pedido p) throws SQLException {
		String insertPedidoQuery = "INSERT INTO Pedidos VALUES (?, ?, ?)";
		try (PreparedStatement preparedStatementPedido = connection.prepareStatement(insertPedidoQuery)) {
			preparedStatementPedido.setString(1, p.getNumCliente());
			preparedStatementPedido.setString(2, p.getNumPedido());
			preparedStatementPedido.setString(3, p.getFecha());
			preparedStatementPedido.executeUpdate();
			System.out.println("El pedido ha sido insertado con éxito");
		}
	}

	private void insertarArticulo_PedidoBBDD(Articulo_Pedido a) throws SQLException {
		String insertArticuloQuery = "INSERT INTO Articulos_Pedidos VALUES (?, ?)";

		try (PreparedStatement preparedStatementPedido = connection.prepareStatement(insertArticuloQuery)) {
			preparedStatementPedido.setString(1, a.getCodigo());
			preparedStatementPedido.setString(2, a.getCantidad());
			preparedStatementPedido.executeUpdate();
			System.out.println("El artículo ha sido insertado con éxito");
		}
	}
	
	private void updateStock(Articulo_Pedido a) throws SQLException {
		ArrayList<Stock> stockArticulos = getStock();
		String insertArticuloQuery = "UPDATE Stock SET Existencias = Existencias - ? WHERE Cod_articulo = ?";
		for(Stock s : stockArticulos) {
			if(s.getCodArticulo().equals(a.getCodigo())){
				try (PreparedStatement preparedStatementPedido = connection.prepareStatement(insertArticuloQuery)) {
					preparedStatementPedido.setString(1, a.getCantidad());
					preparedStatementPedido.setString(2, a.getCodigo());
					preparedStatementPedido.executeUpdate();
					System.out.println("El stock del artículo ha sido actualizado con éxito");
				}
			}
		}
	}

	private ArrayList<Pedido> getListaPedidos() throws SQLException {
		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
		PreparedStatement selectPedidos = connection.prepareStatement("SELECT * FROM Pedidos");

		ResultSet resultSet = selectPedidos.executeQuery();
		while (resultSet.next()) {
			String numCliente = resultSet.getString(1);
			String numPedido = resultSet.getString(2);
			String fecha = resultSet.getString(3);
			Pedido p = new Pedido(numCliente, numPedido, fecha);
			pedidos.add(p);
		}
		return pedidos;
	}
	
	private ArrayList<Stock> getStock() throws SQLException{
		ArrayList<Stock> articulosStock = new ArrayList<Stock>();
		PreparedStatement selectArticulos = connection.prepareStatement("SELECT * FROM Stock");

		ResultSet resultSet2 = selectArticulos.executeQuery();
		while (resultSet2.next()) {
			String codigo = resultSet2.getString(1);
			String cantidad = resultSet2.getString(2);

			Stock a = new Stock(codigo, cantidad);

			articulosStock.add(a);
		}
		return articulosStock;
	}

	private ArrayList<Articulo_Pedido> getListaArticulos_Pedidos() throws SQLException {
		ArrayList<Articulo_Pedido> articulo_Pedidos = new ArrayList<Articulo_Pedido>();
		PreparedStatement selectArticulos = connection.prepareStatement("SELECT * FROM Articulos_Pedidos");

		ResultSet resultSet2 = selectArticulos.executeQuery();
		while (resultSet2.next()) {
			String codigo = resultSet2.getString(1);
			String cantidad = resultSet2.getString(2);

			Articulo_Pedido a = new Articulo_Pedido(codigo, cantidad);

			articulo_Pedidos.add(a);
		}
		return articulo_Pedidos;
	}

	public ArrayList<Cliente> getListaClientes() throws SQLException, ClienteSinCodigoException {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		PreparedStatement selectClientes = connection.prepareStatement("SELECT * FROM Clientes");

		ResultSet resultSet2 = selectClientes.executeQuery();
		while (resultSet2.next()) {
			String numCliente = resultSet2.getString(1);
			String nombre = resultSet2.getString(2);
			String direccion = resultSet2.getString(3);
			String telefono = resultSet2.getString(4);

			Cliente c = new Cliente(numCliente, nombre, direccion, telefono);

			clientes.add(c);
		}
		return clientes;
	}

	public ArrayList<Articulo> getListaArticulos()
			throws SQLException, ClienteSinCodigoException, ArticuloSinCodigoException {
		ArrayList<Articulo> articulos = new ArrayList<Articulo>();
		PreparedStatement selectArticulos = connection.prepareStatement("SELECT * FROM Articulos");

		ResultSet resultSet = selectArticulos.executeQuery();
		while (resultSet.next()) {
			String codigo = resultSet.getString(1);
			String descripcion = resultSet.getString(2);
			String familia = resultSet.getString(3);
			String fechaAlta = resultSet.getString(4);

			Articulo a = new Articulo(codigo, descripcion, familia, fechaAlta);

			articulos.add(a);
		}
		return articulos;
	}

	public ArrayList<Pedido> getListaPedidosXML() {
		return listaPedidosXML;
	}

	public void setListaPedidosXML(ArrayList<Pedido> listaPedidosXML) {
		this.listaPedidosXML = listaPedidosXML;
	}

	public ArrayList<Pedido> getListaPedidoXML_fallidos() {
		return listaPedidoXML_fallidos;
	}

	public void setListaPedidoXML_fallidos(ArrayList<Pedido> listaPedidoXML_fallidos) {
		this.listaPedidoXML_fallidos = listaPedidoXML_fallidos;
	}
	

	public ArrayList<Articulo_Pedido> getListaArticulosXML() {
		return listaArticulosXML;
	}

	public void setListaArticulosXML(ArrayList<Articulo_Pedido> listaArticulosXML) {
		this.listaArticulosXML = listaArticulosXML;
	}

	private static String getNode(String etiqueta, Element elemento) {
		NodeList nodos = elemento.getElementsByTagName(etiqueta).item(0).getChildNodes();
		Node nodo = (Node) nodos.item(0);
		return nodo.getNodeValue();
	}

}

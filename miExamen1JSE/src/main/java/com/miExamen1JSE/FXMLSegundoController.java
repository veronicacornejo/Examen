package com.miExamen1JSE;



import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class FXMLSegundoController {

	Connection con = null;
	
	@FXML
	private Label lblTitulo,lblMensaje, lblResEliminado;
	
	@FXML
	private TextArea txtaListado; 
	
	@FXML
	private TextField txtId, txtNombre, txtApellidos, txtDireccion, txtTelefono, txtUser, txtPass;
	
		
	@FXML
	private void listarUsuarios(ActionEvent event) {
		try {
			String url = "jdbc:sqlite:D:/2021/miDBCJAVA/miDBuser.db";
			con = DriverManager.getConnection(url);

			String cad ="";
			String sql3 = "SELECT * FROM usuario";
			PreparedStatement st2 = con.prepareStatement(sql3);
			ResultSet rs = st2.executeQuery();
			while(rs.next()) {
				cad+= "id: "+rs.getInt("id")+", nomUsuario: "+rs.getString("nomUsuario") + ", password: "+
						rs.getString("password")+", nombres: "+rs.getString("nombres") + ", apellidos: "+rs.getString("apellidos") + ",  dirteccion: "+rs.getString("direccion") + ", telefono: "+rs.getString("telefono")+"\n";
				//System.out.println(cad);
			}
			lblTitulo.setText("Lista de Usuarios");
			txtaListado.setText(cad);
			
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	private void registraUsuario(ActionEvent event) {
		
		PreparedStatement psUsuario =null;
		ResultSet rs = null;
		try {
			String url = "jdbc:sqlite:D:/2021/miDBCJAVA/miDBuser.db";
			con = DriverManager.getConnection(url);

			String ap=txtApellidos.getText() ;
			String nom=txtNombre.getText();
			String dir=txtDireccion.getText();
			int tel=Integer.parseInt(txtTelefono.getText());
			String us=txtUser.getText();
			String ps=txtPass.getText();
			
				String sqlUsuario = "INSERT INTO usuario(nomUsuario, password, nombres, apellidos, direccion, telefono) VALUES (?,?,?,?,?,?)";
				psUsuario = con.prepareStatement(sqlUsuario,Statement.RETURN_GENERATED_KEYS);
				psUsuario.setString(1, us);
				psUsuario.setString(2, ps);
				psUsuario.setString(3, nom);
				psUsuario.setString(4, ap);
				psUsuario.setString(5, dir);
				psUsuario.setInt(6, tel);
				int filasAfectadas = psUsuario.executeUpdate();
				lblMensaje.setText("Usuario registrado con éxito.");
				System.out.println("Insertado con exito");
			
			
		}catch(SQLException e1){
			System.out.println(e1.getMessage());
		}finally {
			try {
				if(rs != null) rs.close();				
				if(psUsuario != null) psUsuario.close();
				if(con != null) con.close();
			} catch (SQLException e3) {
					// TODO Auto-generated catch block
				System.out.println(e3.getMessage());
				}
		}
	}
	
	@FXML
	public void eliminar (ActionEvent event) {
		try {
			String url = "jdbc:sqlite:D:/2021/miDBCJAVA/miDBuser.db";
			con = DriverManager.getConnection(url);
	
			int id =Integer.parseInt(txtId.getText());
			String sql = "DELETE FROM usuario WHERE id = ?";
			try(PreparedStatement ps =  con.prepareStatement(sql)){
						
					ps.setInt(1, id);
					ps.executeUpdate();
					lblResEliminado.setText("Se eliminó al usuario con id: "+id);
			}catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}catch(SQLException e1){
			System.out.println(e1.getMessage());
		}finally {
			try {
				if(con != null) con.close();
			} catch (SQLException e3) {
					// TODO Auto-generated catch block
				System.out.println(e3.getMessage());
				}
		}
					
	}
	  
	@FXML
	private void existeUsuario(ActionEvent event) {
		String us=txtUser.getText();
    	try {
    			String url = "jdbc:sqlite:D:/2021/miDBCJAVA/miDBuser.db";
    			con = DriverManager.getConnection(url);
				String sql3 = "SELECT nomUsuario FROM usuario WHERE nomUsuario=?";
				PreparedStatement st2 = con.prepareStatement(sql3);
				st2.setString(1,us);
				ResultSet rs = st2.executeQuery();
				if(rs.next()) {						
					lblMensaje.setText("El Nombre de Usuario ya existe.");
					System.out.println("No se puede regitrar al usuario, el Nombre de Usuario ya existe.");
				
				}else {
					registraUsuario(event);//ValidaDatos(event);//r
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					if(con != null)
						con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
    		
	}
	/*
	private boolean ValidaDatos(ActionEvent event) {
		if(txtNombre.getText().length() == 0 || txtApellidos.getText().length() == 0 || txtDireccion.getText().length() == 0 || txtUser.getText().length() == 0 || txtPass.getText().length() == 0)
    	{
			lblMensaje.setText("Debe llenar todos los datos.");
    	}else if(hayNumerosAca(txtNombre.getText()) || hayNumerosAca(txtApellidos.getText())) {
    		lblMensaje.setText("No se admiten números en el nombre o apellido.");
    	}else {
    		int n1 = Integer.parseInt(txtTelefono.getText());
        		return true;
        	
    	}
		return false;
	}
	*/
	private boolean hayNumerosAca(String texto) {
		
		for(int i=0; i<texto.length();i++) {
			switch(texto.charAt(i)) {
				case '0': case '1': case '2': 
				case '3': case '4': case '5': 
				case '6': case '7': case '8': 
				case '9': 
					return true;
			}
		}		
		
		return false;
	}
	
}

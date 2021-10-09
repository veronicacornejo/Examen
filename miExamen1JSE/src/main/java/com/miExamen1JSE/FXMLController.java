package com.miExamen1JSE;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
/*
Put header here


 */
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLController implements Initializable {
	
	private boolean sw = false;
	
	Connection con = null;
	
	@FXML
    private TextField txtUsuario;
    
    @FXML
    private TextField txtPasss;
    
    @FXML
    private TextArea txtaListado;
    
    @FXML
    private Label lblVerificacion;
    
    @FXML
    private void btnClickAction(ActionEvent event) {
    	    	
    	if(!cantidadRegistros()) {
    		lblVerificacion.setText("Debe cargar la Base de Datos!");
    		cargarSQL(event);
    	}else {
	    	String user = txtUsuario.getText();
	    	String pas = txtPasss.getText();
	    	
	    	if(buscarUsuario(user,pas)) {
	    		abrirVentana(event);
	    	}else {
	    		lblVerificacion.setText("Usuario y/o password son incorrectos!");
	    	} 
    	}
    	
    }
    
    
    private void abrirVentana(ActionEvent event) {
    	try {
    		Stage escenario = new Stage();
			Scene escena = new Scene(loadFXML("segundoP"));//Crear el archivo
			escenario.setScene(escena);
			escenario.setTitle("Segunda ventana");
			escenario.show();		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
        
    private void cargarSQL(ActionEvent event) {//
    	
    	   	
    	Node nodo = (Node) event.getSource();
    	Stage escenario = (Stage) nodo.getScene().getWindow();
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Elija un archivo SQL");
    	fileChooser.getExtensionFilters().addAll(
    			new FileChooser.ExtensionFilter("TXT", "*.txt"),
    			new FileChooser.ExtensionFilter("Todititos", "*.*")
    			);
    	File archivo = fileChooser.showOpenDialog(escenario);
    	//System.out.println(archivo);
    	if(archivo !=null) {
    		System.out.println("Archivo cargado con éxito.");
    		
    		String[] sql2 = new String[100];
    		int i=0;
    		try {
    			Reader reader = new BufferedReader(
    					new InputStreamReader(
    						new FileInputStream(archivo)
    						)    							
    					);
    				if(reader.ready()) {
    					char[] letras = new char[2000];
    					reader.read(letras);
    					String c2="";
    					for(char ch:letras) {
    						if(ch != ';') {
    							c2+=ch;
    						}else {
    							c2+=ch;
    							sql2[i]=c2;
    							i++;
    							System.out.println("sentencia: \n" +c2);
    							c2="";
    						}
    					}
    					
    				}
    				
    				System.out.println("Nro. Elementos: "+i);
					for(int j=0;j<i;j++) {
						
						ejecutarSQL(sql2[j]);
							System.out.println("sentencia ejecutada: " +j);
						}
    				
    				System.out.println("sentencia final ejecutada");
    				reader.close();
    				sw=true;
    				lblVerificacion.setText("Se cargo la Base de Datos!");
    		}catch(IOException e)  {
				e.printStackTrace();
			}
    		
    	}
    	
    }
    
    @FXML
	private void ejecutarSQL(String sql) {

    	try {
				
				String url = "jdbc:sqlite:D:/2021/miDBCJAVA/miDBuser.db";
				con = DriverManager.getConnection(url);
				DatabaseMetaData meta = con.getMetaData();				
				Statement st = con.createStatement();
				st.execute(sql);
				System.out.println("Se ejcutó la sentencia SQL con éxito");
								
				
			} catch (SQLException e) {
				e.printStackTrace();
				e.getMessage();
				System.out.println("Hubo un problema en la ejecución de la consulta");
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
    
    private boolean buscarUsuario(String usuario, String pass) {

    	try {
    			String url = "jdbc:sqlite:D:/2021/miDBCJAVA/miDBuser.db";
    			con = DriverManager.getConnection(url);
				String sql3 = "SELECT nomUsuario FROM usuario WHERE nomUsuario=? AND password =?";
				PreparedStatement st2 = con.prepareStatement(sql3);
				st2.setString(1,usuario);
				st2.setString(2,pass);
				ResultSet rs = st2.executeQuery();
				if(rs.next()) {
					return true;
				}
				return false;
				
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
    	return false;
		
	}  
    
    private boolean cantidadRegistros() {

    	try {
			String url = "jdbc:sqlite:D:/2021/miDBCJAVA/miDBuser.db";
			con = DriverManager.getConnection(url);
			ResultSet rs=null;
			try{
				String sql3 = "SELECT * FROM usuario";
				PreparedStatement st2 = con.prepareStatement(sql3);
				rs = st2.executeQuery();
			}catch(SQLException e) {
				System.out.println("No existe la tabla usuario");
				return false;
			}			
			if(rs.next()) {
				sw=true;
				return true;
			}
			 return false;
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
    	return false;
		
	}  
      
   		    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	
    }    
}
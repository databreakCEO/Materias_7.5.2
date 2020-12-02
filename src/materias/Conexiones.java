/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario 01
 */
public class Conexiones {

    public Connection conexion = null;
    public PreparedStatement psql;
//    private Object [] datos;
    public Statement st;
    String sql;
    String bd, user, pass, URL;

    public Conexiones(String bd, String user, String pass, String url) {
        this.bd = bd;
        this.user = user;
        this.pass = pass;
        URL = url;
    }

    //Métodos  
    public Connection connect(String bd, String user, String pass, String url) {
        this.bd = bd;
        this.user = user;
        this.pass = pass;
        URL = url;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://" + URL + ":3306/" + bd + "?autoReconnect=true&useSSL=false", user, pass);
            //JOptionPane.showMessageDialog(null,"Conexiones establecida!");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.");

        }
        return conexion;
    }

    public void showAllData(String tabla, JTable table) {
        try {
            connect(bd, user, pass, URL);
            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            modelo.setRowCount(0);
            modelo.setColumnCount(0);
            String columnas[] = new String[1];

            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);

            while (rs.next()) {
                columnas[0] = rs.getString(1);
                modelo.addColumn(columnas[0]);
            }
            // st = (Statement) conexion.createStatement();
            rs = st.executeQuery("select * from " + tabla);
            String datos[] = new String[modelo.getColumnCount()];
            while (rs.next()) {
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);

            }
            table.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
    }

    public Object[][] showAllData(String tabla, String consulta) {
        Object datos[][] = new Object[0][0];
        try {
            connect(bd, user, pass, URL);
            String columnas[] = new String[1];
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);
            int numCol = 0;
            while (rs.next()) {
                columnas[0] = rs.getString(1);
                numCol++;
            }
            // st = (Statement) conexion.createStatement();
            System.out.println(consulta);
            st = (Statement) conexion.createStatement();

            rs = st.executeQuery(consulta);
            int renglones = 0;
            while (rs.next()) {

                renglones++;
            }

            rs = st.executeQuery(consulta);
            datos = new Object[renglones][numCol];
            int index = 0;

            while (rs.next()) {
                for (int i = 0; i < numCol; i++) {
                    datos[index][i] = rs.getObject(i + 1);
//                    if (i < numCol - 1) {
//                        texto += datos[i] + " ";
//                        System.out.print(datos[i] + " ");
//                    } else {
//                        texto += datos[i] + "\n";
//                        System.out.print(datos[i] + "\n");
//                    }
                }
                index++;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return datos;
    }

    public Object[][] showAllData(String consulta, int numCol) {
        Object datos[][] = new Object[0][0];
        try {
            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();

            // st = (Statement) conexion.createStatement();
            System.out.println(consulta);
            st = (Statement) conexion.createStatement();

            ResultSet rs = st.executeQuery(consulta);
            int renglones = 0;
            while (rs.next()) {

                renglones++;
            }

            rs = st.executeQuery(consulta);
            datos = new Object[renglones][numCol];
            int index = 0;

            while (rs.next()) {
                for (int i = 0; i < numCol; i++) {
                    datos[index][i] = rs.getObject(i + 1);
//                    if (i < numCol - 1) {
//                        texto += datos[i] + " ";
//                        System.out.print(datos[i] + " ");
//                    } else {
//                        texto += datos[i] + "\n";
//                        System.out.print(datos[i] + "\n");
//                    }
                }
                index++;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return datos;
    }

    public void showEspecificData(String tabla, String atributo, JTable table) {
        try {
            connect(bd, user, pass, URL);
            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            modelo.setRowCount(0);
            modelo.setColumnCount(0);
            String columnas[] = new String[1];

            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);
//            String clave = rs.getString(1);
            int con = 0;
//            rs = st.executeQuery("show columns from " + tabla);
            while (rs.next()) {
                if (con == 0) {
                    columnas[0] = rs.getString(1);
                }
                modelo.addColumn(rs.getString(1));
                con++;
            }
            // st = (Statement) conexion.createStatement();

            String codigo = "";

            rs = st.executeQuery("select * from " + tabla + " where " + modelo.getColumnName(0) + " = '" + atributo + "'");
            String datos[] = new String[modelo.getColumnCount()];
            while (rs.next()) {
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);

            }
            table.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
    }

    public String showEspecificData(String consulta) {
        String datos[] = new String[1];
        String result;
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                datos[0] = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        result = datos[0];
        return result;
    }

    public String[] Log(String usuario, String contra) {
        String datos[] = new String[9];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = "null";
        }
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("Select * from Alu where Num_Ctrl = '" + usuario + "' and Pass = '" + contra + "';");

            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(3);
                datos[2] = rs.getString(4);
                datos[3] = rs.getString(5);
                datos[4] = rs.getString(6);
                datos[5] = rs.getString(7);
                datos[6] = rs.getString(8);
                datos[7] = rs.getString(9);
                datos[8] = rs.getString(10);

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
            datos[0] = "";
        }
        return datos;
    }
    public String[] ExtraerCuenta(String usuario) {
        String datos[] = new String[9];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = "null";
        }
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("Select * from Alu where Num_Ctrl = '" + usuario+"';");

            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(3);
                datos[2] = rs.getString(4);
                datos[3] = rs.getString(5);
                datos[4] = rs.getString(6);
                datos[5] = rs.getString(7);
                datos[6] = rs.getString(8);
                datos[7] = rs.getString(9);
                datos[8] = rs.getString(10);

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
            datos[0] = "";
        }
        return datos;
    }

    public boolean Loggear(String usuario, String contra) {
        String datos[] = new String[9];
        boolean band = false;
        for (int i = 0; i < datos.length; i++) {
            datos[i] = "null";
        }
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("Select * from Alu where Num_Ctrl = '" + usuario + "' and Pass = '" + contra + "';");

            while (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrectos.");
            return false;

        }
        return false;
    }

    public ArrayList<String[]> Cursando(String usuario) {
        ArrayList<String[]> datos = new ArrayList<>();
        try {
            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("select * from MateriaCursando where Num_Ctrl= '" + usuario + "';");
            while (rs.next()) {
                datos.add(new String[]{rs.getString(2) + rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return datos;
    }

    public String[][] Kardex(String usuario, String carrera) {
        int idCarrera = 0;
        switch (carrera) {
            case "Sistemas":
                idCarrera = 1;
                break;
            case "Mecatronica":
                idCarrera = 8;
                break;
        }
        String datos[][] = new String[1][1];
        try {
            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            System.out.println("Select count(clave) from KardexMat where Num_Ctrl = '" + usuario + "' and ID_Carrera = " + idCarrera + ";");
            //select * from kardexmat where Num_Ctrl = '16130809' and ID_Carrera = 1 order by NombreMat
            ResultSet rs = st.executeQuery("Select count(clave) from KardexMat where Num_Ctrl = '" + usuario + "' and ID_Carrera = " + idCarrera + ";");
            rs.next();
            int filas = rs.getInt(1);
//            System.out.println(filas);
            System.out.println("Select * from KardexMat where Num_Ctrl = '" + usuario + "' and ID_Carrera = " + idCarrera + ";");
            rs = st.executeQuery("Select * from KardexMat where Num_Ctrl = '" + usuario + "' and ID_Carrera = " + idCarrera + ";");
            datos = new String[filas][5];
            int index = 0;
            while (rs.next()) {
                datos[index][0] = rs.getString(2);
                datos[index][1] = rs.getString(3);
                datos[index][2] = rs.getString(4);
                datos[index][3] = rs.getString(5);
                datos[index][4] = rs.getString(6);
                index++;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return datos;
    }

    public String showOnlyAEspecificData(String consulta) {
        String datos[] = new String[1];
        String result = "";
        try {
            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                datos[0] = rs.getString(1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        result = datos[0];
        return result;
    }

    public String ejecutarSQL(String sql) {
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {

        }
        return sql;
    }

    public String Insert(Object[] campos, String tabla) {
        sql = "Insert into " + tabla + " values(";
        for (int i = 0; i < campos.length; i++) {
            if (i < campos.length - 1) {
                sql += "?, ";
            } else {
                sql += "?";
            }

        }
        sql += ") ";
//        System.out.println(sql);
        String query = "insert into " + tabla + " values(";
        for (int i = 0; i < campos.length; i++) {
            if (i < campos.length - 1) {
                query += "'" + campos[i] + "', ";
            } else {
                query += "'" + campos[i] + "'";
            }

        }
        query += ");";
        System.out.println(query);
        try {
            connect(bd, user, pass, URL);
            PreparedStatement psql = conexion.prepareStatement(sql);

            for (int i = 0; i < campos.length; i++) {
                psql.setObject(i + 1, campos[i]);
            }

            psql.executeUpdate();

            psql.close();
//            JOptionPane.showMessageDialog(null, "Exito al registrar");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en dar de alta, revise los datos por favor, asegúrese de que sean los indicados.");
            query = "";
        }
        return query;
    }

    public void deleteAllData(String tabla) {
        sql = "delete * from " + tabla;
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en eliminación del elemento.");

        }
    }

    public void deleteEspecificData(String tabla, String condicionAtributo, String campo) {

        sql = "delete from " + tabla + " where " + condicionAtributo + " = '" + campo + "'";
        System.out.println(sql);
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                sql = "delete from " + tabla + " where " + condicionAtributo + " = " + campo;
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Error en baja");
            } catch (SQLException ex1) {
                JOptionPane.showMessageDialog(null, "Error en baja");
            }
        }
    }

    public void deleteEspecificData(String tabla, Object campo) {

        Object clave[][] = new String[1][2];

        int contador = 0;
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);

            while (rs.next()) {
                if (contador == 0) {
                    clave[0][0] = rs.getString(1);
                    clave[0][1] = rs.getString(2);
                }
                contador++;
            }
            if (!clave[0][1].equals("int(11)")) {
                campo = "'" + campo + "'";
            }

            sql = "delete from " + tabla + " where " + clave[0][0] + " = " + campo;
//            System.out.println("delete from " + tabla + " where " +clave[0][0]+ " = " + "'"+campo+"'");
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en baja");
        }
    }

    public void returnAllDataOnComboBox(JComboBox sel, String atributo, String tabla) {
        try {

            connect(bd, user, pass, URL);

            //   String datos[] = new String[1];
            Statement st;
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT " + atributo + " FROM " + tabla);
            while (rs.next()) {

                sel.addItem(rs.getString(1));

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
        }
    }

    public String[] returnOnlyOneColumn(String tabla, String columna) {
        String[] datos = new String[1];
        int i = 0;
        int contador = 0;
        try {

            connect(bd, user, pass, URL);

            //   String datos[] = new String[1];
            Statement st;
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT " + columna + " FROM " + tabla);
            while (rs.next()) {
                contador++;
            }
            datos = new String[contador];
            rs = st.executeQuery("SELECT " + columna + " FROM " + tabla);
            while (rs.next()) {

                datos[i] = rs.getString(1);
                i++;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
        }
        return datos;
    }

    public void Disconnect() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexiones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Modify(Object[] datos, String tabla, String atributo) {

        try {
            connect(bd, user, pass, URL);
            String[] clave = new String[1];
            String atributos[][] = new String[2][datos.length];
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);
            int i = 0;
            while (rs.next()) {
                if (i == 0) {
                    clave[0] = rs.getString(1);
                }
                atributos[0][i] = rs.getString(1);
                atributos[1][i] = rs.getString(2);
                if (!atributos[1][i].equals("int(11)")) {
                    datos[i] = "'" + datos[i] + "'";
                }
                i++;
            }
            datos[0] = atributo;
            String sql = "";
            for (int j = 0; j < datos.length; j++) {
                if (j < datos.length - 1) {
                    sql += " " + atributos[0][j] + " = " + datos[j] + ",";
                } else {
                    sql += " " + atributos[0][j] + " = " + datos[j];
                }
            }
            String ssql = "UPDATE " + tabla + " SET " + sql + " where " + clave[0] + " = '" + atributo + "'";
            PreparedStatement ps = conexion.prepareStatement(ssql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexiones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    int errMod = 0;

    public String Modificar(String tabla, Object[] datos, String[] atributos, String atributoClave, Object clave) {
        errMod = 0;
        String query = "";
        connect(bd, user, pass, URL);
        String ssql;
        PreparedStatement ps;
        for (int i = 0; i < datos.length; i++) {
            try {
                ssql = "UPDATE " + tabla + " SET " + atributos[i] + " = '" + datos[i] + "' " + " where " + atributoClave + " = '" + clave + "';";
//                    System.out.println(ssql);
                query += ssql + "\n";
                ps = conexion.prepareStatement(ssql);
                ps.executeUpdate();
            } catch (Exception ex) {
                try {
                    ssql = "UPDATE " + tabla + " SET " + atributos[i] + " = '" + datos[i] + " ' " + " where " + atributoClave + " = '" + clave + "';";
//                    System.out.println(ssql);
                    query += ssql + "\n";
                    ps = conexion.prepareStatement(ssql);
                    ps.executeUpdate();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error en el atributo " + atributos[i] + ", no se ha podido efectuar su modificación.");
                    errMod++;
                }
            }
        }
        return query;
    }

    public String ModificarDireccion(String tabla, Object[] datos, String[] atributos, String atributoClave, Object clave) {
        String query = "";
        connect(bd, user, pass, URL);
        String ssql;
        PreparedStatement ps;
        for (int i = 0; i < datos.length; i++) {
            try {
                String aux = "";
                for (int j = 0; j < datos[i].toString().length(); j++) {
                    aux += datos[i].toString().charAt(j);
                    if ((int) datos[i].toString().charAt(j) == 92) {
                        aux += (char) 92;
                        System.out.println("invertida");
                    }

                }
                datos[i] = aux;
                ssql = "UPDATE " + tabla + " SET " + atributos[i] + " = '" + datos[i] + "' " + " where " + atributoClave + " = '" + clave + "'";
                System.out.println(ssql);
                query += ssql + "\n";
                ps = conexion.prepareStatement(ssql);
                ps.executeUpdate();
            } catch (Exception ex) {
                try {
                    ssql = "UPDATE " + tabla + " SET " + atributos[i] + " = '" + datos[i] + " ' " + " where " + atributoClave + " = '" + clave + "'";
                    System.out.println(ssql);
                    query += ssql + "\n";
                    ps = conexion.prepareStatement(ssql);
                    ps.executeUpdate();
                } catch (Exception e) {
                    System.out.println(i);
                }
            }
        }
        return query;
    }

    public ArrayList<String> clavesDisp(String carrera) {
        ArrayList<String> datos = new ArrayList<>();
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();

            ResultSet rs = st.executeQuery("select * from grupo where ID_Carrera = " + carrera + " and espacioAlumnos>5 order by clave;");
            while (rs.next()) {
                datos.add(rs.getString(2) + rs.getString(1));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return datos;
    }

    public String[] regDatos(String tabla, String atributo) {
        String clave[] = new String[1];
        String[] datos = new String[1];
        String result;
        int contador = 0;
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);

            while (rs.next()) {
                if (contador == 0) {
                    clave[0] = rs.getString(1);
                }

                contador++;

            }
            datos = new String[contador];
            //  System.out.println( " Select * From " + tabla + " Where " + clave[0] + " = " + "'"+ atributo +"'");

            sql = " Select * From " + tabla + " Where " + clave[0] + " = " + "'" + atributo + "'";
            rs = st.executeQuery(sql);
            while (rs.next()) {

                for (int i = 0; i < datos.length; i++) {
                    datos[i] = rs.getString(i + 1);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return datos;
        // return result;

    }

    public void regQueryDatos(JTable tabla, String nombrecol, String consulta) {
        DefaultTableModel modelo = new DefaultTableModel();
        String clave[] = new String[1];
        String[] datos = new String[1];
        String result;
        modelo.addColumn(nombrecol);
        int contador = 0;

        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {

                datos[0] = rs.getString(1);
                modelo.addRow(datos);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        tabla.setModel(modelo);
        // return result;

    }

    public String regDatos(String tabla, String columnaEspe, String columnaClave, String atributo) {
//        String[] datos = new String[1];
        String result = "";
        int contador = 0;
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);

            while (rs.next()) {
                if (columnaClave.equals(rs.getString(1))) {
                    if (!"int(11)".equals(rs.getString(2))) {
                        atributo = "'" + atributo + "'";
                    }
                }
                contador++;

            }
//            datos = new String[contador];

            //  System.out.println( " Select * From " + tabla + " Where " + clave[0] + " = " + "'"+ atributo +"'");
            sql = " Select " + columnaEspe + " From " + tabla + " Where " + columnaClave + " = " + atributo;
            rs = st.executeQuery(sql);
            while (rs.next()) {
                result = rs.getString(1);
//                for (int i = 0; i < datos.length; i++) {
//                    datos[i] = rs.getString(i + 1);
//                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return result;
        // return result;

    }

    public String[][] regDatos(String tabla) {
        String[][] datos = new String[1][1];
        int contador = 0;
        try {
            connect(bd, user, pass, URL);

            st = (Statement) conexion.createStatement();
            ResultSet rs = st.executeQuery("show columns from " + tabla);

            while (rs.next()) {
                contador++;
            }
            // st = (Statement) conexion.createStatement();
            int j = 0;
            rs = st.executeQuery("select * from " + tabla);
            datos = new String[contador][contador];
            while (rs.next()) {
                for (int i = 0; i < datos.length; i++) {
                    datos[j][i] = rs.getString(i + 1);
                }
                j++;

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        return datos;
        // return result;

    }

    public String returnAtribute(String tabla, String columna, String dato) {
        String datos[] = new String[1];
        String result;
        try {

            connect(bd, user, pass, URL);
            st = (Statement) conexion.createStatement();
            sql = "select " + columna + " from " + tabla + " where " + columna + " = " + "'" + dato + "'";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                datos[0] = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en consulta");
        }
        result = datos[0];
        return result;

    }
}

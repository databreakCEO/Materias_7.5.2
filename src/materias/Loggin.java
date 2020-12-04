/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import jroundborder.JLabelRound;

/**
 *
 * @author Daniel Gonzalez Cabrera
 */
public class Loggin extends javax.swing.JFrame {

    /**
     * Creates new form Loggin
     */
    int contador = 0;
    Cuenta c;
    File carpeta;
    String[] listado;
    String directorio;
    ImageIcon icon;
    Image image;
    JLabelRound labelCircular = new JLabelRound();
    boolean cambiar = false;
    RoundedBorder rb;
    Conexiones con;
    String carrera, numControl;
    String alu[];
    int creditos = 0;
    String pass;

    public Loggin() {
        initComponents();
        try {
            this.setTitle("Inicio de sesión");
            try {
                this.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/rayo.jpg")).getImage());
            } catch (Exception e) {

            }
            int size = 100;
            //creamos la conexion hacia la base de datos
            con = new Conexiones("ITLDB", "root", "", "localhost");

            //Asignamos el logo del Instituto Tecnologico de la Laguna al jLabel "labelCircular"
            labelCircular.setBounds(0, 0, size, size);
            icon = new ImageIcon(getClass().getResource("/Imagenes/ITL.png"));
            image = Imagenes.getScaledImage(icon.getImage(), this.labelCircular.getWidth(), this.labelCircular.getHeight());
            this.labelCircular.setIcon(new ImageIcon(image));
            jPanel1.add(labelCircular);

            icon = new ImageIcon(getClass().getResource("/Imagenes/papelera.png"));
            image = Imagenes.getScaledImage(icon.getImage(), this.jLabelPapelera.getWidth(), this.jLabelPapelera.getHeight());
            this.jLabelPapelera.setIcon(new ImageIcon(image));

            this.setLocationRelativeTo(null);

            //Creamos un listado de los archivos de carrera dentro de la carpeta "Materias por carrera"
            carpeta = new File("Materias por carrera");
            listado = carpeta.list();
        } catch (Exception e) {

        }
    }

    public void crearArchivos(String nombre) {
        try {
            // creamos un objeto Archivo donde se leeremos toda la informacion
            // de la carrera del alumno, luego se dividira en varios archivos
            // para hacer la creacion de objetos Materia mas sencillo
            Archivo archivo = new Archivo("Materias por carrera\\" + nombre);
            archivo.crearLectura();
            String line = archivo.LeerLinea();
            ArrayList<String> materias = new ArrayList<>();
            ArrayList<String> claves = new ArrayList<>();
            ArrayList<String> horarios = new ArrayList<>();
            ArrayList<String> maestros = new ArrayList<>();
            ArrayList<String> costos = new ArrayList<>();
            ArrayList<String> semestres = new ArrayList<>();
            while (line != null) {
                String cadena = "";
                String cadenaTotal = "";
                String lineNueva = "";
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != " ".charAt(0) && line.charAt(i) != "\t".charAt(0) && i < line.length()) {
                        if (((int) line.charAt(i)) < 47 || ((int) line.charAt(i)) > 58) {
                            cadena += line.charAt(i) + "";

                        } else {

                            break;
                        }
                    } else {
                        cadenaTotal += cadena + " ";
                        cadena = "";

                    }
                }
                materias.add(cadenaTotal);

                for (int i = cadenaTotal.length(); i < line.length(); i++) {
                    lineNueva += line.charAt(i);
                }
                line = lineNueva;
                cadena = "";
                cadenaTotal = "";
                lineNueva = "";
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != " ".charAt(0) && line.charAt(i) != "\t".charAt(0)) {
                        cadena += line.charAt(i);
                    } else {
                        break;
                    }
                }
                claves.add(cadena);
                for (int i = cadena.length(); i < line.length(); i++) {
                    lineNueva += line.charAt(i);
                }
                line = lineNueva;
                cadena = "";
                cadenaTotal = "";
                lineNueva = "";
                int con = 0;
                for (int i = 1; i < line.length(); i++) {

                    if (line.charAt(i) == " ".charAt(0) || line.charAt(i) == "\t".charAt(0)) {
                        con++;
                        if (con == 5) {
                            cadenaTotal += cadena;
                            break;
                        } else {
                            cadena += line.charAt(i);
                        }
                    } else {
                        cadena += line.charAt(i);
                    }
                }

                horarios.add(cadena);
                for (int i = cadena.length() + 2; i < line.length(); i++) {
                    lineNueva += line.charAt(i);
                }
                line = lineNueva;

                maestros.add(line.substring(0, line.length() - 4));
                costos.add(line.substring(line.length() - 3, line.length() - 2));
                semestres.add(line.substring(line.length() - 1, line.length()));
                line = archivo.LeerLinea();

            }
            String n = "";
            for (int i = 0; i < nombre.length() - 4; i++) {
                n += nombre.charAt(i) + "";
            }
            String materia = "";
            for (int i = 0; i < materias.size(); i++) {
                for (int j = 0; j < materias.get(i).length() - 1; j++) {
                    materia += materias.get(i).charAt(j);
                }
                materias.set(i, materia);
                materia = "";
            }
            // se crean los archivos para que recibiran la informacion
            // de la carrera recien separada
            Archivo NombreMaterias = new Archivo(n + "\\NombreMaterias.txt");
            Archivo ClaveMaterias = new Archivo(n + "\\ClaveMaterias.txt");
            Archivo HorarioMaterias = new Archivo(n + "\\HorarioMaterias.txt");
            Archivo MaestroMaterias = new Archivo(n + "\\MaestroMaterias.txt");
            Archivo CostosMaterias = new Archivo(n + "\\Costos.txt");
            Archivo SemestresMaterias = new Archivo(n + "\\Semestres.txt");
            NombreMaterias.crearEscritura();
            ClaveMaterias.crearEscritura();
            HorarioMaterias.crearEscritura();
            MaestroMaterias.crearEscritura();
            CostosMaterias.crearEscritura();
            SemestresMaterias.crearEscritura();
            //se inserta la informacion en los archivos
            for (int i = 0; i < materias.size(); i++) {
                NombreMaterias.EscribirLinea(materias.get(i));
                ClaveMaterias.EscribirLinea(claves.get(i));
                HorarioMaterias.EscribirLinea(horarios.get(i));
                MaestroMaterias.EscribirLinea(maestros.get(i));
                CostosMaterias.EscribirLinea(costos.get(i));
                SemestresMaterias.EscribirLinea(semestres.get(i));
                if (i < materias.size() - 1) {
                    NombreMaterias.NuevaLinea();
                    ClaveMaterias.NuevaLinea();
                    HorarioMaterias.NuevaLinea();
                    MaestroMaterias.NuevaLinea();
                    CostosMaterias.NuevaLinea();
                    SemestresMaterias.NuevaLinea();
                }
            }

            NombreMaterias.CerrarEscritura();
            ClaveMaterias.CerrarEscritura();
            HorarioMaterias.CerrarEscritura();
            MaestroMaterias.CerrarEscritura();
            CostosMaterias.CerrarEscritura();
            SemestresMaterias.CerrarEscritura();
        } catch (Exception e) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabEntrar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextUser = new javax.swing.JTextField();
        jTextPass = new javax.swing.JTextField();
        jLabelPapelera = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(340, 365));
        setResizable(false);
        setSize(new java.awt.Dimension(341, 320));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabEntrar.setBackground(new java.awt.Color(102, 102, 255));
        jLabEntrar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabEntrar.setForeground(new java.awt.Color(255, 255, 255));
        jLabEntrar.setText("          ENTRAR");
        jLabEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabEntrar.setOpaque(true);
        jLabEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabEntrarMouseClicked(evt);
            }
        });
        getContentPane().add(jLabEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 240, 40));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("NUMERO DE CONTROL:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 190, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("CONTRASEÑA:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 120, 120));

        jTextUser.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jTextUser.setToolTipText("Usuario");
        jTextUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTextUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextUserKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUserKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextUserKeyTyped(evt);
            }
        });
        getContentPane().add(jTextUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 240, 30));

        jTextPass.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jTextPass.setToolTipText("Contraseña");
        jTextPass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTextPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextPassKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextPassKeyTyped(evt);
            }
        });
        getContentPane().add(jTextPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 240, 30));

        jLabelPapelera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPapeleraMouseClicked(evt);
            }
        });
        getContentPane().add(jLabelPapelera, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, 30, 30));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 360));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void crearFrameCuenta() {
        try {
            // Se crea el frame Cuenta y se visualiza 
            c = new Cuenta(this, this.carrera, this.carrera + "\\Usuarios\\" + numControl, numControl, pass);
            c.setVisible(true);
            this.setVisible(false);

        } catch (Exception e) {

        }
    }

    private void jLabEntrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabEntrarMouseClicked

        // se crea el objeto para el archivo Logs
        Archivo logs = new Archivo("logs.txt");
        String logeo;
        ArrayList<String> listaLogs = new ArrayList<>();

        //se intenta guardar la informacion del archivo logs en un array
        if (logs.crearLectura()) {
            while ((logeo = logs.LeerLinea()) != null) {
                listaLogs.add(logeo);
            }
            logs.CerrarLectura();
        }

        boolean registrado = false;
        //        String usuario = "20130102";
        //        pass ="1234";
        //        String usuario = "16130809";
        //        pass = "fullcounter";

        String usuario = jTextUser.getText();
        pass = jTextPass.getText();

        //se separa en un arreglo la informacion de cada iteracion del array
        //para tener en un solo arreglo los usuario ya logeados
        String separados[];
        for (int i = 0; i < listaLogs.size(); i++) {
            separados = listaLogs.get(i).split(" - ");
            if (usuario.equals(separados[0])) {
                registrado = true;
                numControl = separados[0];
                carrera = separados[1];
                break;
            }
        }

        //en caso de que no se haya encontrado el usuario en el archivo logs
        //se mandará a la base de datos, la consulta de la informacion del
        //usuario
        if (registrado == false) {
            //se consulta la informacion general del usuario en caso de existir
            alu = con.Log(usuario, pass);

            //en caso de que exista el usuario, se empieza a crear los archivos
            //necesarios para trabajar sobre ellos mas adelante
            if (!alu[0].equals("null") && !alu[1].equals("null")) {
                //se separa la informacion general del usuario en un arreglo
                String[] split = alu[1].split(" ");
                carrera = split[0];
                numControl = alu[0];

                for (String listado1 : listado) {
                    if (listado1.equals(carrera + ".txt")) {
                        crearArchivos(listado1);
                    }
                }

                //se consulta toda la informacion de las materias de la carrera
                //insertandolo en una matriz
                Object[][] matdb;
                try {
                    matdb = con.showAllData(carrera, "Select * from " + carrera);
                } catch (Exception e) {
                    matdb = new Object[0][0];
                    JOptionPane.showMessageDialog(this, "Error en actualización, asegúrese de que tiene acceso a internet. Si el problema persiste pongase en contacto con el creador del programa.");
                }
                //se inserta la informacion en un archivo txt con el nombre de la carrera
                if (matdb.length > 0) {
                    Archivo materiasBD = new Archivo("Materias por carrera\\" + carrera + ".txt");
                    materiasBD.crearEscritura();
                    for (int i = 0; i < matdb.length; i++) {
                        for (int j = 0; j < matdb[0].length; j++) {
                            if (j < matdb[0].length - 1 && j != 1) {
                                materiasBD.EscribirLinea(matdb[i][j] + " ");
                            } else {
                                materiasBD.EscribirLinea(matdb[i][j] + "");
                            }

                        }
                        if (i < matdb.length - 1) {
                            materiasBD.NuevaLinea();
                        }
                    }
                    materiasBD.CerrarEscritura();

                }

                try {

                    //Se verifica si ya existe la carpeta de ese usuario
                    boolean band = false, user = false;
                    carpeta = new File(carrera + "\\Usuarios");
                    listado = carpeta.list() == null ? null : carpeta.list();
                    try {
                    } catch (Exception ex) {
                        listado = null;
                    }
                    if (listado != null) {
                        for (String listado1 : listado) {
                            if (numControl.equals(listado1)) {
                                user = true;
                                break;
                            }
                        }
                    }
                    //En caso de no existir la carpeta del usuario, se creará
                    if (!user) {
                        String nombreArchivo = numControl;
                        nombreArchivo = nombreArchivo.toUpperCase();
                        carpeta = new File(carrera + "\\Usuarios");

                        if (band == false) {

                            try {

                                //se crean los archivos necesarios para el 
                                //funcionamiento del software
                                logs.crearEscritura();
                                for (int i = 0; i < listaLogs.size(); i++) {
                                    logs.EscribirLinea(listaLogs.get(i));
                                    logs.NuevaLinea();
                                }
                                logs.EscribirLinea(numControl + " - " + carrera);
                                logs.CerrarEscritura();
                                Archivo a1 = new Archivo(carrera + "\\NombreMaterias.txt");
                                if (a1.crearLectura()) {

                                    File direct = new File(carrera + "\\Usuarios\\" + nombreArchivo);
                                    direct.mkdir();
                                    direct.createNewFile();
                                    Archivo datos = new Archivo(direct + "\\datos.txt");
                                    datos.crearEscritura();
                                    Archivo kardex = new Archivo(direct + "\\kardex.txt");
                                    kardex.crearEscritura();
                                    Archivo a2 = new Archivo(direct + "\\materiasEstado.txt");
                                    a2.crearEscritura();
                                    Archivo guardar = new Archivo(direct + "\\horariosGuardados.txt");
                                    guardar.crearEscritura();
                                    Archivo cursando = new Archivo(direct + "\\materiasCursando.txt");
                                    cursando.crearEscritura();
                                    ArrayList<String> lista = new ArrayList<>();
                                    String line = a1.LeerLinea();
                                    band = true;
                                    int cont = 0;

                                    while (line != null) {
                                        band = true;
                                        for (int j = 0; j < lista.size(); j++) {
                                            if (line.equals(lista.get(j)) || line.equals("RESIDENCIA") || line.equals("RESIDENCIA PROFESIONAL") || line.equals("TUTORIA")) {
                                                band = false;
                                            }
                                        }
                                        if (band == true) {
                                            lista.add(line);
                                        }
                                        line = a1.LeerLinea();
                                    }

                                    //se consulta el kardex del alumno,
                                    //se inserta en una matriz
                                    String[][] kard = con.Kardex(numControl, carrera);
                                    boolean coincide = false;

                                    //Se verifica si una materia de la lista
                                    //total existe dentro del kardex, de
                                    //no existir en el kardex, se guardará
                                    //en un archivo de texto
                                    for (int j = 0; j < lista.size(); j++) {
                                        coincide = false;
                                        for (String[] kard1 : kard) {
                                            if (lista.get(j).equals(kard1[1]) && Integer.valueOf(kard1[2]) >= 70) {
                                                coincide = true;
                                                break;
                                            }

                                        }
                                        if (cont > 0 && coincide == false) {
                                            a2.NuevaLinea();
                                        }
                                        if (coincide == false) {
                                            a2.EscribirLinea(lista.get(j));
                                            cont++;
                                        }

                                    }

                                    //se inserta los datos del kardex del
                                    //alumno en un archivo txt llamado 
                                    //"Kardex.txt"
                                    for (int i = 0; i < kard.length; i++) {
                                        kardex.EscribirLinea(kard[i][0] + " - " + kard[i][1] + " - " + kard[i][2] + " - " + kard[i][3] + " - " + kard[i][4]);
                                        if (i < kard.length - 1) {
                                            kardex.NuevaLinea();
                                        }
                                        creditos += Integer.valueOf(kard[i][4]);
                                    }

                                    //se extrae de la base de datos, la
                                    //lista de materias que el alumno está
                                    //cursando en el momento  y se guarda
                                    //en un archivo txt
                                    ArrayList<String[]> matCurs = con.Cursando(numControl);
                                    String lineaMat;
                                    for (int i = 0; i < matCurs.size(); i++) {
                                        lineaMat = "";
                                        for (int j = 0; j < matCurs.get(i).length; j++) {
                                            lineaMat += (matCurs.get(i)[j]);
                                            if (j < matCurs.get(i).length - 1) {
                                                lineaMat += " - ";
                                            }

                                        }
                                        cursando.EscribirLinea(lineaMat);
                                        if (i < matCurs.size() - 1) {
                                            cursando.NuevaLinea();
                                        }
                                    }
                                    //se guarda en un archivo txt los datos
                                    //del alumno
                                    for (String alu1 : alu) {
                                        datos.EscribirLinea(alu1);
                                        datos.NuevaLinea();
                                    }
                                    datos.EscribirLinea(creditos + "");

                                    //se cierran todos los archivos para que
                                    //se guarden
                                    a1.CerrarLectura();
                                    a2.CerrarEscritura();
                                    datos.CerrarEscritura();
                                    kardex.CerrarEscritura();
                                    guardar.CerrarEscritura();
                                    cursando.CerrarEscritura();

                                    cambiar = false;
                                } else {
                                    JOptionPane.showMessageDialog(this, "Lo sentimos, aun no existen horarios para esta carrera");
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(this, "Lo sentimos, aun no existen horarios para esta carrera");
                            }

                        } else {
                            contador--;
                            JOptionPane.showMessageDialog(this, "Elija una carrera y escriba un nombre de cuenta");
                        }
                    }
                    crearFrameCuenta();

                } catch (HeadlessException e) {
                    e.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(this, "No se ha podido iniciar sesión con este numero de control, verifique que la contraseña sea correcta.\n Asegúrese de tener internet y ser alumno del Instituto Tecnologico de la Laguna");
            }
        } //En caso de que la carpeta del alumno ya exista, tan tolo se accederá
        //al archivo de datos del usuario, necesarios para la creacion de el 
        //frame "Cuenta"
        else {

            alu = new String[9];
            Archivo dat = new Archivo(carrera + "\\Usuarios\\" + numControl + "\\datos.txt");
            dat.crearLectura();
            String lineaDat;
            int i = 0;

            //Se guardará la información en el arreglo alu 
            while ((lineaDat = dat.LeerLinea()) != null) {
                if (i < 9) {
                    alu[i++] = lineaDat;
                } else {
                    creditos = Integer.valueOf(lineaDat);
                }
            }
            //independientemente de si tuvo que ser creado la carpeta del
            //alumno o no, al terminar cualquiera de los 2 procedimientos
            //se mandará a llamar el frame "Cuenta"
            crearFrameCuenta();

        }

    }//GEN-LAST:event_jLabEntrarMouseClicked

    private void jTextUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUserKeyReleased
    }//GEN-LAST:event_jTextUserKeyReleased

    private void jTextUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUserKeyTyped
        //Solo se aceptarán letras y digitos, tampoco se aceptara que la tecla
        //Ctrl este presionada
        if (evt.getKeyCode() == KeyEvent.VK_CONTROL) {
            evt.consume();
        } else if (!Character.isLetter(evt.getKeyChar()) && !Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextUserKeyTyped

    private void jTextPassKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPassKeyTyped
        //Solo se aceptarán letras y digitos, tampoco se aceptara que la tecla
        //Ctrl este presionada
        if (evt.getKeyCode() == KeyEvent.VK_CONTROL) {
            evt.consume();
        } else if (!Character.isLetter(evt.getKeyChar()) && !Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextPassKeyTyped

    private void jTextPassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPassKeyPressed
        //No se aceptará que la tecla Ctrl o Shift esten presionadas
        if (evt.isControlDown() || evt.isShiftDown()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextPassKeyPressed

    private void jTextUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUserKeyPressed
        //No se aceptará que la tecla Ctrl o Shift esten presionadas
        if (evt.isControlDown() || evt.isShiftDown()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextUserKeyPressed

    private void jLabelPapeleraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPapeleraMouseClicked
        // TODO add your handling code here:

        if (jTextUser.getText() != null && jTextUser.getText().length() > 0) {
            Archivo logs = new Archivo("logs.txt");
            logs.crearLectura();
            String linea = "";
            boolean encontrado = false;
            ArrayList<String> lineas = new ArrayList<>();
            String[] split = {"", ""};
            while ((linea = logs.LeerLinea()) != null) {
                split = linea.split(" - ");
                if (split[0].equals(jTextUser.getText())) {
                    encontrado = true;
                    break;
                } else {
                    lineas.add(linea);
                }
            }
            if (encontrado) {
                logs.crearEscritura();
                Archivo carpetaAEliminar = new Archivo(split[1] + "\\Usuarios\\" + split[0]);
                carpetaAEliminar.Eliminar();
                JOptionPane.showMessageDialog(this, "Los registros del usuario con el numero de control " + split[0] + " han sido eliminados.");
                for (int i = 0; i < lineas.size(); i++) {
                    logs.EscribirLinea(lineas.get(i));
                    if(i<lineas.size()-1){
                        logs.NuevaLinea();
                    }
                    
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Ingrese el número de control del que quiere borrar los registros guardados en su sistema.");
        }
    }//GEN-LAST:event_jLabelPapeleraMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Loggin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Loggin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Loggin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Loggin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new Loggin().setVisible(true);
            }
        }
        );

    }

    //Se crea una clase interna unicamente para darle formato redondo a los
    //JLabels
    class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabEntrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelPapelera;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextPass;
    private javax.swing.JTextField jTextUser;
    // End of variables declaration//GEN-END:variables
}

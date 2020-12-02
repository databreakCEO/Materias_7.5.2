/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

import Utilerias.JTable.ColorCeldas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Gonzalez Cabrera
 */
public class VisualizarHorarios extends javax.swing.JFrame {

    /**
     * Creates new form MateriasFrame2
     */
    DefaultTableModel modelo;
    ArrayList<Horario> horariosTerminados;
    Cuenta c;
    String nombre;
    Color[] colores = new Color[8];
    ColorCeldas horariosPintados;
    Archivo horariosGuardados;
    ArrayList<String> nombres;
    DefaultTableModel modeloTabla;
    DefaultTableModel modeloNombresMaterias;
    ColorCeldas colorCeldas;
    ColorCeldas colorNombres;
    String texto;
    String[] nombreYClaves, claves;
    Horario horario;
    Conexiones con;

    public VisualizarHorarios(String carrera, String nombre, Cuenta c) {
        initComponents();
        try {
//            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            setTitle("Horarios guardados");
            try {
                this.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/rayo.jpg")).getImage());
            } catch (Exception e) {

            }
            this.c = c;
            con = new Conexiones("ITLDB", "root", "", "localhost");
            this.setSize(1350, 718);
            colorCeldas = new ColorCeldas(jTable3);
            colorNombres = new ColorCeldas(jTable2);
            colores[0] = new Color(244, 124, 131);
            colores[1] = new Color(197, 124, 244);
            colores[2] = new Color(241, 244, 124);
            colores[3] = new Color(244, 174, 124);
            colores[4] = new Color(124, 244, 197);
            colores[5] = new Color(124, 206, 244);
            colores[6] = new Color(166, 244, 124);
            colores[7] = new Color(243, 124, 244);
            modelo = (DefaultTableModel) jTable1.getModel();
//        modeloProfesores = (DefaultTableModel) jTable2.getModel();
            this.nombre = nombre;
            horariosGuardados = new Archivo(nombre + "\\horariosGuardados.txt");
            jTable2.setEnabled(false);
            horariosTerminados = new ArrayList<>();

            jTable3.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable3.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(3).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(4).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(5).setPreferredWidth(150);
            jTable3.setRowHeight(75);
            jTable3.setGridColor(Color.black);
            jTable3.setShowGrid(true);
            jTable3.setEnabled(false);

            modeloTabla = (DefaultTableModel) jTable3.getModel();
            modeloNombresMaterias = (DefaultTableModel) jTable2.getModel();
            Materia.crearListaMaterias(carrera);

            for (int i = 0; i < Materia.lista.size() - 1; i++) {
                if (Materia.lista.get(i).codigoMat.equals(Materia.lista.get(i + 1).codigoMat)) {
                    Materia.lista.get(i).unirMateria(Materia.lista.get(i + 1));
                    Materia.lista.remove(i + 1);
                    i--;
                }
            }
            horariosGuardados.crearLectura();
            texto = "";
            nombres = new ArrayList<>();
            horario = new Horario();
            crearMat();
            modelo = (DefaultTableModel) jTable1.getModel();

            for (int i = 0; i < nombres.size(); i++) {
                Object[] o = {i + 1, nombres.get(i)};
                modelo.addRow(o);
            }

            try {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem jMItem = new JMenuItem("Eliminar horario");

                jMItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int r = jTable1.getSelectedRow();

                        if (r >= 0) {
                            int conf = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este horario?");
                            if (conf == 0) {
                                try {
                                    String nombre = jTable1.getValueAt(r, 1).toString();
                                    horariosGuardados.crearLectura();
                                    String horarioG = "";
                                    boolean unique = true;
                                    ArrayList<String> horarios = new ArrayList<>();
                                    while ((horarioG = horariosGuardados.LeerLinea()) != null) {
                                        String separado[] = horarioG.split("->");

                                        if (nombre.equals(separado[0])) {
                                            if (unique) {
                                                unique = false;
                                            } else {
                                                horarios.add(horarioG);
                                            }
                                        } else {
                                            horarios.add(horarioG);
                                        }
                                    }
                                    horariosGuardados.CerrarLectura();
                                    horariosGuardados.crearEscritura();
                                    for (int i = 0; i < horarios.size(); i++) {
                                        horariosGuardados.EscribirLinea(horarios.get(i));
                                        if (i < horarios.size() - 1) {
                                            horariosGuardados.NuevaLinea();
                                        }
                                    }
                                    horariosGuardados.CerrarEscritura();

                                    modelo.setRowCount(0);
//                                    modeloNombresMaterias.setRowCount(0);
                                    modeloNombresMaterias.setRowCount(8);
//                                    modeloTabla.setRowCount(0);
                                    modeloTabla.setRowCount(14);

                                    for (int i = 7; i < 21; i++) {
                                        if (i == 12) {
                                            jTable3.setValueAt((i) + " PM", i - 7, 0);
                                        } else if (i > 12) {
                                            jTable3.setValueAt((i - 12) + " PM", i - 7, 0);
                                        } else {
                                            jTable3.setValueAt(i + " AM", i - 7, 0);
                                        }

                                    }

                                    for (int i = 0; i < 8; i++) {
                                        colorNombres.addCelda(i, 0, Color.WHITE);
                                    }

                                    for (int i = 0; i < jTable3.getRowCount(); i++) {
                                        for (int j = 1; j < 6; j++) {
                                            colorCeldas.addCelda(i, j, Color.WHITE);
                                        }
                                    }

                                    colorNombres.repaint();
                                    horariosGuardados.crearLectura();
                                    texto = "";
                                    nombres = new ArrayList<>();
                                    horario = new Horario();
                                    crearMat();
                                    modelo = (DefaultTableModel) jTable1.getModel();

                                    for (int i = 0; i < nombres.size(); i++) {
                                        Object[] o = {i + 1, nombres.get(i)};
                                        modelo.addRow(o);
                                    }

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "No se pudo eliminar el horario.");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Elija un horario a eliminar.");
                        }
                    }
                });
                JMenuItem Subir = new JMenuItem("Subir horario");
                JMenuItem Bajar = new JMenuItem("Bajar horario");
                Subir.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int r = jTable1.getSelectedRow();

                        if (r >= 1) {
                            String[] datosRow = new String[2];
                            datosRow[0] = jTable1.getValueAt(r, 0).toString();
                            datosRow[1] = jTable1.getValueAt(r, 1).toString();
                            jTable1.setValueAt(jTable1.getValueAt(r - 1, 0).toString(), r, 0);
                            jTable1.setValueAt(jTable1.getValueAt(r - 1, 1).toString(), r, 1);

                            jTable1.setValueAt(datosRow[0], r - 1, 0);
                            jTable1.setValueAt(datosRow[1], r - 1, 1);

                            Horario aux = horariosTerminados.get(r);
                            horariosTerminados.set(r, horariosTerminados.get(r - 1));
                            horariosTerminados.set(r - 1, aux);
                            Mostrar();
                            Guardar();
                        }

                    }
                });
                Bajar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int r = jTable1.getSelectedRow();

                        if (r < jTable1.getRowCount() - 1) {
                            String[] datosRow = new String[2];
                            datosRow[0] = jTable1.getValueAt(r, 0).toString();
                            datosRow[1] = jTable1.getValueAt(r, 1).toString();
                            jTable1.setValueAt(jTable1.getValueAt(r + 1, 0).toString(), r, 0);
                            jTable1.setValueAt(jTable1.getValueAt(r + 1, 1).toString(), r, 1);

                            jTable1.setValueAt(datosRow[0], r + 1, 0);
                            jTable1.setValueAt(datosRow[1], r + 1, 1);

                            Horario aux = horariosTerminados.get(r);
                            horariosTerminados.set(r, horariosTerminados.get(r + 1));
                            horariosTerminados.set(r + 1, aux);
                            Mostrar();
                            Guardar();
                        }

                    }
                });
                popup.add(jMItem);
                popup.add(Subir);
                popup.add(Bajar);
                jTable1.setComponentPopupMenu(popup);
                File matCur = new File(c.nombre + "\\materiasCursando.txt");
                if (matCur.length() > 0) {
                    this.jButEnviar.setEnabled(false);
                }
                System.out.println(matCur.length());
                System.out.println();
            } catch (Exception e) {
            }
        } catch (Exception e) {

        }
    }

    public void Guardar() {
        Archivo horariosGuardados = new Archivo(c.nombre + "\\horariosGuardados.txt");
        String texto = "";
        horariosGuardados.crearEscritura();
        for (int j = 0; j < jTable1.getRowCount(); j++) {
            texto = jTable1.getValueAt(j, 1) + "->";
            for (int i = 0; i < horariosTerminados.get(j).getSchedule().size(); i++) {
                if (i < horariosTerminados.get(j).getSchedule().size() - 1) {
                    texto += horariosTerminados.get(j).getSchedule().get(i).codigoMat + "-";
                } else {
                    texto += horariosTerminados.get(j).getSchedule().get(i).codigoMat;
                }

            }
            horariosGuardados.EscribirLinea(texto);
            if (j < jTable1.getRowCount() - 1) {
                horariosGuardados.NuevaLinea();
            }
        }

        horariosGuardados.CerrarEscritura();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButEnviar = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1342, 718));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane4.setAutoscrolls(true);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(255, 0, 0));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("LOS HORARIOS SE MANDARÁN DE UNO POR UNO, DE ARRIBA HACIA ABAJO, EN EL MOMENTO EN QUE UN HORARIO SEA ACEPTADO TERMINARPA EL ENVIO DE HORARIOS, CONSIDERE ESTO ANTES DE OPRIMIR EL SIGUIENTE BOTÓN");
        jTextArea1.setWrapStyleWord(true);
        jScrollPane4.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 350, 100));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Nombre del horario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(0).setHeaderValue("#");
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(1000);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 350, 350));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"7 AM", "", null, null, null, null},
                {"8 AM", null, null, null, null, null},
                {"9 AM", null, null, null, null, null},
                {"10 AM", null, null, null, null, null},
                {"11 AM", null, null, null, null, null},
                {"12 PM", null, null, null, null, null},
                {"1 PM", null, null, null, null, null},
                {"2 PM", null, null, null, null, null},
                {"3 PM", null, null, null, null, null},
                {"4 PM", null, null, null, null, null},
                {"5 PM", null, null, null, null, null},
                {"6 PM", null, null, null, null, null},
                {"7 PM", null, null, null, null, null},
                {"8 PM", null, null, null, null, null},
                {"9 PM", null, null, null, null, null}
            },
            new String [] {
                "Hora", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable3MousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 980, 640));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 350, 180));

        jButton1.setText("Borrar todos los horarios guardados");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, -1));

        jButEnviar.setBackground(new java.awt.Color(51, 255, 0));
        jButEnviar.setText("ENVIAR HORARIOS");
        jButEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEnviarActionPerformed(evt);
            }
        });
        getContentPane().add(jButEnviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 650, 350, 30));

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setForeground(new java.awt.Color(255, 0, 0));
        jLabel36.setText(" DE OPRIMIR EL SIGUIENTE BOTON ");
        jLabel36.setOpaque(true);
        getContentPane().add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 620, 190, -1));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText(" TERMINARÁ EL ENVIO DE HORARIOS, CONSIDERE ESTO ANTES ");
        jLabel3.setOpaque(true);
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 340, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText(" ABAJO, EN EL MOMENTO EN QUE UN HORARIO SEA ACEPTADO");
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 330, -1));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText(" LOS HORARIOS SE MANDARÁN UNO POR UNO, DE ARRIBA HACIA ");
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 330, 20));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 175));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 180, 175));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 180, 175));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 180, 175));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        jLabel8.setText("Daniel Gonzalez Cabrera");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 0, 180, 175));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 180, 175));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 0, 180, 175));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, -5, 160, 180));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 170, 160, 175));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 175, 180, 175));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 175, 180, 175));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 175, 180, 175));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 175, 180, 175));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 175, 180, 175));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 175, 180, 175));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 175, 180, 175));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        jLabel19.setText("Daniel Gonzalez Cabrera");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 345, 160, -1));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 180, 175));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 350, 180, 175));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 350, 180, 175));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 350, 180, 175));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 350, 180, 175));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 350, 180, 175));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 350, 180, 175));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 500, 160, 220));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 525, 180, 200));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 505, 180, 220));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        jLabel31.setText("Daniel Gonzalez Cabrera");
        getContentPane().add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 505, 180, 220));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 525, 180, 200));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 525, 180, 200));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 525, 180, 200));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 525, 180, 200));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void crearMat() {
        try {
            String linea;
            do {
                linea = horariosGuardados.LeerLinea();
                if (linea != null) {
                    texto = linea;
                    nombreYClaves = texto.split("->");
                    nombres.add(nombreYClaves[0]);
                    claves = nombreYClaves[1].split("-");
                    horario = new Horario();
                    boolean band = false;
                    for (String clave : claves) {

                        for (int j = 0; j < Materia.lista.size(); j++) {
                            if (clave.equals(Materia.lista.get(j).codigoMat)) {
                                horario.getSchedule().add(Materia.lista.get(j));
                                band = true;
                                break;
                            }
                        }
                    }
                    Horario filtrado = new Horario();
                    filtrado.getSchedule().add(horario.getSchedule().get(0));

                    for (int i = 0; i < horario.getSchedule().size(); i++) {
                        band = true;
                        for (int j = 0; j < filtrado.getSchedule().size(); j++) {
                            band = true;
                            for (int k = 0; k < 5; k++) {
                                int HIF = 0, HIS = 0, HFF, HFS;
                                String[] finalesF = filtrado.getSchedule().get(j).horaFinales[k].split(":");
                                String[] finalesS = horario.getSchedule().get(i).horaFinales[k].split(":");
                                String[] iniciosF = filtrado.getSchedule().get(j).horaInicios[k].split(":");
                                String[] iniciosS = horario.getSchedule().get(i).horaInicios[k].split(":");

                                HIF = Integer.valueOf(iniciosF[0]);
                                HIS = Integer.valueOf(iniciosS[0]);
                                HFF = Integer.valueOf(finalesF[0]);
                                HFS = Integer.valueOf(finalesS[0]);
                                if ((HIF <= HIS)
                                        && (HFF >= HFS)
                                        && HIF != 0 && HIS != 0) {
//                                        System.out.println((filtrado.schedule.get(j).horaInicios[k] + "<=" + horario.schedule.get(i).horaInicios[k])
//                                                + " && " + (filtrado.schedule.get(j).horaFinales[k] + ">=" + horario.schedule.get(i).horaFinales[k])
//                                        );
                                    band = false;
//                                        System.out.println(filtrado.schedule.get(j).nombreMat);
//                                        System.out.println(horario.schedule.get(i).nombreMat);
                                    break;
                                }
                            }
//                            if(band == false){
//                                break;
//                            }
                        }
//                        System.out.println(band);
                        if (band == true) {
                            filtrado.getSchedule().add(horario.getSchedule().get(i));
                        }
                    }

                    horariosTerminados.add(filtrado);
//                    horariosTerminados.add(horario);

                }
            } while (linea != null);
            texto += "\n";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void Mostrar() {
        try {
            int r = jTable1.getSelectedRow();
            modeloTabla.setRowCount(0);
            modeloTabla.setNumRows(14);
            modeloNombresMaterias.setRowCount(0);
            modeloNombresMaterias.setRowCount(horariosTerminados.get(r).getSchedule().size());
            for (int i = 7; i < 21; i++) {
                if (i == 12) {
                    jTable3.setValueAt((i) + " PM", i - 7, 0);
                } else if (i > 12) {
                    jTable3.setValueAt((i - 12) + " PM", i - 7, 0);
                } else {
                    jTable3.setValueAt(i + " AM", i - 7, 0);
                }

            }
            colorCeldas = new ColorCeldas(jTable3);
            colorNombres = new ColorCeldas(jTable2);

            for (int i = 0; i < horariosTerminados.get(r).getSchedule().size(); i++) {

                horariosTerminados.get(r).getSchedule().get(i).color = colores[i];
                jTable2.setValueAt(horariosTerminados.get(r).getSchedule().get(i).nombreMat, i, 0);
                colorNombres.addCelda(i, 0, colores[i]);
                colorNombres.addCelda(i, 1, colores[i]);

            }
            horariosTerminados.get(r).getSchedule().forEach((_item) -> {
                horariosTerminados.get(r).Mandar(jTable3, colorCeldas);
            });

            colorCeldas.repaint();
        } catch (Exception e) {

        }
    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        Mostrar();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MousePressed
        // TODO add your handling code here:
        try {
            horariosPintados.repaint();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jTable3MousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:     
        try {
            int opcion = JOptionPane.showConfirmDialog(this, "¿Está usted seguro de borrar todos los horarios guardados?");
            if (opcion == 0) {
                horariosGuardados.crearEscritura();
                horariosGuardados.CerrarEscritura();
                modelo.setRowCount(0);
                modeloNombresMaterias.setRowCount(0);
                modeloNombresMaterias.setRowCount(8);
                modeloTabla.setRowCount(0);
                modeloTabla.setNumRows(14);
                for (int i = 7; i < 21; i++) {
                    if (i == 12) {
                        jTable3.setValueAt((i) + " PM", i - 7, 0);
                    } else if (i > 12) {
                        jTable3.setValueAt((i - 12) + " PM", i - 7, 0);
                    } else {
                        jTable3.setValueAt(i + " AM", i - 7, 0);
                    }

                }
                JOptionPane.showMessageDialog(this, "Horarios borrados con éxito");
            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        // TODO add your handling code here:
        try {
            int r = jTable1.getSelectedRow();
            modeloTabla.setRowCount(0);
            modeloTabla.setNumRows(14);
            modeloNombresMaterias.setRowCount(0);
            modeloNombresMaterias.setRowCount(horariosTerminados.get(r).getSchedule().size());
            for (int i = 7; i < 21; i++) {
                if (i == 12) {
                    jTable3.setValueAt((i) + " PM", i - 7, 0);
                } else if (i > 12) {
                    jTable3.setValueAt((i - 12) + " PM", i - 7, 0);
                } else {
                    jTable3.setValueAt(i + " AM", i - 7, 0);
                }

            }
            colorCeldas = new ColorCeldas(jTable3);
            colorNombres = new ColorCeldas(jTable2);

            for (int i = 0; i < horariosTerminados.get(r).getSchedule().size(); i++) {

                horariosTerminados.get(r).getSchedule().get(i).color = colores[i];
                jTable2.setValueAt(horariosTerminados.get(r).getSchedule().get(i).nombreMat, i, 0);
                colorNombres.addCelda(i, 0, colores[i]);
                colorNombres.addCelda(i, 1, colores[i]);

            }
            horariosTerminados.get(r).getSchedule().forEach((_item) -> {
                horariosTerminados.get(r).Mandar(jTable3, colorCeldas);
            });

            colorCeldas.repaint();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jTable1KeyReleased

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        try {
            c.setVisible(true);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_formWindowClosed

    private void jButEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEnviarActionPerformed
        // TODO add your handling code here:
        JTextField contra = new JPasswordField();
        Object[] message = {
            "Password:", contra
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Introduzca su contraseña por favor.", JOptionPane.OK_CANCEL_OPTION);

        boolean loggeado = con.Loggear(nombre, contra.getText());
        System.out.println(nombre+" "+contra.getText());
        if (option == JOptionPane.OK_OPTION && loggeado) {

            boolean band = true, inicio = false;
            ArrayList<String> clavesDisponibles = con.clavesDisp("1");
            ArrayList<String> claveNoDisponibles = new ArrayList<>();

            for (int i = 0; i < horariosTerminados.size(); i++) {
                band = true;
                for (int j = 0; j < horariosTerminados.get(i).schedule.size(); j++) {
                    if (band) {
                        band = false;
                        for (int k = 0; k < clavesDisponibles.size(); k++) {
                            if (horariosTerminados.get(i).schedule.get(j).codigoMat.equals(clavesDisponibles.get(k))) {
                                band = true;
                            }
                        }
                    } else {
                        claveNoDisponibles.add(horariosTerminados.get(i).schedule.get(j).codigoMat);
                    }
                }
                if (band) {
                    String clave, grupo;
                    for (int j = 0; j < horariosTerminados.get(i).schedule.size(); j++) {
                        clave = horariosTerminados.get(i).schedule.get(j).codigoMat.substring(0, clavesDisponibles.get(j).length() - 1);
                        grupo = horariosTerminados.get(i).schedule.get(j).codigoMat.substring(clavesDisponibles.get(j).length() - 1);
                        System.out.println(clave + " - " + grupo);
                        Object datos[] = {null, grupo, clave, Alumno.numControl, 0, 1};
                        con.Insert(datos, "grupoAlumno");
                    }
                    c.CrearArchivoMateriasCursando();
                    c.refresh();
                    JOptionPane.showMessageDialog(this, "Su selección de carga de materias ha terminado con éxito.");
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "El usuario y/o contraseña son incorrectos .");
        }

    }//GEN-LAST:event_jButEnviarActionPerformed

    public Horario crearHorarios(String clave) {
        Horario horario = new Horario();
        try {
            String[] claves = clave.split(" ");
            for (int i = 0; i < Materia.lista.size(); i++) {
                for (String clave1 : claves) {
                    if (Materia.lista.get(i).codigoMat.equals(clave1)) {
                        horario.insertar(Materia.lista.get(i));
                    }
                }
            }
        } catch (Exception e) {

        }
        return horario;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new VisualizarHorarios("", "", null).setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButEnviar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

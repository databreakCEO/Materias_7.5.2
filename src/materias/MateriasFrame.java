/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

import Utilerias.JTable.ColorCeldas;
import Utilerias.JTable.JTabla;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Gonzalez Cabrera
 */
public class MateriasFrame extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form MateriasFrame2
     */
    Thread t;
    JTabla tablaHorariosColoreados;
    JTabla tablaMateriasColoreadas;
    DefaultTableModel modelo;
    DefaultTableModel modeloProfesores;
    DefaultListModel materiasPendientes;
    ArrayList<String> listaElegidos;
    JList jlist1;
    DefaultListModel listaMateriasPosibles;
    ArrayList<Materia> listaMaestros;
    ArrayList<Materia> listaDesAprov;

    DefaultTableModel modeloTabla;
    ArrayList<Horario> sc;
    ArrayList<Horario> horario;
//    ArrayList<Horario> sc;
    boolean mandar = false;
    boolean mandarIsSelected = false;
    Cuenta c;
    int horariosHabiles = 0;
    int conSpiner = 0;
    String nombre;
    // Color[] colores = {Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.pink};
    Color[] colores = new Color[10];
    int numHorario = 0;
    ColorCeldas horariosPintados;
    ColorCeldas colorCeldas;
    Cargando load;
    int[] creditosMat;

    public MateriasFrame(DefaultListModel materiasPendientes, String carrera, String nombre) {
        initComponents();
        try {
            this.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/rayo.jpg")).getImage());
        } catch (Exception e) {

        }
        setTitle("Creador de horarios");
        load = new Cargando();
        t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        colores[0] = new Color(244, 124, 131);
        colores[1] = new Color(197, 124, 244);
        colores[2] = new Color(241, 244, 124);
        colores[3] = new Color(244, 174, 124);
        colores[4] = new Color(124, 244, 197);
        colores[5] = new Color(124, 206, 244);
        colores[6] = new Color(166, 244, 124);
        colores[7] = new Color(243, 124, 244);
        colores[8] = new Color(255, 248, 220);
        colores[9] = new Color(255, 255, 240);

        modelo = (DefaultTableModel) jTable1.getModel();
        modeloProfesores = (DefaultTableModel) jTable2.getModel();
        listaElegidos = new ArrayList<>();
        listaMaestros = new ArrayList<>();
        this.materiasPendientes = materiasPendientes;
        this.nombre = nombre;

        colorCeldas = new ColorCeldas(jTable1);
        horario = new ArrayList<>();

        modeloTabla = (DefaultTableModel) jTable3.getModel();

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
        listaMateriasPosibles = new DefaultListModel();
        sc = new ArrayList<>();
//        jSpinner1.setValue(1);
        Materia.crearListaMaterias(carrera);
        ArrayList<String> matPen = seccionesRestricciones(carrera, materiasPendientes, Alumno.creditosAcumulados * 100 / 260);
//        if (carrera.equals("Sistemas")) {
//            matPen = secciones(carrera, materiasPendientes);
//        } else {
//            for (int i = 0; i < Materia.lista.size(); i++) {
//                matPen.add(new String[]{"", Materia.lista.get(i).nombreMat});
//            }
//        }
        for (int i = 0; i < matPen.size(); i++) {
            for (int j = 0; j < matPen.size() - 1; j++) {
                if (matPen.get(j).compareTo(matPen.get(j + 1)) > 0) {
                    String aux = matPen.get(j);
                    matPen.set(j, matPen.get(j + 1));
                    matPen.set(j + 1, aux);
                }
            }
        }
        creditosMat = new int[matPen.size()];
        for (int i = 0; i < matPen.size(); i++) {
//            System.out.println("");
//            System.out.print(matPen.get(i)[1] + " ");
            for (int j = 0; j < Materia.lista.size(); j++) {
                if (Materia.lista.get(j).nombreMat.equals(matPen.get(i))) {

                    System.out.print(Materia.lista.get(j).nombreMat + " ");
                    creditosMat[i] = Materia.lista.get(j).costo;
//                    index++;
                    System.out.println(Materia.lista.get(j).costo);
                    break;
                }
            }

        }
        for (int i = 0; i < matPen.size(); i++) {
            modelo.setRowCount(i + 1);
            jTable1.setValueAt(false, i, 0);
            jTable1.setValueAt(matPen.get(i), i, 1);
            jTable1.setValueAt(creditosMat[i], i, 2);
        }

        for (int i = 0; i < Materia.lista.size() - 1; i++) {
            if (Materia.lista.get(i).codigoMat.equals(Materia.lista.get(i + 1).codigoMat)) {
                Materia.lista.get(i).unirMateria(Materia.lista.get(i + 1));
                Materia.lista.remove(i + 1);
                i--;
            }
        }
        this.jLabError.setVisible(false);
    }

    private ArrayList<String> rows = new ArrayList<>();
//    ArrayList<Materia> lista = new ArrayList<>();

    ArrayList<String> datos = new ArrayList<>();
    ArrayList<String> materiasPasadas = new ArrayList<>();

    public ArrayList<String> seccionesRestricciones(String carrera, DefaultListModel materias, int porcentaje) {
        // se definen las restricciones por ligadura directa e indirecta
        ArrayList<ArrayList<String>> restricciones = new ArrayList<>();
        String materia = "";
        rows = new ArrayList<>();
        ArrayList<String> S1 = new ArrayList<>();
        ArrayList<String> S2 = new ArrayList<>();
        ArrayList<String> S3 = new ArrayList<>();
        ArrayList<String> S4 = new ArrayList<>();
        ArrayList<String> S5 = new ArrayList<>();
        ArrayList<String> S6 = new ArrayList<>();
        ArrayList<String> S7 = new ArrayList<>();
        ArrayList<String> S8 = new ArrayList<>();
        ArrayList<String> S9 = new ArrayList<>();
        ArrayList<String> S10 = new ArrayList<>();
        ArrayList<String> S11 = new ArrayList<>();
        ArrayList<String> S12 = new ArrayList<>();
        ArrayList<String> SWOP = new ArrayList<>();
        ArrayList<String> SP = new ArrayList<>();

        boolean band[] = new boolean[20];
        for (int i = 0; i < band.length; i++) {
            band[i] = false;
        }
        switch (carrera) {
            case "Sistemas":
                S1.add("CALCULO DIFERENCIAL");
                S1.add("CALCULO INTEGRAL");
                S1.add("CALCULO VECTORIAL");
                S1.add("ECUACIONES DIFERENCIALES");

                S2.add("FUNDAMENTOS DE PROGRAMACION");
                S2.add("PROGRAMACION ORIENTADA A OBJETOS");
                S2.add("ESTRUCTURA DE DATOS");
                S2.add("TOPICOS AVANZADOS DE PROGRAMACION");

                S3.add("ALGEBRA LINEAL");
                S3.add("INVESTIGACION DE OPERACIONES");
                S3.add("SIMULACION");

                S4.add("TALLER DE INVESTIGACION I");
                S4.add("TALLER DE INVESTIGACION II");

                S5.add("FISICA GENERAL");
                S5.add("PRINCIPIOS ELECTRICOS Y APLICACIONES DIGITALES");
                S5.add("ARQUITECTURA DE COMPUTADORAS");
                S5.add("LENGUAJES DE INTERFAZ");

                S6.add("FUNDAMENTOS DE INGENIERIA DE SOFTWARE");
                S6.add("INGENIERIA DE SOFTWARE");
                S6.add("GESTION DE PROYECTOS DE SOFTWARE");

                // Restriccion de alguna de las anteriores
                S7.add("SISTEMAS OPERATIVOS");
                S7.add("TALLER DE SISTEMAS OPERATIVOS");

                S8.add("LENGUAJES Y AUTOMATAS I");
                S8.add("LENGUAJES Y AUTOMATAS II");

                S9.add("FUNDAMENTOS DE BASES DE DATOS");
                S9.add("TALLER DE BASE DE DATOS");
                S9.add("PROGRAMACION WEB");

                S10.add("FUNDAMENTOS DE TELECOMUNICACIONES");
                S10.add("REDES DE COMPUTADORAS");
                S10.add("CONMUTACION Y ENRUTAMIENTO EN REDES DE DATOS");

                //Despues de 60%
                S11.add("DESARROLLO WEB PILA COMPLETA I");
                S11.add("DESARROLLO WEB PILA COMPLETA II");

                S12.add("PROGRAMACION LOGICA Y FUNCIONAL");
                S12.add("COMPUTACION INTELIGENTE");

                SP.add("SEGURIDAD");
                SP.add("COMPUTACION INTELIGENTE");
                SP.add("TALLER DE DESARROLLO HUMANO");
                SP.add("ADMINISTRACION DE BASE DE DATOS");
                SP.add("ADMINISTRACION DE REDES");
                SP.add("DESARROLLO EN ANDROID");
                SP.add("DESARROLLO EN IOS");
                SP.add("INTELIGENCIA ARTIFICIAL");
                SP.add("SISTEMAS PROGRAMABLES");
                SP.add("DESARROLLO DE HABILIDADES PROFESIONALES EN INFORMATICA");
                SP.add("BIG DATA");
                SP.add("COMPUTO EN LA NUBE");

                SWOP.add("CONTABILIDAD FINANCIERA");
                SWOP.add("CULTURA EMPRESARIAL");
                SWOP.add("DESARROLLO SUSTENTABLE");
                SWOP.add("FUNDAMENTOS DE INVESTIGACION");
                SWOP.add("MATEMATICAS DISCRETAS");
                SWOP.add("METODOS NUMERICOS");
                SWOP.add("PROBABILIDAD Y ESTADISTICA");
                SWOP.add("QUIMICA");
                SWOP.add("TALLER DE ADMINISTRACION");
                SWOP.add("TALLER DE ETICA");

                restricciones.add(SWOP);

                restricciones.add(S1);
                restricciones.add(S2);
                restricciones.add(S3);
                restricciones.add(S4);
                restricciones.add(S5);
                restricciones.add(S6);
                restricciones.add(S7);
                restricciones.add(S8);
                restricciones.add(S9);
                restricciones.add(S10);
                restricciones.add(S11);
                restricciones.add(S12);

                restricciones.add(SP);

                // band[0] = false;   false si no ha pasado POO
                for (int i = materias.size() - 1; i >= 0; i--) {
                    materia = materias.get(i).toString().toString();
                    OUTER:
                    for (int j = 0; j < restricciones.size(); j++) {
                        for (int k = 0; k < restricciones.get(j).size(); k++) {
                            if (restricciones.get(j).get(k).equals(materia)) {
                                switch (materia) {
                                    case "PROGRAMACION ORIENTADA A OBJETOS":
                                        band[0] = true;
                                        break;
                                    case "ESTRUCTURA DE DATOS":
                                        band[1] = true;
                                        break;
                                    case "PROBABILIDAD Y ESTADISTICA":
                                        band[2] = true;
                                        break;
                                    case "PRINCIPIOS ELECTRICOS Y APLICACIONES DIGITALES":
                                        band[3] = true;
                                        break;
                                    case "PROGRAMACION WEB":
                                        band[4] = true;
                                        break;
                                }
                                restricciones.get(j).remove(k);
                                materias.remove(i);
                                break OUTER;
                            }
                        }
                    }
                }

                if (porcentaje < 60) {
                    restricciones.get(12).clear();
                    restricciones.get(11).clear();
                    restricciones.get(13).clear();
                } else {
                    for (int i = 0; i < restricciones.get(restricciones.size() - 1).size(); i++) {
                        rows.add(restricciones.get(restricciones.size() - 1).get(i));
                    }
                }

                if (!band[4]) {
                    restricciones.get(11).clear();
                }

                if (!band[0]) {
                    restricciones.get(9).clear();
                }
                if (!band[1]) {
                    restricciones.get(7).clear();
                    restricciones.get(8).clear();
                    //aun falta graficacion
                }
                if (S3.size() <= 2) {
                    if (!band[2]) {
                        restricciones.get(3).clear();
                    }
                }

                if (!band[3]) {
                    restricciones.get(10).clear();
                }

                for (int i = 0; i < restricciones.get(0).size(); i++) {
                    rows.add(restricciones.get(0).get(i));
                }
                for (int j = 1; j < restricciones.size() - 1; j++) {
                    if (restricciones.get(j).size() > 0) {
                        rows.add(restricciones.get(j).get(0));
                    }
                }

                break;

            case "Electrica":
                // se definen las restricciones por ligadura directa e indirecta
                ArrayList<String> calculo = new ArrayList<>();
                calculo.add("CALCULO DIFERENCIAL");
                calculo.add("CALCULO INTEGRAL");
                calculo.add("CALCULO VECTORIAL");
                calculo.add("ECUACIONES DIFERENCIALES");
                calculo.add("CONTROL I");
                calculo.add("CONTROL II");

                ArrayList<String> electromagnetismo = new ArrayList<>();
                electromagnetismo.add("ELECTROMAGNETISMO");
                electromagnetismo.add("CIRCUITOS ELECTRICOS I");
                electromagnetismo.add("CIRCUITOS ELECTRICOS II");
                electromagnetismo.add("TRANSFORMADORES");

                ArrayList<String> electronica = new ArrayList<>();
                electronica.add("ELECTRONICA ANALOGICA");
                electronica.add("ELECTRONICA DIGITAL");
                electronica.add("ELECTRONICA INDUSTRIAL");

                ArrayList<String> maquinaria = new ArrayList<>();
                maquinaria.add("MAQUINAS SINCRONICAS Y CD");
                maquinaria.add("CENTRALES ELECTRICAS");

                ArrayList<String> instalaciones = new ArrayList<>();
                instalaciones.add("INSTALACIONES ELECTRICAS");
                instalaciones.add("INSTALACIONES ELECTRICAS INDUSTRIALES");

                ArrayList<String> programacion = new ArrayList<>();
                programacion.add("PROGRAMACION");
                programacion.add("METODOS NUMERICOS");

                ArrayList<String> tallerinv = new ArrayList<>();
                tallerinv.add("TALLER DE INVESTIGACION I");
                tallerinv.add("TALLER DE INVESTIGACION II");

                ArrayList<String> instrumentacion = new ArrayList<>();
                instrumentacion.add("INSTRUMENTACION");
                instrumentacion.add("INSTRUMENTACION VIRTUAL");

                ArrayList<String> quimica = new ArrayList<>();
                quimica.add("QUIMICA");
                quimica.add("MECANICA DE FLUIDOS Y TERMODINAMICA");

                ArrayList<String> teoriaelectro = new ArrayList<>();
                teoriaelectro.add("TEORIA ELECTROMAGNETICA");

                ArrayList<String> pruebasymtto = new ArrayList<>();
                pruebasymtto.add("PRUEBAS Y MANTENIMIENTO ELECTRICO");

                ArrayList<String> automatizacion = new ArrayList<>();
                automatizacion.add("AUTOMATIZACION PROGRAMABLE");

                ArrayList<String> costos = new ArrayList<>();
                costos.add("COSTOS Y PRESUPUESTOS DE PROYECTOS ELECTRICOS");

                ArrayList<String> sistemasiluminacion = new ArrayList<>();
                costos.add("SISTEMAS DE ILUMINACION");

                ArrayList<String> lowporcent = new ArrayList<>();
                lowporcent.add("PROBABILIDAD Y ESTADISTICA");
                lowporcent.add("DESARROLLO HUMANO");
                lowporcent.add("FUNDAMENTOS DE INVESTIGACION");
                lowporcent.add("TALLER DE ETICA");
                lowporcent.add("MECANICA CLASICA");
                lowporcent.add("TECNOLOGIA DE LOS MATERIALES");
                lowporcent.add("COMUNICACION HUMANA");
                lowporcent.add("DESARROLLO SUSTENTABLE");
                lowporcent.add("DIBUJO ASISITDO POR COMPUTADORA");
                lowporcent.add("ALGEBRA LINEAL");
                lowporcent.add("MEDICIONES ELECTRICAS");
                lowporcent.add("FISICA MODERNA");
                lowporcent.add("GESTION EMPRESARIAL Y LIDERAZGO");
                lowporcent.add("EQUIPOS MECANICOS");

                ArrayList<String> highporcent = new ArrayList<>();
                highporcent.add("CONTROL DE MAQUINAS ELECTRICAS");
                highporcent.add("MODELADO DE SISTEMAS ELECTRICOS DE POTENCIA");
                highporcent.add("CONTROLADOR LOGICO PROGRAMABLE");
                highporcent.add("SISTEMAS RENOVABLES");
                highporcent.add("CONTROL DEL ARRANQUE Y VELOCIDAD DEL MOTOR DE INDUCCION");
                highporcent.add("REDES DE TIERRA");
                highporcent.add("SISTEMAS ELECTRICOS DE POTENCIA");
                highporcent.add("PROTECCION DE SISTEMAS ELECTRICOS DE POTENCIA");
                highporcent.add("CALIDAD DE LA ENERGIA ELECTRICA");
                highporcent.add("EFICENCIA ENERGETICA EN SISTEMAS ELECTRICOS");

                ArrayList<ArrayList<String>> secciones = new ArrayList<>();
                boolean electro = false,
                 circuit1 = false,
                 circuit2 = false;
                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();

                    if (materia.equals("ELECTROMAGNETISMO")) {
                        electro = true;
                    }
                    for (int j = 0; j < calculo.size(); j++) {
                        if (materia.equals(calculo.get(j))) {
                            calculo.remove(j);
                            break;
                        }
                    }
                    for (int j = 0; j < electromagnetismo.size(); j++) {
                        if (materia.equals(electromagnetismo.get(j))) {
                            electromagnetismo.remove(j);
                            break;
                        }
                    }

                }

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();

                    if (materia.equals("CIRCUITOS ELECTRICOS I")) {
                        circuit1 = true;
                    }
                }

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();

                    if (materia.equals("CIRCUITOS ELECTRICOS II")) {
                        circuit2 = true;
                    }
                }

                boolean transfo = false;

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();

                    if (materia.equals("TRANSFORMADORES")) {
                        transfo = true;
                    }
                }

                if (electro) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = quimica.size() - 1; j >= 0; j--) {
                            if (materia.equals(quimica.get(j))) {
                                quimica.remove(j);
                            }
                        }
                    }
                    secciones.add(teoriaelectro);
                }

                if (circuit1) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = electronica.size() - 1; j >= 0; j--) {
                            if (materia.equals(electronica.get(j))) {
                                electronica.remove(j);
                            }
                        }
                    }
                    secciones.add(electronica);
                }

                boolean instala = true;
                if (circuit2) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = instalaciones.size() - 1; j >= 0; j--) {
                            if (materia.equals(instalaciones.get(j))) {
                                instalaciones.remove(j);
                            }
                        }

                    }
                    secciones.add(instalaciones);

                }

                boolean tranfo = true;
                for (int i = 0; i < electromagnetismo.size(); i++) {
                    if (electromagnetismo.get(i).equals("TRANSFORMADORES")) {
                        tranfo = false;
                    }
                }

                secciones.add(electromagnetismo);

                //se crean restricciones por porcentaje de creditos (55% de creditos)
                if (porcentaje >= 55) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = tallerinv.size() - 1; j >= 0; j--) {
                            if (materia.equals(tallerinv.get(j))) {
                                tallerinv.remove(j);
                                break;
                            }
                        }
                        for (int j = instrumentacion.size() - 1; j >= 0; j--) {
                            if (materia.equals(instrumentacion.get(j))) {
                                instrumentacion.remove(j);
                                break;
                            }
                        }
                        for (int j = maquinaria.size() - 1; j >= 0; j--) {
                            if (materia.equals(maquinaria.get(j))) {
                                maquinaria.remove(j);
                                break;
                            }
                        }

                        if (instala) {
                            for (int j = sistemasiluminacion.size() - 1; j >= 0; j--) {
                                if (materia.equals(sistemasiluminacion.get(j))) {
                                    sistemasiluminacion.remove(j);
                                    break;
                                }
                            }
                        }

                        for (int j = highporcent.size() - 1; j >= 0; j--) {
                            if (materia.equals(highporcent.get(j))) {
                                highporcent.remove(j);

                            }
                        }

                    }
                }
                // Materias sin restriccion directa ni de porcentaje de creditos
                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    for (int j = lowporcent.size() - 1; j >= 0; j--) {
                        if (lowporcent.get(j).equals(materia)) {
                            lowporcent.remove(j);
                        }
                    }

                    for (int j = quimica.size() - 1; j >= 0; j--) {
                        if (quimica.get(j).equals(materia)) {
                            quimica.remove(j);
                            break;
                        }
                    }
                    for (int j = programacion.size() - 1; j >= 0; j--) {
                        if (programacion.get(j).equals(materia)) {
                            programacion.remove(j);
                            break;
                        }
                    }
                    for (int j = teoriaelectro.size() - 1; j >= 0; j--) {
                        if (teoriaelectro.get(j).equals(materia)) {
                            teoriaelectro.remove(j);
                            break;
                        }
                    }

                }

                if (tranfo) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = pruebasymtto.size() - 1; j >= 0; j--) {
                            if (materia.equals(pruebasymtto.get(j))) {
                                pruebasymtto.remove(j);
                            }
                        }
                    }
                    secciones.add(pruebasymtto);
                }

                secciones.add(calculo);
                secciones.add(programacion);

                secciones.add(quimica);

                if (porcentaje >= 55) {

                    secciones.add(instrumentacion);
                    secciones.add(maquinaria);
                    secciones.add(sistemasiluminacion);
                    secciones.add(automatizacion);
                    secciones.add(costos);
                    secciones.add(pruebasymtto);
                    secciones.add(tallerinv);

                    for (int i = 0; i < highporcent.size(); i++) {
                        rows.add(highporcent.get(i));

                    }
                }

                for (int i = 0; i < secciones.size(); i++) {
                    if (secciones.get(i).size() > 0) {
                        rows.add(secciones.get(i).get(0));
                    }
                }
                for (int i = 0; i < lowporcent.size(); i++) {
                    rows.add(lowporcent.get(i));
                }
                break;

            case "Energias Renovables":
                calculo = new ArrayList<>();
                calculo.add("CALCULO DIFERENCIAL");
                calculo.add("CALCULO INTEGRAL");
                calculo.add("CALCULO VECTORIAL");
                calculo.add("ECUACIONES DIFERENCIALES");

                ArrayList<String> quimicas = new ArrayList<>();
                quimicas.add("QUIMICA");
                quimicas.add("BIOQUIMICA");
                quimicas.add("MICROBIOLOGIA");
                quimicas.add("BIOCOMBUSTIBLES");

                electromagnetismo = new ArrayList<>();
                electromagnetismo.add("ELECTROMAGNETISMO");
                electromagnetismo.add("CIRCUITOS ELECTRICOS I");
                electromagnetismo.add("CIRCUITOS ELECTRICOS II");

                ArrayList<String> estatica = new ArrayList<>();
                estatica.add("ESTATICA Y DINAMICA");
                estatica.add("RESISTENCIA DE MATERIALES");

                ArrayList<String> termodinamica = new ArrayList<>();
                termodinamica.add("TERMODINAMICA");
                termodinamica.add("MECANICA DE FLUIDOS");
                termodinamica.add("MAQUINAS HIDRAULICAS");

                ArrayList<String> microbiologia = new ArrayList<>();
                // microbiologia.add("MICROBIOLOGIA");
                // microbiologia.add("BIOCOMBUSTIBLES");

                tallerinv = new ArrayList<>();
                tallerinv.add("TALLER DE INVESTIGACION I");
                tallerinv.add("TALLER DE INVESTIGACION II");

                ArrayList<String> comportamiento = new ArrayList<>();
                comportamiento.add("COMPORTAMIENTO HUMANO EN LAS ORGANIZACIONES");
                comportamiento.add("GESTION DE EMPRESA DE ENERGIA RENOVABLE");

                instrumentacion = new ArrayList<>();
                instrumentacion.add("INSTRUMENTACION");

                ArrayList<String> maquinelec = new ArrayList<>();
                maquinelec.add("MAQUINAS ELECTRICAS");
                maquinelec.add("INSTALACIONES ELECTRICAS E ILUMINACION");

                ArrayList<String> refrigeracion = new ArrayList<>();
                refrigeracion.add("REFRIGERACION Y AIRE ACONDICIONADO");

                ArrayList<String> sisterm = new ArrayList<>();
                sisterm.add("SISTEMAS TERMICOS");

                lowporcent = new ArrayList<>();
                lowporcent.add("TALLER DE ETICA");
                lowporcent.add("FUNDAMENTOS DE INVESTIGACION");
                lowporcent.add("DIBUJO");
                lowporcent.add("FUENTES RENOVABLES DE ENERGIA");
                lowporcent.add("ESTADISTICA Y DISEÑO DE EXPERIMENTOS");
                lowporcent.add("TALLER DE SISTEMAS DE INFORMACION GEOGRAFICA");
                lowporcent.add("DESARROLLO SUSTENTABLE");
                lowporcent.add("TECNOLOGIA E INGENIERIA DE MATERIALES");
                lowporcent.add("PROGRAMACION");
                lowporcent.add("ALGEBRA LINEAL");
                lowporcent.add("METROLOGIA MECANICA Y ELECTRICA");
                lowporcent.add("OPTICA Y SEMICONDUCTORES");
                lowporcent.add("TRANSFERENCIA DE CALOR");

                highporcent = new ArrayList<>();
                highporcent.add("SISTEMAS SOLARES FOTOVOLTAICOS Y TERMICOS");
                highporcent.add("SIMULACION DE SISTEMAS DE ENERGIAS RENOVABLES");
                highporcent.add("ENERGIA EOLICA");
                highporcent.add("FORMULACION Y EVALUACION DE PROYECTOS DE ENERGIAS RENOVABLES");
                highporcent.add("ADMINISTRACION Y TECNICAS DE CONSERVACION");
                highporcent.add("AUDITORIA ENERGETICA");

                secciones = new ArrayList<>();
                boolean calvec = false,
                 circuele = false,
                 bioq = true;
                for (int i = 0; i < materias.size(); i++) {

                    materia = materias.get(i).toString();
                    if (materia.equals("CALCULO VECTORIAL")) {
                        calvec = true;
                    }

                    for (int j = 0; j < calculo.size(); j++) {
                        if (materia.equals(calculo.get(j))) {
                            calculo.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < quimicas.size(); j++) {
                        if (materia.equals(quimicas.get(j))) {
                            quimicas.remove(j);
                            break;
                        }
                    }

                }

                for (int i = 0; i < calculo.size(); i++) {
                    if (calculo.get(i).equals("CALCULO VECTORIAL")) {
                        calvec = false;
                    }
                }

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("CIRCUITOS ELECTRICOS I")) {
                        circuele = true;
                    }

                    for (int j = 0; j < calculo.size(); j++) {
                        if (materia.equals(calculo.get(j))) {
                            calculo.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < estatica.size(); j++) {
                        if (materia.equals(estatica.get(j))) {
                            estatica.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < lowporcent.size(); j++) {
                        if (materia.endsWith(lowporcent.get(j))) {
                            lowporcent.remove(j);
                            break;
                        }
                    }

                }

                boolean estaticdinamic = false;

                for (int i = 0; i < quimicas.size(); i++) {
                    if (quimicas.get(i).equals("BIOQUIMICA")) {
                        bioq = false;
                    }
                }

                boolean tecnoeinge = true,
                 metroymeca = false;

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("METROLOGIA,MECANICA Y ELECTRICA")) {
                        metroymeca = true;
                    }
                }

                if (calvec) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = 0; j < estatica.size(); j++) {
                            if (materia.equals(estatica.get(j))) {
                                estatica.remove(j);
                            }
                        }

                        for (int j = 0; j < electromagnetismo.size(); j++) {
                            if (materia.equals(electromagnetismo.get(j))) {
                                electromagnetismo.remove(j);
                            }
                        }
                    }
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        if (materia.equals("ESTATICA Y DINAMICA")) {
                            estaticdinamic = true;
                        }
                    }
                    secciones.add(electromagnetismo);
                    secciones.add(estatica);
                }
                if (!calvec) {
                    // rows.add(new String[]{"", "CALCULO VECTORIAL"});
                }

                if (circuele) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = 0; j < maquinelec.size(); j++) {
                            if (materia.equals(maquinelec.get(j))) {
                                maquinelec.remove(j);
                            }
                        }
                    }
                }
                if (!circuele) {
                    //rows.add(new String[]{"", "CIRCUITOS ELECTRICOS"});

                }
                if (bioq) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = 0; j < microbiologia.size(); j++) {
                            if (materia.equals(microbiologia.get(j))) {
                                microbiologia.remove(j);
                            }
                        }
                    }
                }

                if (!bioq) {
                    //  rows.add(new String[]{"", "BIOQUIMICA"});
                }

                if (metroymeca) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = 0; j < instrumentacion.size(); j++) {
                            if (materia.equals(instrumentacion.get(j))) {
                                instrumentacion.remove(j);
                            }
                        }
                    }
                    secciones.add(instrumentacion);

                }

                if (!metroymeca) {
                    //  rows.add(new String[]{"", "METROLOGIA MECANICA Y ELECTRICA"});
                }

                if (estaticdinamic) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = 0; j < termodinamica.size(); j++) {
                            if (materia.equals(termodinamica.get(j))) {
                                termodinamica.remove(j);
                            }
                        }
                    }
                    secciones.add(termodinamica);
                }

                secciones.add(microbiologia);

                //se crean restricciones por porcentaje de creditos (55% de creditos)
                if (porcentaje >= 55) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = 0; j < tallerinv.size(); j++) {
                            if (materia.equals(tallerinv.get(j))) {
                                tallerinv.remove(j);
                                break;
                            }
                        }

                        for (int j = 0; j < comportamiento.size(); j++) {
                            if (materia.equals(comportamiento.get(j))) {
                                comportamiento.remove(j);
                                break;
                            }
                        }

                        for (int j = 0; j < highporcent.size(); j++) {
                            if (materia.equals(highporcent.get(j))) {
                                highporcent.remove(j);
                                break;
                            }
                        }
                    }
                    boolean mecanic = false;
                    for (int i = 0; i < calculo.size(); i++) {
                        if (calculo.get(i).equals("MECANICA DE FLUIDOS")) {
                            mecanic = true;
                        }
                    }
                    if (mecanic) {
                        for (int i = 0; i < materias.size(); i++) {
                            materia = materias.get(i).toString();
                            for (int j = 0; j < refrigeracion.size(); j++) {
                                if (materia.equals(refrigeracion.get(j))) {
                                    refrigeracion.remove(j);
                                    break;
                                }
                            }

                        }
                    }
                    secciones.add(refrigeracion);
                }

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    for (int j = lowporcent.size() - 1; j >= 0; j--) {
                        if (lowporcent.get(j).equals(materia)) {
                            lowporcent.remove(j);
                        }
                    }

                    for (int j = 0; j < refrigeracion.size(); j++) {
                        if (materia.equals(refrigeracion.get(j))) {
                            refrigeracion.remove(j);
                            break;
                        }
                    }

                }

                secciones.add(calculo);
                secciones.add(quimicas);
                secciones.add(comportamiento);

                if (porcentaje >= 55) {
                    secciones.add(tallerinv);

                    secciones.add(sisterm);
                    secciones.add(maquinelec);
                    for (int i = 0; i < highporcent.size(); i++) {
                        rows.add(highporcent.get(i));

                    }
                }
                for (int i = 0; i < secciones.size(); i++) {
                    if (secciones.get(i).size() > 0) {
                        rows.add(secciones.get(i).get(0));
                    }
                }
                /*
        secciones.add(single55percent);

        secciones.add(singleWOPercent);

                 */

                for (int i = 0; i < lowporcent.size(); i++) {
                    rows.add(lowporcent.get(i));
                }
                break;

            case "Electronica":
                calculo = new ArrayList<>();
                calculo.add("CALCULO DIFERENCIAL");
                calculo.add("CALCULO INTEGRAL");
                calculo.add("CALCULO VECTORIAL");
                calculo.add("ECUACIONES DIFERENCIALES");

                ArrayList<String> medicioneselec = new ArrayList<>();
                medicioneselec.add("MEDICIONES ELECTRICAS");
                medicioneselec.add("FISICA DE SEMICONDUCTORES");

                programacion = new ArrayList<>();
                programacion.add("PROGRAMACION ESTRUCTURADA");
                programacion.add("PROGRAMACION VISUAL");

                ArrayList<String> diseño = new ArrayList<>();
                diseño.add("DISE?O DIGITAL");
                diseño.add("DISE?O DIGITAL CON VHDL");

                ArrayList<String> circuitelec = new ArrayList<>();
                circuitelec.add("CIRCUITOS ELECTRICOS I");
                circuitelec.add("CIRCUITOS ELECTRICOS II");

                ArrayList<String> diodos = new ArrayList<>();
                diodos.add("DIODOS Y TRANSISTORES");
                diodos.add("DISEÑOS DE TRANSISTORES");
                diodos.add("AMPLIFICACIONES OPERACIONALES");
                diodos.add("INSTRUMENTACION");

                ArrayList<String> control = new ArrayList<>();
                control.add("CONTROL I");
                control.add("CONTROL II");
                control.add("CONTROL DIGITAL");

                tallerinv = new ArrayList<>();
                tallerinv.add("TALLER DE INVESTIGACION I");
                tallerinv.add("TALLER DE INVESTIGACION II");

                lowporcent = new ArrayList<>();
                lowporcent.add("DESARROLLO SUSTENTABLE");
                lowporcent.add("QUIMICA ");
                lowporcent.add("TALLER DE ETICA");
                lowporcent.add("FUNDAMENTOS DE INVESTIGACION");
                lowporcent.add("COMUNICACION HUMANA");
                lowporcent.add("PROBABILIDAD Y ESTADISTICA");
                lowporcent.add("MECANICA CLASICA");
                lowporcent.add("TOPICOS SELECTOS DE FISICA");
                lowporcent.add("DESARROLLO HUMANAO");
                lowporcent.add("ELECTROMAGNETISMO");
                lowporcent.add("ALGEBRA LINEAL");
                lowporcent.add("DESARROLLO PROFESIONAL");
                lowporcent.add("MARCO LEGAL DE LA EMPRESA");
                lowporcent.add("ANALISIS NUMERICO");
                lowporcent.add("ADMINISTRACION GERENCIAL");
                lowporcent.add("PROGRAMACION ESTRUCTURADA");

                highporcent = new ArrayList<>();
                highporcent.add("RADIO Y TELERECEPCIONES");
                highporcent.add("COMUNICACION DIGITAL");
                highporcent.add("TRANSFORMACIONES LINEALES");
                highporcent.add("RADIACION Y ANTENAS");
                highporcent.add("COMUNICACION ANALOGICA");
                highporcent.add("COMUNICACION DIGITAL");
                highporcent.add("FUNDAMENTOS FINANCIEROS");
                highporcent.add("INTRODUCCION A LA TELECOMUNICACIONES");
                highporcent.add("CONTROLADORES LOGICOS PROGRAMABLES");
                highporcent.add("DESARROLLO Y EVALUACION DE PROYECTOS");
                highporcent.add("ELECTRONICA DE POTENCIA");
                highporcent.add("OPTOELECTRONICA");
                highporcent.add("ELECTROFISIOLOGIA ");
                highporcent.add("ANATOMIA Y FISIOLOGIA");
                highporcent.add("TRATAMIENTO DE BIOSEÑALES");
                highporcent.add("METROLOGIA Y NORMATIVIDAD EN BIOINSTRUMENTOS");
                highporcent.add("ELECTRONICA BIOINSTRUMENTAL");
                highporcent.add("SENSORES BIOELECTRONICOS");
                highporcent.add("PROCESAMIENTO DIGITAL DE IMAGENES MEDICAS");
                highporcent.add("PROCESAMIENTO DIGITAL DE SEÑALES");
                highporcent.add("MODELO DE SISTEMAS MECANICOS");
                highporcent.add("SISTEMAS DE CONTROL EN TIEMPO REAL");
                highporcent.add("ROBOTICA");
                highporcent.add("CONTROL NO LINEAL");
                highporcent.add("TOPICOS AVANZADOS DE MECATRONICA");
                highporcent.add("MAQUINAS ELECTRICAS II");
                highporcent.add("INTRODUCCION A LAS ENERGIAS RENOVABLES");
                highporcent.add("CONVERTIDORES ELECTROONICOS DE POTENCIA");
                highporcent.add("CONTROL DE MAQUINAS ELECTRICAS");
                highporcent.add("TRACCION ELECTRICA");
                highporcent.add("LAS ENERGIAS RENOVABLES EN LOS SISTEMAS ELECTRICOS");

                ArrayList<String> teoriaelectromagnetica = new ArrayList<>();
                teoriaelectromagnetica.add("TEORIA ELECTROMAGNETICA");

                ArrayList<String> maquinaselectricas = new ArrayList<>();
                maquinaselectricas.add("MAQUINAS ELECTRICAS");

                ArrayList<String> microcontroladores = new ArrayList<>();
                microcontroladores.add("MICROCONTROLADORES");

                secciones = new ArrayList<>();
                boolean electromag = false;

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("ELECTROMAGNETISMO")) {
                        electromag = true;
                    }

                    for (int j = 0; j < calculo.size(); j++) {
                        if (materia.equals(calculo.get(j))) {
                            calculo.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < medicioneselec.size(); j++) {
                        if (materia.equals(medicioneselec.get(j))) {
                            medicioneselec.remove(j);
                            break;
                        }
                    }
                    for (int j = 0; j < programacion.size(); j++) {
                        if (materia.equals(programacion.get(j))) {
                            programacion.remove(j);
                            break;
                        }
                    }
                    for (int j = 0; j < diseño.size(); j++) {
                        if (materia.equals(diseño.get(j))) {
                            diseño.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < circuitelec.size(); j++) {
                        if (materia.equals(circuitelec.get(j))) {
                            circuitelec.remove(j);
                            break;
                        }
                    }

                }

                circuit1 = false;
                boolean diseñodig = false;

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("CIRCUITOS ELECTRICOS I")) {
                        circuit1 = true;
                    }
                }

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("DISE?O DIGITAL CON VHDL")) {
                        diseñodig = true;
                    }
                }

                if (diseñodig) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = microcontroladores.size() - 1; j >= 0; j--) {
                            if (materia.equals(microcontroladores.get(j))) {
                                microcontroladores.remove(j);
                            }
                        }
                    }
                    secciones.add(microcontroladores);
                }

                if (circuit1) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = diodos.size() - 1; j >= 0; j--) {
                            if (materia.equals(diodos.get(j))) {
                                diodos.remove(j);
                            }
                        }
                    }
                    secciones.add(diodos);
                }

                if (!circuit1) {
                    //  rows.add(new String[]{"", "CIRCUITOS ELECTRICOS I"});
                }

                if (electromag) {

                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = teoriaelectromagnetica.size() - 1; j >= 0; j--) {
                            if (materia.equals(teoriaelectromagnetica.get(j))) {
                                teoriaelectromagnetica.remove(j);
                            }
                        }

                        for (int j = maquinaselectricas.size() - 1; j >= 0; j--) {
                            if (materia.equals(maquinaselectricas.get(j))) {
                                maquinaselectricas.remove(j);
                            }
                        }
                    }
                    secciones.add(maquinaselectricas);
                    secciones.add(teoriaelectromagnetica);
                }
                if (!electromag) {
                    // rows.add(new String[]{"", "ELECTROMAGNETISMO"});
                }

                secciones.add(diseño);
                secciones.add(circuitelec);

                //se crean restricciones por porcentaje de creditos (55% de creditos)
                if (porcentaje >= 55) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        if (diseñodig) {

                            for (int j = microcontroladores.size() - 1; j >= 0; j--) {
                                if (materia.equals(microcontroladores.get(j))) {
                                    microcontroladores.remove(j);
                                    break;
                                }
                            }

                        }
                        for (int j = control.size() - 1; j >= 0; j--) {
                            if (materia.equals(control.get(j))) {
                                control.remove(j);
                                break;
                            }
                        }

                        for (int j = highporcent.size() - 1; j >= 0; j--) {
                            if (materia.equals(highporcent.get(j))) {
                                highporcent.remove(j);
                            }
                        }

                        if (!diseñodig) {
                            // rows.add(new String[]{"", "DISEÑO DIGITAL CON VHDL"});
                        }
                    }
                }

                // Materias sin restriccion directa ni de porcentaje de creditos
                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    for (int j = lowporcent.size() - 1; j >= 0; j--) {
                        if (lowporcent.get(j).equals(materia)) {
                            lowporcent.remove(j);
                        }
                    }

                }

                secciones.add(calculo);
                secciones.add(medicioneselec);
                secciones.add(circuitelec);
                secciones.add(programacion);

                if (porcentaje >= 55) {
                    secciones.add(control);
                    //secciones.add(diodos);

                    for (int i = 0; i < highporcent.size(); i++) {
                        rows.add(highporcent.get(i));

                    }
                }

                for (int i = 0; i < secciones.size(); i++) {
                    if (secciones.get(i).size() > 0) {
                        rows.add(secciones.get(i).get(0));
                    }
                }
                /*
        secciones.add(single55percent);

        secciones.add(singleWOPercent);

                 */

                for (int i = 0; i < lowporcent.size(); i++) {
                    rows.add(lowporcent.get(i));
                }
                break;

            case "Industrial":
                calculo = new ArrayList<>();
                calculo.add("CALCULO DIFERENCIAL");
                calculo.add("CALCULO INTEGRAL");
                calculo.add("ALGEBRA LINEAL");
                calculo.add("CALCULO VECTORIAL");
                calculo.add("FISICA");

                ArrayList<String> probabilidad = new ArrayList<>();
                probabilidad.add("PROBABILIDAD Y ESTADISITCA");
                probabilidad.add("ESTADISTICA INFERENCIAL I");
                probabilidad.add("CONTROL ESTADISITCO DE CALIDAD");

                ArrayList<String> propiedades = new ArrayList<>();
                propiedades.add("PROPIEDADES DE LOS MATERIALES");
                propiedades.add("PROCESOS DE FABRICACION");

                ArrayList<String> operaciones = new ArrayList<>();
                operaciones.add("INVESTIGACION DE OPERACIONES I");
                operaciones.add("INVESTIGACION DE OPERACIONES II");
                operaciones.add("ADMINISTRACION DE OPERACIONES I");
                operaciones.add("ADMINISTRACION DE OPERACIONES II");

                ArrayList<String> estadisticainfe = new ArrayList<>();
                estadisticainfe.add("ESTADISITCA INFERENCIAL II");
                estadisticainfe.add("TOPICOS DE INGENIERIA DE LA CALIDAD I");
                estadisticainfe.add("TOPICOS DE INGENIERIA DE LA CALIDAD II");

                ArrayList<String> estudio = new ArrayList<>();
                estudio.add("ESTUDIO DEL TRABAJO I");
                estudio.add("ESTUDIO DEL TRABAJO II");
                estudio.add("ERGONOMIA");

                ArrayList<String> taller = new ArrayList<>();
                taller.add("TALLER DE INVESTIGACION I");
                taller.add("TALLER DE INVESTIGACION II");

                ArrayList<String> medicion = new ArrayList<>();
                medicion.add("MEDICION Y MEJORAMIENTO DE LA PRODUCTIVIDAD I");
                medicion.add("MEDICION Y MEJORAMIENTO DE LA PRODUCTIVIDAD II");

                ArrayList<String> sistemas = new ArrayList<>();
                sistemas.add("SISTEMAS INTEGRADOS DE MANUFACTURA I");
                sistemas.add("SISTEMAS INTEGRADOS DE MANUFACTURA II");

                lowporcent = new ArrayList<>();
                lowporcent.add("DIBUJO INDUSTRIAL");
                lowporcent.add("TALLER DE HERRAMIENTAS INTELECTUALES");
                lowporcent.add("QUIMICA");
                lowporcent.add("TALLER DE ETICA");
                lowporcent.add("FUNDAMENTOS DE INVESTIGACION");
                lowporcent.add("ECONOMIA");
                lowporcent.add("METODOLOGIA Y NORMAILIZACION");
                lowporcent.add("TALLER DE LIDERAZGO");
                lowporcent.add("ANALISIS DE LA REALIDAD NACIONAL");
                lowporcent.add("ALGORITMOS Y LENGUAJES DE PROGRAMACION");
                lowporcent.add("HIGIENE Y SEGURIDAD INDUSTRIAL");
                lowporcent.add("ELECTRICIDAD Y ELECTRONICA INDUSTRIAL");
                lowporcent.add("GESTION DE COSTOS");
                lowporcent.add("MERCADOTECNIA");
                lowporcent.add("ADMINISTRACION DE PROYECTOS");

                highporcent = new ArrayList<>();
                highporcent.add("DESARROLLO SUSTENTABLE");
                highporcent.add("INGENIERIA ECONOMICA");
                highporcent.add("SISTEMAS DE MANUFACTURA");
                highporcent.add("PROGRAMACION Y EVALUACION DE PROYECTOS");
                highporcent.add("PLANEACION ESTRATEGICA");
                highporcent.add("INGENIERIA DE SISTEMAS");
                highporcent.add("LOGISTICA Y CADENAS DE SUMINISTRO");
                highporcent.add("ADMINISTRACION DEL MANTENIMIENTO");
                highporcent.add("RELACIONES INDUSTRIALES");
                highporcent.add("PLANEACION FINANCIERA");
                highporcent.add("TRANSPORTE Y TRAFICO");
                highporcent.add("ALMACENES");
                highporcent.add("SERVICIO AL CLIENTE");
                highporcent.add("SISTEMAS INTEGRADDOS DE MANUFACTURA");

                ArrayList<String> inventarios = new ArrayList<>();
                inventarios.add("INVENTARIOS");
                inventarios.add("COMPRAS");

                ArrayList<String> envase = new ArrayList<>();
                envase.add("ENVASE, EMPAQUE Y EMBALAJE");

                ArrayList<String> simulacion = new ArrayList<>();
                simulacion.add("SIMULACION");

                ArrayList<String> sistcali = new ArrayList<>();
                sistcali.add("GESTION DE LOS SITEMAS DE CALIDAD");

                secciones = new ArrayList<>();
                boolean algelineal = false,
                 estainfe = false;

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("ALGEBRA LINEAL")) {
                        algelineal = true;
                    }

                    for (int j = 0; j < calculo.size(); j++) {
                        if (materia.equals(calculo.get(j))) {
                            calculo.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < probabilidad.size(); j++) {
                        if (materia.equals(probabilidad.get(j))) {
                            probabilidad.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < propiedades.size(); j++) {
                        if (materia.equals(propiedades.get(j))) {
                            propiedades.remove(j);
                            break;
                        }
                    }

                    for (int j = 0; j < estudio.size(); j++) {
                        if (materia.equals(estudio.get(j))) {
                            estudio.remove(j);
                            break;
                        }
                    }
                }

                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("ESTADISTICA INFERENCIAL I")) {
                        estainfe = true;
                    }
                }
                boolean inve2 = false;
                if (algelineal) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        for (int j = 0; j < operaciones.size(); j++) {
                            if (materia.equals(operaciones.get(j))) {
                                operaciones.remove(j);
                                break;
                            }
                        }

                    }
                    secciones.add(operaciones);

                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        if (materia.equals("INVESTIGACION DE OPERACIONES II")) {
                            inve2 = true;
                        }
                    }
                }

                boolean ingecali1 = false;
                if (estainfe) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();

                        for (int j = 0; j < estadisticainfe.size(); j++) {
                            if (materia.equals(estadisticainfe.get(j))) {
                                estadisticainfe.remove(j);
                                break;
                            }
                        }

                    }

                    secciones.add(estadisticainfe);

                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();
                        if (materia.equals("TOPICOS DE INGENIERIA DE LA CALIDAD I")) {
                            ingecali1 = true;
                        }
                    }
                }

                if (inve2) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();

                        for (int j = 0; j < simulacion.size(); j++) {
                            if (materia.equals(simulacion.get(j))) {
                                simulacion.remove(j);
                                break;
                            }
                        }

                    }
                    secciones.add(simulacion);
                }

                if (ingecali1) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();

                        for (int j = 0; j < sistcali.size(); j++) {
                            if (materia.equals(sistcali.get(j))) {
                                sistcali.remove(j);
                                break;
                            }
                        }

                    }

                    // secciones.add (sistcali);
                }
                boolean admin = false;
                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    if (materia.equals("ADMINISTRACION DE OPERACIONES I")) {
                        admin = true;
                    }
                }

                if (porcentaje >= 55) {
                    for (int i = 0; i < materias.size(); i++) {
                        materia = materias.get(i).toString();

                        for (int j = medicion.size() - 1; j >= 0; j--) {
                            if (materia.equals(medicion.get(j))) {
                                medicion.remove(j);
                                break;
                            }
                        }

                        for (int j = sistemas.size() - 1; j >= 0; j--) {
                            if (materia.equals(sistemas.get(j))) {
                                sistemas.remove(j);
                                break;
                            }
                        }

                        for (int j = 0; j < sistcali.size(); j++) {
                            if (materia.equals(sistcali.get(j))) {
                                sistcali.remove(j);
                                break;
                            }
                        }

                        for (int j = highporcent.size() - 1; j >= 0; j--) {
                            if (materia.equals(highporcent.get(j))) {
                                highporcent.remove(j);
                            }
                        }

                    }

                    if (admin) {
                        for (int i = 0; i < materias.size(); i++) {
                            materia = materias.get(i).toString();

                            for (int j = 0; j < inventarios.size(); j++) {
                                if (materia.equals(inventarios.get(j))) {
                                    inventarios.remove(j);
                                    break;
                                }
                            }

                        }

                        secciones.add(inventarios);
                    }
                }

                boolean higiene = false;
                for (int i = 0; i < materias.size(); i++) {
                    materia = materias.get(i).toString();
                    for (int j = lowporcent.size() - 1; j >= 0; j--) {
                        if (lowporcent.get(j).equals(materia)) {
                            lowporcent.remove(j);
                        }
                        if (materia.equals("HIGIENE Y SEGURIDAD INDUSTRIAL")) {
                            higiene = true;
                        }
                    }

                }

                secciones.add(calculo);
                secciones.add(probabilidad);
                secciones.add(propiedades);
                secciones.add(taller);
                if (estudio.size() == 1) {
                    if (higiene) {
                        secciones.add(estudio);
                    }
                } else {
                    secciones.add(estudio);
                }

                if (porcentaje >= 55) {
                    secciones.add(sistcali);
                    secciones.add(sistemas);
                    secciones.add(medicion);
                    //secciones.add (inventarios);
                    secciones.add(envase);
                    //secciones.add(diodos);

                    for (int i = 0; i < highporcent.size(); i++) {
                        rows.add(highporcent.get(i));

                    }
                }

                for (int i = 0; i < secciones.size(); i++) {
                    if (secciones.get(i).size() > 0) {
                        rows.add(secciones.get(i).get(0));
                    }
                }

                for (int i = 0; i < lowporcent.size(); i++) {
                    rows.add(lowporcent.get(i));
                }
                break;

            case "Administracion":
                ArrayList<String> singleWOPercent = new ArrayList<>();
                singleWOPercent.add("TEORÍA GENERAL DE LA ADMINISTRACIÓN");
                singleWOPercent.add("INFORMÁTICA PARA LA ADMINISTRACIÓN");
                singleWOPercent.add("TALLER DE ÉTICA");
                singleWOPercent.add("FUNDAMENTOS DE INVESTIGACIÓN");
                singleWOPercent.add("MATEMTICAS APLICADAS A LA ADMON");
                singleWOPercent.add("CONTABILIDAD GENERAL");
                singleWOPercent.add("FUNCIÓN ADMINISTRATIVA I");
                singleWOPercent.add("ESTADÍSTICA PARA LA ADMINISTRACIÓN I");
                singleWOPercent.add("DERECHO LABORAL Y SEGURIDAD SOCIAL");
                singleWOPercent.add("COMUNICACIÓN CORPORATIVA");
                singleWOPercent.add("TALLER DE DESARROLLO HUMANO");
                singleWOPercent.add("COSTOS DE MANUFACTURA");
                singleWOPercent.add("FUNCIÓN ADMINISTRATIVA II");
                singleWOPercent.add("ESTADÍSTICA PARA LA ADMINISTRACIÓN II");
                singleWOPercent.add("DERECHO EMPRESARIAL");
                singleWOPercent.add("COMPORTAMIENTO ORGANIZACIONAL");
                singleWOPercent.add("DINÁMICA SOCIAL");
                singleWOPercent.add("CONTABILIDAD GERENCIAL");
                singleWOPercent.add("GESTIÓN ESTRATÉGICA DEL CAPITAL HUMANO I");
                singleWOPercent.add("PROCESOS ESTRUCTURALES");
                singleWOPercent.add("MÉTODOS CUANTITATIVOS PARA ADMINISTRACIÓN");
                singleWOPercent.add("FUNDAMENTOS DE MERCADOTECNIA");
                singleWOPercent.add("ECONOMÍA EMPRESARIAL");
                singleWOPercent.add("MATEMÁTICAS FINANCIERAS");
                singleWOPercent.add("GESTIÓN ESTRATÉGICA DEL CAPITAL HUMANO II");
                singleWOPercent.add("DERECHO FISCAL");
                singleWOPercent.add("MEZCLA DE MERCADOTECNIA");
                singleWOPercent.add("MACROECONOMÍA");
                singleWOPercent.add("DESARROLLO SUSTENTABLE");
                singleWOPercent.add("ADMINISTRACIÓN FINANCIERA I");
                singleWOPercent.add("GESTIÓN DE LA RETRIBUCIÓN");
                singleWOPercent.add("INNOVACIÓN Y EMPRENDEDURISMO");
                singleWOPercent.add("TALLER DE INVESTIGACIÓN I");
                singleWOPercent.add("SISTEMAS DE INFROMACIÓN DE MERCADOTECNIA");
                singleWOPercent.add("ADMINISTRACIÓN FINANCIERA II");
                singleWOPercent.add("PRODUCCIÓN");
                singleWOPercent.add("PLAN DE NEGOCIOS");
                singleWOPercent.add("PROCESOS DE DIRECCIÓN");
                singleWOPercent.add("TALLER DE INVESTIGACIÓN II");
                singleWOPercent.add("ECONOMÍA INTERNACIONAL");
                singleWOPercent.add("ADMINISTRACIÓN DE LA CALIDAD");
                singleWOPercent.add("DIAGNÓSTICO Y EVALUACIÓN EMPRESARIAL");
                singleWOPercent.add("CONSULTORÍA EMPRESARIAL");
                singleWOPercent.add("FORMULACIÓN Y EVALUACIÓN DE PROYECTOS");
                singleWOPercent.add("DESARROLLO ORGANIZACIONAL");
                singleWOPercent.add("ADMINISTRACIÓN DE VENTAS");
                singleWOPercent.add("SEMINARIO DE RECURSOS HUMANOS");
                singleWOPercent.add("ADMINISTRACIÓN FINANCIERA III");
                singleWOPercent.add("SEMINARIO DE NEGOCIOS");
                singleWOPercent.add("SEMINARIO DE FORMACION ROFESIONAL");

                //ArrayList<ArrayList<String>> secciones = new ArrayList<>();
                // Materias sin restriccion directa ni de porcentaje de creditos
                for (int i = 0; i < singleWOPercent.size(); i++) {
                    rows.add(singleWOPercent.get(i));
                }
                break;

            case "Gestion":
                S1.add("FUNDAMENTOS DE INVESTIGACION");
                S1.add("TALLER DE INVESTIGACION I");
                S1.add("TALLER DE INVESTIGACION II");

                S2.add("CALCULO DIFERENCIAL");
                S2.add("CALCULO INTEGRAL");
                S2.add("PROBABILIDAD Y ESTADISTICA DESCRIPTIVA");
                S2.add("ESTADISTICA INFERENCIAL I");
                S2.add("ESTADISTICA INFERENCIAL II");

                S3.add("CONTABILIDAD ORIENTADA A LOS NEGOCIOS");
                S3.add("COSTOS EMPRESARIALES");
                S3.add("INSTRUMENTOS DE PRESUPUESTACION EMPRESARIAL");

                S4.add("HABILIDADES DIRECTIVAS I");
                S4.add("HABILIDADES DIRECTIVAS II");
                S4.add("GESTION DEL CAPITAL HUMANO");

                S5.add("ECONOMIA EMPRESARIAL");
                S5.add("ENTORNO MACROECONOMICO");

                S6.add("MERCADOTECNIA");
                S6.add("SISTEMAS DE INFORMACION DE LA MERCADOTECNIA");
                S6.add("MERCADOTECNIA ELECTRONICA");

                S7.add("GESTION DE LA PRODUCCION I");
                S7.add("GESTION DE LA PRODUCCION II");

                //60%
                S8.add("TOPICOS DE LA GESTION MODERNA");
                S8.add("PROYECTOS SUSTENTABLS Y SOSTENIBLES");

                SWOP.add("DESARROLLO HUMANO");
                SWOP.add("FUNDAMENTOS DE GESTION EMPRESARIAL");
                SWOP.add("FUNDAMENTOS DE FISICA");
                SWOP.add("FUNDAMENTOS DE QUIMICA");
                SWOP.add("SOFTWARE DE APLICACIÓN EJECUTIVO");
                SWOP.add("DINÁMICA SOCIAL");
                SWOP.add("TALLER DE ETICA");
                SWOP.add("LEGISLACION LABORAL");
                SWOP.add("MARCO LEGAL DE LAS ORGANIZACIONES");
                SWOP.add("ALGEBRA LINEAL");
                SWOP.add("INGENIERÍA ECONOMICA");
                SWOP.add("INVESTIGACION DE OPERACIONES");
                SWOP.add("FINANZAS EN LAS ORGANIZACIONES");
                SWOP.add("INGENIERIA DE PROCESOS");
                SWOP.add("ADMINISTRACION DE LA SALUD Y SEGURIDAD OCUPACIONAL");
                SWOP.add("EL EMPREDEDOR Y LA INNOVACION");
                SWOP.add("DISE?O ORGANIZACIONAL");
                SWOP.add("CALIDAD APLICADA A LA GESTION EMPRESARIAL");
                SWOP.add("PLAN DE NEGOCIOS");
                SWOP.add("GESTION ESTRATEGICA");
                SWOP.add("DESARROLLO SUSTENTABLE");

                SP.add("INGENIERIA FINANCIERA");
                SP.add("CADENA DE SUMINISTROS");
                SP.add("INGENIERIA DE SISTEMAS");
                SP.add("ADMINISTRACION DE SERVICIOS");
                SP.add("EVALUACION DE PROYECTOS DE INVERSION");

                restricciones = new ArrayList<>();
                restricciones.add(SWOP);

                restricciones.add(S1);
                restricciones.add(S2);
                restricciones.add(S3);
                restricciones.add(S4);
                restricciones.add(S5);
                restricciones.add(S6);
                restricciones.add(S7);

                restricciones.add(S8);

                restricciones.add(SP);

                // band[0] = false;   false si no ha pasado POO
                for (int i = materias.size() - 1; i >= 0; i--) {
                    materia = materias.get(i).toString();
                    OUTER:
                    for (int j = 0; j < restricciones.size(); j++) {
                        for (int k = 0; k < restricciones.get(j).size(); k++) {
                            if (restricciones.get(j).get(k).equals(materia)) {
                                restricciones.get(j).remove(k);
                                materias.remove(i);
                                break OUTER;
                            }
                        }
                    }
                }
                if (porcentaje < 60) {

                    restricciones.get(8).clear();
                    restricciones.get(9).clear();
                } else {
                    for (int i = 0; i < restricciones.get(restricciones.size() - 1).size(); i++) {
                        rows.add(restricciones.get(restricciones.size() - 1).get(i));
                    }
                }
                for (int i = 0; i < restricciones.get(0).size(); i++) {
                    rows.add(restricciones.get(0).get(i));
                }
                for (int j = 1; j < restricciones.size() - 1; j++) {
                    if (restricciones.get(j).size() > 0) {
                        rows.add(restricciones.get(j).get(0));
                    }
                }
                break;
            case "Mecanica":
                S1.add("CALCULO DIFERENCIAL");
                S1.add("CALCULO INTEGRAL");
                S1.add("CALCULO VECTORIAL");
                S1.add("ECUACIONES DIFERENCIALES");

                S2.add("QUIMICA");
                S2.add("INGENIERIA DE MATERIALES METALICOS");
                S2.add("INGENIERIA DE MATERIALES NO METALICOS");
                S2.add("PROCESOS DE MANUFACTURA");

                S3.add("FUNDAMENTOS DE INVESTIGACION");
                S3.add("TALLER DE INVESTIGACION I");
                S3.add("TALLER DE INVESTIGACION II");

                S4.add("ESTATICA");
                S4.add("MECANICA DE MATERIALES I");
                S4.add("MECANICA DE MATERIALES II");
                S4.add("DISE?O MECANICO I");
                S4.add("DISE?O MECANICO II");

                S5.add("PROCESO ADMINISTRATIVO");
                S5.add("CONTABILIDAD Y COSTOS");

                S6.add("ELECTROMAGNETISMO");
                S6.add("CIRCUITOS Y MAQUINAS ELECTRICAS");
                S6.add("INSTRUMENTACION Y CONTROL");
                S6.add("AUTOMATIZACION INDUSTRIAL");

                S7.add("MECANISMOS");
                S7.add("VIBRACIONES MECANICAS");
                S7.add("MANTENIMIENTO");

                S8.add("TERMODINAMICA");
                S8.add("TRANSFERENCIA DE CALOR");
                S8.add("MAQUINAS DE FLUIDOS COMPRESIBLES");
                S8.add("REFRIGERACION Y AIRE ACONDICIONADO");

                S9.add("MECANICA DE FLUIDOS");
                S9.add("SISTEMAS E INSTALACIONES HIDRAULICAS");
                S9.add("MAQUINAS DE FLUIDOS INCOMPRESIBLES");

                S10.add("DINAMICA");

                S11.add("SISTEMAS DE GENERACION DE ENERGIA");

                S12.add("GESTION DE PROYECTOS");

                SP.add("DINAMICA DE MAQUINAS (OPTATIVA)");
                SP.add("ROBOTICA (OPTATIVA)");
                SP.add("TOPICOS DE DISE?O");
                SP.add("MANUFACTURA AVANZADA");
                SP.add("INSTALACIONES MECANICAS");
                SP.add("AHORRO DE ENERGIA (OPTATIVA)");
                SP.add("INTERCAMBIADORES DE CALOR");
                SP.add("DISE?O DE EQUIPOS TERMICOS");

                SWOP.add("DIBUJO MECANICO");
                SWOP.add("METROLOGIA Y NORMALIZACION");
                SWOP.add("TALLER DE ETICA");
                SWOP.add("PROBABILIDAD Y ESTADISTICA");
                SWOP.add("ALGEBRA LINEAL");
                SWOP.add("ALGORITMOS Y PROGRAMACION");
                SWOP.add("CALIDAD");
                SWOP.add("SISTEMAS ELECTRONICOS");
                SWOP.add("METODOS NUMERICOS");
                SWOP.add("DESARROLLO SUSTENTABLE");
                SWOP.add("HIGIENE Y SEGURIDAD INDUSTRIAL");

                restricciones.add(SWOP);

                restricciones.add(S1);
                restricciones.add(S2);
                restricciones.add(S3);
                restricciones.add(S4);
                restricciones.add(S5);
                restricciones.add(S6);
                restricciones.add(S7);
                restricciones.add(S8);
                restricciones.add(S9);
                restricciones.add(S10);
                restricciones.add(S11);
                restricciones.add(S12);

                restricciones.add(SP);

                for (int i = materias.size() - 1; i >= 0; i--) {
                    materia = materias.get(i).toString();
                    OUTER:
                    for (int j = 0; j < restricciones.size(); j++) {
                        for (int k = 0; k < restricciones.get(j).size(); k++) {
                            if (restricciones.get(j).get(k).equals(materia)) {
                                switch (materia) {
                                    case "ESTATICA":
                                        band[0] = true;
                                        break;
                                    case "TERMODINAMICA":
                                        band[1] = true;
                                        break;
                                    case "TRANSFERENCIA DE CALOR":
                                        band[2] = true;
                                        break;
                                    case "SISTEMAS DE GENERACION DE ENERGIA":
                                        band[3] = true;
                                        break;
                                }
                                restricciones.get(j).remove(k);
                                materias.remove(i);
                                break OUTER;
                            }
                        }
                    }
                }

                if (porcentaje < 50) {
                    restricciones.get(12).clear();

                }

                if (porcentaje < 60) {
                    restricciones.get(13).clear();
                } else {
                    for (int i = 0; i < restricciones.get(restricciones.size() - 1).size(); i++) {
                        rows.add(restricciones.get(restricciones.size() - 1).get(i));
                    }
                }

                if (S1.size() <= 2) {
                    if (!band[0]) {
                        restricciones.get(3).clear();
                    }
                }
                if (!band[0]) {
                    restricciones.get(10).clear();

                }

                if (!band[1]) {
                    restricciones.get(9).clear();
                }

                if (!band[2]) {
                    restricciones.get(11).clear();
                }
                if (S8.size() == 1) {
                    if (!band[3]) {
                        restricciones.get(8).clear();
                    }
                }
                for (int i = 0; i < restricciones.get(0).size(); i++) {
                    rows.add(restricciones.get(0).get(i));
                }
                for (int j = 1; j < restricciones.size() - 1; j++) {
                    if (restricciones.get(j).size() > 0) {
                        rows.add(restricciones.get(j).get(0));
                    }
                }

                break;

            case "Mecatronica":

                S1.add("CALCULO DIFERENCIAL");
                S1.add("CALCULO INTEGRAL");
                S1.add("CALCULO VECTORIAL");
                S1.add("ECUACIONES DIFERENCIALES");
                S1.add("DINAMICA DE SISTEMAS");
                S1.add("CONTROL");
                S1.add("CONTROL DIGITAL");

                S2.add("QUIMICA");
                S2.add("CIENCIA E INGENIERIA DE MATERIALES");
                S2.add("PROCESOS DE FABRICACION");

                S3.add("FUNDAMENTOS DE INVESTIGACION");
                S3.add("TALLER DE INVESTIGACION I");
                S3.add("TALLER DE INVESTIGACION II");

                S4.add("ESTATICA");
                S4.add("DINAMICA");
                S4.add("MECANISMOS");
                S4.add("VIBRACIONES MECANICAS");

                S5.add("ELECTRO MAGNETISMO");
                S5.add("ANALISIS DE CIRCUITOS ELECTRICOS");
                S5.add("MAQUINAS ELECTRICAS");

                S6.add("FUNDAMENTOS DE TERMODINAMICA");
                S6.add("ANALISIS DE FLUIDOS");

                S7.add("MECANICA DE MATERIALES");
                S7.add("DISE?O DE ELEMENTOS MECANICOS");

                //40%
                S8.add("ELECTRONICA ANALOGICA");
                S8.add("ELECTRONICA DE POTENCIA APLICADA");

                S9.add("ELECTRONICA DIGITAL");
                S9.add("MICROCONTROLADORES");

                S10.add("CIRCUITOS HIDRAULICOS Y NEUMATICOS");
                S10.add("CONTROLADORES LOGICOS PROGRAMABLES");

                //60%
                S11.add("INSTRUMENTACION");
                S11.add("MANTENIMIENTO");
                S11.add("FORM. Y EVALUACION DE PROYECTOS");
                S11.add("PROGRAMACION AVANZADA");
                S11.add("MANUFACTURA AVANZADA");

//              Automatizacion y control  
//                SP.add("REDES INDUSTRIALES");
                SP.add("CONTROL DE PROCESOS");
                SP.add("SENSORES INTELIGENTES");
                SP.add("PLANIFICACION DE MOVIMIENTOS DE ROBOTS (OPTATIVA)");
                SP.add("TOPICOS DE MANUFACTURA Y ROBOTICA");
                SP.add("ROBOTICA");
                SP.add("ANALISIS DE SISTEMAS MECATRONICOS (OPTATIVA)");
                SP.add("TEMAS SELECTOS DE REDES DE COMUNICACION Y ALMACENAMIENTO DE DATOS");

//              Manufactura y robotica            
//                SP.add("REDES INDUSTRIALES");
//                SP.add("(OPTATIVA) TOPICOS DE ROBOTICA");
                SWOP.add("TALLER DE ETICA");
                SWOP.add("DIBUJO ASISTIDO POR COMPUTADORA");
                SWOP.add("METROLOGIA Y NORMALIZACION");
                SWOP.add("ALGEBRA LINEAL");
                SWOP.add("PROGRAMACION BASICA");
                SWOP.add("ESTADISTICA Y CONTROL DE CALIDAD");
                SWOP.add("ADMINISTRACION Y CONTABILIDAD");
                SWOP.add("METODOS NUMERICOS");
                SWOP.add("DESARROLLO SUSTENTABLE");

                restricciones.add(SWOP);

                restricciones.add(S1);
                restricciones.add(S2);
                restricciones.add(S3);
                restricciones.add(S4);
                restricciones.add(S5);
                restricciones.add(S6);
                restricciones.add(S7);
                restricciones.add(S8);
                restricciones.add(S9);
                restricciones.add(S10);
                restricciones.add(S11);

                restricciones.add(SP);

                for (int i = materias.size() - 1; i >= 0; i--) {
                    materia = materias.get(i).toString();
                    OUTER:
                    for (int j = 0; j < restricciones.size(); j++) {
                        for (int k = 0; k < restricciones.get(j).size(); k++) {
                            if (restricciones.get(j).get(k).equals(materia)) {
                                switch (materia) {
                                    case "CALCULO VECTORIAL":
                                        band[0] = true;
                                        break;
                                    case "FUNDAMENTOS DE TERMODINAMICA":
                                        band[1] = true;
                                        break;
                                    case "ELECTRONICA ANALOGICA":
                                        band[2] = true;
                                        break;
//                                    case "DINAMICA DE SISTEMAS":
//                                        band[3] = true;
//                                        break;
                                }
                                restricciones.get(j).remove(k);
                                materias.remove(i);
                                break OUTER;
                            }
                        }
                    }
                }

                if (porcentaje < 40) {
                    restricciones.get(8).clear();
                }

                if (!band[0]) {
                    restricciones.get(4).clear();
                }

                if (S4.size() <= 3) {
                    if (!band[1]) {
                        restricciones.get(4).clear();
                    }
                }

                if (!band[2]) {
                    restricciones.get(9).clear();
                }

                if (S1.size() < 4) {
                    if (porcentaje < 50) {
                        restricciones.get(1).clear();
                    } else if (porcentaje < 60 && S1.size() < 2) {
                        restricciones.get(1).clear();
                    }
                }

                if (porcentaje < 60) {
                    restricciones.get(12).clear();
                } else {
                    for (int i = 0; i < restricciones.get(restricciones.size() - 1).size(); i++) {
                        rows.add(restricciones.get(restricciones.size() - 1).get(i));
                    }
                }

                for (int i = 0; i < restricciones.get(0).size(); i++) {
                    rows.add(restricciones.get(0).get(i));
                }
                for (int j = 1; j < restricciones.size() - 1; j++) {
                    if (restricciones.get(j).size() > 0) {
                        rows.add(restricciones.get(j).get(0));
                    }
                }
                break;

            case "Quimica":
                S1.add("CALCULO DIFERENCIAL");
                S1.add("CALCULO INTEGRAL");
                S1.add("CALCULO VECTORIAL");
                S1.add("ECUACIONES DIFERENCIALES");

                S2.add("QUIMICA INORGANICA");
                S2.add("QUIMICA ANALITICA");
                S2.add("ANALISIS INSTRUMENTAL");

                S3.add("QUIMICA ORGANICA I");
                S3.add("QUIMICA ORGANICA II");

                S4.add("TERMODINAMICA");
                S4.add("FISICOQUIMICA I");
                S4.add("FISICOQUIMICA II");
                S4.add("REACTORES QUIMICOS");
                S4.add("LABORATORIO INTEGRAL II");

                S5.add("BALANCE DE MATERIA Y ENERGIA");
                S5.add("MECANISMOS DE TRANSFERENCIA");
                S5.add("BALANCE DE MOMENTUM, CALOR Y MASA");
                S5.add("PROCESOS DE SEPARACION II");

                S6.add("PROCESOS DE SEPARACION I");
                S6.add("LABORATORIO INTEGRAL I");

                S7.add("TALLER DE INVESTIGACION I");

                S8.add("ELECTRICIDAD, MAGNETISMO Y OPTICA");

                S9.add("PROCESOS DE SEPARACION III");

                S10.add("LABORATORIO INTEGRAL III");
                S10.add("SINTESIS Y OPTIMIZACION DE PROCESOS");

                S11.add("TALLER DE INVESTIGACION II");
                S11.add("INGENIERIA DE PROYECTOS");

                SP.add("LABORATORIO DE QUIMICA AMBIENTAL");
                SP.add("MICROBIOLOGIA AMBIENTAL");
                SP.add("GESTION DE RESIDUOS SOLIDOS Y PELIGROSOS");
                SP.add("CONTROL DE LA CONTAMINACION ATMOSFERICA");
                SP.add("TRATAMIENTO DE AGUAS");
                SP.add("EVALUACION Y MANIFESTACION DEL IMPACTO AMBIENTAL");

//              Metalurgia
                SP.add("INTRODUCCION  A LA METALURGIA");
                SP.add("CONCENTRACION DE MINERALES");
                SP.add("HIDROMETALURGIA");
                SP.add("PIROMETALURGIA");
                SP.add("ELECTROMETALURGIA");

                SWOP.add("TALLER DE ETICA");
                SWOP.add("FUNDAMENTOS DE INVESTIGACION");
                SWOP.add("PROGRAMACION");
                SWOP.add("DIBUJO ASISTIDO POR COMPUTADORA");
                SWOP.add("ALGEBRA LINEAL");
                SWOP.add("MECANICA CLASICA");
                SWOP.add("SALUD Y SEGURIDAD EN EL TRABAJO");
                SWOP.add("ANALISIS DE DATOS EXPERIMENTALES");
                SWOP.add("GESTION DE LA CALIDAD");
                SWOP.add("DESARROLLO SUSTENTABLE");
                SWOP.add("INGENIERIA AMBIENTAL");
                SWOP.add("METODOS NUMERICOS");
                SWOP.add("INGENIERIA DE COSTOS");
                SWOP.add("TALLER DE ADMINISTRACION GERENCIAL");
                SWOP.add("INSTRUMENTACION Y CONTROL");
                SWOP.add("SIMULACION DE PROCESOS");

                restricciones.add(SWOP);

                restricciones.add(S1);
                restricciones.add(S2);
                restricciones.add(S3);
                restricciones.add(S4);
                restricciones.add(S5);
                restricciones.add(S6);
                restricciones.add(S7);
                restricciones.add(S8);
                restricciones.add(S9);
                restricciones.add(S10);
                restricciones.add(S11);

                restricciones.add(SP);

                for (int i = materias.size() - 1; i >= 0; i--) {
                    materia = materias.get(i).toString();
                    OUTER:
                    for (int j = 0; j < restricciones.size(); j++) {
                        for (int k = 0; k < restricciones.get(j).size(); k++) {
                            if (restricciones.get(j).get(k).equals(materia)) {
                                switch (materia) {
                                    case "CALCULO INTEGRAL":
                                        band[0] = true;
                                        break;
                                    case "CALCULO VECTORIAL":
                                        band[1] = true;
                                        break;
                                    case "TERMODINAMICA":
                                        band[2] = true;
                                        break;
                                    case "MECANISMOS DE TRANSFERENCIA":
                                        band[3] = true;
                                        break;
                                    case "BALANCE DE MOMENTUM, CALOR Y MASA":
                                        band[4] = true;
                                        break;
                                    case "PROCESOS DE SEPARACION III":
                                        band[5] = true;
                                        break;
                                    case "TALLER DE INVESTIGACION I":
                                        band[6] = true;
                                        break;
                                }
                                restricciones.get(j).remove(k);
                                materias.remove(i);
                                break OUTER;
                            }
                        }
                    }
                }

                if (!band[0]) {
                    restricciones.get(4).clear();
                }

                if (!band[1]) {
                    restricciones.get(8).clear();
                }

                if (!band[2]) {
                    restricciones.get(5).clear();
                }
                if (!band[3]) {
                    restricciones.get(6).clear();
                }
                if (!band[4]) {
                    restricciones.get(9).clear();
                }
                if (!band[5]) {
                    restricciones.get(10).clear();
                }
                if (!band[6]) {
                    restricciones.get(11).clear();
                }

                if (porcentaje < 60) {
                    restricciones.get(12).clear();
                }
                for (int i = 0; i < restricciones.get(0).size(); i++) {
                    rows.add(restricciones.get(0).get(i));
                }
                for (int i = 0; i < restricciones.get(10).size(); i++) {
                    rows.add(restricciones.get(10).get(i));
                }
                for (int i = 0; i < restricciones.get(11).size(); i++) {
                    rows.add(restricciones.get(11).get(i));
                }
                for (int j = 1; j < restricciones.size() - 3; j++) {
                    if (restricciones.get(j).size() > 0) {
                        rows.add(restricciones.get(j).get(0));
                    }
                }
                for (int i = 0; i < restricciones.get(restricciones.size() - 1).size(); i++) {
                    rows.add(restricciones.get(restricciones.size() - 1).get(i));
                }
                break;
        }
        sort();

        return rows;
    }

    public void sort() {
        for (String row : rows) {
            for (int j = 0; j < rows.size() - 1; j++) {
                if ((rows.get(j).compareTo(rows.get(j + 1)) > 0)) {
                    String aux = rows.get(j);
                    rows.set(j, rows.get(j + 1));
                    rows.set(j + 1, aux);
                }
            }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        CrearHorarios = new javax.swing.JButton();
        SiguienteHorario = new javax.swing.JButton();
        AnnteriorHorario = new javax.swing.JButton();
        jNumHorario = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jRadioButPrimera = new javax.swing.JRadioButton();
        jRadioButHoras = new javax.swing.JRadioButton();
        jLabCreditos = new javax.swing.JLabel();
        jLabError = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
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
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Materia", "Costo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable1MouseDragged(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(1000);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 350, 280));

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

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 980, 640));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("Posibles Horarios: ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, -1, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Profesor", "Hora"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable2MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(3);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 350, 140));

        CrearHorarios.setBackground(new java.awt.Color(51, 255, 0));
        CrearHorarios.setText("Crear Horarios");
        CrearHorarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearHorariosActionPerformed(evt);
            }
        });
        getContentPane().add(CrearHorarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 160, 30));

        SiguienteHorario.setText("Siguiente");
        SiguienteHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SiguienteHorarioActionPerformed(evt);
            }
        });
        getContentPane().add(SiguienteHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 30, -1, -1));

        AnnteriorHorario.setText("Anterior");
        AnnteriorHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnnteriorHorarioActionPerformed(evt);
            }
        });
        getContentPane().add(AnnteriorHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, -1, -1));

        jNumHorario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jNumHorario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jNumHorario.setText("Horario #1");
        jNumHorario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(jNumHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 160, 30));

        jButton1.setText("Guardar Horario");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 30, 140, -1));

        buttonGroup1.add(jRadioButPrimera);
        jRadioButPrimera.setText("Ord. por primera hora");
        jRadioButPrimera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButPrimeraActionPerformed(evt);
            }
        });
        getContentPane().add(jRadioButPrimera, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 0, -1, -1));

        buttonGroup1.add(jRadioButHoras);
        jRadioButHoras.setSelected(true);
        jRadioButHoras.setText("Ord. por horas escolares");
        jRadioButHoras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButHorasActionPerformed(evt);
            }
        });
        getContentPane().add(jRadioButHoras, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, -1, -1));

        jLabCreditos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabCreditos.setText("Creditos acumulados: ");
        getContentPane().add(jLabCreditos, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 180, 30));

        jLabError.setBackground(new java.awt.Color(215, 9, 9));
        jLabError.setForeground(new java.awt.Color(255, 255, 255));
        jLabError.setText(" LIMITE DE CREDITOS EXCEDIDO ");
        jLabError.setOpaque(true);
        getContentPane().add(jLabError, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Marque las casillas de las materias de las que quiera hacer horarios");
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Desmarque los maestros que no quiera en sus horarios");
        jLabel20.setOpaque(true);
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 0, 80, 175));

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

        jLabel36.setText("Numero de horas: ");
        getContentPane().add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 30, 140, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 0, 180, 175));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 170, 70, 175));

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
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 350, 70, 175));

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
        getContentPane().add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 520, 70, 175));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 525, 180, 175));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 525, 180, 175));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        jLabel31.setText("Daniel Gonzalez Cabrera");
        getContentPane().add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 525, 180, 175));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 525, 180, 175));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 525, 180, 175));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 525, 180, 175));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/engranes.jpg"))); // NOI18N
        getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 525, 180, 175));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
    }//GEN-LAST:event_jTable2MouseClicked

    private void CrearHorariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearHorariosActionPerformed
        try {
            t = new Thread(this);
            t.start();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_CrearHorariosActionPerformed

    private void AnnteriorHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnnteriorHorarioActionPerformed
        // TODO add your handling code here:
        try {
            if (numHorario > 0) {
                horariosPintados = new ColorCeldas(jTable3);
                numHorario--;
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
                sc.get(numHorario).Mandar(jTable3, horariosPintados);
                jNumHorario.setText("Horario #" + (numHorario + 1));
            }
            jLabel36.setText("Numero de horas: " + (sc.get(numHorario).horaSalida - sc.get(numHorario).horaEntrada));

        } catch (Exception e) {

        }

    }//GEN-LAST:event_AnnteriorHorarioActionPerformed

    private void SiguienteHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SiguienteHorarioActionPerformed
        try {
            if (numHorario < sc.size() - 1) {
                horariosPintados = new ColorCeldas(jTable3);
                numHorario++;
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
                sc.get(numHorario).Mandar(jTable3, horariosPintados);
                jNumHorario.setText("Horario #" + (numHorario + 1));
            }
            jLabel36.setText("Numero de horas: " + (sc.get(numHorario).horaSalida - sc.get(numHorario).horaEntrada));

        } catch (Exception e) {

        }
    }//GEN-LAST:event_SiguienteHorarioActionPerformed

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
            if (sc.size() > 0) {
                String nombreA = "";
                while ("".equals(nombreA)) {
                    nombreA = JOptionPane.showInputDialog("Elija un nombre para este horario.", nombreA);
                    if (nombreA != null) {
                        if (nombreA.length() > 0) {
                            Archivo horariosGuardados = new Archivo(c.nombre + "\\horariosGuardados.txt");
                            horariosGuardados.crearLectura();
                            String texto = "";
                            try {
                                String line;

                                do {
                                    line = horariosGuardados.LeerLinea();
                                    if (line != null) {
                                        texto += line + "\n";

                                    }
                                } while (line != null);

                            } catch (Exception ex) {
                            }
                            horariosGuardados.crearEscritura();
                            texto += nombreA + "->";
                            for (int i = 0; i < sc.get(numHorario).getSchedule().size(); i++) {
                                if (i < sc.get(numHorario).getSchedule().size() - 1) {
                                    texto += sc.get(numHorario).getSchedule().get(i).codigoMat + "-";
                                } else {
                                    texto += sc.get(numHorario).getSchedule().get(i).codigoMat;
                                }

                            }
                            horariosGuardados.EscribirLinea(texto);
                            horariosGuardados.CerrarEscritura();

                        } else {
                            JOptionPane.showMessageDialog(this, "Asigne un nombre al horario que desea guardar.");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Asegúrese de que tenga al menos un horario creado.");
            }
        } catch (HeadlessException ex) {
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed

    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        // TODO add your handling code here:
        try {
            InsertarMaterias();
            int r = jTable1.getSelectedRow();
            jTable1.changeSelection(r, 0, false, false);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void jTable1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseDragged

    }//GEN-LAST:event_jTable1MouseDragged

    private void jTable2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseReleased
        // TODO add your handling code here:
        try {
            int r = jTable2.getSelectedRow();
            for (int j = 0; j < Materia.lista.size(); j++) {
                for (int i = 0; i < listaMaestros.size(); i++) {
                    if (listaMaestros.get(i).codigoMat.equals(Materia.lista.get(j).codigoMat)) {
                        Materia.lista.get(j).aprobado = (boolean) jTable2.getValueAt(i, 0);
                    }
                }

            }
            horario.clear();
            for (int i = 0; i < listaElegidos.size(); i++) {
                crearArreglosHorarios(listaElegidos.get(i), colores[i]);
            }
            jTable2.clearSelection();
            t = new Thread(this);
            t.start();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jTable2MouseReleased

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        try {
            c.setVisible(true);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_formWindowClosed

    public ArrayList<Horario> Sort(ArrayList<Horario> a, int opt) {
        ArrayList<Horario> nuevo = new ArrayList<>();
        nuevo.add(a.get(0));
        if (opt == 0) {
            for (int i = 1; i < a.size(); i++) {
                nuevo.add(a.get(i));
                for (int j = nuevo.size() - 1; j >= 1; j--) {
                    if (nuevo.get(j).horaEntrada < nuevo.get(j - 1).horaEntrada) {
                        Horario b = nuevo.get(j);
                        nuevo.set(j, nuevo.get(j - 1));
                        nuevo.set(j - 1, b);
                    } else {
                        break;
                    }
                }
            }
        } else {
            for (int i = 1; i < a.size(); i++) {
                nuevo.add(a.get(i));
                for (int j = nuevo.size() - 1; j >= 1; j--) {
                    if ((nuevo.get(j).horaSalida - nuevo.get(j).horaEntrada)
                            < (nuevo.get(j - 1).horaSalida - nuevo.get(j - 1).horaEntrada)) {
                        Horario b = nuevo.get(j);
                        nuevo.set(j, nuevo.get(j - 1));
                        nuevo.set(j - 1, b);
                    } else {
                        break;
                    }
                }
            }
        }

        return nuevo;
    }
    private void jRadioButPrimeraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButPrimeraActionPerformed
        // TODO add your handling code here:
        if (sc.size() > 0) {

            sc = Sort(sc, 0);
            numHorario = 0;

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
            horariosHabiles = 0;
            horariosPintados = new ColorCeldas(jTable3);

            if (sc.size() > 0) {
                sc.get(0).Mandar(jTable3, horariosPintados);
            }
            jLabel1.setText("Posibles Horarios " + sc.size());
            jTable1.setEnabled(true);
            this.jNumHorario.setText("Horario #" + (numHorario + 1));
        }
    }//GEN-LAST:event_jRadioButPrimeraActionPerformed

    private void jRadioButHorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButHorasActionPerformed
        // TODO add your handling code here:
//        OrdenarXHorasEscolares(sc, 0, sc.size() - 1, 0);
        if (sc.size() > 0) {
            sc = Sort(sc, 1);
            numHorario = 0;

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
            horariosHabiles = 0;
            horariosPintados = new ColorCeldas(jTable3);

            if (sc.size() > 0) {
                sc.get(0).Mandar(jTable3, horariosPintados);
            }
            jLabel1.setText("Posibles Horarios " + sc.size());
            jTable1.setEnabled(true);
            this.jNumHorario.setText("Horario #" + (numHorario + 1));
        }
    }//GEN-LAST:event_jRadioButHorasActionPerformed

    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped
        // TODO add your handling code here:


    }//GEN-LAST:event_jTable1KeyTyped

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            try {
                InsertarMaterias();
                int r = jTable1.getSelectedRow();
                System.out.println(r);
                jTable1.changeSelection(r, 0, false, false);

            } catch (Exception e) {

            }
        }
    }//GEN-LAST:event_jTable1KeyReleased
    public void crearArreglosHorarios(String materia, Color color) {
        try {
            horario.add(new Horario());

            for (int j = 0; j < Materia.lista.size(); j++) {
                if (materia.equals(Materia.lista.get(j).nombreMat.substring(0, Materia.lista.get(j).nombreMat.length())) && Materia.lista.get(j).aprobado) {
                    Materia.lista.get(j).color = color;
                    horario.get(horario.size() - 1).getSchedule().add(Materia.lista.get(j));
                }
            }
        } catch (Exception e) {

        }
    }

    public void CrearCombinaciones(int ind, int n, Horario h) {
        for (int i = 0; i < horario.get(ind).getSchedule().size(); i++) {
            if (ind == 0) {
                h = new Horario();
                h.getSchedule().add(horario.get(ind).getSchedule().get(i));
                CrearCombinaciones(ind + 1, n, h);
            } else if (ind < n - 1) {
                Horario aux = null;
                try {
                    aux = (Horario) h.clone();
                } catch (CloneNotSupportedException ex) {
                }
                if (aux != null && aux.insertar(horario.get(ind).getSchedule().get(i))) {
                    CrearCombinaciones(ind + 1, n, aux);
                }
            } else {
                Horario aux = null;
                try {
                    aux = (Horario) h.clone();
                } catch (CloneNotSupportedException ex) {
                }
                if (aux != null && aux.insertar(horario.get(ind).getSchedule().get(i))) {
                    sc.add(aux);
                }
            }
        }
    }

    public void OrdenarXHorasEscolares(ArrayList<Horario> a, int start, int end, int opt) {
        int i = start;
        int j = end;
        int n1, n2, n3;
        Horario pivot;
        if (j - i >= 1) {
            if (opt == 0) {
                pivot = a.get(i);
                n2 = (pivot.horaSalida - pivot.horaEntrada);
                while (j > i) {
                    while ((a.get(i).horaSalida - a.get(i).horaEntrada - n2) <= 0 && i < end && j > i) {
                        i++;
                    }
                    while ((a.get(j).horaSalida - a.get(j).horaEntrada - n2) >= 0 && j > start && j >= i) {
                        j--;
                    }
                    if (j > i) {
                        swap(a, i, j);
                    }
                }

                swap(a, start, j);

                OrdenarXHorasEscolares(a, start, j - 1, opt);
                OrdenarXHorasEscolares(a, j + 1, end, opt);
            }
        }
    }

    private static void swap(ArrayList<Horario> a, int i, int j) {
        Horario temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    public void crearHorarios() {
        try {
            numHorario = 0;

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
            sc = new ArrayList<>();

            try {
                CrearCombinaciones(0, listaElegidos.size(), new Horario());
                for (int i = 0; i < sc.size(); i++) {
                    sc.get(i).ordenarHoras();
                }

                OrdenarXHorasEscolares(sc, 0, sc.size() - 1, 0);
                horariosHabiles = 0;
                horariosPintados = new ColorCeldas(jTable3);

                if (sc.size() > 0) {
                    sc.get(0).Mandar(jTable3, horariosPintados);
                }
                jLabel1.setText("Posibles Horarios " + sc.size());

                if (sc.size() < 1) {
                    JOptionPane.showMessageDialog(this, "No se ha podido crear ningun horario con estas especificaciones");
                }

                load.setVisible(false);
            } catch (HeadlessException ex) {
                load.setVisible(false);
                JOptionPane.showMessageDialog(this, "Debe escoger de 2 a 8 materias para hacer horarios.");
            }
            jTable1.setEnabled(true);
            this.jNumHorario.setText("Horario #" + (numHorario + 1));
        } catch (HeadlessException e) {

        }
    }
    int contAzul = 0;
    int suma;

    public void InsertarMaterias() {
        suma = 0;
        colorCeldas = new ColorCeldas(jTable1);
        int r = jTable1.getSelectedRow();
        listaElegidos.clear();
        int index = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if ((boolean) jTable1.getValueAt(i, 0) == true) {
                listaElegidos.add(jTable1.getValueAt(i, 1).toString());
                suma += Integer.valueOf(jTable1.getValueAt(i, 2).toString());
            }
        }
        if (suma > 36) {
            suma -= Integer.valueOf(jTable1.getValueAt(r, 2).toString());
            for (int i = 0; i < listaElegidos.size(); i++) {
                if (jTable1.getValueAt(r, 1).toString().equals(listaElegidos.get(i))) {
                    listaElegidos.remove(i);
                    jTable1.setValueAt(false, r, 0);
                }
            }
            this.jLabError.setText(" LIMITE DE " + Alumno.creditos + " CREDITOS EXCEDIDO ");
            this.jLabError.setVisible(true);
        } else {
            this.jLabError.setVisible(false);
        }
        if ((boolean) jTable1.getValueAt(r, 0) != true) {
            colorCeldas.addCelda(r, 0, new Color(57, 105, 138));
            colorCeldas.addCelda(r, 1, new Color(57, 105, 138));
            colorCeldas.addCelda(r, 2, new Color(57, 105, 138));
        }

        for (int i = 0; i < listaElegidos.size(); i++) {
            for (int j = 0; j < jTable1.getRowCount(); j++) {
                if (listaElegidos.get(i).equals(jTable1.getValueAt(j, 1))) {
                    colorCeldas.addCelda(j, 0, colores[index]);
                    colorCeldas.addCelda(j, 1, colores[index]);
                    colorCeldas.addCelda(j, 2, colores[index++]);
                    break;
                }
            }
        }

        colorCeldas.repaint();
        jLabCreditos.setText("Creditos acumulados: " + suma);
        int cont = 0;
        boolean band = false;
        listaMaestros.clear();
        String materiaTabla, materiaLista;
        for (int i = 0; i < Materia.lista.size(); i++) {
            materiaTabla = jTable1.getValueAt(r, 1).toString();
            materiaLista = Materia.lista.get(i).nombreMat;
            if (materiaTabla.equals(materiaLista)) {
                cont++;
                modeloProfesores.setRowCount(cont);
                jTable2.setValueAt(Materia.lista.get(i).aprobado, cont - 1, 0);
                jTable2.setValueAt(Materia.lista.get(i).maestroMat, cont - 1, 1);
                int mayor = 0;
                int indice = 0;
                for (int j = 0; j < Materia.lista.get(i).horarioMat.length; j++) {
                    String[] finales = Materia.lista.get(i).horaFinales[j].split(":");
                    String[] inicios = Materia.lista.get(i).horaInicios[j].split(":");
                    int horaFinal = Integer.valueOf(finales[0]);
                    int horaInicio = Integer.valueOf(inicios[0]);
                    if ((horaFinal - horaInicio) > mayor) {
                        mayor = (horaFinal - horaInicio);
                        indice = j;
                    }
                }
                jTable2.setValueAt(Materia.lista.get(i).horarioMat[indice], cont - 1, 2);
                band = true;
                listaMaestros.add(Materia.lista.get(i));
            } else {
                if (band) {
                    break;
                }
            }
        }

        horario.clear();
        if (listaElegidos.size() > 1) {
            for (int i = 0; i < listaElegidos.size(); i++) {
                crearArreglosHorarios(listaElegidos.get(i), colores[i]);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MateriasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MateriasFrame(null, "", "").setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnnteriorHorario;
    private javax.swing.JButton CrearHorarios;
    private javax.swing.JButton SiguienteHorario;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabCreditos;
    private javax.swing.JLabel jLabError;
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
    private javax.swing.JLabel jNumHorario;
    private javax.swing.JRadioButton jRadioButHoras;
    private javax.swing.JRadioButton jRadioButPrimera;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
//        t.start();
        load.setVisible(true);
        crearHorarios();
    }
}

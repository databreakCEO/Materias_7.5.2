/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

import Utilerias.JTable.ColorCeldas;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Gonzalez Cabrera
 */
public class Horario implements Cloneable{

    protected ArrayList<Materia> schedule = new ArrayList<>();
    ArrayList<Materia> profesoresDesaprobados = new ArrayList<>();
    int horaEntrada, horaSalida;
    ArrayList<Integer> horasEscolares = new ArrayList<>();
    ArrayList<Integer> horasLibres = new ArrayList<>();
    JTable tabla;
    Color[] colores = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.pink};
//    ColorCeldas colorCeldas = new ColorCeldas(tabla);

     
    public ArrayList<Materia> getSchedule() {
        return schedule;
    }

    @Override
     public Object clone() throws CloneNotSupportedException{
        Horario obj=null;
        try{
            obj=(Horario)super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        obj.schedule=(ArrayList<Materia>)obj.schedule.clone();
        return obj;
    }
    
    public void setSchedule(ArrayList<Materia> schedule) {
        ArrayList<Materia> aux = new ArrayList<>();
        aux = schedule;
        this.schedule=aux;
    }

    public void Mandar(JTable tabla, ColorCeldas colorCeldas) {
        for (int j = 0; j < schedule.size(); j++) {
            for (int columnas = 0; columnas < 5; columnas++) {
                int horaInicioS = 0;
                int horaFinalS = 0;
                String[] finalesS = schedule.get(j).horaFinales[columnas].split(":");
                String[] iniciosS = schedule.get(j).horaInicios[columnas].split(":");

                horaInicioS = Integer.valueOf(iniciosS[0]);
                horaFinalS = Integer.valueOf(finalesS[0]);
                for (int renglones = 0; renglones < horaFinalS - horaInicioS; renglones++) {
                    colorCeldas.addCelda(horaInicioS - 7 + renglones, columnas + 1, schedule.get(j).color);
                    tabla.setValueAt("<html>" + "Clave: " + schedule.get(j).codigoMat + "      Salon: " + schedule.get(j).salon[columnas] + "<br>" + schedule.get(j).maestroMat + "</html>", horaInicioS - 7 + renglones, columnas + 1);
                    colorCeldas.repaint();
                }

            }
            if (schedule.get(j).materiaCombinada != null) {
                for (int columnas = 0; columnas < 5; columnas++) {
                    int horaInicioS = 0;
                    int horaFinalS = 0;
                    String[] finalesS = schedule.get(j).materiaCombinada.horaFinales[columnas].split(":");
                    String[] iniciosS = schedule.get(j).materiaCombinada.horaInicios[columnas].split(":");

                    horaInicioS = Integer.valueOf(iniciosS[0]);
                    horaFinalS = Integer.valueOf(finalesS[0]);
                    for (int renglones = 0; renglones < horaFinalS - horaInicioS; renglones++) {
                        colorCeldas.addCelda(horaInicioS - 7 + renglones, columnas + 1, schedule.get(j).color);
                        tabla.setValueAt("<html>" + "Clave: " + schedule.get(j).materiaCombinada.codigoMat + "      Salon: " + schedule.get(j).materiaCombinada.salon[columnas] + "<br>" + schedule.get(j).materiaCombinada.maestroMat + "</html>", horaInicioS - 7 + renglones, columnas + 1);
                        colorCeldas.repaint();
                    }

                }
            }
        }
    }

    public void Mandar(JTable tabla) {
        DefaultTableModel table = (DefaultTableModel) tabla.getModel();

        for (int i = 0; i < schedule.size(); i++) {
            String datos[] = new String[8];

            datos[0] = schedule.get(i).nombreMat;
            datos[1] = schedule.get(i).codigoMat;
            datos[2] = schedule.get(i).horarioMat[0];
            datos[3] = schedule.get(i).horarioMat[1];
            datos[4] = schedule.get(i).horarioMat[2];
            datos[5] = schedule.get(i).horarioMat[3];
            datos[6] = schedule.get(i).horarioMat[4];
            datos[7] = schedule.get(i).maestroMat;
            table.addRow(datos);

        }
        String datos[] = new String[8];
        table.addRow(datos);
    }

    public void Mostrar() {
        for (int i = 0; i < schedule.size(); i++) {
            schedule.get(i).Mostrar();

        }
    }

    public void capturarHorasLibre() {
        int hora = 0;
        for (int i = 0; i < this.schedule.size(); i++) {
            horasEscolares.add(schedule.get(i).horaInicio);
        }

        for (int i = 7; i < 20; i++) {
            if (horasEscolares.get(hora) != i) {
                horasLibres.add(i);

            } else {
                if (hora < horasEscolares.size() - 1) {
                    hora++;
                }

            }

        }

    }

    
    boolean band = true;

    public boolean insertar(Materia m) {
        band = true;
        int horaInicioS;
        int horaInicioM;
        int horafinalS;
        int horafinalM;

        int horaInicioS2;
        int horaInicioM2;
        int horafinalS2;
        int horafinalM2;
        String[] finalesS;
        String[] finalesM;

        String[] iniciosM;
        String[] iniciosS;
        if (schedule.size() > 0) {
            for (int i = 0; i < schedule.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    finalesS = schedule.get(i).horaFinales[j].split(":");
                    finalesM = m.horaFinales[j].split(":");

                    iniciosM = m.horaInicios[j].split(":");
                    iniciosS = schedule.get(i).horaInicios[j].split(":");

                    horaInicioS = Integer.valueOf(iniciosS[0]);
                    horaInicioM = Integer.valueOf(iniciosM[0]);
                    horafinalS = Integer.valueOf(finalesS[0]);
                    horafinalM = Integer.valueOf(finalesM[0]);
                    if (((horaInicioS <= horaInicioM && horafinalS >= horafinalM)
                            || (horaInicioM <= horaInicioS && horafinalM >= horafinalS))
                            && (horaInicioS != 0 || horaInicioM != 0)) {
                        band = false;
                    }
                    if (schedule.get(i).materiaCombinada != null) {
                        String[] finalesS2 = schedule.get(i).materiaCombinada.horaFinales[j].split(":");
                        String[] finalesM2 = m.horaFinales[j].split(":");

                        String[] iniciosM2 = m.horaInicios[j].split(":");
                        String[] iniciosS2 = schedule.get(i).materiaCombinada.horaInicios[j].split(":");

                        horaInicioS2 = Integer.valueOf(iniciosS2[0]);
                        horaInicioM2 = Integer.valueOf(iniciosM2[0]);
                        horafinalS2 = Integer.valueOf(finalesS2[0]);
                        horafinalM2 = Integer.valueOf(finalesM2[0]);
                        if (((horaInicioS2 <= horaInicioM2 && horafinalS2 >= horafinalM2)
                                || (horaInicioM2 <= horaInicioS2 && horafinalM2 >= horafinalS2))
                                && (horaInicioS2 != 0 || horaInicioM2 != 0)) {
                            band = false;
                        }
                    }

                    if (m.materiaCombinada != null) {
                        String[] finalesS2 = schedule.get(i).horaFinales[j].split(":");
                        String[] finalesM2 = m.materiaCombinada.horaFinales[j].split(":");

                        String[] iniciosM2 = m.materiaCombinada.horaInicios[j].split(":");
                        String[] iniciosS2 = schedule.get(i).horaInicios[j].split(":");

                        horaInicioS2 = Integer.valueOf(iniciosS2[0]);
                        horaInicioM2 = Integer.valueOf(iniciosM2[0]);
                        horafinalS2 = Integer.valueOf(finalesS2[0]);
                        horafinalM2 = Integer.valueOf(finalesM2[0]);
                        if (((horaInicioS2 <= horaInicioM2 && horafinalS2 >= horafinalM2)
                                || (horaInicioM2 <= horaInicioS2 && horafinalM2 >= horafinalS2))
                                && (horaInicioS2 != 0 || horaInicioM2 != 0)) {
                            band = false;
                        }
                    }
                }
            }
        }
        if (band == true) {
            schedule.add(m);
        }

        return band;
    }

    public static ArrayList<Materia> quickSort(ArrayList<Materia> a, int start, int end, int opcion) {
        int i = start;
        int j = end;
        if (opcion == 1) {
            if (j - i >= 1) {
                Materia pivot = a.get(i);
                while (j > i) {
                    while (a.get(i).codigoMat.compareTo(pivot.codigoMat) <= 0 && i < end && j > i) {
                        i++;
                    }
                    while (a.get(j).codigoMat.compareTo(pivot.codigoMat) >= 0 && j > start && j >= i) {
                        j--;
                    }
                    if (j > i) {
                        swap(a, i, j);
                    }
                }

                swap(a, start, j);

                quickSort(a, start, j - 1, opcion);
                quickSort(a, j + 1, end, opcion);
            }
        } else {
            if (j - i >= 1) {
                Materia pivot = a.get(i);
                while (j > i) {
//                if (schedule.get(j).horaInicio > schedule.get(j + 1).horaInicio && (schedule.get(j).horaInicio != 0 && schedule.get(j + 1).horaInicio != 0)) {

                    while ((a.get(i).horaInicio <= pivot.horaInicio) && (a.get(i).horaInicio != 0 && pivot.horaInicio != 0) && i < end && j > i) {
                        i++;
                    }
                    while ((a.get(j).horaInicio >= pivot.horaInicio) && (a.get(j).horaInicio != 0 && pivot.horaInicio != 0) && j > start && j >= i) {
                        j--;
                    }
                    if (j > i) {
                        swap(a, i, j);
                    }
                }

                swap(a, start, j);

                quickSort(a, start, j - 1, opcion);
                quickSort(a, j + 1, end, opcion);
            }
        }
        return a;
    }

    

    public static void swap(ArrayList<Materia> a, int i, int j) {
        try {
            Materia temp = a.get(i);
            a.set(i, a.get(j));
            a.set(j, temp);
        } catch (Exception e) {
        }
    }

    public void ordenarCodigos() {
        quickSort(schedule, 0, schedule.size() - 1, 1);

    }

    public void ordenarHoras() {
        quickSort(schedule, 0, schedule.size() - 1, 0);
        horaEntrada = this.schedule.get(0).horaInicio;
        horaSalida = this.schedule.get(schedule.size() - 1).horaTermino;
    }

    public boolean isEmpty() {
        return this.schedule.isEmpty();
    }

}

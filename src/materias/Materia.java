/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Daniel Gonzalez Cabrera
 */
public class Materia {

    String nombreMat;
    String codigoMat;
    String[] horarioMat;
    String maestroMat;
    String[] salon = {"", "", "", "", ""};
    int hora;
    int horaInicio = 0;
    int horaTermino = 0;
    String[] horaInicios = new String[5];
    String[] horaFinales = new String[5];
    boolean aprobado = true;
    Color color = Color.GRAY;
    Materia materiaCombinada;
    int costo, semestre;

    
    @Override
    public Object clone() throws CloneNotSupportedException{
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

    public void unirMateria(Materia m) {
        materiaCombinada = m;
    }

    public Materia(String nombreMat, String codigoMat, String[] horarioMat, String maestroMat, int semestre,int costo) {
        this.nombreMat = nombreMat;
        this.codigoMat = codigoMat;
        this.horarioMat = horarioMat;
        this.maestroMat = maestroMat;
        this.costo=costo;
        this.semestre=semestre;
//        System.out.println(nombreMat + " - "+codigoMat+" - "+horarioMat[0]+" "+horarioMat[1]+" "+horarioMat[2]+" "
//                                     +horarioMat[3]+" "+horarioMat[4]+" "+" - "+maestroMat);
        String hora = "";

        int index = 0;

        for (int n = 0; n < 5; n++) {
//            System.out.println(horarioMat[n]);
            for (int i = 0; i < horarioMat[n].length(); i++) {
                if (horarioMat[n].charAt(i) == "/".charAt(0)) {
//                    System.out.println(horarioMat[n]);
                    for (int j = i + 1; j < horarioMat[n].length(); j++) {
                        salon[index] += horarioMat[n].charAt(j);

                    }
//                    System.out.println(salon[index]);
//                    System.out.println(hora.length());
                    switch (hora.length()) {
                        case 1:
//                            System.out.println("1");
                            horaInicios[index] = 0 + "";
                            horaFinales[index] = 0 + "";
                            break;
                        case 11:
//                            System.out.println("2");
                            try {
                                if (Integer.valueOf(hora.charAt(0) + "" + hora.charAt(1)) == 0 && Integer.valueOf(hora.charAt(3) + "" + hora.charAt(4)) > 0) {
                                    horaInicios[index] = hora.charAt(3) + "" + hora.charAt(4) + "" + hora.charAt(2) + "" + hora.charAt(0) + "" + hora.charAt(1);
//                                    System.out.println("cambiada ini "+codigoMat+" "+nombreMat);
                                } else {
                                    horaInicios[index] = hora.charAt(0) + "" + hora.charAt(1) + "" + hora.charAt(2) + "" + hora.charAt(3) + "" + hora.charAt(4);
                                }

                                if (Integer.valueOf(hora.charAt(6) + "" + hora.charAt(7)) == 0 && Integer.valueOf(hora.charAt(9) + "" + hora.charAt(10)) > 0) {

                                    horaFinales[index] = hora.charAt(9) + "" + hora.charAt(10) + "" + hora.charAt(8) + "" + hora.charAt(6) + "" + hora.charAt(7);
//                                    System.out.println("cambiada fin "+codigoMat+" "+nombreMat);

                                } else {
                                    horaFinales[index] = hora.charAt(6) + "" + hora.charAt(7) + "" + hora.charAt(8) + "" + hora.charAt(9) + "" + hora.charAt(10);
                                }
//                                System.out.println(horaInicios[index] + "  -  " + horaFinales[index]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                    }
                    index++;
                    hora = "";
                    break;
                } else {
                    hora += horarioMat[n].charAt(i);

                }
            }
        }
        int n = 0;
        /*for (int i = 0; i < 5; i++) {
            System.out.println(nombreMat+i + " - " + horaInicios[i] + " " + horaFinales[i]);
        }*/
        while ("".equals(hora)) {
//            System.out.println(hora);
            if (n < 5) {
                for (int i = 0; i < horarioMat[n].length(); i++) {
//                    System.out.println(horarioMat[n]);
                    if (horarioMat[n].charAt(i) == "/".charAt(0)) {
                        break;
                    } else {
                        hora += horarioMat[n].charAt(i);

                    }
                }
                if (hora.length() == 1) {
                    hora = "";
                    n++;
                }
            } else {
                break;
            }
        }
//        System.out.println("llego aqui");
        if (hora.length() == 11) {
            horaInicio = Integer.valueOf(hora.charAt(0) + "" + hora.charAt(1));
//            System.out.println(horaInicio);
            horaTermino = Integer.valueOf(hora.charAt(6) + "" + hora.charAt(7));
//            System.out.println(horaTermino);
        }

//        this.hora = Integer.valueOf(hora);
    }

    public String getNombreMat() {
        return nombreMat;
    }

    public void setNombreMat(String nombreMat) {
        this.nombreMat = nombreMat;
    }

    public String getCodigoMat() {
        return codigoMat;
    }

    public void setCodigoMat(String codigoMat) {
        this.codigoMat = codigoMat;
    }

    public String[] getHorarioMat() {
        return horarioMat;
    }

    public void setHorarioMat(String[] horarioMat) {
        this.horarioMat = horarioMat;
    }

    public String getMaestroMat() {
        return maestroMat;
    }

    public void setMaestroMat(String maestroMat) {
        this.maestroMat = maestroMat;
    }

    public void Mostrar() {
        String horario = "";
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                horario += horarioMat[i];
            } else {
                horario += horarioMat[i] + " - ";
            }
        }
//        System.out.println(horario);
    }

    public Materia() {
    }

    public String ProfeHora() {
        String result = codigoMat + " " + maestroMat + " - " + horarioMat[0];
        return result;
    }

    public String mandarTodo() {
        return nombreMat + " " + codigoMat + " " + horarioMat[0] + " " + horarioMat[1] + " " + horarioMat[2] + " " + horarioMat[3] + " " + horarioMat[4] + " "
                + maestroMat+" "+semestre+" "+costo;
    }

    public String insertarAsignatura() {
        String MateriaRecortada = codigoMat.substring(0, codigoMat.length() - 1);
        return "insert into asignatura values('" + MateriaRecortada + "', '" + nombreMat + ");";
    }

    public String insertarDocente(int i) {

        String[] split = maestroMat.split(" ");
        try {
            return "insert into docente values(" + i + ",'" + split[0] + "','" + split[1] + "','" + split[2] + " " + split[3] + "');";
        } catch (Exception ex) {
            return "insert into docente values(" + i + ",'" + split[0] + "','" + split[1] + "','" + split[2] + "');";
        }
    }

    public String insertarGrupo(int ID) {
        String MateriaRecortada = codigoMat.substring(0, codigoMat.length() - 1);

        return "insert into grupo values('" + codigoMat + "', '" + MateriaRecortada + "'," + ID + ",'"
                + horarioMat[0] + " " + horarioMat[1] + " " + horarioMat[2] + " " + horarioMat[3] + " " + horarioMat[4] + "'," + "35, " + "'si'" + ");";
    }
        static ArrayList<Materia> lista = new ArrayList<>();

    public static void crearListaMaterias(String carrera){
        Archivo a1 = new Archivo(carrera + "\\NombreMaterias.txt");
        Archivo a2 = new Archivo(carrera + "\\ClaveMaterias.txt");
        Archivo a3 = new Archivo(carrera + "\\HorarioMaterias.txt");
        Archivo a4 = new Archivo(carrera + "\\MaestroMaterias.txt");
        Archivo a5 = new Archivo(carrera + "\\Semestres.txt");
        Archivo a6 = new Archivo(carrera + "\\Costos.txt");
        a1.crearLectura();
        a2.crearLectura();
        a3.crearLectura();
        a4.crearLectura();
        a5.crearLectura();
        a6.crearLectura();
        String line = a1.LeerLinea();
        String line2 = a2.LeerLinea();
        String line3 = a3.LeerLinea();
        String line4 = a4.LeerLinea();
        String line5 = a5.LeerLinea();
        String line6 = a6.LeerLinea();
        int cont2 = 0;
        String[] horas = new String[5];
        String hora = "";
        while (line != null) {
            cont2 = 0;
            for (int i = 0; i < line3.length(); i++) {
                if (line3.charAt(i) != "\t".charAt(0) && line3.charAt(i) != " ".charAt(0)) {
                    hora += line3.charAt(i);

                }
                if (line3.charAt(i) == "\t".charAt(0) || line3.charAt(i) == " ".charAt(0) || i == line3.length() - 1) {
                    horas[cont2] = hora;
                    hora = "";
                    cont2++;
                }
            }
            if (!line.equals("RESIDENCIA") && !line.equals("RESIDENCIA PROFESIONAL") && !line.equals("TUTORIA")) {
                lista.add(new Materia(line, line2, horas, line4,Integer.valueOf(line5),Integer.valueOf(line6)));
//                System.out.println(lista.get(lista.size()-1).costo);
            }

            line = a1.LeerLinea();
            line2 = a2.LeerLinea();
            line3 = a3.LeerLinea();
            line4 = a4.LeerLinea();
            line5 = a5.LeerLinea();
            line6 = a6.LeerLinea();
            horas = new String[5];
        }
        a1.CerrarLectura();
        a2.CerrarLectura();
        a3.CerrarLectura();
        a4.CerrarLectura();
        
//        for (int i = 0; i < lista.size(); i++) {
//            System.out.println(lista.get(i).mandarTodo());
//        }
//        return lista;
    }
}

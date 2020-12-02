/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel Gonzalez Cabrera
 */
public class Archivo {

    FileInputStream input;
    FileOutputStream output;
    InputStreamReader reader;
    BufferedReader bufferedReader;
    OutputStreamWriter writer;
    BufferedWriter bufferedWriter;
    File path;

    public Archivo(String direccion) {
        path = new File(direccion);
        
    }

    public void crearEscritura() {
        try {
            output = new FileOutputStream(path);
            writer = new OutputStreamWriter(output);
            bufferedWriter = new BufferedWriter(writer);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean crearLectura() {
        try {
            input = new FileInputStream(path);
            CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
            decoder.onMalformedInput(CodingErrorAction.IGNORE);
            reader = new InputStreamReader(input, decoder);
            bufferedReader = new BufferedReader(reader);
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }

    }

    public void CerrarLectura() {
        try {
            bufferedReader.close();
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void CerrarEscritura() {
        try {
            bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String LeerLinea() {
        String linea = null;
        try {
            linea = bufferedReader.readLine();
        } catch (IOException ex) {
        }

        return linea;
    }

    public void EscribirLinea(String linea) {
        try {
            bufferedWriter.write(linea);
         
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void NuevaLinea() {
        try {
            bufferedWriter.newLine();
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

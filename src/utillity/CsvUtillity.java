/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utillity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import video.VideoConfiguration;
import static utillity.CsvUtillity.Mode.WRITE;

/**
 *
 * @author Rober
 */
public class CsvUtillity implements FileUtility{
    
    public enum Mode{
        READ,
        WRITE;
    }
    
    private final Mode mode;
    private final String file_path;
    
    PrintWriter pw;
    StringBuilder sb;
    
    public CsvUtillity(String file_path, Mode m){
        mode = m;
        this.file_path = file_path;
        
        if (mode == WRITE){
            try {
                
                pw = new PrintWriter(new File(file_path));
                sb = new StringBuilder();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CsvUtillity.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void writeKeyValue(String key, String value){
        sb.append(key);
        sb.append(",");
        sb.append(value);
        sb.append("\n");
    }
    
    public void finalizeFile(){
        if (mode == WRITE){
            pw.write(sb.toString());
            pw.close();
        }
    }
    
    
    public List<String> readCsvLines(){
        List<String> wordList = new LinkedList<>();
        
        try {
            BufferedReader rdr = new BufferedReader(new FileReader(new File(file_path)));
            wordList = rdr.lines().collect(Collectors.toList());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvUtillity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wordList;
    }
}

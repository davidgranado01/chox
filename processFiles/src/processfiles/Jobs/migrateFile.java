/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package processfiles.Jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class migrateFile{
    
    Connection conn;
    boolean bFlag = true;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public migrateFile(Connection conn){
        setConn(conn);
    }

    public boolean execute() throws SQLException, FileNotFoundException, IOException{
        
        deleteTempTable();
        prepareTempTable();
        processPhysicalFile();
        processAttachment();
        
        return true;
    }
    
    private void processAttachment() throws SQLException{
    
        Statement s = null;
        
        try{
            
            if(bFlag){
                
                s = conn.createStatement();
                s.execute("update attachment as a set file_buffer = temp.file_buffer from temp_attachment temp where temp.id=a.id and temp.isproceesed=true;update attachment set file_type='pdf' where file_type in ('PDF', 'pdf', 'Pdf');");    
                bFlag = true;
                
            }

        } catch (SQLException ex) {
            
            bFlag = false;
            Logger.getLogger(migrateFile.class.getName()).log(Level.SEVERE, null, ex);   
            
        }finally{
            
            s.close();
            
        }
    }
    
    private void deleteTempTable() throws SQLException{

        Statement s = null;
        
        try{
            
            if(bFlag){
                
                s = conn.createStatement();
                s.execute("delete from temp_attachment");    
                bFlag = true;
                
            }

        } catch (SQLException ex) {
            
            bFlag = false;
            Logger.getLogger(migrateFile.class.getName()).log(Level.SEVERE, null, ex);   
            
        }finally{
            
            s.close();
            
        }
        
    }

    private void prepareTempTable() throws SQLException{

        Statement s = null;
        
        try{
            
            if(bFlag){
                
                s = conn.createStatement();
                s.execute("insert into temp_attachment select * from attachment");    
                bFlag = true;
                
            }

        } catch (SQLException ex) {
            
            bFlag = false;
            Logger.getLogger(migrateFile.class.getName()).log(Level.SEVERE, null, ex);   
            
        }finally{
            
            s.close();
            
        }
        
    }
    
    private String getFileLocation() throws SQLException{
    
        Statement s = null;
        String sFileName = "";
        
        try{
                
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("select value from global_configuration where parameter='attachment_path'"); 

            while (rs.next()) {
                sFileName = rs.getString("value");
            }

        } catch (SQLException ex) {
            Logger.getLogger(migrateFile.class.getName()).log(Level.SEVERE, null, ex);   
        }finally{
            s.close();
        }    
        
        return sFileName;
        
    }
    
    private void processPhysicalFile() throws SQLException, FileNotFoundException, IOException{
        
        Statement s = null;
        
        try{
            
            if(bFlag){
                
                String filerLocation = getFileLocation();
                
                s = conn.createStatement();
                ResultSet rs = s.executeQuery("select * from temp_attachment where isProceesed = "+false); 
                
                while (rs.next()) {
                    
                    String sFileName = rs.getString("file_name");
                    int attachmentId = Integer.parseInt(rs.getString("id"));
                    
                    File file = new File(filerLocation+sFileName);
                    
                    if(file.isFile() && file.canRead()){
                        
                        FileInputStream streamIn = new FileInputStream(file);
                        byte fileContent[] = new byte[(int)file.length()];
                        streamIn.read(fileContent);
                        
                        flagFile(fileContent, true, attachmentId);

                    }else{
                        
                        flagFile(null, false, attachmentId);
                    }
                    
                }
                
                bFlag = true;
                
            }

        } catch (SQLException ex) {
            
            bFlag = false;
            Logger.getLogger(migrateFile.class.getName()).log(Level.SEVERE, null, ex);   
            
        }finally{
            
            s.close();
            
        }        
    
    }
    
    private void flagFile(byte[] obj, boolean isProcess, int attachmentId) throws SQLException{
        
        Statement s = null; 
        
        try{
            
            if(bFlag){
                
                String sStatement = "";
                PreparedStatement ps = null;
                
                if(obj!=null && isProcess){

                    sStatement = "update temp_attachment set isProceesed=?, file_buffer=? where id=?;";
                    ps = conn.prepareStatement(sStatement);
                    ps.setBoolean(1, isProcess);
                    ps.setBytes(2, obj);
                    ps.setInt(3, attachmentId);
                    
                }else{
                    
                    sStatement = "update temp_attachment set isProceesed=? where id=?;";
                    ps = conn.prepareStatement(sStatement);
                    ps.setBoolean(1, isProcess);
                    ps.setInt(2, attachmentId);
                    
                }
                
                ps.executeUpdate();
                bFlag = true;
                
            }

        } catch (SQLException ex) {
            
            bFlag = false;
            Logger.getLogger(migrateFile.class.getName()).log(Level.SEVERE, null, ex);   
            
        }
        
    
    }
    
}


package dhf.mx.com.sifei.timbrado;

import dhf.mx.com.sifei.cancelacion.Cancelacion_Service;
import dhf.mx.com.sifei.timbradoe.SIFEIService;
import dhf.mx.com.sifei.timbradoe.SifeiException_Exception;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.activation.DataHandler;
import org.ini4j.Wini;

/**
 * Ejemplo de timbrado PHP del WS SOAP getCFDI() de SIFEI. * 
 * @author Daniel
 * El CFDI(XML) debe estar sellado correctamente
 * Nota: Para simplificar los ejemplos todas las rutas son relativas y 
 * los datos se leen de un archivo config.ini, lo cual no debe de hacerse en un ambiente de produccion.
 * 
 * Este ejemplo usa java-ws.
 * 
 */
public class Main {

   

    public static void main(String[] args) throws IOException {
       cancelar(); 
     //timbrar();
    }
    
    public static void cancelar() throws IOException{
        try {
             Wini ini = new Wini(new File("./config.ini"));
             //los datos de se leen desde un archivo de configuracion, no hacer esto en produccion
            String UsuarioSIFEI = ini.get("timbrado", "UsuarioSIFEI", String.class);
            String PasswordSIFEI = ini.get("timbrado", "PasswordSIFEI", String.class);
            String IdEquipo=ini.get("timbrado", "IdEquipoGenerado", String.class);            
            String pfxPath=ini.get("timbrado", "PFX", String.class);
            String pfxPass=ini.get("timbrado", "PFXPass", String.class);
            
            //se lee el PFX
            byte[] pfxBytes=Files.readAllBytes(Paths.get(pfxPath));
            Cancelacion_Service service= new Cancelacion_Service();
            
            
            List<String> uuids = new ArrayList();
            
            uuids.add("f2817145-de1a-4746-8fea-907d1a74e7de");
            

            
            //consumimos el serivicio que en caso de exito devuelve un acuse XML
           String acuseCancelacion=  service.getCancelacionPort().cancelaCFDI(
                   UsuarioSIFEI,
                   PasswordSIFEI, 
                   IdEquipo, 
                   pfxBytes, //pfx 
                   pfxPass, //password del pfx
                   uuids //lista de UUID a cancelar
           );
           
           System.out.println(acuseCancelacion);
           
           
           
        }catch(dhf.mx.com.sifei.cancelacion.SifeiException e){
               System.err.println(e.getFaultInfo().getCodigo());
             System.err.println(e.getFaultInfo().getError());
             System.err.println(e.getFaultInfo().getDetalle());
             
        }
    }
    
    public static void timbrar(){
         try {
            System.out.print("incianado");
            Wini ini = new Wini(new File("./config.ini"));

            String UsuarioSIFEI = ini.get("timbrado", "UsuarioSIFEI", String.class);
            String PasswordSIFEI = ini.get("timbrado", "PasswordSIFEI", String.class);
            String IdEquipo=ini.get("timbrado", "IdEquipoGenerado", String.class);
            
            //se leen los bytes del xml
            byte[] bFile = Files.readAllBytes(Path.of("./assets/xml_sellado.xml"));
            SIFEIService c = new SIFEIService();            
            //se invoca el metodo getCFDI que timbra el cfdi ,
            //en caso de exito el servicio responde con un zip que contiene el CFDI timbrado, getSIFEIPort ya nos devuelve los bytes del zip
            //en caso de error una excepcion de tipo SifeiExcepcion<
            byte[] returns = c.getSIFEIPort().
                    getCFDI(
                            UsuarioSIFEI, 
                            PasswordSIFEI,
                            bFile, 
                            "", 
                            IdEquipo
                    );
            //en memoria se extrae el cfdi(inico archivo dentro del zip)
           String XMLTimbrado=  getFileFromZip(returns);

           System.out.println(XMLTimbrado);
            
         } 
         catch(SifeiException_Exception e){
             //errores al consumir el es
             System.err.println(e.getFaultInfo().getCodigo());
             System.err.println(e.getFaultInfo().getError());
             System.err.println(e.getFaultInfo().getMessage());
             System.err.println(e.getFaultInfo().getXml());
         }
         catch (Exception e) {
             
            System.err.println(e.getMessage());
            e.printStackTrace();
        } 
    }
    public static String getFileFromZip(byte[] returns) throws FileNotFoundException, IOException {
        //se vierten los bytes dentro de un inputStream
        InputStream inputStream = new ByteArrayInputStream(returns);

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry = null;
        //se itera dentro de cada elemento
        
        while ((entry = zipInputStream.getNextEntry()) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = zipInputStream.read();
            while (b >= 0) {
                baos.write(b);
                b = zipInputStream.read();
            }
            zipInputStream.close();
            return new String(baos.toByteArray());
            
        }
        zipInputStream.close();
        return null;
    }
}

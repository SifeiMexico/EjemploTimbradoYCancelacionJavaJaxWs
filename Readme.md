# ![Sifei](https://www.sifei.com.mx/web/image/res.company/1/logo?unique=38c7250)




# Ejemplos de timbrado y cancelación en JAVA

Este repositorio incluye en ejemplos de los servicios SOAP de timbrado y cancelación de Sifei en en lenguaje JAVA por medio de JAX-WS.

 
## Configuración de ejemplos



Los ejemplos se alimentan de un archivo config.ini para leer los datos de conexion, **No hacer esto en produccion**.
La url esta configurada al entorno de pruebas.

Para ejecutar estas pruebas debes solicitar tus accesos de QA(pruebas).

http://sifei.com.mx/

```ini
#Nota este archivo alimenta los ejemplos, no usar en produccion.
[timbrado]
UsuarioSIFEI = RFC # usuario sifei
PasswordSIFEI = 12345678a #password de usuario de sifei 
IdEquipoGenerado = f1563ce5 # ide equipo

 
PFX = CER_KEY.pfx    #Solo para el servicio de cancelacion
PFXPass = 12345678 #password del pfx necesario para cencelar. 

```
### Rutas
Los ejemplos ademas utilizan rutas relativas, por lo que deberas cambiarlas por las rutas de tus archivos de prueba.

## Metodos con ejemplos

WS           | Método           |Descripción
------------ | -----------------|-------------
Timbrado     | `getCFDI()`      |Metodo para timbrar CFDI
Cancelación  | `cancelaCFDI()`  | Metodo para cancelar CFDI

## ¿Necesitas mas ejemplos?
Si necesitas mas ejemplos del resto de servicios favor de generar un nuevo issue
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package acos3;



import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;


public class acos3 extends Applet {


    final static byte[] File_perso = {(byte) 0xFF, (byte) 0x02};
    final static byte[] File_Mana = {(byte) 0xFF, (byte) 0x04};
    private final byte[] File_4 = new byte[256];

    private final byte[] File = new byte[256];
    private final boolean[] Etat = new boolean[256];
    private boolean myfile_selected = false; 
    private byte Number_of_file = 0 ;

	private final static byte CLA = (byte) 0x80;
    private final static byte INS_SELECT_FILE = (byte) 0xA4;
    private final static byte INS_WRITE_FILE = (byte) 0xB0;
    private final static byte INS_READ_FILE = (byte) 0xD0;

public static void install(byte bArray[], short bOffset, byte bLength) throws ISOException {
        new acos3().register();
    }
	
		

public void process(APDU apdu) throws ISOException {
        if (selectingApplet()) {
            return;
        }
        for (byte i = 0; i < 256; i++){
            Etat[i]=false;}

        byte[] buff = apdu.getBuffer();

        if (buff[ISO7816.OFFSET_CLA] != CLA) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }
          switch (buff[ISO7816.OFFSET_INS]) 
          {
            case INS_SELECT_FILE:
                
                if (buff[ISO7816.OFFSET_CDATA] == File_perso[0] && buff[ISO7816.OFFSET_CDATA + 1] == File_perso[1]) {
                    myfile_selected = true;
                    Number_of_file=buff[ISO7816.OFFSET_CDATA + 2];
                   // file_2[0].lenght=Number_of_file;
                    for (byte i = 0; i < Number_of_file; i++){
                        Etat[i] = true;
                        }}
                else if (buff[ISO7816.OFFSET_CDATA] == File_Mana[0] && buff[ISO7816.OFFSET_CDATA + 1] == File_Mana[1]){
                    myfile_selected = true;
                    for (byte i = 0; i < Number_of_file; i=(byte) (i+2)){
                    
                    File_4[i]=buff[ISO7816.OFFSET_CDATA + 2];
                    
                    File_4[i+1]=buff[ISO7816.OFFSET_CDATA + 3];

                    
                }}        

                else if(Etat[Number_of_file] = true){
                    
                    for (byte i = 0; i < Number_of_file; i=(byte) (i+2)){

                        if((buff[ISO7816.OFFSET_CDATA + 1] == File_4[i +1]) && buff[ISO7816.OFFSET_CDATA] == File_4[i]){
                            myfile_selected = true;}}}

                
                   
                else{
                    ISOException.throwIt(ISO7816.SW_FILE_NOT_FOUND);
                }
                break;

            case INS_WRITE_FILE:
               
                    if (myfile_selected) {
                        while(Etat[Number_of_file])
                    
                            {
                    
                                byte len = buff[ISO7816.OFFSET_LC];

                                for (byte i = 0; i < len; i++) {

                                    File[i] = buff[ISO7816.OFFSET_CDATA + i];
                                }

                       
                                if (len == 255) {
                                ISOException.throwIt(ISO7816.SW_FILE_FULL);
                                     }}}
                        
                    else {
                                 ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                        }
                break;
                    
            

                case INS_READ_FILE:
                
                        if (myfile_selected) {
                            while(Etat[Number_of_file]){
                    
                                byte offset = buff[ISO7816.OFFSET_P1];
                                 byte len = buff[ISO7816.OFFSET_P2];

                                if (offset + len > 255) {
                                     ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                                                            }

                                for (byte i = 0; i < len; i++) {
                                    buff[i] = File[offset + i];
                                     }

                        
                                 apdu.setOutgoingAndSend((byte) 0, len);} }
                     
                        else {
                             ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                                }
                break; }}}
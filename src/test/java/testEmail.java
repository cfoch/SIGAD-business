
import com.sigad.sigad.app.controller.EmailController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jorgeespinoza
 */
public class testEmail {

    public static void main(String[] args){
        EmailController email = new EmailController(
                "Gracias por tu compra",
                "Jorge",
                "jeespinozam@pucp.pe", 
                "¡Tu pedido ha sido aceptado!. " + 
                "Recuerda que si tu medio de pago es a través de un depósito en un banco, tienes 2 horas para realizarlo, pasado este tiempo tu pedido será cancelado.\n" + 
                "\n" + "Agradecemos tu preferencia.\n" + "El equipo de MargaritaTel.\n\n\n\n");
        email.emailSend();
        
        email.emailSendWithPdf("/Users/jorgeespinoza/Java/SIGAD-business/src/main/resources/pdf/WinServerFund_SA_1.1_1.2.pdf");
    }
}

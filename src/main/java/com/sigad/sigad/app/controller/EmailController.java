/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.controller;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

/**
 *
 * @author jorgeespinoza
 */
public class EmailController {
    String title = null;
    String name = null;
    String emailTo = null;
    String message = null;
    String pdfRoute = null;
    public EmailController(String title, String name, String emailTo, String message){
        this.title = title;
        this.name = name;
        this.emailTo = emailTo;
        this.message = message;
    }
    
    public void emailSend(){
        // almost everything is optional:
        ResourcesController resources = new ResourcesController();
        String template = getTemplate();
        template = template.replace("Nombrex", name);
        template = template.replace("Título", title);
        template = template.replace("mensaje", message);
        
        Email email = EmailBuilder.startingBlank()
                .from("MargaritaTel", "margaritatel20118@gmail.com")
                .to(name, emailTo)
                .withReplyTo("MargaritaTel", "margaritaTel@gmail.pe")
                .withSubject(title)
                .withHTMLText(template)
                .withPlainText("Gracias por su compra, por favor realice el pago de ser necesario")
                .withHeader("X-Priority", 5)
                .buildEmail();
        Mailer mailer;
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 25, "margaritatel2018@gmail.com", "FY2-ENE-8HE-pw4")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .withDebugLogging(true)
                .buildMailer();
        mailer.sendMail(email);
    }
    
    public void emailSendWithPdf(String pdfRoute){
        this.pdfRoute = pdfRoute;
        ResourcesController resources = new ResourcesController();
        byte[] pdfByteArray = resources.getFileInBytes(pdfRoute);
        if(pdfByteArray.length == 0){
            System.out.println("Error en la obtención del pdf");
            return;
        }
        
        // almost everything is optional:
        String template = getTemplate();
        template = template.replace("Nombrex", name);
        template = template.replace("Título", title);
        template = template.replace("mensaje", message);
        
        Email email = EmailBuilder.startingBlank()
                .from("MargaritaTel", "margaritatel20118@gmail.com")
                .to(name, emailTo)
                .withReplyTo("MargaritaTel", "margaritaTel@gmail.pe")
                .withSubject(title)
                .withAttachment("invitation", pdfByteArray, "application/pdf")
                .withHTMLText(template)
                .withPlainText("Gracias por su compra, por favor realice el pago de ser necesario")
                .withHeader("X-Priority", 5)
                .buildEmail();
        Mailer mailer;
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 25, "margaritatel2018@gmail.com", "FY2-ENE-8HE-pw4")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .withDebugLogging(true)
                .buildMailer();
        mailer.sendMail(email);
    }

    private String getTemplate() {
        return "<!doctype html>\n" +
"<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
"	<head>\n" +
"		<!-- NAME: FOLLOW UP -->\n" +
"		<!--[if gte mso 15]>\n" +
"		<xml>\n" +
"			<o:OfficeDocumentSettings>\n" +
"			<o:AllowPNG/>\n" +
"			<o:PixelsPerInch>96</o:PixelsPerInch>\n" +
"			</o:OfficeDocumentSettings>\n" +
"		</xml>\n" +
"		<![endif]-->\n" +
"		<meta charset=\"UTF-8\">\n" +
"        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
"		<title>*|MC:SUBJECT|*</title>\n" +
"        \n" +
"    <style type=\"text/css\">\n" +
"		p{\n" +
"			margin:10px 0;\n" +
"			padding:0;\n" +
"		}\n" +
"		table{\n" +
"			border-collapse:collapse;\n" +
"		}\n" +
"		h1,h2,h3,h4,h5,h6{\n" +
"			display:block;\n" +
"			margin:0;\n" +
"			padding:0;\n" +
"		}\n" +
"		img,a img{\n" +
"			border:0;\n" +
"			height:auto;\n" +
"			outline:none;\n" +
"			text-decoration:none;\n" +
"		}\n" +
"		body,#bodyTable,#bodyCell{\n" +
"			height:100%;\n" +
"			margin:0;\n" +
"			padding:0;\n" +
"			width:100%;\n" +
"		}\n" +
"		.mcnPreviewText{\n" +
"			display:none !important;\n" +
"		}\n" +
"		#outlook a{\n" +
"			padding:0;\n" +
"		}\n" +
"		img{\n" +
"			-ms-interpolation-mode:bicubic;\n" +
"		}\n" +
"		table{\n" +
"			mso-table-lspace:0pt;\n" +
"			mso-table-rspace:0pt;\n" +
"		}\n" +
"		.ReadMsgBody{\n" +
"			width:100%;\n" +
"		}\n" +
"		.ExternalClass{\n" +
"			width:100%;\n" +
"		}\n" +
"		p,a,li,td,blockquote{\n" +
"			mso-line-height-rule:exactly;\n" +
"		}\n" +
"		a[href^=tel],a[href^=sms]{\n" +
"			color:inherit;\n" +
"			cursor:default;\n" +
"			text-decoration:none;\n" +
"		}\n" +
"		p,a,li,td,body,table,blockquote{\n" +
"			-ms-text-size-adjust:100%;\n" +
"			-webkit-text-size-adjust:100%;\n" +
"		}\n" +
"		.ExternalClass,.ExternalClass p,.ExternalClass td,.ExternalClass div,.ExternalClass span,.ExternalClass font{\n" +
"			line-height:100%;\n" +
"		}\n" +
"		a[x-apple-data-detectors]{\n" +
"			color:inherit !important;\n" +
"			text-decoration:none !important;\n" +
"			font-size:inherit !important;\n" +
"			font-family:inherit !important;\n" +
"			font-weight:inherit !important;\n" +
"			line-height:inherit !important;\n" +
"		}\n" +
"		.templateContainer{\n" +
"			max-width:600px !important;\n" +
"		}\n" +
"		a.mcnButton{\n" +
"			display:block;\n" +
"		}\n" +
"		.mcnImage,.mcnRetinaImage{\n" +
"			vertical-align:bottom;\n" +
"		}\n" +
"		.mcnTextContent{\n" +
"			word-break:break-word;\n" +
"		}\n" +
"		.mcnTextContent img{\n" +
"			height:auto !important;\n" +
"		}\n" +
"		.mcnDividerBlock{\n" +
"			table-layout:fixed !important;\n" +
"		}\n" +
"	/*\n" +
"	@tab Page\n" +
"	@section Heading 1\n" +
"	@style heading 1\n" +
"	*/\n" +
"		h1{\n" +
"			/*@editable*/color:#222222;\n" +
"			/*@editable*/font-family:Helvetica;\n" +
"			/*@editable*/font-size:40px;\n" +
"			/*@editable*/font-style:normal;\n" +
"			/*@editable*/font-weight:bold;\n" +
"			/*@editable*/line-height:150%;\n" +
"			/*@editable*/letter-spacing:normal;\n" +
"			/*@editable*/text-align:center;\n" +
"		}\n" +
"	/*\n" +
"	@tab Page\n" +
"	@section Heading 2\n" +
"	@style heading 2\n" +
"	*/\n" +
"		h2{\n" +
"			/*@editable*/color:#222222;\n" +
"			/*@editable*/font-family:Helvetica;\n" +
"			/*@editable*/font-size:34px;\n" +
"			/*@editable*/font-style:normal;\n" +
"			/*@editable*/font-weight:bold;\n" +
"			/*@editable*/line-height:150%;\n" +
"			/*@editable*/letter-spacing:normal;\n" +
"			/*@editable*/text-align:left;\n" +
"		}\n" +
"	/*\n" +
"	@tab Page\n" +
"	@section Heading 3\n" +
"	@style heading 3\n" +
"	*/\n" +
"		h3{\n" +
"			/*@editable*/color:#444444;\n" +
"			/*@editable*/font-family:Helvetica;\n" +
"			/*@editable*/font-size:22px;\n" +
"			/*@editable*/font-style:normal;\n" +
"			/*@editable*/font-weight:bold;\n" +
"			/*@editable*/line-height:150%;\n" +
"			/*@editable*/letter-spacing:normal;\n" +
"			/*@editable*/text-align:center;\n" +
"		}\n" +
"	/*\n" +
"	@tab Page\n" +
"	@section Heading 4\n" +
"	@style heading 4\n" +
"	*/\n" +
"		h4{\n" +
"			/*@editable*/color:#999999;\n" +
"			/*@editable*/font-family:Georgia;\n" +
"			/*@editable*/font-size:20px;\n" +
"			/*@editable*/font-style:italic;\n" +
"			/*@editable*/font-weight:normal;\n" +
"			/*@editable*/line-height:125%;\n" +
"			/*@editable*/letter-spacing:normal;\n" +
"			/*@editable*/text-align:left;\n" +
"		}\n" +
"	/*\n" +
"	@tab Header\n" +
"	@section Header Container Style\n" +
"	*/\n" +
"		#templateHeader{\n" +
"			/*@editable*/background-color:#F7F7F7;\n" +
"			/*@editable*/background-image:none;\n" +
"			/*@editable*/background-repeat:no-repeat;\n" +
"			/*@editable*/background-position:center;\n" +
"			/*@editable*/background-size:cover;\n" +
"			/*@editable*/border-top:0;\n" +
"			/*@editable*/border-bottom:0;\n" +
"			/*@editable*/padding-top:27px;\n" +
"			/*@editable*/padding-bottom:27px;\n" +
"		}\n" +
"	/*\n" +
"	@tab Header\n" +
"	@section Header Interior Style\n" +
"	*/\n" +
"		.headerContainer{\n" +
"			/*@editable*/background-color:transparent;\n" +
"			/*@editable*/background-image:none;\n" +
"			/*@editable*/background-repeat:no-repeat;\n" +
"			/*@editable*/background-position:center;\n" +
"			/*@editable*/background-size:cover;\n" +
"			/*@editable*/border-top:0;\n" +
"			/*@editable*/border-bottom:0;\n" +
"			/*@editable*/padding-top:0;\n" +
"			/*@editable*/padding-bottom:0;\n" +
"		}\n" +
"	/*\n" +
"	@tab Header\n" +
"	@section Header Text\n" +
"	*/\n" +
"		.headerContainer .mcnTextContent,.headerContainer .mcnTextContent p{\n" +
"			/*@editable*/color:#808080;\n" +
"			/*@editable*/font-family:Helvetica;\n" +
"			/*@editable*/font-size:16px;\n" +
"			/*@editable*/line-height:150%;\n" +
"			/*@editable*/text-align:left;\n" +
"		}\n" +
"	/*\n" +
"	@tab Header\n" +
"	@section Header Link\n" +
"	*/\n" +
"		.headerContainer .mcnTextContent a,.headerContainer .mcnTextContent p a{\n" +
"			/*@editable*/color:#00ADD8;\n" +
"			/*@editable*/font-weight:normal;\n" +
"			/*@editable*/text-decoration:underline;\n" +
"		}\n" +
"	/*\n" +
"	@tab Body\n" +
"	@section Body Container Style\n" +
"	*/\n" +
"		#templateBody{\n" +
"			/*@editable*/background-color:#FFFFFF;\n" +
"			/*@editable*/background-image:none;\n" +
"			/*@editable*/background-repeat:no-repeat;\n" +
"			/*@editable*/background-position:center;\n" +
"			/*@editable*/background-size:cover;\n" +
"			/*@editable*/border-top:0;\n" +
"			/*@editable*/border-bottom:0;\n" +
"			/*@editable*/padding-top:0px;\n" +
"			/*@editable*/padding-bottom:0px;\n" +
"		}\n" +
"	/*\n" +
"	@tab Body\n" +
"	@section Body Interior Style\n" +
"	*/\n" +
"		.bodyContainer{\n" +
"			/*@editable*/background-color:transparent;\n" +
"			/*@editable*/background-image:none;\n" +
"			/*@editable*/background-repeat:no-repeat;\n" +
"			/*@editable*/background-position:center;\n" +
"			/*@editable*/background-size:cover;\n" +
"			/*@editable*/border-top:0;\n" +
"			/*@editable*/border-bottom:0;\n" +
"			/*@editable*/padding-top:0;\n" +
"			/*@editable*/padding-bottom:0;\n" +
"		}\n" +
"	/*\n" +
"	@tab Body\n" +
"	@section Body Text\n" +
"	*/\n" +
"		.bodyContainer .mcnTextContent,.bodyContainer .mcnTextContent p{\n" +
"			/*@editable*/color:#808080;\n" +
"			/*@editable*/font-family:Helvetica;\n" +
"			/*@editable*/font-size:16px;\n" +
"			/*@editable*/line-height:150%;\n" +
"			/*@editable*/text-align:left;\n" +
"		}\n" +
"	/*\n" +
"	@tab Body\n" +
"	@section Body Link\n" +
"	*/\n" +
"		.bodyContainer .mcnTextContent a,.bodyContainer .mcnTextContent p a{\n" +
"			/*@editable*/color:#00ADD8;\n" +
"			/*@editable*/font-weight:normal;\n" +
"			/*@editable*/text-decoration:underline;\n" +
"		}\n" +
"	/*\n" +
"	@tab Footer\n" +
"	@section Footer Style\n" +
"	*/\n" +
"		#templateFooter{\n" +
"			/*@editable*/background-color:#333333;\n" +
"			/*@editable*/background-image:none;\n" +
"			/*@editable*/background-repeat:no-repeat;\n" +
"			/*@editable*/background-position:center;\n" +
"			/*@editable*/background-size:cover;\n" +
"			/*@editable*/border-top:0;\n" +
"			/*@editable*/border-bottom:0;\n" +
"			/*@editable*/padding-top:0px;\n" +
"			/*@editable*/padding-bottom:0px;\n" +
"		}\n" +
"	/*\n" +
"	@tab Footer\n" +
"	@section Footer Interior Style\n" +
"	*/\n" +
"		.footerContainer{\n" +
"			/*@editable*/background-color:transparent;\n" +
"			/*@editable*/background-image:none;\n" +
"			/*@editable*/background-repeat:no-repeat;\n" +
"			/*@editable*/background-position:center;\n" +
"			/*@editable*/background-size:cover;\n" +
"			/*@editable*/border-top:0;\n" +
"			/*@editable*/border-bottom:0;\n" +
"			/*@editable*/padding-top:0;\n" +
"			/*@editable*/padding-bottom:0;\n" +
"		}\n" +
"	/*\n" +
"	@tab Footer\n" +
"	@section Footer Text\n" +
"	*/\n" +
"		.footerContainer .mcnTextContent,.footerContainer .mcnTextContent p{\n" +
"			/*@editable*/color:#FFFFFF;\n" +
"			/*@editable*/font-family:Helvetica;\n" +
"			/*@editable*/font-size:12px;\n" +
"			/*@editable*/line-height:150%;\n" +
"			/*@editable*/text-align:center;\n" +
"		}\n" +
"	/*\n" +
"	@tab Footer\n" +
"	@section Footer Link\n" +
"	*/\n" +
"		.footerContainer .mcnTextContent a,.footerContainer .mcnTextContent p a{\n" +
"			/*@editable*/color:#FFFFFF;\n" +
"			/*@editable*/font-weight:normal;\n" +
"			/*@editable*/text-decoration:underline;\n" +
"		}\n" +
"	@media only screen and (min-width:768px){\n" +
"		.templateContainer{\n" +
"			width:600px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		body,table,td,p,a,li,blockquote{\n" +
"			-webkit-text-size-adjust:none !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		body{\n" +
"			width:100% !important;\n" +
"			min-width:100% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnRetinaImage{\n" +
"			max-width:100% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnImage{\n" +
"			width:100% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnCartContainer,.mcnCaptionTopContent,.mcnRecContentContainer,.mcnCaptionBottomContent,.mcnTextContentContainer,.mcnBoxedTextContentContainer,.mcnImageGroupContentContainer,.mcnCaptionLeftTextContentContainer,.mcnCaptionRightTextContentContainer,.mcnCaptionLeftImageContentContainer,.mcnCaptionRightImageContentContainer,.mcnImageCardLeftTextContentContainer,.mcnImageCardRightTextContentContainer,.mcnImageCardLeftImageContentContainer,.mcnImageCardRightImageContentContainer{\n" +
"			max-width:100% !important;\n" +
"			width:100% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnBoxedTextContentContainer{\n" +
"			min-width:100% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnImageGroupContent{\n" +
"			padding:9px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnCaptionLeftContentOuter .mcnTextContent,.mcnCaptionRightContentOuter .mcnTextContent{\n" +
"			padding-top:9px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnImageCardTopImageContent,.mcnCaptionBottomContent:last-child .mcnCaptionBottomImageContent,.mcnCaptionBlockInner .mcnCaptionTopContent:last-child .mcnTextContent{\n" +
"			padding-top:18px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnImageCardBottomImageContent{\n" +
"			padding-bottom:9px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnImageGroupBlockInner{\n" +
"			padding-top:0 !important;\n" +
"			padding-bottom:0 !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnImageGroupBlockOuter{\n" +
"			padding-top:9px !important;\n" +
"			padding-bottom:9px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnTextContent,.mcnBoxedTextContentColumn{\n" +
"			padding-right:18px !important;\n" +
"			padding-left:18px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcnImageCardLeftImageContent,.mcnImageCardRightImageContent{\n" +
"			padding-right:18px !important;\n" +
"			padding-bottom:0 !important;\n" +
"			padding-left:18px !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"		.mcpreview-image-uploader{\n" +
"			display:none !important;\n" +
"			width:100% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Heading 1\n" +
"	@tip Make the first-level headings larger in size for better readability on small screens.\n" +
"	*/\n" +
"		h1{\n" +
"			/*@editable*/font-size:30px !important;\n" +
"			/*@editable*/line-height:125% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Heading 2\n" +
"	@tip Make the second-level headings larger in size for better readability on small screens.\n" +
"	*/\n" +
"		h2{\n" +
"			/*@editable*/font-size:26px !important;\n" +
"			/*@editable*/line-height:125% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Heading 3\n" +
"	@tip Make the third-level headings larger in size for better readability on small screens.\n" +
"	*/\n" +
"		h3{\n" +
"			/*@editable*/font-size:20px !important;\n" +
"			/*@editable*/line-height:150% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Heading 4\n" +
"	@tip Make the fourth-level headings larger in size for better readability on small screens.\n" +
"	*/\n" +
"		h4{\n" +
"			/*@editable*/font-size:18px !important;\n" +
"			/*@editable*/line-height:150% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Boxed Text\n" +
"	@tip Make the boxed text larger in size for better readability on small screens. We recommend a font size of at least 16px.\n" +
"	*/\n" +
"		.mcnBoxedTextContentContainer .mcnTextContent,.mcnBoxedTextContentContainer .mcnTextContent p{\n" +
"			/*@editable*/font-size:14px !important;\n" +
"			/*@editable*/line-height:150% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Header Text\n" +
"	@tip Make the header text larger in size for better readability on small screens.\n" +
"	*/\n" +
"		.headerContainer .mcnTextContent,.headerContainer .mcnTextContent p{\n" +
"			/*@editable*/font-size:16px !important;\n" +
"			/*@editable*/line-height:150% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Body Text\n" +
"	@tip Make the body text larger in size for better readability on small screens. We recommend a font size of at least 16px.\n" +
"	*/\n" +
"		.bodyContainer .mcnTextContent,.bodyContainer .mcnTextContent p{\n" +
"			/*@editable*/font-size:16px !important;\n" +
"			/*@editable*/line-height:150% !important;\n" +
"		}\n" +
"\n" +
"}	@media only screen and (max-width: 480px){\n" +
"	/*\n" +
"	@tab Mobile Styles\n" +
"	@section Footer Text\n" +
"	@tip Make the footer content text larger in size for better readability on small screens.\n" +
"	*/\n" +
"		.footerContainer .mcnTextContent,.footerContainer .mcnTextContent p{\n" +
"			/*@editable*/font-size:14px !important;\n" +
"			/*@editable*/line-height:150% !important;\n" +
"		}\n" +
"\n" +
"}</style></head>\n" +
"    <body>\n" +
"		<!--*|IF:MC_PREVIEW_TEXT|*-->\n" +
"		<!--[if !gte mso 9]><!----><span class=\"mcnPreviewText\" style=\"display:none; font-size:0px; line-height:0px; max-height:0px; max-width:0px; opacity:0; overflow:hidden; visibility:hidden; mso-hide:all;\">*|MC_PREVIEW_TEXT|*</span><!--<![endif]-->\n" +
"		<!--*|END:IF|*-->\n" +
"        <center>\n" +
"            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\">\n" +
"                <tr>\n" +
"                    <td align=\"center\" valign=\"top\" id=\"bodyCell\">\n" +
"                        <!-- BEGIN TEMPLATE // -->\n" +
"                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
"							<tr>\n" +
"								<td align=\"center\" valign=\"top\" id=\"templateHeader\" data-template-container>\n" +
"									<!--[if (gte mso 9)|(IE)]>\n" +
"									<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"600\" style=\"width:600px;\">\n" +
"									<tr>\n" +
"									<td align=\"center\" valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
"									<![endif]-->\n" +
"									<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"templateContainer\">\n" +
"										<tr>\n" +
"                                			<td valign=\"top\" class=\"headerContainer\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnCaptionBlock\">\n" +
"    <tbody class=\"mcnCaptionBlockOuter\">\n" +
"        <tr>\n" +
"            <td class=\"mcnCaptionBlockInner\" valign=\"top\" style=\"padding:9px;\">\n" +
"                \n" +
"\n" +
"\n" +
"\n" +
"<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mcnCaptionRightContentOuter\" width=\"100%\">\n" +
"    <tbody><tr>\n" +
"        <td valign=\"top\" class=\"mcnCaptionRightContentInner\" style=\"padding:0 9px ;\">\n" +
"            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mcnCaptionRightImageContentContainer\" width=\"176\">\n" +
"                <tbody><tr>\n" +
"                    <td class=\"mcnCaptionRightImageContent\" align=\"center\" valign=\"top\">\n" +
"                    \n" +
"                        \n" +
"\n" +
"                        <img alt=\"\" src=\"https://gallery.mailchimp.com/cec11958e3e0dc9ba23680b74/images/6a153804-fc5a-4fbf-804e-bb1446dbeedf.gif\" width=\"176\" style=\"max-width:480px;\" class=\"mcnImage\">\n" +
"                        \n" +
"\n" +
"                    \n" +
"                    </td>\n" +
"                </tr>\n" +
"            </tbody></table>\n" +
"            <table class=\"mcnCaptionRightTextContentContainer\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"352\">\n" +
"                <tbody><tr>\n" +
"                    <td valign=\"top\" class=\"mcnTextContent\">\n" +
"                        <div style=\"text-align: center;\">&nbsp;</div>\n" +
"\n" +
"<h1 class=\"null\" style=\"text-align: center;\"><span style=\"font-family:georgia,times,times new roman,serif\">MARGARITA TEL</span></h1>\n" +
"\n" +
"<div style=\"text-align: center;\">&nbsp;</div>\n" +
"\n" +
"                    </td>\n" +
"                </tr>\n" +
"            </tbody></table>\n" +
"        </td>\n" +
"    </tr>\n" +
"</tbody></table>\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table></td>\n" +
"										</tr>\n" +
"									</table>\n" +
"									<!--[if (gte mso 9)|(IE)]>\n" +
"									</td>\n" +
"									</tr>\n" +
"									</table>\n" +
"									<![endif]-->\n" +
"								</td>\n" +
"                            </tr>\n" +
"							<tr>\n" +
"								<td align=\"center\" valign=\"top\" id=\"templateBody\" data-template-container>\n" +
"									<!--[if (gte mso 9)|(IE)]>\n" +
"									<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"600\" style=\"width:600px;\">\n" +
"									<tr>\n" +
"									<td align=\"center\" valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
"									<![endif]-->\n" +
"									<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"templateContainer\">\n" +
"										<tr>\n" +
"                                			<td valign=\"top\" class=\"bodyContainer\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnTextBlockOuter\">\n" +
"        <tr>\n" +
"            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top:9px;\">\n" +
"              	<!--[if mso]>\n" +
"				<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
"				<tr>\n" +
"				<![endif]-->\n" +
"			    \n" +
"				<!--[if mso]>\n" +
"				<td valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
"				<![endif]-->\n" +
"                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width:100%; min-width:100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
"                    <tbody><tr>\n" +
"                        \n" +
"                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding-top:0; padding-right:18px; padding-bottom:9px; padding-left:18px;\">\n" +
"                        \n" +
"                            <h2 class=\"null\" style=\"text-align: center;\"><span style=\"font-size:32px\"><span style=\"font-family:open sans,helvetica neue,helvetica,arial,sans-serif\">Título</span></span></h2>\n" +
"\n" +
"                        </td>\n" +
"                    </tr>\n" +
"                </tbody></table>\n" +
"				<!--[if mso]>\n" +
"				</td>\n" +
"				<![endif]-->\n" +
"                \n" +
"				<!--[if mso]>\n" +
"				</tr>\n" +
"				</table>\n" +
"				<![endif]-->\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnDividerBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnDividerBlockOuter\">\n" +
"        <tr>\n" +
"            <td class=\"mcnDividerBlockInner\" style=\"min-width: 100%; padding: 18px;\">\n" +
"                <table class=\"mcnDividerContent\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width: 100%;border-top-width: 2px;border-top-style: solid;border-top-color: #EAEAEA;\">\n" +
"                    <tbody><tr>\n" +
"                        <td>\n" +
"                            <span></span>\n" +
"                        </td>\n" +
"                    </tr>\n" +
"                </tbody></table>\n" +
"<!--            \n" +
"                <td class=\"mcnDividerBlockInner\" style=\"padding: 18px;\">\n" +
"                <hr class=\"mcnDividerContent\" style=\"border-bottom-color:none; border-left-color:none; border-right-color:none; border-bottom-width:0; border-left-width:0; border-right-width:0; margin-top:0; margin-right:0; margin-bottom:0; margin-left:0;\" />\n" +
"-->\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnTextBlockOuter\">\n" +
"        <tr>\n" +
"            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top:9px;\">\n" +
"              	<!--[if mso]>\n" +
"				<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
"				<tr>\n" +
"				<![endif]-->\n" +
"			    \n" +
"				<!--[if mso]>\n" +
"				<td valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
"				<![endif]-->\n" +
"                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width:100%; min-width:100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
"                    <tbody><tr>\n" +
"                        \n" +
"                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding-top:0; padding-right:18px; padding-bottom:9px; padding-left:18px;\">\n" +
"                        \n" +
"                            <h3 class=\"null\" style=\"text-align: left;\">Hola Nombrex:</h3>\n" +
"\n" +
"<p><br>\n" +
"mensaje</p>\n" +
"\n" +
"                        </td>\n" +
"                    </tr>\n" +
"                </tbody></table>\n" +
"				<!--[if mso]>\n" +
"				</td>\n" +
"				<![endif]-->\n" +
"                \n" +
"				<!--[if mso]>\n" +
"				</tr>\n" +
"				</table>\n" +
"				<![endif]-->\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnDividerBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnDividerBlockOuter\">\n" +
"        <tr>\n" +
"            <td class=\"mcnDividerBlockInner\" style=\"min-width: 100%; padding: 9px 18px 0px;\">\n" +
"                <table class=\"mcnDividerContent\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%;\">\n" +
"                    <tbody><tr>\n" +
"                        <td>\n" +
"                            <span></span>\n" +
"                        </td>\n" +
"                    </tr>\n" +
"                </tbody></table>\n" +
"<!--            \n" +
"                <td class=\"mcnDividerBlockInner\" style=\"padding: 18px;\">\n" +
"                <hr class=\"mcnDividerContent\" style=\"border-bottom-color:none; border-left-color:none; border-right-color:none; border-bottom-width:0; border-left-width:0; border-right-width:0; margin-top:0; margin-right:0; margin-bottom:0; margin-left:0;\" />\n" +
"-->\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnDividerBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnDividerBlockOuter\">\n" +
"        <tr>\n" +
"            <td class=\"mcnDividerBlockInner\" style=\"min-width: 100%; padding: 9px 18px 0px;\">\n" +
"                <table class=\"mcnDividerContent\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%;\">\n" +
"                    <tbody><tr>\n" +
"                        <td>\n" +
"                            <span></span>\n" +
"                        </td>\n" +
"                    </tr>\n" +
"                </tbody></table>\n" +
"<!--            \n" +
"                <td class=\"mcnDividerBlockInner\" style=\"padding: 18px;\">\n" +
"                <hr class=\"mcnDividerContent\" style=\"border-bottom-color:none; border-left-color:none; border-right-color:none; border-bottom-width:0; border-left-width:0; border-right-width:0; margin-top:0; margin-right:0; margin-bottom:0; margin-left:0;\" />\n" +
"-->\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table></td>\n" +
"										</tr>\n" +
"									</table>\n" +
"									<!--[if (gte mso 9)|(IE)]>\n" +
"									</td>\n" +
"									</tr>\n" +
"									</table>\n" +
"									<![endif]-->\n" +
"								</td>\n" +
"                            </tr>\n" +
"                            <tr>\n" +
"								<td align=\"center\" valign=\"top\" id=\"templateFooter\" data-template-container>\n" +
"									<!--[if (gte mso 9)|(IE)]>\n" +
"									<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"600\" style=\"width:600px;\">\n" +
"									<tr>\n" +
"									<td align=\"center\" valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
"									<![endif]-->\n" +
"									<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"templateContainer\">\n" +
"										<tr>\n" +
"                                			<td valign=\"top\" class=\"footerContainer\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnFollowBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnFollowBlockOuter\">\n" +
"        <tr>\n" +
"            <td align=\"center\" valign=\"top\" style=\"padding:9px\" class=\"mcnFollowBlockInner\">\n" +
"                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnFollowContentContainer\" style=\"min-width:100%;\">\n" +
"    <tbody><tr>\n" +
"        <td align=\"center\" style=\"padding-left:9px;padding-right:9px;\">\n" +
"            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%;\" class=\"mcnFollowContent\">\n" +
"                <tbody><tr>\n" +
"                    <td align=\"center\" valign=\"top\" style=\"padding-top:9px; padding-right:9px; padding-left:9px;\">\n" +
"                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
"                            <tbody><tr>\n" +
"                                <td align=\"center\" valign=\"top\">\n" +
"                                    <!--[if mso]>\n" +
"                                    <table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
"                                    <tr>\n" +
"                                    <![endif]-->\n" +
"                                    \n" +
"                                        <!--[if mso]>\n" +
"                                        <td align=\"center\" valign=\"top\">\n" +
"                                        <![endif]-->\n" +
"                                        \n" +
"                                        \n" +
"                                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"display:inline;\">\n" +
"                                                <tbody><tr>\n" +
"                                                    <td valign=\"top\" style=\"padding-right:10px; padding-bottom:9px;\" class=\"mcnFollowContentItemContainer\">\n" +
"                                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnFollowContentItem\">\n" +
"                                                            <tbody><tr>\n" +
"                                                                <td align=\"left\" valign=\"middle\" style=\"padding-top:5px; padding-right:10px; padding-bottom:5px; padding-left:9px;\">\n" +
"                                                                    <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"\">\n" +
"                                                                        <tbody><tr>\n" +
"                                                                            \n" +
"                                                                                <td align=\"center\" valign=\"middle\" width=\"24\" class=\"mcnFollowIconContent\">\n" +
"                                                                                    <a href=\"http://www.facebook.com\" target=\"_blank\"><img src=\"https://cdn-images.mailchimp.com/icons/social-block-v2/outline-light-facebook-48.png\" style=\"display:block;\" height=\"24\" width=\"24\" class=\"\"></a>\n" +
"                                                                                </td>\n" +
"                                                                            \n" +
"                                                                            \n" +
"                                                                                <td align=\"left\" valign=\"middle\" class=\"mcnFollowTextContent\" style=\"padding-left:5px;\">\n" +
"                                                                                    <a href=\"http://www.facebook.com\" target=\"\" style=\"font-family: Helvetica;font-size: 14px;text-decoration: none;color: #FFFFFF;\">Facebook</a>\n" +
"                                                                                </td>\n" +
"                                                                            \n" +
"                                                                        </tr>\n" +
"                                                                    </tbody></table>\n" +
"                                                                </td>\n" +
"                                                            </tr>\n" +
"                                                        </tbody></table>\n" +
"                                                    </td>\n" +
"                                                </tr>\n" +
"                                            </tbody></table>\n" +
"                                        \n" +
"                                        <!--[if mso]>\n" +
"                                        </td>\n" +
"                                        <![endif]-->\n" +
"                                    \n" +
"                                        <!--[if mso]>\n" +
"                                        <td align=\"center\" valign=\"top\">\n" +
"                                        <![endif]-->\n" +
"                                        \n" +
"                                        \n" +
"                                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"display:inline;\">\n" +
"                                                <tbody><tr>\n" +
"                                                    <td valign=\"top\" style=\"padding-right:0; padding-bottom:9px;\" class=\"mcnFollowContentItemContainer\">\n" +
"                                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnFollowContentItem\">\n" +
"                                                            <tbody><tr>\n" +
"                                                                <td align=\"left\" valign=\"middle\" style=\"padding-top:5px; padding-right:10px; padding-bottom:5px; padding-left:9px;\">\n" +
"                                                                    <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"\">\n" +
"                                                                        <tbody><tr>\n" +
"                                                                            \n" +
"                                                                                <td align=\"center\" valign=\"middle\" width=\"24\" class=\"mcnFollowIconContent\">\n" +
"                                                                                    <a href=\"mailto:margaritatel2018@gmail.com\" target=\"_blank\"><img src=\"https://cdn-images.mailchimp.com/icons/social-block-v2/outline-light-forwardtofriend-48.png\" style=\"display:block;\" height=\"24\" width=\"24\" class=\"\"></a>\n" +
"                                                                                </td>\n" +
"                                                                            \n" +
"                                                                            \n" +
"                                                                                <td align=\"left\" valign=\"middle\" class=\"mcnFollowTextContent\" style=\"padding-left:5px;\">\n" +
"                                                                                    <a href=\"mailto:margaritatel2018@gmail.com\" target=\"\" style=\"font-family: Helvetica;font-size: 14px;text-decoration: none;color: #FFFFFF;\">Email</a>\n" +
"                                                                                </td>\n" +
"                                                                            \n" +
"                                                                        </tr>\n" +
"                                                                    </tbody></table>\n" +
"                                                                </td>\n" +
"                                                            </tr>\n" +
"                                                        </tbody></table>\n" +
"                                                    </td>\n" +
"                                                </tr>\n" +
"                                            </tbody></table>\n" +
"                                        \n" +
"                                        <!--[if mso]>\n" +
"                                        </td>\n" +
"                                        <![endif]-->\n" +
"                                    \n" +
"                                    <!--[if mso]>\n" +
"                                    </tr>\n" +
"                                    </table>\n" +
"                                    <![endif]-->\n" +
"                                </td>\n" +
"                            </tr>\n" +
"                        </tbody></table>\n" +
"                    </td>\n" +
"                </tr>\n" +
"            </tbody></table>\n" +
"        </td>\n" +
"    </tr>\n" +
"</tbody></table>\n" +
"\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnDividerBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnDividerBlockOuter\">\n" +
"        <tr>\n" +
"            <td class=\"mcnDividerBlockInner\" style=\"min-width: 100%; padding: 18px;\">\n" +
"                <table class=\"mcnDividerContent\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width: 100%;border-top-width: 2px;border-top-style: solid;border-top-color: #505050;\">\n" +
"                    <tbody><tr>\n" +
"                        <td>\n" +
"                            <span></span>\n" +
"                        </td>\n" +
"                    </tr>\n" +
"                </tbody></table>\n" +
"<!--            \n" +
"                <td class=\"mcnDividerBlockInner\" style=\"padding: 18px;\">\n" +
"                <hr class=\"mcnDividerContent\" style=\"border-bottom-color:none; border-left-color:none; border-right-color:none; border-bottom-width:0; border-left-width:0; border-right-width:0; margin-top:0; margin-right:0; margin-bottom:0; margin-left:0;\" />\n" +
"-->\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width:100%;\">\n" +
"    <tbody class=\"mcnTextBlockOuter\">\n" +
"        <tr>\n" +
"            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top:9px;\">\n" +
"              	<!--[if mso]>\n" +
"				<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
"				<tr>\n" +
"				<![endif]-->\n" +
"			    \n" +
"				<!--[if mso]>\n" +
"				<td valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
"				<![endif]-->\n" +
"                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width:100%; min-width:100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
"                    <tbody><tr>\n" +
"                        \n" +
"                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding-top:0; padding-right:18px; padding-bottom:9px; padding-left:18px;\">\n" +
"                        \n" +
"                            <em>Copyright © 2018 MargaritaTel, All rights reserved.</em><br>\n" +
"<br>\n" +
"<strong>Our mailing address is:</strong><br>\n" +
"margaritatel2018@gmail.com\n" +
"                        </td>\n" +
"                    </tr>\n" +
"                </tbody></table>\n" +
"				<!--[if mso]>\n" +
"				</td>\n" +
"				<![endif]-->\n" +
"                \n" +
"				<!--[if mso]>\n" +
"				</tr>\n" +
"				</table>\n" +
"				<![endif]-->\n" +
"            </td>\n" +
"        </tr>\n" +
"    </tbody>\n" +
"</table></td>\n" +
"										</tr>\n" +
"									</table>\n" +
"									<!--[if (gte mso 9)|(IE)]>\n" +
"									</td>\n" +
"									</tr>\n" +
"									</table>\n" +
"									<![endif]-->\n" +
"								</td>\n" +
"                            </tr>\n" +
"                        </table>\n" +
"                        <!-- // END TEMPLATE -->\n" +
"                    </td>\n" +
"                </tr>\n" +
"            </table>\n" +
"        </center>\n" +
"    </body>\n" +
"</html>";
    }
}

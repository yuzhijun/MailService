package com.lenovo.simplemail;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class MailSenderMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleMail simpleMail = new SimpleMail();
		simpleMail.setSubject("这是个测试邮件");
//		simpleMail.setContent("接收到了吗？");
		simpleMail.setContent("这是一封邮件正文带图片<img src='cid:xxx.jpg'>的邮件");
		// 发送邮件
        SimpleMailSender sms = MailSenderFactory.getSender(MailSenderType.SERVICE);
        try {
//			sms.send("18950189322@163.com",simpleMail);
//        	sms.sendAttachMail("18950189322@163.com", "D:\\maven\\settings.xml", simpleMail);
//        	sms.sendImageMail("18950189322@163.com", "src\\ic_launcher-web.png", simpleMail);
        	sms.sendMixedMail("18950189322@163.com", "src\\ic_launcher-web.png",  "D:\\maven\\settings.xml", simpleMail);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}

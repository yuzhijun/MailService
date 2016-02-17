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
		simpleMail.setSubject("���Ǹ������ʼ�");
//		simpleMail.setContent("���յ�����");
		simpleMail.setContent("����һ���ʼ����Ĵ�ͼƬ<img src='cid:xxx.jpg'>���ʼ�");
		// �����ʼ�
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

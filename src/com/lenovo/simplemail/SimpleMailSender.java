package com.lenovo.simplemail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.text.AbstractDocument.Content;

/**
 * �򵥵��ʼ����������ɵ�����Ⱥ��
 * 
 * @author yuzhijun
 * */
public class SimpleMailSender {

	/**
	 * �����ʼ���props�ļ�
	 */
	private final transient Properties props = System.getProperties();
	/**
	 * �ʼ���������¼��֤
	 */
	private transient MailAuthenticator authenticator;

	/**
	 * ����session
	 */
	private transient Session session;

	/**
	 * ��ʼ���ʼ�������
	 * 
	 * @param smtpHostName
	 *            SMTP�ʼ���������ַ
	 * @param username
	 *            �����ʼ����û���(��ַ)
	 * @param password
	 *            �����ʼ�������
	 */
	public SimpleMailSender(final String smtpHostName, final String username,
			final String password) {
		init(username, password, smtpHostName);
	}

	/**
	 * ��ʼ���ʼ�������
	 * 
	 * @param username
	 *            �����ʼ����û���(��ַ)�����Դ˽���SMTP��������ַ
	 * @param password
	 *            �����ʼ�������
	 */
	public SimpleMailSender(final String username, final String password) {
		// ͨ�������ַ������smtp���������Դ�������䶼����
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);

	}

	/**
	 * ��ʼ��
	 * 
	 * @param username
	 *            �����ʼ����û���(��ַ)
	 * @param password
	 *            ����
	 * @param smtpHostName
	 *            SMTP������ַ
	 */
	private void init(String username, String password, String smtpHostName) {
		// ��ʼ��props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		// ��֤
		authenticator = new MailAuthenticator(username, password);
		// ����session
		session = Session.getInstance(props, authenticator);
		session.setDebug(true);
	}

	/**
	 * �����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param subject
	 *            �ʼ�����
	 * @param content
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String subject, Object content)
			throws AddressException, MessagingException {
		// ����mime�����ʼ�
		final MimeMessage message = new MimeMessage(session);
		// ���÷�����
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ���
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// ��������
		message.setSubject(subject);
		// �����ʼ�����
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// ����
		Transport.send(message);
	}

	/**
	 * �����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * 
	 * @param recipientCC
	 *            �����������ַ
	 * @param subject
	 *            �ʼ�����
	 * @param content
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 * */
	public void send(String recipient, String recipientCC, String subject,
			Object content) throws AddressException, MessagingException {
		// ����mime�����ʼ�
		final MimeMessage message = new MimeMessage(session);
		// ���÷�����
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ���
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// ���ó�����
		message.setRecipient(RecipientType.CC, new InternetAddress(recipientCC));
		// ��������
		message.setSubject(subject);
		// �����ʼ�����
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// ����
		Transport.send(message);
	}

	/**
	 * �����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * 
	 * @param recipientCCs
	 *            �����������ַ
	 * @param subject
	 *            �ʼ�����
	 * @param content
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 * */
	public void send(String recipient, List<String> recipientCCs,
			String subject, Object content) throws AddressException,
			MessagingException {
		// ����mime�����ʼ�
		final MimeMessage message = new MimeMessage(session);
		// ���÷�����
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ���
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// ���ó�����
		final int num = recipientCCs.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipientCCs.get(i));
		}
		message.setRecipients(RecipientType.CC, addresses);
		// ��������
		message.setSubject(subject);
		// �����ʼ�����
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// ����
		Transport.send(message);
	}

	/**
	 * ���ʹ��������ʼ�
	 * 
	 * @param recipient
	 *            �ռ��˵������ַ
	 * @param attachAddress
	 *            �������ݵ�ַ
	 * @param subject
	 *            �ʼ�����
	 * @param content
	 *            �ʼ�����
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 * 
	 * */
	public void sendAttachMail(String recipient, String attachAddress,
			String subject, Object content) throws AddressException,
			MessagingException, UnsupportedEncodingException {
		// ����mime�����ʼ�
		final MimeMessage message = new MimeMessage(session);
		// ���÷�����
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ���
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// ��������
		message.setSubject(subject);
		// �����ʼ����ģ�Ϊ�˱����ʼ����������������⣬��Ҫʹ��charset=UTF-8ָ���ַ�����
		MimeBodyPart text = new MimeBodyPart();
		text.setContent(content.toString(), "text/html;charset=UTF-8");
		// �����ʼ�����
		MimeBodyPart attach = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(attachAddress));
		attach.setDataHandler(dh);
		attach.setFileName(MimeUtility.encodeText(dh.getName()));
		// ���������������ݹ�ϵ
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(text);
		mp.addBodyPart(attach);
		mp.setSubType("mixed");
		message.setContent(mp);

		Transport.send(message);
	}

	/**
	 * ���ʹ�ͼƬ���ʼ�
	 * 
	 * @param recipient
	 *            �ռ��˵������ַ
	 * @param attachAddress
	 *            ͼƬ��ַ
	 * @param subject
	 *            �ʼ�����
	 * @param content
	 *            �ʼ�����
	 * @throws MessagingException
	 * @throws AddressException
	 * 
	 * */
	public void sendImageMail(String recipient, String imageAddress,
			String subject, Object content) throws AddressException,
			MessagingException {
		// ����mime�����ʼ�
		final MimeMessage message = new MimeMessage(session);
		// ���÷�����
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ���
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// ��������
		message.setSubject(subject);
		// ׼���ʼ�����
		// ׼���ʼ���������
		MimeBodyPart text = new MimeBodyPart();
		text.setContent(content, "text/html;charset=UTF-8");
		// ׼��ͼƬ����
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(imageAddress));
		image.setDataHandler(dh);
		image.setContentID("xxx.jpg");
		// �������ݹ�ϵ
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(image);
		mm.setSubType("related");
		message.setContent(mm);

		Transport.send(message);
	}

	/**
	 * ���ʹ�������ͼƬ���ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param imageAddress
	 *            ͼƬ��ַ
	 * @param attachAddress
	 *            ������ַ
	 * @param subject
	 *            ����
	 * @param content
	 *            �ʼ�����
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 * 
	 * */
	public void sendMixedMail(String recipient, String imageAddress,
			String attachAddress, String subject, String content)
			throws AddressException, MessagingException,
			UnsupportedEncodingException {
		// ����mime�����ʼ�
		final MimeMessage message = new MimeMessage(session);
		// ���÷�����
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ���
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// ��������
		message.setSubject(subject);
		// ����
		MimeBodyPart text = new MimeBodyPart();
		text.setContent(content, "text/html;charset=UTF-8");

		// ͼƬ
		MimeBodyPart image = new MimeBodyPart();
		image.setDataHandler(new DataHandler(new FileDataSource(imageAddress)));
		image.setContentID("xxx.jpg");

		// ����
		MimeBodyPart attach = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(attachAddress));
		attach.setDataHandler(dh);
		attach.setFileName(MimeUtility.encodeText(dh.getName()));

		// ������ϵ:���ĺ�ͼƬ
		MimeMultipart mp1 = new MimeMultipart();
		mp1.addBodyPart(text);
		mp1.addBodyPart(image);
		mp1.setSubType("related");

		// ������ϵ:���ĺ͸���
		MimeMultipart mp2 = new MimeMultipart();
		mp2.addBodyPart(text);
		mp2.addBodyPart(attach);
		mp2.setSubType("mixed");

		// �������ĵ�bodypart
		MimeBodyPart mainContent = new MimeBodyPart();
		mainContent.setContent(mp1);
		mp2.addBodyPart(mainContent);
		mp2.setSubType("mixed");
		

		message.setContent(mp2);
		Transport.send(message);
	}

	/**
	 * Ⱥ���ʼ�
	 * 
	 * @param recipients
	 *            �ռ�����
	 * @param subject
	 *            ����
	 * @param content
	 *            ����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(List<String> recipients, String subject, Object content)
			throws AddressException, MessagingException {
		// ����mime�����ʼ�
		final MimeMessage message = new MimeMessage(session);
		// ���÷�����
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ�����
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients.get(i));
		}
		// �����ռ���
		message.setRecipients(RecipientType.TO, addresses);
		// ��������
		message.setSubject(subject);
		// �����ʼ�����
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// ����
		Transport.send(message);
	}

	/**
	 * �����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param mail
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, SimpleMail mail)
			throws AddressException, MessagingException {
		send(recipient, mail.getSubject(), mail.getContent());
	}

	/**
	 * �����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param recipientCC
	 *            �����������ַ
	 * @param mail
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String recipientCC, SimpleMail mail)
			throws AddressException, MessagingException {
		send(recipient, recipientCC, mail.getSubject(), mail.getContent());
	}

	/**
	 * �����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param recipientCCs
	 *            �����������ַ
	 * @param mail
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, List<String> recipientCCs,
			SimpleMail mail) throws AddressException, MessagingException {
		send(recipient, recipientCCs, mail.getSubject(), mail.getContent());
	}

	/**
	 * ���͸����ʼ�
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param attachAddress
	 *            ������ַ
	 * @param mail
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendAttachMail(String recipient, String attachAddress,
			SimpleMail mail) throws AddressException, MessagingException,
			UnsupportedEncodingException {
		sendAttachMail(recipient, attachAddress, mail.getSubject(),
				mail.getContent());
	}

	/**
	 * ���ʹ�ͼƬ���ʼ�
	 * 
	 * @param recipient
	 *            �ռ��˵������ַ
	 * @param attachAddress
	 *            ͼƬ��ַ
	 * @param mail
	 *            �ʼ�����
	 * @throws MessagingException
	 * @throws AddressException
	 * 
	 * */
	public void sendImageMail(String recipient, String imageAddress,
			SimpleMail mail) throws AddressException, MessagingException {
		sendImageMail(recipient, imageAddress, mail.getSubject(),
				mail.getContent());
	}
	
	/**
	 * ���ʹ�ͼƬ�͸������ʼ�
	 * 
	 * 
	 * @param recipient
	 *            �ռ��������ַ
	 * @param imageAddress
	 *            ͼƬ��ַ
	 * @param attachAddress
	 *            ������ַ
	 * @param mail
	 *            �ʼ�����
	 * @throws UnsupportedEncodingException 
	 * @throws MessagingException
	 * @throws AddressException
	 * 
	 * */
	public void sendMixedMail(String recipient, String imageAddress,
			String attachAddress,SimpleMail mail) throws AddressException, UnsupportedEncodingException, MessagingException{
		sendMixedMail(recipient, imageAddress, attachAddress, mail.getSubject(), mail.getContent());
	}

	/**
	 * Ⱥ���ʼ�
	 * 
	 * @param recipients
	 *            �ռ�����
	 * @param mail
	 *            �ʼ�����
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(List<String> recipients, SimpleMail mail)
			throws AddressException, MessagingException {
		send(recipients, mail.getSubject(), mail.getContent());
	}

}

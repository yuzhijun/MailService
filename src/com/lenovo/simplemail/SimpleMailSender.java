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
 * 简单的邮件发送器，可单发和群发
 * 
 * @author yuzhijun
 * */
public class SimpleMailSender {

	/**
	 * 发送邮件的props文件
	 */
	private final transient Properties props = System.getProperties();
	/**
	 * 邮件服务器登录验证
	 */
	private transient MailAuthenticator authenticator;

	/**
	 * 邮箱session
	 */
	private transient Session session;

	/**
	 * 初始化邮件发送器
	 * 
	 * @param smtpHostName
	 *            SMTP邮件服务器地址
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            发送邮件的密码
	 */
	public SimpleMailSender(final String smtpHostName, final String username,
			final String password) {
		init(username, password, smtpHostName);
	}

	/**
	 * 初始化邮件发送器
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)，并以此解析SMTP服务器地址
	 * @param password
	 *            发送邮件的密码
	 */
	public SimpleMailSender(final String username, final String password) {
		// 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);

	}

	/**
	 * 初始化
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            密码
	 * @param smtpHostName
	 *            SMTP主机地址
	 */
	private void init(String username, String password, String smtpHostName) {
		// 初始化props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		// 验证
		authenticator = new MailAuthenticator(username, password);
		// 创建session
		session = Session.getInstance(props, authenticator);
		session.setDebug(true);
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String subject, Object content)
			throws AddressException, MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * 
	 * @param recipientCC
	 *            抄送人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 * */
	public void send(String recipient, String recipientCC, String subject,
			Object content) throws AddressException, MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置抄送人
		message.setRecipient(RecipientType.CC, new InternetAddress(recipientCC));
		// 设置主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * 
	 * @param recipientCCs
	 *            抄送人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 * */
	public void send(String recipient, List<String> recipientCCs,
			String subject, Object content) throws AddressException,
			MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置抄送人
		final int num = recipientCCs.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipientCCs.get(i));
		}
		message.setRecipients(RecipientType.CC, addresses);
		// 设置主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}

	/**
	 * 发送带附件的邮件
	 * 
	 * @param recipient
	 *            收件人的邮箱地址
	 * @param attachAddress
	 *            附件内容地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 * 
	 * */
	public void sendAttachMail(String recipient, String attachAddress,
			String subject, Object content) throws AddressException,
			MessagingException, UnsupportedEncodingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置主题
		message.setSubject(subject);
		// 创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
		MimeBodyPart text = new MimeBodyPart();
		text.setContent(content.toString(), "text/html;charset=UTF-8");
		// 创建邮件附件
		MimeBodyPart attach = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(attachAddress));
		attach.setDataHandler(dh);
		attach.setFileName(MimeUtility.encodeText(dh.getName()));
		// 创建容器描述数据关系
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(text);
		mp.addBodyPart(attach);
		mp.setSubType("mixed");
		message.setContent(mp);

		Transport.send(message);
	}

	/**
	 * 发送带图片的邮件
	 * 
	 * @param recipient
	 *            收件人的邮箱地址
	 * @param attachAddress
	 *            图片地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws MessagingException
	 * @throws AddressException
	 * 
	 * */
	public void sendImageMail(String recipient, String imageAddress,
			String subject, Object content) throws AddressException,
			MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置主题
		message.setSubject(subject);
		// 准备邮件数据
		// 准备邮件正文数据
		MimeBodyPart text = new MimeBodyPart();
		text.setContent(content, "text/html;charset=UTF-8");
		// 准备图片数据
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(imageAddress));
		image.setDataHandler(dh);
		image.setContentID("xxx.jpg");
		// 描述数据关系
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(image);
		mm.setSubType("related");
		message.setContent(mm);

		Transport.send(message);
	}

	/**
	 * 发送带附件和图片的邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param imageAddress
	 *            图片地址
	 * @param attachAddress
	 *            附件地址
	 * @param subject
	 *            主题
	 * @param content
	 *            邮件正文
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 * 
	 * */
	public void sendMixedMail(String recipient, String imageAddress,
			String attachAddress, String subject, String content)
			throws AddressException, MessagingException,
			UnsupportedEncodingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置主题
		message.setSubject(subject);
		// 正文
		MimeBodyPart text = new MimeBodyPart();
		text.setContent(content, "text/html;charset=UTF-8");

		// 图片
		MimeBodyPart image = new MimeBodyPart();
		image.setDataHandler(new DataHandler(new FileDataSource(imageAddress)));
		image.setContentID("xxx.jpg");

		// 附件
		MimeBodyPart attach = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(attachAddress));
		attach.setDataHandler(dh);
		attach.setFileName(MimeUtility.encodeText(dh.getName()));

		// 描述关系:正文和图片
		MimeMultipart mp1 = new MimeMultipart();
		mp1.addBodyPart(text);
		mp1.addBodyPart(image);
		mp1.setSubType("related");

		// 描述关系:正文和附件
		MimeMultipart mp2 = new MimeMultipart();
		mp2.addBodyPart(text);
		mp2.addBodyPart(attach);
		mp2.setSubType("mixed");

		// 代表正文的bodypart
		MimeBodyPart mainContent = new MimeBodyPart();
		mainContent.setContent(mp1);
		mp2.addBodyPart(mainContent);
		mp2.setSubType("mixed");
		

		message.setContent(mp2);
		Transport.send(message);
	}

	/**
	 * 群发邮件
	 * 
	 * @param recipients
	 *            收件人们
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(List<String> recipients, String subject, Object content)
			throws AddressException, MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人们
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients.get(i));
		}
		// 设置收件人
		message.setRecipients(RecipientType.TO, addresses);
		// 设置主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param mail
	 *            邮件对象
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, SimpleMail mail)
			throws AddressException, MessagingException {
		send(recipient, mail.getSubject(), mail.getContent());
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param recipientCC
	 *            抄送人邮箱地址
	 * @param mail
	 *            邮件对象
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String recipientCC, SimpleMail mail)
			throws AddressException, MessagingException {
		send(recipient, recipientCC, mail.getSubject(), mail.getContent());
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param recipientCCs
	 *            抄送人邮箱地址
	 * @param mail
	 *            邮件对象
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, List<String> recipientCCs,
			SimpleMail mail) throws AddressException, MessagingException {
		send(recipient, recipientCCs, mail.getSubject(), mail.getContent());
	}

	/**
	 * 发送附件邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param attachAddress
	 *            附件地址
	 * @param mail
	 *            邮件对象
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
	 * 发送带图片的邮件
	 * 
	 * @param recipient
	 *            收件人的邮箱地址
	 * @param attachAddress
	 *            图片地址
	 * @param mail
	 *            邮件对象
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
	 * 发送带图片和附件的邮件
	 * 
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param imageAddress
	 *            图片地址
	 * @param attachAddress
	 *            附件地址
	 * @param mail
	 *            邮件对象
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
	 * 群发邮件
	 * 
	 * @param recipients
	 *            收件人们
	 * @param mail
	 *            邮件对象
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(List<String> recipients, SimpleMail mail)
			throws AddressException, MessagingException {
		send(recipients, mail.getSubject(), mail.getContent());
	}

}

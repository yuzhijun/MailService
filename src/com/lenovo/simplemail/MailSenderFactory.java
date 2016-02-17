package com.lenovo.simplemail;

public class MailSenderFactory {

	/**
	 * ·þÎñÓÊÏä
	 */
	private static SimpleMailSender serviceSms = null;

	/**
	 * »ñÈ¡ÓÊÏä
	 * 
	 * @param type
	 *            ÓÊÏäÀàÐÍ
	 * @return ·ûºÏÀàÐÍµÄÓÊÏä
	 */
	public static SimpleMailSender getSender(MailSenderType type) {
		if (type == MailSenderType.SERVICE) {
			if (serviceSms == null) {
				serviceSms = new SimpleMailSender("自己的邮箱", "自己的邮箱密码");
			}
			return serviceSms;
		}
		return null;
	}
}

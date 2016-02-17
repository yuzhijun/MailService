package com.lenovo.simplemail;

public class MailSenderFactory {

	/**
	 * ��������
	 */
	private static SimpleMailSender serviceSms = null;

	/**
	 * ��ȡ����
	 * 
	 * @param type
	 *            ��������
	 * @return �������͵�����
	 */
	public static SimpleMailSender getSender(MailSenderType type) {
		if (type == MailSenderType.SERVICE) {
			if (serviceSms == null) {
				serviceSms = new SimpleMailSender("664281688@qq.com", "850663zzzjmf");
			}
			return serviceSms;
		}
		return null;
	}
}

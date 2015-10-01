package com.app.showpledge.shared;

public class UserVerificationUtil {
	
	public static String getVerificationCode(Long inUserId) {
		return String.valueOf(inUserId);
	}
	
	public static Long getUserIdFromCode(String inCode) {
		return new Long(inCode);
	}

}

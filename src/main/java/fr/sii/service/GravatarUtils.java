package fr.sii.service.auth;


import fr.sii.service.auth.MD5Utils;

import java.security.MessageDigest;

public final class GravatarUtils {

	public static String getImageURL(String email) {
		String hash = MD5Utils.md5Hex(email);

		return "http://www.gravatar.com/avatar/".concat(hash).concat("?s=250");
	}

}
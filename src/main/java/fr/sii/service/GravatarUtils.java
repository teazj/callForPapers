package fr.sii.service;


import fr.sii.service.auth.MD5Utils;

public final class GravatarUtils {

	public static String getImageURL(String email) {
		String hash = MD5Utils.md5Hex(email);

		return "https://www.gravatar.com/avatar/".concat(hash).concat("?s=250");
	}

}

package common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public class HelloMvcUtils {

	/**
	 * SHA256 | SHA512 (SHA1 또는 MD5는 사용하지 말 것)
	 * @param salt 
	 * 
	 */
	public static String encrypt(String password, String salt) {
		// 1. 암호화 Hashing
		MessageDigest md = null;
		byte[] encrypted = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] input = password.getBytes("utf-8");
			byte[] saltBytes = salt.getBytes("utf-8");
			md.update(saltBytes); // salt값으로 MessageDigest 객체 갱신 
			encrypted = md.digest(input); // MessageDigest 객체에 raw password 전달
		} catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// 2. 인코딩 (단순문자변환)
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(encrypted);
	}
	/**
	 * 
	 * @param cPage
	 * @param numPerPage
	 * @param totalContents
	 * @param url /mvc/admin/memberList
	 * @return
	 */
	public static String getPagebar(int cPage, int numPerPage, int totalContents, String url) {
		StringBuilder pagebar = new StringBuilder();
		int totalPages = (int) Math.ceil((double) totalContents / numPerPage); // 전체페이지 수
		int pagebarSize = 5;
		int pagebarStart = ((cPage - 1) / pagebarSize) * pagebarSize + 1;
		int pagebarEnd = pagebarStart + pagebarSize - 1;
		int pageNo = pagebarStart;

		url += "?cPage=";
		
		// 이전 prev
		if(pageNo == 1) {
			// prev버튼 비활성화
		} else {
			// prev버튼 활성화
			pagebar.append("<a href='" + url + (pageNo - 1) + "'>prev</a>");
		}
		
		// 번호
		while(pageNo <= pagebarEnd && pageNo <= totalPages) {
			if(pageNo == cPage) {
				// 현재 페이지인 경우
				pagebar.append("<span class='cPage'>" + pageNo + "</span>");
				pagebar.append("\n");
			} else {
				// 현재 페이지가 아닌 경우(링크 필요)
				pagebar.append("<a href='" + url + pageNo + "'>" + pageNo + "</a>");
			}
			pageNo++;
		}
		
		// 다음 next
		if(pageNo > totalPages) {
			
		} else {
			pagebar.append("<a href='" + url + pageNo + "'>next</a>");
			pagebar.append("\n");
			
		}
		return pagebar.toString();
	}

}

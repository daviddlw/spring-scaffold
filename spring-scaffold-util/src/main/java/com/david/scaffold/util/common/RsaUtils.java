package com.david.scaffold.util.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.david.scaffold.util.constants.Constants;

/**
 * daviddai
 */
public class RsaUtils {

	private static final Logger logger = LoggerFactory.getLogger(RsaUtils.class);

	private static final String CHARSET = "UTF-8";

	private static final String algorithm = "SHA256withRSA";

	/**
	 * 网联请求报文签名
	 * 
	 * @param privateKey
	 *            机构私钥字符串
	 * @param content
	 *            签名原文
	 * @return 签名密文
	 * @throws Exception
	 */
	public static String sign(String privateKey, String content) throws Exception {
		Signature signature = Signature.getInstance(algorithm);
		signature.initSign(convertPrivateKey(privateKey));
		signature.update(content.getBytes(CHARSET));
		return Base64.encodeBase64String(signature.sign());
	}

	/**
	 * 网联返回报文验签
	 * 
	 * @param publicKey
	 *            网联公钥字符串
	 * @param content
	 *            验签原文报文
	 * @param signStr
	 *            网联返回签名字符串
	 * @return 验签结果
	 * @throws Exception
	 */
	public static boolean vertify(String publicKey, String content, String signStr) throws Exception {
		Signature signature = Signature.getInstance(algorithm);
		signature.initVerify(convertPublicKey(publicKey));
		// 防止待验签的报文里面存有换行符而影响验签结果
		content = content.replaceAll("\r\n", "");
		signature.update(content.getBytes(CHARSET));
		return signature.verify(Base64.decodeBase64(signStr.getBytes(CHARSET)));
	}

	/**
	 * 对称密钥公钥加密
	 * 
	 * @param publicKey
	 *            网联公钥字符串
	 * @param content
	 *            密钥原文
	 * @return 加密密文
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String publicKey, String content) throws Exception {
		String result = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, convertPublicKey(publicKey));
			byte[] encoded = cipher.doFinal(content.getBytes(CHARSET));
			result = Base64.encodeBase64String(encoded);
		} catch (Exception e) {
			logger.error("encryptByPublicKey error:", e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 对称密钥密文解密
	 * 
	 * @param privateKey
	 *            机构私钥字符串
	 * @param content
	 *            网联对称密钥密文
	 * @return 对称密钥明文
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String privateKey, String content) throws Exception {
		String result = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, convertPrivateKey(privateKey));
			byte[] encoded = cipher.doFinal(Base64.decodeBase64(content.getBytes(CHARSET)));
			result = new String(encoded, CHARSET);
		} catch (Exception e) {
			logger.error("decryptByPrivateKey error:", e);
			e.printStackTrace();
		}
		return result;
	}

	public static PrivateKey convertPrivateKey(String keyStr) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(keyStr.getBytes(CHARSET)));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	public static PublicKey convertPublicKey(String keyStr) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decodeBase64(keyStr);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		return pubKey;
	}

	/**
	 * 将公钥文件转换成rsa公钥文本
	 * @param certPath 公钥文件路径
	 * @return 公钥字符串
	 */
	public static String convertCertFileToRsaPublicKey(String certPath) {
		String result = "";
		try (FileInputStream fileInputStream = new FileInputStream(certPath)) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
			X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
			result = Base64.encodeBase64String(x509Certificate.getPublicKey().getEncoded());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
		return result;
	}

	public static String encryptByPublicKey(String src, PublicKey publicKey) {
		byte[] destBytes = rsaByPublicKey(src.getBytes(Charset.forName(Constants.UTF_8)), publicKey, 1);
		if (destBytes == null) {
			return null;
		}
		return Base64.encodeBase64String(destBytes);
	}

	public static PublicKey getPublicKeyFromFile(String pubCerPath) {
		FileInputStream pubKeyStream = null;
		try {
			pubKeyStream = new FileInputStream(pubCerPath);
			int length = pubKeyStream.available();
			byte[] reads = new byte[length];
			pubKeyStream.read(reads, 0, length);
			return getPublicKeyByText(new String(reads, Charset.forName(Constants.UTF_8)));
		} catch (FileNotFoundException e) {
			logger.error("publicKey does not exist:", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("read privateKey file failed:", e);
			e.printStackTrace();
		} finally {
			if (pubKeyStream != null) {
				try {
					pubKeyStream.close();
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	public static PublicKey getPublicKeyByText(String pubKeyText) {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");

			BufferedReader br = new BufferedReader(new StringReader(pubKeyText));
			String line = null;
			StringBuilder keyBuffer = new StringBuilder();
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("-")) {
					keyBuffer.append(line);
				}
			}
			Base64 base64 = new Base64();
			Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(base64.decode(keyBuffer.toString())));

			return certificate.getPublicKey();
		} catch (Exception e) {
			logger.error("analysis publicKey content failed:", e);
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] rsaByPublicKey(byte[] srcData, PublicKey publicKey, int mode) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(mode, publicKey);

			int blockSize = mode == 1 ? 117 : 128;
			byte[] encryptedData = null;
			for (int i = 0; i < srcData.length; i += blockSize) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(srcData, i, i + blockSize));
				encryptedData = ArrayUtils.addAll(encryptedData, doFinal);
			}
			return encryptedData;
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException:", e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error("NoSuchPaddingException:", e);
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			logger.error("IllegalBlockSizeException:", e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			logger.error("BadPaddingException:", e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			logger.error("InvalidKeyException:", e);
			e.printStackTrace();
		}
		return null;
	}

}

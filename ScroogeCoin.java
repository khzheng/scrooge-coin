import java.security.*;
import java.math.BigInteger;

public class ScroogeCoin {
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
		// crypto setup
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(1024, random);
		
		// generate a keypair for Scrooge
		KeyPair pair = keyGen.generateKeyPair();
		PrivateKey privateKeyScrooge = pair.getPrivate();
		PublicKey publicKeyScrooge = pair.getPublic();
		
		// generate a keypair for Alice
		pair = keyGen.generateKeyPair();
		PrivateKey privateKeyAlice = pair.getPrivate();
		PublicKey publicKeyAlice = pair.getPublic();
		
		// System.out.println("Scrooge private: " + privateKeyScrooge + " public: " + publicKeyScrooge);
		// System.out.println("Alice private: " + privateKeyAlice + " public: " + publicKeyAlice);
		
		// create root transaction so that Scrooge owns a coin
		Transaction tx = new Transaction();
		tx.addOutput(5, publicKeyScrooge);
		
		// just create an arbritrary initial hash value 
		byte[] initialHash = BigInteger.valueOf(543754544).toByteArray();
		tx.addInput(initialHash, 0);
		
		// have Scrooge sign root transaction
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKeyScrooge);
		signature.update(tx.getRawDataToSign(0));
 		byte[] sig = signature.sign();
		tx.addSignature(sig, 0);
		
		// boolean verified = Crypto.verifySignature(publicKeyScrooge, tx.getRawDataToSign(0), sig);
	}
}
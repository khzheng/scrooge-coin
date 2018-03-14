import java.security.*;

public class ScroogeCoin {
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
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
	}
}
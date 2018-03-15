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
		
		// create root transaction so that Scrooge owns a coin
		int initialCoinValue = 5;
		byte[] initialHash = BigInteger.valueOf(543754544).toByteArray();	// just create an arbritrary initial hash value 
		Transaction rootTx = new Transaction();
		rootTx.addInput(initialHash, 0);
		rootTx.addOutput(initialCoinValue, publicKeyScrooge);
		
		// have Scrooge sign root transaction
		Signature signature = Signature.getInstance("SHA256withRSA");
 		byte[] sig = signMessage(signature, rootTx.getRawDataToSign(0), privateKeyScrooge);
		rootTx.addSignature(sig, 0);
		rootTx.finalize();
		
//		 System.out.println("rootTx verified? " + Crypto.verifySignature(publicKeyScrooge, rootTx.getRawDataToSign(0), sig));
		
		// root transaction for Scrooge is unspent so add it to the UTXOPool
		UTXO utxo = new UTXO(rootTx.getHash(), 0);
		UTXOPool utxoPool = new UTXOPool();
		utxoPool.addUTXO(utxo, rootTx.getOutput(0));
		
		// split the coin of value 5 and send to Alice
		Transaction tx = new Transaction();
		tx.addInput(rootTx.getHash(), 0);	// index 0 has value of 5
		tx.addOutput(2, publicKeyAlice);
		tx.addOutput(3, publicKeyAlice);

		// Scrooge needs to sign it since he's giving coins to Alice
		sig = signMessage(signature, tx.getRawDataToSign(0), privateKeyScrooge);
		tx.addSignature(sig, 0);
		tx.finalize();

		// System.out.println("tx verified? " + Crypto.verifySignature(publicKeyScrooge, tx.getRawDataToSign(0), sig));
		TxHandler txHandler = new TxHandler(utxoPool);
		System.out.println("tx valild? " + txHandler.isValidTx(tx));
	}
	
	public static byte[] signMessage(Signature signature, byte[] message, PrivateKey privateKey) throws InvalidKeyException, SignatureException {
		signature.initSign(privateKey);
		signature.update(message);
		return signature.sign();
	}
}
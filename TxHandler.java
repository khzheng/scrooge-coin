import java.util.HashSet;

public class TxHandler {

    private UTXOPool pool;
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        pool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */

    public boolean isValidTx(Transaction tx) {
        if (tx == null)
            return false;

        boolean isValid = true;
        HashSet<UTXO> usedUtxos = new HashSet<>();
        int sumInputs = 0;
        int sumOutputs = 0;

        // verify inputs
        for (int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input input = tx.getInput(i);
            if (input == null) {
                isValid = false;
                break;
            }

            // (1) coin in input must be unspent
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            if (!pool.contains(utxo) || usedUtxos.contains(utxo)) {
                isValid = false;
                break;
            }

            // (2) verify input signature
            Transaction.Output output = pool.getTxOutput(utxo); // get prevTx output
            if (!Crypto.verifySignature(output.address, tx.getRawDataToSign(i), input.signature)) {
                isValid = false;
                break;
            }

            // (3) prevent double spend by adding utxo to a used set
            usedUtxos.add(utxo);

            // sum up the inputs values
            sumInputs += output.value;
        }

        for (int j = 0; j < tx.numOutputs(); j++) {
            Transaction.Output output = tx.getOutput(j);

            // (4) output values should be non-negative
            if (output.value < 0) {
                isValid = false;
                break;
            }

            sumOutputs += output.value;
        }

        // (5) sum of input values must be >= sum of output values
        if (sumOutputs > sumInputs)
            isValid = false;

		return isValid;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
		return possibleTxs;
    }

}

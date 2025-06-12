package app.apollo.server;

import app.apollo.common.Block;

/***
 * BlockValidator is a interface refering to block validation strategy.
 * Its responsibility is to check if block holds proper data.
 *
*/
public interface BlockValidator {

    /***
     *  Method checks if checksum and fingerprint is valid for given data.
     * @param block Block that needs validation
     * @return Status of block validation
     */
    public boolean isBlockValid(Block block);
}

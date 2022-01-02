/*******************************************************************************
 *  This file is made by Probal D. Saikia on 3/1/2022
 *  https://github.com/Master-COLLiDER
 *  NOTICE: This file is subject to the terms and conditions defined
 * in the file 'LICENSE' which is part of this source code package.
 ******************************************************************************/

package com.mastercollider.stegofierfx.Encryption;

public enum EncryptionType {
    NOT_ENCRYPTED, AES256, TRIPLEDES, RSA;

    public static EncryptionType getEncryptionType( int  num)
    {
        if (num == 1) {
            return AES256;
        }else if (num == 2){
            return TRIPLEDES;
        }else if (num == 3){
            return RSA;
        }
        else{
            return NOT_ENCRYPTED;
        }
    }

    public static int getEncryptionType(EncryptionType type)
    {
        if (type == AES256) {
            return 1;
        }else if (type == TRIPLEDES){
            return 2;
        }else if (type == RSA){
            return 3;
        }
        else {
            return 0;
        }
    }

}

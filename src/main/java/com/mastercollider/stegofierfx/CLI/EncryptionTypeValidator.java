package com.mastercollider.stegofierfx.CLI;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class EncryptionTypeValidator implements IParameterValidator {
    public void validate(String name, String value)
            throws ParameterException {
        int n = Integer.parseInt(value);
        if (n < 0 || n > 3) {
            throw new ParameterException("Parameter " + name + " should be between (0 - 3) (found " + value +")");
        }
    }
}

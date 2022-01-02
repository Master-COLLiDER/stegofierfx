package com.mastercollider.stegofierfx.CLI;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class LSBValidator implements IParameterValidator {
    public void validate(String name, String value)
            throws ParameterException {
        int n = Integer.parseInt(value);
        if (n < 0 || n > 7) {
            throw new ParameterException("Parameter " + name + " should be between (0 - 7) (found " + value +")");
        }
    }
}

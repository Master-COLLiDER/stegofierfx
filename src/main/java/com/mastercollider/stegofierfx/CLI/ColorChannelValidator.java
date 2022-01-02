package com.mastercollider.stegofierfx.CLI;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ColorChannelValidator implements IParameterValidator {
    public void validate(String name, String value)
            throws ParameterException {
        int n = Integer.parseInt(value);
        if (n < 1 || n > 7) {
            throw new ParameterException("Parameter " + name + " should be between (1 - 7) (found " + value +")");
        }
    }
}

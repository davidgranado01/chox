/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.xmlValidation.xmlInterface;

import chox.xmlValidation.result.ParseResult;
import java.io.File;

/**
 *
 * @author Carlson
 */
public interface fileValidationInterface {
    String getRuleId();
    ParseResult validate(File file, String fileName, ParseResult parseResult);
}

package io.github.yamf.remark;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Parser for input tables.
 * @author jens dietrich
 */
public interface InputTableParser {
    InputTable parse(Path file) throws IOException;
}

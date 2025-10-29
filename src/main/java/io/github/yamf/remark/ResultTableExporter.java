package io.github.yamf.remark;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Abstraction for exporting result tables.
 * @author jens dietrich
 */
public interface ResultTableExporter {
    void export(ResultTable table, Path file) throws IOException;
}

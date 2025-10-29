package io.github.yamf.remark;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A (input data) table.
 * @author jens dietrich
 */
public interface Table {

    List<String> getHeaders();
    List<Row> getRows();

    interface Cell {
        List<String> getValues();
        default Set<String> getValuesAsSet() {
            Set<String> values = new HashSet<>();
            values.addAll(getValues());
            return Collections.unmodifiableSet(values);
        }
    }

    interface Row {
        List<Cell> getCells();
    }


}

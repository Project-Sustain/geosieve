package sustain.geosieve.druid.geosievetransform;

import org.apache.druid.data.input.Row;
import org.apache.druid.segment.transform.RowFunction;

public class BloomLookupRowFunction implements RowFunction {
    public Object eval(final Row row) {
        return "new value!!";
    }
}

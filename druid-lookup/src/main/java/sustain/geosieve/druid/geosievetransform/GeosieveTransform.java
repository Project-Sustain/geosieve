package sustain.geosieve.druid.geosievetransform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.druid.data.input.Row;
import org.apache.druid.guice.annotations.Json;
import org.apache.druid.segment.transform.RowFunction;
import org.apache.druid.segment.transform.Transform;

import java.util.ArrayList;
import java.util.List;

public class GeosieveTransform implements Transform {
    private final String name;
    private final String expression;

    @JsonCreator
    public GeosieveTransform(@JsonProperty("name") final String name,
                             @JsonProperty("expression") final String expression) {
        this.name = name;
        this.expression = expression;
    }

    @JsonProperty
    @Override
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getExpression() {
        return expression;
    }

    @Override
    public RowFunction getRowFunction() {
        return new BloomLookupRowFunction();
    }

    // Obviously, this is just proof-of-concept testing at the moment...
    static class BloomLookupRowFunction implements RowFunction {
        @Override
        public Object eval(final Row row) {
            return 5;
        }
    }
}

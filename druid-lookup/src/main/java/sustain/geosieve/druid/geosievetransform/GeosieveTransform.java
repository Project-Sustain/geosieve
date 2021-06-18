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
    private final String target;

    @JsonCreator
    public GeosieveTransform(@JsonProperty("name") final String name,
                             @JsonProperty("target") final String target) {
        this.name = name;
        this.target = target;
    }

    @JsonProperty
    @Override
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getTarget() {
        return target;
    }

    @Override
    public RowFunction getRowFunction() {
        return new BloomLookupRowFunction();
    }
}

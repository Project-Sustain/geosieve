package sustain.geosieve.create;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.*;

// Horrible disgusting low-level class
public class JsonExtractor {
    private final JsonParser parser;

    public JsonExtractor(JsonParser parser) {
        this.parser = parser;
    }

    public Map<String, Object> getFromNextObject(String... fields) {
        boolean moreObjects = findNextObject();
        if (!moreObjects) {
            throw new NoSuchElementException();
        }

        Map<String, Object> props = new HashMap<>();

        synchronized (parser) {
            while (advanceToNextField()) {
                for (String field : fields) {
                    if (addIfNeeded(props, field)) {
                        break;
                    }
                }
            }
        }

        return props;
    }

    private boolean addIfNeeded(Map<String, Object> props, String field) {
        try {
            if (parser.getCurrentToken() != null && parser.getCurrentName().equals(field)) {
                parser.nextToken();
                props.put(field, getCurrentValueAsObject());
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getCurrentValueAsObject() {
        try {
            switch (parser.getCurrentToken()) {
                case VALUE_NUMBER_FLOAT: {
                    return parser.getDoubleValue();
                } case VALUE_NUMBER_INT: {
                    return parser.getIntValue();
                } case VALUE_FALSE: {
                    return false;
                } case VALUE_TRUE: {
                    return true;
                } default: case VALUE_STRING: {
                    return parser.getValueAsString();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean advanceToNextField() {
        try {
            while (parser.getCurrentToken() != null) {
                JsonToken next = parser.nextToken();
                switch (next) {
                    case END_ARRAY:
                    case END_OBJECT: {
                        return false;
                    } case FIELD_NAME: {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean findNextObject() {
        try {
            synchronized (parser) {
                while (parser.currentToken() != JsonToken.START_OBJECT) {
                    if (parser.nextToken() == null) {
                        return false;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

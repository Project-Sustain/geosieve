package sustain.geosieve.create.datasource;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.*;

// Horrible disgusting low-level class
public class JsonExtractor {
    private final JsonParser parser;
    private boolean needsNextObject = true;
    private boolean atEnd = false;

    public JsonExtractor(JsonParser parser) {
        this.parser = parser;
    }

    public synchronized Map<String, Object> getFromNextObject(String... fields) {
        getNextObjectIfNeeded();

        Map<String, Object> props = new HashMap<>();

        while (advanceToNextField()) {
            for (String field : fields) {
                if (addIfNeeded(props, field)) {
                    break;
                }
            }
        }

        return props;
    }

    private void getNextObjectIfNeeded() {
        if (needsNextObject) {
            boolean moreObjects = findNextObject();
            if (!moreObjects) {
                throw new NoSuchElementException();
            }
        } else if (atEnd) {
            throw new NoSuchElementException();
        }
        needsNextObject = true;
    }

    public synchronized boolean moreObjectsExist() {
        if (needsNextObject) {
            needsNextObject = false;
            boolean more = findNextObject();
            if (!more) {
                atEnd = true;
            }
            return more;
        } else {
            return !atEnd;
        }
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
                if (next == null) {
                    return false;
                }
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
            while (parser.currentToken() != JsonToken.START_OBJECT) {
                if (parser.nextToken() == null) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package common;

import common.Network.SimpleField;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by howor on 28.01.2017.
 */
public class Utils {
    public static List<SimpleField> convertToSimpleFields(List<Field> fields) {
        return fields.stream().map( p ->new SimpleField(p.getX(),p.getY())).collect(Collectors.toList());
    }
}

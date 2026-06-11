package POC.statemanagement;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.time.OffsetDateTime;

public class OffsetDateTimeRepresenter extends Representer {

    public OffsetDateTimeRepresenter(DumperOptions dumperOptions){
        super(dumperOptions);
        this.representers.put(OffsetDateTime.class, new RepresenterOffsetDateTime());
    }

    private class RepresentOffsetDateTime implements Representer{
        @Override
        public Node representData(Object data){
            OffsetDateTime dateTime = (OffsetDateTime) data;
            String value = dateTime.toString();
            return representScalar(new Tag("!OffsetDateTime"), value);
        }
    }
}

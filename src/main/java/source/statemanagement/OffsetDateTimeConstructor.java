    package POC.statemanagement;

    import org.apache.logging.log4j.Logger;
    import org.yaml.snakeyaml.LoaderOptions;
    import org.yaml.snakeyaml.constructor.Constructor;
    import org.yaml.snakeyaml.nodes.ScalarNode;

    import java.time.OffsetDateTime;

    public class OffsetDateTimeConstructor extends Constructor {
        private static final Logger log = LogManager.getLogger(OffsetDateTimeConstructor.class);
        public OffsetDateTimeConstructor(Class<?> theRoot, LoaderOptions loaderConfig){
            super(theRoot, loaderConfig);
            this.yamlConstructors.put(new Tag("!OffsetDateTime"), new ConstructorOffsetDateTime());
        }

        private class ConstructorOffsetDateTime extends ConstructScalar{
            @Override
            public Object construct(Node node){
                String value = ((ScalarNode) node).getValue();
                log.info(value);
                try{
                    return OffsetDateTime.parse(value);
                }catch (Exception e){
                    return null;
                }
            }
        }
    }

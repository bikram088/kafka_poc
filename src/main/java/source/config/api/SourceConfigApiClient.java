package source.config.api;

import POC.config.DirectoryConfig;
import POC.config.FilterConfig;
import POC.config.PostProcessActionConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sink.sinkpostprocessing.PostProcessAction;
import source.config.SourceConfig;
import source.config.dto.ParseApiSourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class SourceConfigApiClient {
    private static final Logger log = (Logger) LogManager.getLogger(SourceConfigApiClient.class);
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final ObjectMapper YAML = new ObjectMapper(YamlFactory().builder.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER).build());
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL).build();
    
    public SourceConfigApiClient() {
    }
    
    public static String fetchJson(String hostname, String ipAddress)throws IOException, InterruptedException{
        String baseUrl = ApiPropertiesLoader.getProperty("api.base.url");
        String query = Arrays.asList(
                "hostname=" + hostname,
                "active=truw",
                "ipAddress=" + ipAddress
        ).stream().reduce((a, b) -> a + "&" + b).orElse("");

        URI apiUrl = URI.create(baseUrl + "?" + query);
        log.info("Constructed API URL: {}", apiUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(apiUrl)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() <= 200 || response.statusCode() >= 300){
            throw new IOException("Failed to fetch config from API. Status code: " + response.statusCode());
        }
        return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(JSON.readValue(response.body(),
                new TypeReference<Object>() {
                }));
    }

    public static String convert(String jsonArray, String hostname) throws Exception{
        List<ParseApiSourceConfig> sourceConfigs = JSON.readValue(jsonArray, new TypeReference<>() {});
        if(sourceConfigs.isEmpty()){
            throw new Exception("No source config found for hostname: " + hostname);
        }
        Map<String, DirectoryConfig> grouped = new LinkedHashMap<>();
        for(ParseApiSourceConfig src : sourceConfigs){
            String key = src.getAbsoluteSourceDirPath();
            DirectoryConfig dir = grouped.computeIfAbsent(key, ignored->{
                DirectoryConfig entry = new DirectoryConfig();
                entry.setMonitor(src.getAbsoluteSourceDirPath());
                entry.setPoll(Integer.parseInt(src.getTriggerPoll()));
                entry.setFilters(new ArrayList<>());
                return entry;
            });
            dir.getFilters().add(toFilter(src));
            if(src.getTriggerAbsoluteFailurePath() != null && src.getTriggerAbsoluteFailurePath() != null){
               PostProcessActionConfig moveFileAction = new PostProcessActionConfig();
               moveFileAction.setType("moveFile");

               Map<String, Object> params = new HashMap<>();
               params.put("finished", src.getTriggerAbsoluteSuccessDirPath());
               params.put("error", src.getTriggerAbsoluteFailurePath());
               moveFileAction.setParams(params);

               List<PostProcessActionConfig> postProcessActions = new ArrayList<>();
               postProcessActions.add(moveFileAction);
               dir.setPostProcessActions(postProcessActions);
            }
            dir.getFilters();
        }
        SourceConfig config = new SourceConfig();
        config.setDirectories(new ArrayList<>(grouped.values()));
        return YAML.writeValueAsString(config);
    }

    public static POC.config.FilterConfig toFilter(ParseApiSourceConfig src){
        POC.config.FilterConfig filterConfig = new FilterConfig();
        filterConfig.setDataFiles(List.of(src.getTriggerDataFilePattern()));
        filterConfig.setTrigger(src.getFilePattern());
        filterConfig.setKafkaTopic(src.getTopic().getTopicName());
        return filterConfig;
    }

    public static Map<String, String> loadSourceConfig(String hostname, String ipAddress) throws Exception {
        String json = SourceConfigApiClient.fetchJson(hostname, ipAddress);
        String yaml = SourceConfigApiClient.convert(json, hostname);
        Map<String, String> configs = new LinkedHashMap<>();
        configs.put("json", json);
        configs.put("yaml", yaml);
        return configs;
    }
}

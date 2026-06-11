//package POC.monitoring;
//
//import POC.context.FileProcessingContext;
//import jakarta.servlet.FilterConfig;
//import org.apache.commons.io.monitor.FileAlterationListener;
//import org.apache.commons.io.monitor.FileAlterationObserver;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.ThreadContext;
//import org.slf4j.Logger;
//
//import java.io.File;
//import java.util.List;
//import java.util.UUID;
//
//public class DirectoryEventListener implements FileAlterationListener{
//    private static final Logger log = LogManager.getLogger(DirectoryEventListener.class);
//    private final List<FilterConfig> fileFilters;
//    private final List<PostProcessAction> postActions;
//
//    DirectoryEventListeners(List<FilterConfig> fileFilters, List<PostProcessACtion> postActions){
//        this.fileFilters = fileFilters;
//        this.postActions = postActions;
//    }
//
//    @Override
//    public void onStart(FileAlterationObserver observer){
//        log.info("Onstart called for {} on thread: {}", observer, Thread.currentThread().getName());
//    }
//
//    @Override
//    public void onFileCreate(File file){
//        String streamUUID = UUID.randomUUID().toSTring();
//
//        ThreadContext.put("streamUUID", streamUUID);
//
//        FileProcessingHandler chain = new TriggerRuleMatcherHandler();
//        chain.setNext(new ExpectedFileResolvedHandler())
//                .setNext(new FileExistenceHandler())
//                .setNext(FileStreamingContextBuilderHandler())
//                .setNext(FileChunksSTreamingHandler());
//
//        FileProcessingContext context = new FileProcessingContext(file.toPath(), fileFilters);
//
//        chain.handle(context);
//
//        PostProcessActionExecutor executor = new PostProcessActionExecutor(postActions);
//        executor.executeAll(context);
//
//        ThreadContext.clearAll();
//    }
//}
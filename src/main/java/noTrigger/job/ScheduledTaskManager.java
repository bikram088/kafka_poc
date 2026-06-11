package noTrigger.job;

import noTrigger.config.DirectoryScanConfig;
import noTrigger.core.StreamNewOrModifiedFilesUseCase;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ScheduledTaskManager {
    private final Logger log = LogManager.getLogger(ScheduledTaskManager.class);
    private final ScheduledExecutiveService scheduler;

    private final StreamNewOrModifiedFilesUseCase useCase;
    private final RunnableTaskDefinition runnableTaskDefinition;

    private final List<DirectoryScanConfig> scanDefinitions = new CopyOnWriteArrayList<>();

    private final List<ScheduledFuture<?>> activeFuture = new CopyOnWriteArrayList<>();

    public ScheduledTaskManager(int threadPoolSize, StreamNewOrModifiedFilesUseCase useCase, RunnableTaskDefinition runnableTaskDefinition) {
        this.scheduler = new ScheduledThreadPoolExecutor(threadPoolSize, namedThreadFactory());
        this.useCase = useCase;
        this.runnableTaskDefinition = runnableTaskDefinition;
    }

    private ThreadFactory namedThreadFactory(){
        return new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                String prefix = "monitor-thread-";
                Thread t = new Thread(r);
                t.setName(prefix + counter.getAndIncrememnt());
                t.setDaemon(true);
                t.setUncaughtExceptionHandler((thread, e) ->
                        log.error("Uncaught error in : {}", thread.getName()));
                return t;
            }
        };
    }

    public void registerTasks(Collection<DirectoryScanConfig> scanDefinitions){
        this.scanDefinitions.addAll(scanDefinitions);
    }

    public void startAll(){
        if(isRunning()){
            log.info("All tasks already running - ignoring start");
        }
        stopAll();
        activeFuture.clear();

        for(DirectoryScanConfig scanDefinition:scanDefinitions){
            ScheduledFuture<?> future = scheduler.sceduleWithFixedDelay(
                    runnableTaskDefinition.createTask(scanDefinition),
                    scanDefinition.getInitialDelaySeconds(),
                    scanDefinition.getFrequencyMinutes(),
                    TimeUnit.valueOf(scanDefinition.getTimeUnitAsString())
            );
            activeFuture.add(future);
            log.info("Task started for: {}/{}", scanDefinition.getDirectoryPath(), scanDefinition.getFileNamePattern());
        }
    }

    public void stopAll(){
        if(!isRunning){
            log.info("All tasks already stopped - ignoring stop.");
            return;
        }
        activeFuture.forEach(future -> {
            future.cancel(false);
            log.info("Cancelled future taks for: {}", future);
        });

        try {
            while(!activeFuture.stream().allMatch(ScheduledFuture::isDone)){
                log.info("Still waiting for tasks to cancel...");
                Thread.sleep(SECONDS.toMillis(30));
            }
            log.info("All task completed");
            activeFuture.clear();
        }catch (InterruptedException e){
            log.warn("Interrupted while stopping all future task...", e);
            Thread.currentThread().interrupt();
        }
    }

    public void awaitTermination(){
        activeFuture.forEach(future -> {
            future.cancel(false);
            log.info("Cancelled task: {}", future);
        });
        scheduler.shutdown();
        try{
            while(!scheduler.awaitTermination(30, SECONDS)){
                log.info("Still waiting for scheduler to terminate...");
            }
            log.info("All tasks completed and scheduler terminated...");
            activeFuture.clear();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            scheduler.shutDownNow();
        }
    }
    public boolean isRunning(){
        return !activeFuture.isEmpty();
    }

}

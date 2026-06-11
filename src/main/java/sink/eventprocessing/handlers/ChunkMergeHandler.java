package sink.eventprocessing.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sink.eventprocessing.FileChunk;
import sink.eventprocessing.FileMergeContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChunkMergeHandler extends Handler {
    private static final Logger log = LogManager.getLogger(ChunkMergeHandler.class);

    @Override
    public boolean process(FileMergeContext context) {
        List<Path> chunkFiles = null;
        try (Stream<Path> stream = Files.list(context.getChunkDirPath())){
            chunkFiles = stream.filter(Files::isRegularFile).collect(Collectors.toList());
            log.debug("chunk paths: {}", chunkFiles);
        }catch (IOException e){
            return false;
        }

        Path mergeFilePath = context.getChunkDirPath().getParent();
        mergeFilePath = mergeFilePath.resolve(context.getFileName());
        context.setMergedFilePath(mergeFilePath);

        try(RandomAccessFile raf = new RandomAccessFile(mergeFilePath.toFile(), "rw")){
            raf.setLength(context.getFileSize());
            for(Path chunkFile:chunkFiles){
                FileChunk chunk = deserialize(chunkFile);
                raf.seek(chunk.getChunkOffset());
                raf.write(chunk.getData(), 0, chunk.getChunkActualSize());
            }
        }catch (IOException | ClassNotFoundException){
            log.error("Problem is merging chunk", e);
        }
        return true;
    }
    private FileChunk deserialize(Path file) throws IOException, ClassNotFoundException{
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))){
            return (FileChunk) ois.readObject();
        }
    }
}

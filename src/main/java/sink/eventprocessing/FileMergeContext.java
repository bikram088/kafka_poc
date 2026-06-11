package sink.eventprocessing;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import sink.config.SinkRules;

import java.nio.file.Path;

public class FileMergeContext {
    SinkRules rules;
    ConsumerRecord<String, byte[]> record;

    private int matchingRuleIndex = -1;
    String uuid;
    String fileName;
    String fileNameWithoutExtension;
    long fileSize;
    String checksum;
    int chunkNumber;
    int chunkActualSize;
    boolean lastChunk;
    Path chunkDirPath;
    Path mergeFilePath;
    boolean merged = false;
    int chunkStartOffset;

    public int getChunkStartOffset() {
        return chunkStartOffset;
    }

    public void setChunkStartOffset(int chunkStartOffset) {
        this.chunkStartOffset = chunkStartOffset;
    }

    public FileMergeContext(ConsumerRecord<String, byte[]> record, SinkRules rules){
        this.record = record;
        this.rules = rules;
    }

    public SinkRules getRules() {
        return rules;
    }

    public void setRules(SinkRules rules) {
        this.rules = rules;
    }

    public ConsumerRecord<String, byte[]> getRecord() {
        return record;
    }

    public void setRecord(ConsumerRecord<String, byte[]> record) {
        this.record = record;
    }

    public int getMatchingRuleIndex() {
        return matchingRuleIndex;
    }

    public void setMatchingRuleIndex(int matchingRuleIndex) {
        this.matchingRuleIndex = matchingRuleIndex;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    public void setFileNameWithoutExtension(String fileNameWithoutExtension) {
        this.fileNameWithoutExtension = fileNameWithoutExtension;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public int getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public int getChunkActualSize() {
        return chunkActualSize;
    }

    public void setChunkActualSize(int chunkActualSize) {
        this.chunkActualSize = chunkActualSize;
    }

    public boolean isLastChunk() {
        return lastChunk;
    }

    public void setLastChunk(boolean lastChunk) {
        this.lastChunk = lastChunk;
    }

    public Path getChunkDirPath() {
        return chunkDirPath;
    }

    public void setChunkDirPath(Path chunkDirPath) {
        this.chunkDirPath = chunkDirPath;
    }

    public Path getMergeFilePath() {
        return mergeFilePath;
    }

    public void setMergeFilePath(Path mergeFilePath) {
        this.mergeFilePath = mergeFilePath;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }
}

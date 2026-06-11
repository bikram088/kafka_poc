package POC.statemanagement;

import java.io.IOException;

public interface AppStateStoreListener {
    void loadState() throws IOException;
    void saveState() throws IOException;
    void dumpState();
}

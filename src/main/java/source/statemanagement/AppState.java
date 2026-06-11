package POC.statemanagement;

import java.util.List;

public class AppState {
    List<DirectoryState> directories;

     public List<DirectoryState> getDirectories(){
         return directories;
     }

     public void setDirectories(List<DirectoryState> directories){
         this.directories = directories;
     }
}

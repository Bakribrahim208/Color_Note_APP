package bakribrahim.com.smartNote.viewmodel;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class newsViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;
    private  int checklistID;
    public newsViewModelFactory(Application application, String param ) {
        mApplication = application;
        mParam = param;
        this. checklistID=checklistID;
     }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new NoteviewModel(mApplication,mParam);
    }
}
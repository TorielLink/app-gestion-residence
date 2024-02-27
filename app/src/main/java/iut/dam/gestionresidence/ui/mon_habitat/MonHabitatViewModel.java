package iut.dam.gestionresidence.ui.mon_habitat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonHabitatViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MonHabitatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MON HABITAT fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
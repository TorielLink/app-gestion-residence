package iut.dam.gestionresidence.ui.fragments.my_habitat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyHabitatViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyHabitatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MON HABITAT fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
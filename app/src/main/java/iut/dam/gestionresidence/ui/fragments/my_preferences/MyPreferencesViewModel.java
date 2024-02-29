package iut.dam.gestionresidence.ui.fragments.my_preferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyPreferencesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyPreferencesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MES PREFERENCES fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
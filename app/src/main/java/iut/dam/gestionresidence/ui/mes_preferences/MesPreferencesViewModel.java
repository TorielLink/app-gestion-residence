package iut.dam.gestionresidence.ui.mes_preferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MesPreferencesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MesPreferencesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MES PREFERENCES fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
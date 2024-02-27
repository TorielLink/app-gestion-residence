package iut.dam.gestionresidence.ui.liste_habitants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListeHabitantsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListeHabitantsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is LISTE DES HABITANTS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
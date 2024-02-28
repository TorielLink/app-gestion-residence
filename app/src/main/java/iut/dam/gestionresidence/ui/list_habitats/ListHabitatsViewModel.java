package iut.dam.gestionresidence.ui.list_habitats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListHabitatsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListHabitatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is LISTE DES HABITANTS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
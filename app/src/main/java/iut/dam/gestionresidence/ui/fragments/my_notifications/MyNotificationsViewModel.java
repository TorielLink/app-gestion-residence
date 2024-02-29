package iut.dam.gestionresidence.ui.fragments.my_notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyNotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyNotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MES NOTIFICATIONS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
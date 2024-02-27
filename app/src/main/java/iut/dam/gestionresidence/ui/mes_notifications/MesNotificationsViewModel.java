package iut.dam.gestionresidence.ui.mes_notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MesNotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MesNotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MES NOTIFICATIONS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}